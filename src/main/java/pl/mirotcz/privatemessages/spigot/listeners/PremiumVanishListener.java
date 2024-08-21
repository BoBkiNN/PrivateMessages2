package pl.mirotcz.privatemessages.spigot.listeners;

import de.myzelyam.api.vanish.PlayerShowEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class PremiumVanishListener implements Listener {
   private PrivateMessages instance;

   public PremiumVanishListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onAppear(PlayerShowEvent e) {
      if (this.instance.getSettings().NOTIFY_UNREAD_MESSAGES_AFTER_UNVANISH) {
         this.instance.getNotifierTask().addPlayerWaitingForNotification(e.getPlayer().getName());
      }
   }
}
