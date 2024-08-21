package pl.mirotcz.privatemessages.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class SoundCommand {
   public SoundCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.sound")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         Player pl = (Player)sender;
         Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> {
            pl.openInventory(PrivateMessages.get().getManagers().getMenuManager().getSoundsMenu().get(1));
         });
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
