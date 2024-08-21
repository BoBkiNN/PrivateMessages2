package pl.mirotcz.privatemessages.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class ReloadCommand extends Command {
   private PrivateMessages instance;

   public ReloadCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public void execute(CommandSender sender, String[] args) {
      this.instance.getUtils().getMainUtils().reloadPlugin();
      Messenger.send(sender, this.instance.getMessages().INFO_CONFIG_RELOADED);
   }
}
