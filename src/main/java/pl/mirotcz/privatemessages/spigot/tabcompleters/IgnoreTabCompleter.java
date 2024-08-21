package pl.mirotcz.privatemessages.spigot.tabcompleters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class IgnoreTabCompleter implements TabCompleter {
   public List onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.ignore")) {
         return complete;
      } else {
         if (args.length == 1) {
            boolean vanish_bypass = sender.hasPermission("pm.message.vanished");
            Iterator var7;
            Player player;
            if (args[0].length() != 0) {
               var7 = Bukkit.getOnlinePlayers().iterator();

               label56:
               while(true) {
                  do {
                     if (!var7.hasNext()) {
                        break label56;
                     }

                     player = (Player)var7.next();
                  } while(PrivateMessages.get().getManagers().getVanishManager().isVanishSupported() && PrivateMessages.get().getVanish().isVanished(player) && !vanish_bypass);

                  if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                     complete.add(player.getName());
                  }
               }
            } else {
               var7 = Bukkit.getOnlinePlayers().iterator();

               label43:
               while(true) {
                  do {
                     if (!var7.hasNext()) {
                        break label43;
                     }

                     player = (Player)var7.next();
                  } while(PrivateMessages.get().getManagers().getVanishManager().isVanishSupported() && PrivateMessages.get().getVanish().isVanished(player) && !vanish_bypass);

                  complete.add(player.getName());
               }
            }
         }

         if (complete.isEmpty()) {
            complete.add("[nick]");
         }

         return complete;
      }
   }
}
