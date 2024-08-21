package pl.mirotcz.privatemessages.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class ClearCommand extends Command {
   private PrivateMessages instance;

   public ClearCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (!(sender instanceof ProxiedPlayer)) {
            Messenger.send(sender, this.instance.getMessages().INFO_YOU_NOT_PLAYER);
         } else if (args.length != 0) {
            Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_PMCLEAR);
         } else {
            ProxiedPlayer pl = (ProxiedPlayer)sender;
            this.instance.getStorage().clearUnreadMessages(pl.getName());
            Messenger.send(sender, this.instance.getMessages().INFO_MESSAGES_CLEARED);
         }
      });
   }
}
