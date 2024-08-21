package pl.mirotcz.privatemessages.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.events.PlayerSettingsModifyEvent;

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
