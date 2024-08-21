package pl.mirotcz.privatemessages.bungee.listeners;

import java.util.Iterator;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.events.PrivateMessageEvent;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class PrivateMessageListener implements Listener {
   private PrivateMessages instance;

   public PrivateMessageListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onMessage(PrivateMessageEvent e) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         Message message = e.getMessage();
         Iterator var3 = ProxyServer.getInstance().getPlayers().iterator();

         while(var3.hasNext()) {
            ProxiedPlayer pl = (ProxiedPlayer)var3.next();
            if (!message.getSenderName().equalsIgnoreCase(pl.getName()) && !message.getRecipientName().equalsIgnoreCase(pl.getName()) && pl.hasPermission("pm.spy")) {
               PlayerSettings settings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
               if (settings.isMessageSpyEnabled()) {
                  Messenger.sendCustomPrefix(pl, "", this.instance.getMessages().SPY_MESSAGE_FORMAT.replaceAll("<sender>", message.getSenderName()).replaceAll("<recipient>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               }
            }
         }

         if (this.instance.getSettings().SPY_IN_CONSOLE) {
            Messenger.sendCustomPrefix(ProxyServer.getInstance().getConsole(), "", this.instance.getMessages().SPY_MESSAGE_FORMAT.replaceAll("<sender>", message.getSenderName()).replaceAll("<recipient>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
         }

      });
   }
}
