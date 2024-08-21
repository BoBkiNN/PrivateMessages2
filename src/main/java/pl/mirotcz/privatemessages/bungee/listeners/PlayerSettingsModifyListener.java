package pl.mirotcz.privatemessages.bungee.listeners;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.events.PlayerSettingsModifyEvent;

public class PlayerSettingsModifyListener implements Listener {
   private PrivateMessages instance;

   public PlayerSettingsModifyListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onModify(PlayerSettingsModifyEvent e) {
      PlayerSettings settings = e.getPlayerSettings();
      this.instance.getManagers().getPlayerSettingsManager().addPendingChanges(settings);
   }
}
