package pl.mirotcz.privatemessages.spigot.commands;

import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class IgnoredCommand {
   public IgnoredCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.ignore")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         if (args.length != 0) {
            Messenger.send(sender, PrivateMessages.get().getMessages().HELP_COMMAND_IGNORED);
            return;
         }

         Player pl = (Player)sender;
         PlayerSettings settings = PrivateMessages.get().getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
         Set ignored = settings.getIgnoredPlayers();
         Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_IGNORED_PLAYERS.replaceAll("<ignored>", ignored.toString()));
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
