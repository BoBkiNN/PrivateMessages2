package pl.mirotcz.privatemessages.bungee.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class MessageCommand extends Command implements TabExecutor {
   private PrivateMessages instance;

   public MessageCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (args.length < 2) {
            Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_MESSAGE);
         } else {
            ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
            String recipientName = args[0];
            boolean vanish = false;
            boolean offline = false;
            boolean known_player_name = false;
            if (pl != null && pl.isConnected()) {
               recipientName = pl.getName();
               if (this.instance.getManagers().getVanishManager().isVanishSupported()) {
                  if (this.instance.getManagers().getVanishManager().getVanish().isVanished(pl)) {
                     vanish = true;
                  }
               } else if (this.instance.getManagers().getVanishManager().isVanishedSomewhere(recipientName)) {
                  vanish = true;
               }
            } else {
               if (!this.instance.getSettings().ALLOW_SENDING_MESSAGES_TO_OFFLINE_PLAYERS) {
                  Messenger.send(sender, this.instance.getMessages().INFO_OFFLINE_MESSAGING_DISABLED);
                  return;
               }

               String known_valid_player_name = this.instance.getManagers().getKnownPlayersManager().getKnownPlayer(recipientName);
               if (known_valid_player_name != null) {
                  recipientName = known_valid_player_name;
                  known_player_name = true;
               }

               offline = true;
            }

            this.instance.getMessageSending().sendMessage(sender, pl, recipientName, known_player_name, offline, vanish, args);
         }
      });
   }

   public Iterable onTabComplete(CommandSender sender, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.message")) {
         return complete;
      } else {
         if (args.length == 1) {
            boolean vanish_bypass = sender.hasPermission("pm.message.vanished");
            Iterator var5;
            ProxiedPlayer player;
            if (args[0].length() != 0) {
               var5 = ProxyServer.getInstance().getPlayers().iterator();

               label64:
               while(true) {
                  do {
                     if (!var5.hasNext()) {
                        break label64;
                     }

                     player = (ProxiedPlayer)var5.next();
                  } while(this.instance.getManagers().getVanishManager().isVanishSupported() && this.instance.getVanish().isVanished(player) && !vanish_bypass);

                  if (!this.instance.getManagers().getVanishManager().isVanishedSomewhere(player.getName()) && player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                     complete.add(player.getName());
                  }
               }
            } else {
               var5 = ProxyServer.getInstance().getPlayers().iterator();

               label51:
               while(true) {
                  do {
                     if (!var5.hasNext()) {
                        break label51;
                     }

                     player = (ProxiedPlayer)var5.next();
                  } while(this.instance.getManagers().getVanishManager().isVanishSupported() && this.instance.getVanish().isVanished(player) && !vanish_bypass);

                  if (!this.instance.getManagers().getVanishManager().isVanishedSomewhere(player.getName())) {
                     complete.add(player.getName());
                  }
               }
            }
         }

         if (args.length == 2 && args[1].length() == 0) {
            complete.add("[message]");
         }

         return complete;
      }
   }
}
