package pl.mirotcz.bungeecord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Metrics {
   public static final int B_STATS_VERSION = 1;
   private static final String URL = "https://bStats.org/submitData/bungeecord";
   private final Plugin plugin;
   private boolean enabled;
   private String serverUUID;
   private boolean logFailedRequests = false;
   private static boolean logSentData;
   private static boolean logResponseStatusText;
   private static final List knownMetricsInstances;
   private final List charts = new ArrayList();

   public Metrics(Plugin plugin) {
      this.plugin = plugin;

      try {
         this.loadConfig();
      } catch (IOException var4) {
         plugin.getLogger().log(Level.WARNING, "Failed to load bStats config!", var4);
         return;
      }

      if (this.enabled) {
         Class usedMetricsClass = this.getFirstBStatsClass();
         if (usedMetricsClass != null) {
            if (usedMetricsClass == this.getClass()) {
               linkMetrics(this);
               this.startSubmitting();
            } else {
               try {
                  usedMetricsClass.getMethod("linkMetrics", Object.class).invoke((Object)null, this);
               } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var5) {
                  if (this.logFailedRequests) {
                     plugin.getLogger().log(Level.WARNING, "Failed to link to first metrics class " + usedMetricsClass.getName() + "!", var5);
                  }
               }
            }

         }
      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void addCustomChart(CustomChart chart) {
      if (chart == null) {
         this.plugin.getLogger().log(Level.WARNING, "Chart cannot be null");
      }

      this.charts.add(chart);
   }

   public static void linkMetrics(Object metrics) {
      knownMetricsInstances.add(metrics);
   }

   public JsonObject getPluginData() {
      JsonObject data = new JsonObject();
      String pluginName = this.plugin.getDescription().getName();
      String pluginVersion = this.plugin.getDescription().getVersion();
      data.addProperty("pluginName", pluginName);
      data.addProperty("pluginVersion", pluginVersion);
      JsonArray customCharts = new JsonArray();
      Iterator var5 = this.charts.iterator();

      while(var5.hasNext()) {
         CustomChart customChart = (CustomChart)var5.next();
         JsonObject chart = customChart.getRequestJsonObject(this.plugin.getLogger(), this.logFailedRequests);
         if (chart != null) {
            customCharts.add(chart);
         }
      }

      data.add("customCharts", customCharts);
      return data;
   }

   private void startSubmitting() {
      this.plugin.getProxy().getScheduler().schedule(this.plugin, this::submitData, 2L, 30L, TimeUnit.MINUTES);
   }

   private JsonObject getServerData() {
      int playerAmount = this.plugin.getProxy().getOnlineCount();
      playerAmount = playerAmount > 500 ? 500 : playerAmount;
      int onlineMode = this.plugin.getProxy().getConfig().isOnlineMode() ? 1 : 0;
      String bungeecordVersion = this.plugin.getProxy().getVersion();
      int managedServers = this.plugin.getProxy().getServers().size();
      String javaVersion = System.getProperty("java.version");
      String osName = System.getProperty("os.name");
      String osArch = System.getProperty("os.arch");
      String osVersion = System.getProperty("os.version");
      int coreCount = Runtime.getRuntime().availableProcessors();
      JsonObject data = new JsonObject();
      data.addProperty("serverUUID", this.serverUUID);
      data.addProperty("playerAmount", playerAmount);
      data.addProperty("managedServers", managedServers);
      data.addProperty("onlineMode", onlineMode);
      data.addProperty("bungeecordVersion", bungeecordVersion);
      data.addProperty("javaVersion", javaVersion);
      data.addProperty("osName", osName);
      data.addProperty("osArch", osArch);
      data.addProperty("osVersion", osVersion);
      data.addProperty("coreCount", coreCount);
      return data;
   }

   private void submitData() {
      JsonObject data = this.getServerData();
      JsonArray pluginData = new JsonArray();
      Iterator var3 = knownMetricsInstances.iterator();

      while(var3.hasNext()) {
         Object metrics = var3.next();

         try {
            Object plugin = metrics.getClass().getMethod("getPluginData").invoke(metrics);
            if (plugin instanceof JsonObject) {
               pluginData.add((JsonObject)plugin);
            }
         } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var6) {
         }
      }

      data.add("plugins", pluginData);

      try {
         sendData(this.plugin, data);
      } catch (Exception var7) {
         if (this.logFailedRequests) {
            this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats!", var7);
         }
      }

   }

   private void loadConfig() throws IOException {
      Path configPath = this.plugin.getDataFolder().toPath().getParent().resolve("bStats");
      configPath.toFile().mkdirs();
      File configFile = new File(configPath.toFile(), "config.yml");
      if (!configFile.exists()) {
         this.writeFile(configFile, "#bStats collects some data for plugin authors like how many servers are using their plugins.", "#To honor their work, you should not disable it.", "#This has nearly no effect on the server performance!", "#Check out https://bStats.org/ to learn more :)", "enabled: true", "serverUuid: \"" + UUID.randomUUID().toString() + "\"", "logFailedRequests: false", "logSentData: false", "logResponseStatusText: false");
      }

      Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
      this.enabled = configuration.getBoolean("enabled", true);
      this.serverUUID = configuration.getString("serverUuid");
      this.logFailedRequests = configuration.getBoolean("logFailedRequests", false);
      logSentData = configuration.getBoolean("logSentData", false);
      logResponseStatusText = configuration.getBoolean("logResponseStatusText", false);
   }

   private Class getFirstBStatsClass() {
      Path configPath = this.plugin.getDataFolder().toPath().getParent().resolve("bStats");
      configPath.toFile().mkdirs();
      File tempFile = new File(configPath.toFile(), "temp.txt");

      try {
         String className = this.readFile(tempFile);
         if (className != null) {
            try {
               return Class.forName(className);
            } catch (ClassNotFoundException var5) {
            }
         }

         this.writeFile(tempFile, this.getClass().getName());
         return this.getClass();
      } catch (IOException var6) {
         if (this.logFailedRequests) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to get first bStats class!", var6);
         }

         return null;
      }
   }

   private String readFile(File file) throws IOException {
      if (!file.exists()) {
         return null;
      } else {
         FileReader fileReader = new FileReader(file);
         Throwable var3 = null;

         Object var6;
         try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Throwable var5 = null;

            try {
               var6 = bufferedReader.readLine();
            } catch (Throwable var29) {
               var6 = var29;
               var5 = var29;
               throw var29;
            } finally {
               if (bufferedReader != null) {
                  if (var5 != null) {
                     try {
                        bufferedReader.close();
                     } catch (Throwable var28) {
                        var5.addSuppressed(var28);
                     }
                  } else {
                     bufferedReader.close();
                  }
               }

            }
         } catch (Throwable var31) {
            var3 = var31;
            throw var31;
         } finally {
            if (fileReader != null) {
               if (var3 != null) {
                  try {
                     fileReader.close();
                  } catch (Throwable var27) {
                     var3.addSuppressed(var27);
                  }
               } else {
                  fileReader.close();
               }
            }

         }

         return (String)var6;
      }
   }

   private void writeFile(File file, String... lines) throws IOException {
      if (!file.exists()) {
         file.createNewFile();
      }

      FileWriter fileWriter = new FileWriter(file);
      Throwable var4 = null;

      try {
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Throwable var6 = null;

         try {
            String[] var7 = lines;
            int var8 = lines.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String line = var7[var9];
               bufferedWriter.write(line);
               bufferedWriter.newLine();
            }
         } catch (Throwable var32) {
            var6 = var32;
            throw var32;
         } finally {
            if (bufferedWriter != null) {
               if (var6 != null) {
                  try {
                     bufferedWriter.close();
                  } catch (Throwable var31) {
                     var6.addSuppressed(var31);
                  }
               } else {
                  bufferedWriter.close();
               }
            }

         }
      } catch (Throwable var34) {
         var4 = var34;
         throw var34;
      } finally {
         if (fileWriter != null) {
            if (var4 != null) {
               try {
                  fileWriter.close();
               } catch (Throwable var30) {
                  var4.addSuppressed(var30);
               }
            } else {
               fileWriter.close();
            }
         }

      }

   }

   private static void sendData(Plugin plugin, JsonObject data) throws Exception {
      if (data == null) {
         throw new IllegalArgumentException("Data cannot be null");
      } else {
         if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data.toString());
         }

         HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://bStats.org/submitData/bungeecord")).openConnection();
         byte[] compressedData = compress(data.toString());
         connection.setRequestMethod("POST");
         connection.addRequestProperty("Accept", "application/json");
         connection.addRequestProperty("Connection", "close");
         connection.addRequestProperty("Content-Encoding", "gzip");
         connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("User-Agent", "MC-Server/1");
         connection.setDoOutput(true);
         DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
         outputStream.write(compressedData);
         outputStream.flush();
         outputStream.close();
         InputStream inputStream = connection.getInputStream();
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         StringBuilder builder = new StringBuilder();

         String line;
         while((line = bufferedReader.readLine()) != null) {
            builder.append(line);
         }

         bufferedReader.close();
         if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder.toString());
         }

      }
   }

   private static byte[] compress(String str) throws IOException {
      if (str == null) {
         return null;
      } else {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
         gzip.write(str.getBytes(StandardCharsets.UTF_8));
         gzip.close();
         return outputStream.toByteArray();
      }
   }

   static {
      if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
         String defaultPackage = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 110, 103, 101, 101, 99, 111, 114, 100});
         String examplePackage = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
         if (Metrics.class.getPackage().getName().equals(defaultPackage) || Metrics.class.getPackage().getName().equals(examplePackage)) {
            throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
         }
      }

      knownMetricsInstances = new ArrayList();
   }

   public static class AdvancedBarChart extends CustomChart {
      private final Callable callable;

      public AdvancedBarChart(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         JsonObject values = new JsonObject();
         Map map = (Map)this.callable.call();
         if (map != null && !map.isEmpty()) {
            boolean allSkipped = true;
            Iterator var5 = map.entrySet().iterator();

            while(true) {
               Map.Entry entry;
               do {
                  if (!var5.hasNext()) {
                     if (allSkipped) {
                        return null;
                     }

                     data.add("values", values);
                     return data;
                  }

                  entry = (Map.Entry)var5.next();
               } while(((int[])entry.getValue()).length == 0);

               allSkipped = false;
               JsonArray categoryValues = new JsonArray();
               int[] var8 = (int[])entry.getValue();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  int categoryValue = var8[var10];
                  categoryValues.add(new JsonPrimitive(categoryValue));
               }

               values.add((String)entry.getKey(), categoryValues);
            }
         } else {
            return null;
         }
      }
   }

   public static class SimpleBarChart extends CustomChart {
      private final Callable callable;

      public SimpleBarChart(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         JsonObject values = new JsonObject();
         Map map = (Map)this.callable.call();
         if (map != null && !map.isEmpty()) {
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry entry = (Map.Entry)var4.next();
               JsonArray categoryValues = new JsonArray();
               categoryValues.add(new JsonPrimitive((Number)entry.getValue()));
               values.add((String)entry.getKey(), categoryValues);
            }

            data.add("values", values);
            return data;
         } else {
            return null;
         }
      }
   }

   public static class MultiLineChart extends CustomChart {
      private final Callable callable;

      public MultiLineChart(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         JsonObject values = new JsonObject();
         Map map = (Map)this.callable.call();
         if (map != null && !map.isEmpty()) {
            boolean allSkipped = true;
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry entry = (Map.Entry)var5.next();
               if ((Integer)entry.getValue() != 0) {
                  allSkipped = false;
                  values.addProperty((String)entry.getKey(), (Number)entry.getValue());
               }
            }

            if (allSkipped) {
               return null;
            } else {
               data.add("values", values);
               return data;
            }
         } else {
            return null;
         }
      }
   }

   public static class SingleLineChart extends CustomChart {
      private final Callable callable;

      public SingleLineChart(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         int value = (Integer)this.callable.call();
         if (value == 0) {
            return null;
         } else {
            data.addProperty("value", value);
            return data;
         }
      }
   }

   public static class DrilldownPie extends CustomChart {
      private final Callable callable;

      public DrilldownPie(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      public JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         JsonObject values = new JsonObject();
         Map map = (Map)this.callable.call();
         if (map != null && !map.isEmpty()) {
            boolean reallyAllSkipped = true;
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry entryValues = (Map.Entry)var5.next();
               JsonObject value = new JsonObject();
               boolean allSkipped = true;

               for(Iterator var9 = ((Map)map.get(entryValues.getKey())).entrySet().iterator(); var9.hasNext(); allSkipped = false) {
                  Map.Entry valueEntry = (Map.Entry)var9.next();
                  value.addProperty((String)valueEntry.getKey(), (Number)valueEntry.getValue());
               }

               if (!allSkipped) {
                  reallyAllSkipped = false;
                  values.add((String)entryValues.getKey(), value);
               }
            }

            if (reallyAllSkipped) {
               return null;
            } else {
               data.add("values", values);
               return data;
            }
         } else {
            return null;
         }
      }
   }

   public static class AdvancedPie extends CustomChart {
      private final Callable callable;

      public AdvancedPie(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         JsonObject values = new JsonObject();
         Map map = (Map)this.callable.call();
         if (map != null && !map.isEmpty()) {
            boolean allSkipped = true;
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry entry = (Map.Entry)var5.next();
               if ((Integer)entry.getValue() != 0) {
                  allSkipped = false;
                  values.addProperty((String)entry.getKey(), (Number)entry.getValue());
               }
            }

            if (allSkipped) {
               return null;
            } else {
               data.add("values", values);
               return data;
            }
         } else {
            return null;
         }
      }
   }

   public static class SimplePie extends CustomChart {
      private final Callable callable;

      public SimplePie(String chartId, Callable callable) {
         super(chartId);
         this.callable = callable;
      }

      protected JsonObject getChartData() throws Exception {
         JsonObject data = new JsonObject();
         String value = (String)this.callable.call();
         if (value != null && !value.isEmpty()) {
            data.addProperty("value", value);
            return data;
         } else {
            return null;
         }
      }
   }

   public abstract static class CustomChart {
      private final String chartId;

      CustomChart(String chartId) {
         if (chartId != null && !chartId.isEmpty()) {
            this.chartId = chartId;
         } else {
            throw new IllegalArgumentException("ChartId cannot be null or empty!");
         }
      }

      private JsonObject getRequestJsonObject(Logger logger, boolean logFailedRequests) {
         JsonObject chart = new JsonObject();
         chart.addProperty("chartId", this.chartId);

         try {
            JsonObject data = this.getChartData();
            if (data == null) {
               return null;
            } else {
               chart.add("data", data);
               return chart;
            }
         } catch (Throwable var5) {
            if (logFailedRequests) {
               logger.log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, var5);
            }

            return null;
         }
      }

      protected abstract JsonObject getChartData() throws Exception;
   }
}
