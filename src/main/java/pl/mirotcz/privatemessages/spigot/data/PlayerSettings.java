package pl.mirotcz.privatemessages.spigot.data;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.events.PlayerSettingsModifyEvent;

public class PlayerSettings {
   private String PLAYER_NAME = null;
   private int MESSAGE_NOTIFICATION_SOUND_NUMBER = 1;
   private boolean MESSAGE_NOTIFICATION_SOUND_ENABLED = true;
   private boolean MESSAGE_SPY_ENABLED = false;
   private Set<String> IGNORED_PLAYERS = new HashSet<>();
   private boolean PRIVATE_MESSAGES_ENABLED = true;

   public void load(String player_name, int sound_number, boolean sound_enabled, boolean spy_enabled, Set<String> ignored_players, boolean msg_enabled) {
      this.PLAYER_NAME = player_name;
      this.MESSAGE_NOTIFICATION_SOUND_NUMBER = sound_number;
      this.MESSAGE_NOTIFICATION_SOUND_ENABLED = sound_enabled;
      this.MESSAGE_SPY_ENABLED = spy_enabled;
      this.IGNORED_PLAYERS = ignored_players;
      this.PRIVATE_MESSAGES_ENABLED = msg_enabled;
   }

   public String getPlayerName() {
      return this.PLAYER_NAME;
   }

   public void setPlayerName(String name) {
      this.PLAYER_NAME = name;
   }

   public int getMessageNotificationSoundNumber() {
      return this.MESSAGE_NOTIFICATION_SOUND_NUMBER;
   }

   public boolean isMessageNotificationSoundEnabled() {
      return this.MESSAGE_NOTIFICATION_SOUND_ENABLED;
   }

   public boolean isMessageSpyEnabled() {
      return this.MESSAGE_SPY_ENABLED;
   }

   public synchronized void setMessageSpyEnabled(boolean value) {
      this.MESSAGE_SPY_ENABLED = value;
      Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
         Bukkit.getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
      });
   }

   public synchronized void setMessageNotificationSoundEnabled(boolean value) {
      this.MESSAGE_NOTIFICATION_SOUND_ENABLED = value;
      Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
         Bukkit.getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
      });
   }

   public synchronized void setMessageNotificationSoundNumber(int value) {
      this.MESSAGE_NOTIFICATION_SOUND_NUMBER = value;
      Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
         Bukkit.getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
      });
   }

   public Set<String> getIgnoredPlayers() {
      return this.IGNORED_PLAYERS;
   }

   public synchronized void setIgnoredPlayers(Set<String> value) {
      this.IGNORED_PLAYERS = value;
      Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
         Bukkit.getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
      });
   }

   public boolean arePrivateMessagesEnabled() {
      return this.PRIVATE_MESSAGES_ENABLED;
   }

   public synchronized void setPrivateMessagesEnabled(boolean value) {
      this.PRIVATE_MESSAGES_ENABLED = value;
      Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
         Bukkit.getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
      });
   }
}
