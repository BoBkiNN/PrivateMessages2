package pl.mirotcz.privatemessages.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.SettingsReloaderTask;
import pl.mirotcz.privatemessages.spigot.managers.PlayerSettingsManager;

public class PlayerJoinListener implements Listener {
   private PrivateMessages instance;

   public PlayerJoinListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void PlayerJoin(PlayerJoinEvent e) {
      if (this.instance.getSettings().NOTIFY_UNREAD_MESSAGES_AFTER_JOIN) {
         this.instance.getNotifierTask().addPlayerWaitingForNotification(e.getPlayer().getName());
      }

      PlayerSettingsManager manager = this.instance.getManagers().getPlayerSettingsManager();
      String player_name = e.getPlayer().getName();
      if (manager.getPlayerSettings(player_name) == null) {
         manager.createNewSettings(e.getPlayer());
      }

      SettingsReloaderTask task = this.instance.getSettingsReloaderTask();
      if (task != null) {
         task.addPlayerWaitingForReload(player_name);
      }

   }
}
