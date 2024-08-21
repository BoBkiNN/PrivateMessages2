package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class ToggleCommand {
   public ToggleCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.toggle")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         Player pl = (Player)sender;
         PlayerSettings settings = PrivateMessages.get().getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
         if (settings.arePrivateMessagesEnabled()) {
            settings.setPrivateMessagesEnabled(false);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_MESSAGES_NOW_DISABLED);
         } else {
            settings.setPrivateMessagesEnabled(true);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_MESSAGES_NOW_ENABLED);
         }
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
