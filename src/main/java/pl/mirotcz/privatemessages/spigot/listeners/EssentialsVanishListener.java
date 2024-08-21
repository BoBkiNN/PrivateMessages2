package pl.mirotcz.privatemessages.spigot.listeners;

import net.ess3.api.IUser;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class EssentialsVanishListener implements Listener {
   private PrivateMessages instance;

   public EssentialsVanishListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onAppear(VanishStatusChangeEvent e) {
      if (this.instance.getSettings().NOTIFY_UNREAD_MESSAGES_AFTER_UNVANISH) {
         IUser user = e.getAffected();
         if (user.isVanished()) {
            this.instance.getNotifierTask().addPlayerWaitingForNotification(user.getBase().getName());
         }

      }
   }
}
