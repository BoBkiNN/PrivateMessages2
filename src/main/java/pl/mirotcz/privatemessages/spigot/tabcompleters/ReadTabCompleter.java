package pl.mirotcz.privatemessages.spigot.tabcompleters;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ReadTabCompleter implements TabCompleter {
   public List onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.read")) {
         return complete;
      } else {
         if (args.length == 1 && args[0].length() == 0) {
            complete.add("[msg number]");
         }

         return complete;
      }
   }
}
