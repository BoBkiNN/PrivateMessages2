package pl.mirotcz.privatemessages.bungee.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class IgnoreCommand extends Command implements TabExecutor {
   private PrivateMessages instance;

   public IgnoreCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (!(sender instanceof ProxiedPlayer)) {
            Messenger.send(sender, this.instance.getMessages().INFO_YOU_NOT_PLAYER);
         } else if (args.length != 1) {
            Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_IGNORE);
         } else {
            String name = args[0];
            ProxiedPlayer pl = (ProxiedPlayer)sender;
            PlayerSettings settings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
            Set ignored = settings.getIgnoredPlayers();
            if (ignored.contains(name)) {
               ignored.remove(name);
               Messenger.send(pl, (String)this.instance.getMessages().INFO_PLAYER_NOW_NOT_IGNORED.replaceAll("<player>", name));
            } else {
               ignored.add(name);
               Messenger.send(pl, (String)this.instance.getMessages().INFO_PLAYER_NOW_IGNORED.replaceAll("<player>", name));
            }

            settings.setIgnoredPlayers(ignored);
         }
      });
   }

   public Iterable onTabComplete(CommandSender sender, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.ignore")) {
         return complete;
      } else {
         if (args.length == 1) {
            boolean vanish_bypass = sender.hasPermission("pm.message.vanished");
            Iterator var5;
            ProxiedPlayer player;
            if (args[0].length() != 0) {
               var5 = ProxyServer.getInstance().getPlayers().iterator();

               label62:
               while(true) {
                  do {
                     if (!var5.hasNext()) {
                        break label62;
                     }

                     player = (ProxiedPlayer)var5.next();
                  } while(this.instance.getManagers().getVanishManager().isVanishSupported() && this.instance.getVanish().isVanished(player) && !vanish_bypass);

                  if (!this.instance.getManagers().getVanishManager().isVanishedSomewhere(player.getName()) && player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                     complete.add(player.getName());
                  }
               }
            } else {
               var5 = ProxyServer.getInstance().getPlayers().iterator();

               label49:
               while(true) {
                  do {
                     if (!var5.hasNext()) {
                        break label49;
                     }

                     player = (ProxiedPlayer)var5.next();
                  } while(this.instance.getManagers().getVanishManager().isVanishSupported() && this.instance.getVanish().isVanished(player) && !vanish_bypass);

                  if (!this.instance.getManagers().getVanishManager().isVanishedSomewhere(player.getName())) {
                     complete.add(player.getName());
                  }
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
