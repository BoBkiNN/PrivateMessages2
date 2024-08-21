package pl.mirotcz.privatemessages.bungee.commands;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerTempData;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class ReplyCommand extends Command implements TabExecutor {
   private PrivateMessages instance;

   public ReplyCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (args.length < 1) {
            Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_REPLY);
         } else if (!(sender instanceof ProxiedPlayer)) {
            Messenger.send(sender, this.instance.getMessages().INFO_YOU_NOT_PLAYER);
         } else {
            PlayerTempData data = this.instance.getManagers().getPlayerTempDataManger().getData(sender.getName());
            if (data.getLastMessageSender() == null) {
               Messenger.send(sender, this.instance.getMessages().INFO_NONE_TO_REPLY);
            } else {
               ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(data.getLastMessageSender());
               String recipientName = data.getLastMessageSender();
               boolean offline = false;
               boolean vanish = false;
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
         }
      });
   }

   public Iterable onTabComplete(CommandSender sender, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.reply")) {
         return complete;
      } else {
         if (args.length == 1 && args[0].length() == 0) {
            complete.add("[message]");
         }

         return complete;
      }
   }
}
