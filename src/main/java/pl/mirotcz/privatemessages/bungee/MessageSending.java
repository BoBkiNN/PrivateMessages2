package pl.mirotcz.privatemessages.bungee;

import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.events.PrivateMessageEvent;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.StringUtils;

public class MessageSending {
   private PrivateMessages instance;

   public MessageSending(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void sendMessage(CommandSender sender, ProxiedPlayer recipient, String recipientName, boolean known_recipient_name, boolean offline, boolean vanish, String[] args) {
      if (recipient == sender && !this.instance.getSettings().ALLOW_SENDING_MESSAGES_TO_SELF) {
         Messenger.send(sender, this.instance.getMessages().INFO_CANNOT_MESSAGE_SELF);
      } else if (recipient == null && !known_recipient_name) {
         Messenger.send(sender, this.instance.getMessages().INFO_PLAYER_NOT_FOUND);
      } else {
         PlayerSettings recipientSettings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(recipientName);
         PlayerSettings senderSettings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(sender.getName());
         if (recipientSettings.getIgnoredPlayers().contains(sender.getName()) && !sender.hasPermission("pm.message.ignored")) {
            Messenger.send(sender, this.instance.getMessages().INFO_PLAYER_IGNORES_YOU);
         } else if (senderSettings.getIgnoredPlayers().contains(recipientName) && !sender.hasPermission("pm.message.ignored")) {
            Messenger.send(sender, this.instance.getMessages().INFO_PLAYER_IGNORED);
         } else if (!recipientSettings.arePrivateMessagesEnabled() && !sender.hasPermission("pm.message.disabled")) {
            Messenger.send(sender, this.instance.getMessages().INFO_PLAYER_MESSAGES_DISABLED);
         } else if (!senderSettings.arePrivateMessagesEnabled() && !sender.hasPermission("pm.message.disabled")) {
            Messenger.send(sender, this.instance.getMessages().INFO_MESSAGES_DISABLED);
         } else {
            StringBuilder text = new StringBuilder();

            for(int i = 0; i < args.length; ++i) {
               if (i != 0 || !args[i].equalsIgnoreCase(recipientName)) {
                  text.append(args[i]);
                  if (i + 1 < args.length) {
                     text.append(" ");
                  }
               }
            }

            String final_text = StringUtils.getSafeString(text.toString());
            Message message = new Message(sender.getName(), recipientName, System.currentTimeMillis(), final_text);
            ProxyServer.getInstance().getScheduler().schedule(this.instance, () -> {
               ProxyServer.getInstance().getPluginManager().callEvent(new PrivateMessageEvent(message));
            }, 50L, TimeUnit.MILLISECONDS);
            boolean vanishBypass = false;
            if (!vanish) {
               if (!offline) {
                  Messenger.sendCustomPrefix(sender, "", this.instance.getMessages().MESSAGE_TO_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               } else {
                  Messenger.sendCustomPrefix(sender, "", this.instance.getMessages().MESSAGE_TO_OFFLINE_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               }
            } else if (sender.hasPermission("pm.message.vanished")) {
               Messenger.sendCustomPrefix(sender, "", this.instance.getMessages().MESSAGE_TO_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               vanishBypass = true;
            } else {
               Messenger.sendCustomPrefix(sender, "", this.instance.getMessages().MESSAGE_TO_OFFLINE_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
            }

            if (recipient != null && recipient.isConnected()) {
               if (vanish && (!vanish || !vanishBypass)) {
                  this.instance.getManagers().getPendingMessagesManager().addPendingUnreadMessage(message);
               } else {
                  this.notifyPlayerMessage(message);
               }
            } else {
               this.instance.getManagers().getPendingMessagesManager().addPendingUnreadMessage(message);
            }

            if (recipient != null) {
               this.instance.getManagers().getPlayerTempDataManger().getData(recipient.getName()).setLastMessageSender(sender.getName());
            }

            if (sender instanceof ProxiedPlayer) {
               this.instance.getManagers().getPlayerTempDataManger().getData(sender.getName()).setLastMessageSender(recipientName);
            }

            this.instance.getManagers().getPendingMessagesManager().addPendingMessage(message);
            if (!vanish) {
               this.instance.getManagers().getListenersManager().getPluginMessagesListener().sendSoundNotify(recipientName);
            }

         }
      }
   }

   public void notifyPlayerMessage(Message message) {
      ProxiedPlayer recipient = ProxyServer.getInstance().getPlayer(message.getRecipientName());
      if (recipient != null) {
         Messenger.sendCustomPrefix(recipient, "", this.instance.getMessages().MESSAGE_FROM_FORMAT.replaceAll("<player>", message.getSenderName()).replaceAll("<message>", message.getMessageContent()));
      }

   }
}
