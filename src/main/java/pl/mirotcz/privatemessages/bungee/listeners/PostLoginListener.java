package pl.mirotcz.privatemessages.bungee.listeners;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.managers.KnownPlayersManager;
import pl.mirotcz.privatemessages.bungee.managers.PlayerSettingsManager;

public class PostLoginListener implements Listener {
   private PrivateMessages instance;

   public PostLoginListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onPostLogin(PostLoginEvent e) {
      if (this.instance.getSettings().NOTIFY_UNREAD_MESSAGES_AFTER_JOIN) {
         this.instance.getNotifierTask().addPlayerWaitingForNotification(e.getPlayer().getName());
      }

      String player_name = e.getPlayer().getName();
      PlayerSettingsManager manager = this.instance.getManagers().getPlayerSettingsManager();
      if (manager.getPlayerSettings(player_name) == null) {
         manager.createNewSettings(player_name);
      }

      KnownPlayersManager pm = this.instance.getManagers().getKnownPlayersManager();
      if (pm.getKnownPlayer(player_name) == null) {
         pm.addKnownPlayer(player_name);
      }

   }
}
