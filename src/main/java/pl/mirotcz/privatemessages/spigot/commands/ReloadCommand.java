package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.command.CommandSender;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;

public class ReloadCommand {
   public ReloadCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.admin")) {
         MainUtils.reloadPlugin();
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_CONFIG_RELOADED);
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
