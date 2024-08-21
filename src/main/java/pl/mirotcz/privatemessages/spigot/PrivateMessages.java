package pl.mirotcz.privatemessages.spigot;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.mirotcz.bukkit.Metrics;
import pl.mirotcz.privatemessages.spigot.inventories.MessagesInventory;
import pl.mirotcz.privatemessages.spigot.managers.Managers;
import pl.mirotcz.privatemessages.spigot.managers.MessagesManager;
import pl.mirotcz.privatemessages.spigot.managers.SettingsManager;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.storage.Storage;
import pl.mirotcz.privatemessages.spigot.storage.Storage_MySQL;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish;

public final class PrivateMessages extends JavaPlugin {
    private static PrivateMessages instance;
   private Managers managers;
   private volatile MessagesInventory global_history_inv = null;
   private volatile long last_global_history_inv_gen = 0L;
   private BukkitTask cache_task = null;
   private BungeeVanishNotifierTask bungee_vanish_task = null;
   private SettingsReloaderTask settings_reloader_task = null;
   private NotifierTask notifier_task = null;

   public void onEnable() {
      instance = this;
      this.managers = new Managers(instance);
      this.managers.load();
      Messenger.setPrefix(this.managers.getMessagesManager().PLUGIN_PREFIX);
       Vanish vanish = this.managers.getVanishManager().getVanish();
      if (!this.managers.getVanishManager().isVanishSupported()) {
         Messenger.sendConsole("Vanish plugin not found! Vanish support disabled.");
      } else {
         this.getServer().getPluginManager().registerEvents((Listener) vanish, instance);
      }

      this.startCacheTask();
      if (instance.getSettings().BUNGEECORD) {
         this.bungee_vanish_task = new BungeeVanishNotifierTask(instance);
         this.bungee_vanish_task.startTask();
      }

      if (this.getSettings().BUNGEECORD && this.getStorage() instanceof Storage_MySQL) {
         this.settings_reloader_task = new SettingsReloaderTask(instance);
         this.settings_reloader_task.start();
      }

      this.notifier_task = new NotifierTask(instance);
      this.notifier_task.start();
      if (this.getSettings().METRICS) {
         new Metrics(instance);
      }

   }

   public void onDisable() {
      this.managers.getPlayerSettingsManager().savePendingChanges();
      this.managers.getPlayerSettingsManager().cancelSaveTask();
      this.managers.getPendingMessagesManager().savePendingMessagesToStorage();
      this.managers.getPendingMessagesManager().cancelSaveTask();
      this.managers.getStorageManager().getStorage().closePool();
      this.stopCacheTask();
      if (this.bungee_vanish_task != null) {
         this.bungee_vanish_task.stopTask();
      }

      if (this.settings_reloader_task != null) {
         this.settings_reloader_task.stop();
      }

      if (this.notifier_task != null) {
         this.notifier_task.stop();
      }

   }

   public static PrivateMessages get() {
      return instance;
   }

   public Managers getManagers() {
      return this.managers;
   }

   public MessagesManager getMessages() {
      return this.managers.getMessagesManager();
   }

   public SettingsReloaderTask getSettingsReloaderTask() {
      return this.settings_reloader_task;
   }

   public NotifierTask getNotifierTask() {
      return this.notifier_task;
   }

   public Storage getStorage() {
      return this.managers.getStorageManager().getStorage();
   }

   public SettingsManager getSettings() {
      return this.managers.getSettingsManager();
   }

   public Vanish getVanish() {
      return this.managers.getVanishManager().getVanish();
   }

   public static String checkVersion() {
      try {
          return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
      } catch (ArrayIndexOutOfBoundsException var2) {
         return null;
      }
   }

   public void setGlobalHistoryInv(MessagesInventory inv) {
      this.global_history_inv = inv;
   }

   public MessagesInventory getGlobalHistoryInv() {
      return this.global_history_inv;
   }

   public synchronized void setLastGlobalHistoryInvGen(long time) {
      this.last_global_history_inv_gen = time;
   }

   public long getLastGlobalHistoryInvGen() {
      return this.last_global_history_inv_gen;
   }

   public void startCacheTask() {
      this.cache_task = Bukkit.getScheduler().runTaskTimer(instance, () -> {
         if (System.currentTimeMillis() - this.getLastGlobalHistoryInvGen() >= (this.getSettings().GLOBAL_HISTORY_CACHE_TIME_SECONDS * 1000L)) {
            this.clearMessagesCache();
         }

      }, 20L, 20L);
   }

   public void stopCacheTask() {
      if (this.cache_task != null) {
         this.cache_task.cancel();
      }

   }

   public void clearMessagesCache() {
      if (this.getGlobalHistoryInv() != null) {
         this.setGlobalHistoryInv(null);
      }

   }
}
