package pl.mirotcz.privatemessages.spigot.commands;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;

public class IgnoreCommand {
   public IgnoreCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.ignore")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         if (args.length != 1) {
            Messenger.send(sender, PrivateMessages.get().getMessages().HELP_COMMAND_IGNORE);
            return;
         }

         Player pl = (Player)sender;
         PlayerSettings settings = PrivateMessages.get().getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
         Set ignored = settings.getIgnoredPlayers();
         String name = args[0];
         Object choosenPlayer;
         if (Bukkit.getPlayer(args[0]) != null) {
            choosenPlayer = Bukkit.getPlayer(args[0]);
         } else {
            choosenPlayer = MainUtils.getOfflinePlayer(args[0], Bukkit.getOfflinePlayers());
         }

         if (choosenPlayer != null) {
            name = ((OfflinePlayer)choosenPlayer).getName();
         }

         if (ignored.contains(name)) {
            ignored.remove(name);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_PLAYER_NOW_NOT_IGNORED.replaceAll("<player>", name));
         } else {
            ignored.add(name);
            Messenger.send(pl, (String)PrivateMessages.get().getMessages().INFO_PLAYER_NOW_IGNORED.replaceAll("<player>", name));
         }

         settings.setIgnoredPlayers(ignored);
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
