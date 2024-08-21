package pl.mirotcz.privatemessages.bungee.commands;

import java.util.Set;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class IgnoredCommand extends Command {
   private PrivateMessages instance;

   public IgnoredCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (!(sender instanceof ProxiedPlayer)) {
            Messenger.send(sender, this.instance.getMessages().INFO_YOU_NOT_PLAYER);
         } else if (args.length != 0) {
            Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_IGNORED);
         } else {
            ProxiedPlayer pl = (ProxiedPlayer)sender;
            PlayerSettings settings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
            Set ignored = settings.getIgnoredPlayers();
            Messenger.send(pl, (String)this.instance.getMessages().INFO_IGNORED_PLAYERS.replaceAll("<ignored>", ignored.toString()));
         }
      });
   }
}
