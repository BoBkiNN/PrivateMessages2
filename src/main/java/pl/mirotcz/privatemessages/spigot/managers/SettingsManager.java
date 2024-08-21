package pl.mirotcz.privatemessages.spigot.managers;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.bukkit.Material;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class SettingsManager {
   public String STORAGE_TYPE;
   public String DB_HOST;
   public String DB_NAME;
   public String DB_USER;
   public String DB_PASS;
   public int DB_PORT;
   public DateTimeFormatter DATE_FORMATTER;
   public String DATE_TIME_ZONE;
   public int MESSAGE_SAVE_INTERVAL_SECONDS = 10;
   public int MAX_HISTORY_MESSAGES_PER_SECOND = 5000;
   public boolean ALLOW_SENDING_MESSAGES_TO_SELF = true;
   public boolean ALLOW_REPLYING_TO_CONSOLE = true;
   public boolean ALLOW_SENDING_MESSAGES_TO_OFFLINE_PLAYERS = true;
   public boolean SPY_IN_CONSOLE = false;
   public Material SOUND_HELP_ITEM_MATERIAL = null;
   public int GLOBAL_HISTORY_CACHE_TIME_SECONDS = 600;
   public boolean BUNGEECORD = false;
   public boolean METRICS = true;
   public boolean NOTIFY_UNREAD_MESSAGES_AFTER_JOIN = true;
   public boolean NOTIFY_UNREAD_MESSAGES_AFTER_UNVANISH = true;

   public void load() {
      ConfigManager config = PrivateMessages.get().getManagers().getConfigManagers().getMainConfigManager();
      this.STORAGE_TYPE = config.getConfig().getString("Storage.type");
      this.DB_HOST = config.getConfig().getString("Storage.hostname");
      this.DB_NAME = config.getConfig().getString("Storage.database");
      this.DB_USER = config.getConfig().getString("Storage.user");
      this.DB_PASS = config.getConfig().getString("Storage.password");
      this.DB_PORT = config.getConfig().getInt("Storage.port");
      this.DATE_TIME_ZONE = config.getConfig().getString("MessagesHistory.TimeZone");
      this.DATE_FORMATTER = DateTimeFormatter.ofPattern(config.getConfig().getString("MessagesHistory.DateFormat")).withZone(ZoneId.of(this.DATE_TIME_ZONE));
      this.ALLOW_SENDING_MESSAGES_TO_SELF = config.getConfig().getBoolean("AllowSendingMessagesToSelf");
      this.ALLOW_SENDING_MESSAGES_TO_OFFLINE_PLAYERS = config.getConfig().getBoolean("AllowSendingMessagesToOfflinePlayers");
      this.SOUND_HELP_ITEM_MATERIAL = Material.valueOf(config.getConfig().getString("SoundHelpItem"));
      this.SPY_IN_CONSOLE = config.getConfig().getBoolean("SpyInConsole");
      this.MESSAGE_SAVE_INTERVAL_SECONDS = config.getConfig().getInt("MessagesSaveIntervalSeconds");
      this.MAX_HISTORY_MESSAGES_PER_SECOND = config.getConfig().getInt("MaxHistoryMessagesPerSecond");
      this.GLOBAL_HISTORY_CACHE_TIME_SECONDS = config.getConfig().getInt("GlobalHistoryCacheTimeSeconds");
      this.BUNGEECORD = config.getConfig().getBoolean("BungeeCord");
      this.METRICS = config.getConfig().getBoolean("Metrics");
      this.NOTIFY_UNREAD_MESSAGES_AFTER_JOIN = config.getConfig().getBoolean("NotifyUnreadMessagesAfterJoin");
      this.NOTIFY_UNREAD_MESSAGES_AFTER_UNVANISH = config.getConfig().getBoolean("NotifyUnreadMessagesAfterUnvanish");
   }

   public void save() {
      ConfigManager config = PrivateMessages.get().getManagers().getConfigManagers().getMainConfigManager();
      config.getConfig().set("SpyInConsole", this.SPY_IN_CONSOLE);
      config.saveConfig();
      config.reloadConfig();
   }
}
