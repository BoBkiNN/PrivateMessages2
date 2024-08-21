package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class SpyCommand {
   public SpyCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.spy")) {
         if (sender instanceof ConsoleCommandSender) {
            if (PrivateMessages.get().getSettings().SPY_IN_CONSOLE) {
               PrivateMessages.get().getSettings().SPY_IN_CONSOLE = false;
               Messenger.send(sender, PrivateMessages.get().getMessages().INFO_SPY_DISABLED);
            } else {
               PrivateMessages.get().getSettings().SPY_IN_CONSOLE = true;
               Messenger.send(sender, PrivateMessages.get().getMessages().INFO_SPY_ENABLED);
            }

            PrivateMessages.get().getSettings().save();
            return;
         }

         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         Player pl = (Player)sender;
         PlayerSettings settings = PrivateMessages.get().getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
         if (settings.isMessageSpyEnabled()) {
            settings.setMessageSpyEnabled(false);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_SPY_DISABLED);
         } else {
            settings.setMessageSpyEnabled(true);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_SPY_ENABLED);
         }
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
