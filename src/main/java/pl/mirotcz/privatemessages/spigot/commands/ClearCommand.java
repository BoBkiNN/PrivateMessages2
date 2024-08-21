package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class ClearCommand {
   public ClearCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.clear")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         if (args.length != 0) {
            Messenger.send(sender, PrivateMessages.get().getMessages().HELP_COMMAND_PMCLEAR);
            return;
         }

         Player pl = (Player)sender;
         PrivateMessages.get().getStorage().clearUnreadMessages(pl.getName());
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_MESSAGES_CLEARED);
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
