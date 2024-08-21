package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.MessageSending;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;

public class MessageCommand {
   public MessageCommand(CommandSender sender, String[] args) {
      PrivateMessages instance = PrivateMessages.get();
      if (sender.hasPermission("pm.message")) {
         if (args.length < 2) {
            Messenger.send(sender, instance.getMessages().HELP_COMMAND_MESSAGE);
            return;
         }

         Player pl = Bukkit.getPlayer(args[0]);
         OfflinePlayer op = null;
         String recipientName = args[0];
         boolean vanish = false;
         boolean offline = false;
         if (pl == null) {
            if (!instance.getSettings().ALLOW_SENDING_MESSAGES_TO_OFFLINE_PLAYERS) {
               Messenger.send(sender, instance.getMessages().INFO_OFFLINE_MESSAGING_DISABLED);
               return;
            }

            op = MainUtils.getOfflinePlayer(args[0], Bukkit.getOfflinePlayers());
            if (op != null) {
               recipientName = op.getName();
            }

            offline = true;
         } else {
            recipientName = pl.getName();
            if (instance.getManagers().getVanishManager().isVanishSupported() && instance.getManagers().getVanishManager().getVanish().isVanished(pl)) {
               vanish = true;
            }
         }

         MessageSending.sendMessage(sender, pl, op, recipientName, offline, vanish, args);
      } else {
         Messenger.send(sender, instance.getMessages().INFO_NO_PERMISSION);
      }

   }
}
