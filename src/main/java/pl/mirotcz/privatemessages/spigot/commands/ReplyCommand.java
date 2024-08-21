package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.MessageSending;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerTempData;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;

public class ReplyCommand {
   public ReplyCommand(CommandSender sender, String[] args) {
      PrivateMessages instance = PrivateMessages.get();
      if (sender.hasPermission("pm.reply")) {
         if (args.length < 1) {
            Messenger.send(sender, instance.getMessages().HELP_COMMAND_REPLY);
            return;
         }

         if (!(sender instanceof Player)) {
            Messenger.send(sender, instance.getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         PlayerTempData data = instance.getManagers().getPlayerTempDataManger().getData((Player)sender);
         if (data.getLastMessageSender() == null) {
            Messenger.send(sender, instance.getMessages().INFO_NONE_TO_REPLY);
            return;
         }

         Player pl = Bukkit.getPlayer(data.getLastMessageSender());
         OfflinePlayer op = null;
         String recipientName = null;
         boolean offline = false;
         boolean vanish = false;
         if (pl != null && pl.isOnline()) {
            recipientName = pl.getName();
            if (instance.getManagers().getVanishManager().isVanishSupported() && instance.getManagers().getVanishManager().getVanish().isVanished(pl)) {
               vanish = true;
            }
         } else {
            if (!instance.getSettings().ALLOW_SENDING_MESSAGES_TO_OFFLINE_PLAYERS) {
               Messenger.send(sender, instance.getMessages().INFO_OFFLINE_MESSAGING_DISABLED);
               return;
            }

            op = MainUtils.getOfflinePlayer(data.getLastMessageSender(), Bukkit.getOfflinePlayers());
            if (op != null) {
               recipientName = op.getName();
            }

            offline = true;
         }

         MessageSending.sendMessage(sender, pl, op, recipientName, offline, vanish, args);
      } else {
         Messenger.send(sender, instance.getMessages().INFO_NO_PERMISSION);
      }

   }
}
