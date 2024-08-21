package pl.mirotcz.privatemessages.bungee.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {
   private final String fileName;
   private final Plugin plugin;
   private File configFile;
   private Configuration fileConfiguration;

   public ConfigManager(Plugin plugin, String fileName) {
      if (plugin == null) {
         throw new IllegalArgumentException("plugin cannot be null");
      } else {
         this.plugin = plugin;
         this.fileName = fileName;
         this.loadConfig();
      }
   }

   public void loadConfig() {
      File dataFolder = this.plugin.getDataFolder();
      if (dataFolder == null) {
         throw new IllegalStateException();
      } else {
         if (!dataFolder.exists()) {
            dataFolder.mkdirs();
         }

         this.configFile = new File(dataFolder, this.fileName);
         if (this.configFile != null && this.configFile.exists()) {
            System.out.println("Path: " + this.configFile.getAbsolutePath());
            System.out.println("2");
            this.configFile = new File(dataFolder, this.fileName);

            try {
               this.fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configFile);
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         } else {
            System.out.println("1");
            InputStream stream = this.plugin.getResourceAsStream(this.fileName);

            try {
               byte[] buffer = new byte[stream.available()];
               stream.read(buffer);
               OutputStream outStream = new FileOutputStream(this.configFile);
               outStream.write(buffer);
               outStream.close();
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         }

      }
   }

   public void reloadConfig() {
      this.loadConfig();
   }

   public Configuration getConfig() {
      if (this.fileConfiguration == null) {
         this.reloadConfig();
      }

      return this.fileConfiguration;
   }

   public void saveConfig() {
      if (this.fileConfiguration != null && this.configFile != null) {
         try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.fileConfiguration, new File(this.plugin.getDataFolder(), this.fileName));
         } catch (IOException var2) {
            this.plugin.getLogger().warning("&eCould not save config to " + this.configFile + " " + var2);
         }

      }
   }

   public void saveDefaultConfig() {
      if (!this.plugin.getDataFolder().exists()) {
         this.plugin.getDataFolder().mkdir();
      }

      File file = new File(this.plugin.getDataFolder(), "config.yml");
      if (!file.exists()) {
         InputStream stream = this.plugin.getResourceAsStream(this.fileName);

         try {
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
            OutputStream outStream = new FileOutputStream(this.configFile);
            outStream.write(buffer);
            outStream.close();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

   }
}
