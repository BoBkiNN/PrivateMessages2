package pl.mirotcz.privatemessages.spigot.listeners;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.events.PrivateMessageEvent;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class PrivateMessageListener implements Listener {
   private PrivateMessages instance;

   public PrivateMessageListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onMessage(PrivateMessageEvent e) {
      Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
         Message message = e.getMessage();
         Iterator var3 = Bukkit.getOnlinePlayers().iterator();

         while(var3.hasNext()) {
            Player pl = (Player)var3.next();
            if (!message.getSenderName().equalsIgnoreCase(pl.getName()) && !message.getRecipientName().equalsIgnoreCase(pl.getName()) && pl.hasPermission("pm.spy")) {
               PlayerSettings settings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
               if (settings.isMessageSpyEnabled()) {
                  Messenger.sendCustomPrefix(pl, "", this.instance.getMessages().SPY_MESSAGE_FORMAT.replaceAll("<sender>", message.getSenderName()).replaceAll("<recipient>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               }
            }
         }

         if (this.instance.getSettings().SPY_IN_CONSOLE) {
            Messenger.sendCustomPrefix(Bukkit.getConsoleSender(), "", this.instance.getMessages().SPY_MESSAGE_FORMAT.replaceAll("<sender>", message.getSenderName()).replaceAll("<recipient>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
         }

      });
   }
}
