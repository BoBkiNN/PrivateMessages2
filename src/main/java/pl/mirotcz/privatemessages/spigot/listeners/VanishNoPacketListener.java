package pl.mirotcz.privatemessages.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class VanishNoPacketListener implements Listener {
   private final PrivateMessages instance;

   public VanishNoPacketListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onAppear(VanishStatusChangeEvent e) {
      if (this.instance.getSettings().NOTIFY_UNREAD_MESSAGES_AFTER_UNVANISH) {
         if (!e.isVanishing()) {
            this.instance.getNotifierTask().addPlayerWaitingForNotification(e.getPlayer().getName());
         }

      }
   }
}
