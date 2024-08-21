package pl.mirotcz.privatemessages.spigot;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.data.PlayerTempData;
import pl.mirotcz.privatemessages.spigot.events.PrivateMessageEvent;
import pl.mirotcz.privatemessages.spigot.managers.SoundData;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.StringUtils;

public class MessageSending {
   public static void sendMessage(CommandSender sender, Player recipient, OfflinePlayer offlineRecipient, String recipientName, boolean offline, boolean vanish, String[] args) {
      PrivateMessages instance = PrivateMessages.get();
      if (recipient == sender && !instance.getSettings().ALLOW_SENDING_MESSAGES_TO_SELF) {
         Messenger.send(sender, instance.getMessages().INFO_CANNOT_MESSAGE_SELF);
      } else if (recipient == null && offlineRecipient == null) {
         Messenger.send(sender, instance.getMessages().INFO_PLAYER_NOT_FOUND);
      } else {
         PlayerSettings recipientSettings = instance.getManagers().getPlayerSettingsManager().getPlayerSettings(recipientName);
         PlayerSettings senderSettings = instance.getManagers().getPlayerSettingsManager().getPlayerSettings(sender.getName());
         if (recipientSettings.getIgnoredPlayers().contains(sender.getName()) && !sender.hasPermission("pm.message.ignored")) {
            Messenger.send(sender, instance.getMessages().INFO_PLAYER_IGNORES_YOU);
         } else if (senderSettings.getIgnoredPlayers().contains(recipientName) && !sender.hasPermission("pm.message.ignored")) {
            Messenger.send(sender, instance.getMessages().INFO_PLAYER_IGNORED);
         } else if (!recipientSettings.arePrivateMessagesEnabled() && !sender.hasPermission("pm.message.disabled")) {
            Messenger.send(sender, instance.getMessages().INFO_PLAYER_MESSAGES_DISABLED);
         } else if (!senderSettings.arePrivateMessagesEnabled() && !sender.hasPermission("pm.message.disabled")) {
            Messenger.send(sender, instance.getMessages().INFO_MESSAGES_DISABLED);
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
            Bukkit.getScheduler().runTask(instance, () -> {
               Bukkit.getPluginManager().callEvent(new PrivateMessageEvent(message));
            });
            boolean vanishBypass = false;
            if (!vanish) {
               if (!offline) {
                  Messenger.sendCustomPrefix(sender, "", instance.getMessages().MESSAGE_TO_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               } else {
                  Messenger.sendCustomPrefix(sender, "", instance.getMessages().MESSAGE_TO_OFFLINE_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               }
            } else if (sender.hasPermission("pm.message.vanished")) {
               Messenger.sendCustomPrefix(sender, "", instance.getMessages().MESSAGE_TO_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
               vanishBypass = true;
            } else {
               Messenger.sendCustomPrefix(sender, "", instance.getMessages().MESSAGE_TO_OFFLINE_FORMAT.replaceAll("<player>", message.getRecipientName()).replaceAll("<message>", message.getMessageContent()));
            }

            if (recipient != null && recipient.isOnline()) {
               if (vanish && (!vanish || !vanishBypass)) {
                  instance.getManagers().getPendingMessagesManager().addPendingUnreadMessage(message);
               } else {
                  notifyPlayerMessage(message);
               }
            } else {
               instance.getManagers().getPendingMessagesManager().addPendingUnreadMessage(message);
            }

            if (recipient != null) {
               instance.getManagers().getPlayerTempDataManger().getData(recipient).setLastMessageSender(sender.getName());
            }

            if (sender instanceof Player) {
               instance.getManagers().getPlayerTempDataManger().getData((Player)sender).setLastMessageSender(recipientName);
            }

            instance.getManagers().getPendingMessagesManager().addPendingMessage(message);
         }
      }
   }

   public static void notifyPlayerMessage(Message message) {
      Player recipient = Bukkit.getPlayer(message.getRecipientName());
      PrivateMessages instance = PrivateMessages.get();
      if (recipient != null) {
         Messenger.sendCustomPrefix(recipient, "", instance.getMessages().MESSAGE_FROM_FORMAT.replaceAll("<player>", message.getSenderName()).replaceAll("<message>", message.getMessageContent()));
         PlayerSettings settings = instance.getManagers().getPlayerSettingsManager().getPlayerSettings(recipient.getName());
         SoundData soundData = instance.getManagers().getSoundDataManager().getData(settings.getMessageNotificationSoundNumber());
         if (soundData != null) {
            recipient.playSound(recipient.getLocation(), soundData.getSound(), soundData.getVolume(), soundData.getPitch());
            PlayerTempData tempData = instance.getManagers().getPlayerTempDataManger().getData(recipient);
            if (!tempData.hasReceivedPmSoundTip()) {
               Messenger.send(recipient, instance.getMessages().INFO_SOUND_CHANGE_TIP);
               tempData.setReceivedPmSoundTip(true);
            }
         }
      }

   }

   public static void soundNotifyPlayer(String player_name) {
      PrivateMessages instance = PrivateMessages.get();
      Player player = Bukkit.getPlayer(player_name);
      if (player != null) {
         PlayerSettings settings = instance.getManagers().getPlayerSettingsManager().getPlayerSettings(player.getName());
         if (!settings.isMessageNotificationSoundEnabled()) {
            return;
         }

         SoundData soundData = instance.getManagers().getSoundDataManager().getData(settings.getMessageNotificationSoundNumber());
         if (soundData != null) {
            player.playSound(player.getLocation(), soundData.getSound(), soundData.getVolume(), soundData.getPitch());
            PlayerTempData tempData = instance.getManagers().getPlayerTempDataManger().getData(player);
            if (!tempData.hasReceivedPmSoundTip()) {
               Messenger.send(player, instance.getMessages().INFO_SOUND_CHANGE_TIP);
               tempData.setReceivedPmSoundTip(true);
            }
         }
      }

   }
}
