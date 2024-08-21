package pl.mirotcz.privatemessages.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class SpyCommand extends Command {
   private PrivateMessages instance;

   public SpyCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (!(sender instanceof ProxiedPlayer)) {
            if (this.instance.getSettings().SPY_IN_CONSOLE) {
               this.instance.getSettings().SPY_IN_CONSOLE = false;
               Messenger.send(sender, this.instance.getMessages().INFO_SPY_DISABLED);
            } else {
               this.instance.getSettings().SPY_IN_CONSOLE = true;
               Messenger.send(sender, this.instance.getMessages().INFO_SPY_ENABLED);
            }

            this.instance.getSettings().save();
         } else {
            ProxiedPlayer pl = (ProxiedPlayer)sender;
            PlayerSettings settings = this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(pl.getName());
            if (settings.isMessageSpyEnabled()) {
               settings.setMessageSpyEnabled(false);
               Messenger.send(pl, (String)this.instance.getMessages().INFO_SPY_DISABLED);
            } else {
               settings.setMessageSpyEnabled(true);
               Messenger.send(pl, (String)this.instance.getMessages().INFO_SPY_ENABLED);
            }

         }
      });
   }
}
