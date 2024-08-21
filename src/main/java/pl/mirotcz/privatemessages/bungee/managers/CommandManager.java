package pl.mirotcz.privatemessages.bungee.managers;

import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.commands.ClearCommand;
import pl.mirotcz.privatemessages.bungee.commands.IgnoreCommand;
import pl.mirotcz.privatemessages.bungee.commands.IgnoredCommand;
import pl.mirotcz.privatemessages.bungee.commands.MessageCommand;
import pl.mirotcz.privatemessages.bungee.commands.ReadCommand;
import pl.mirotcz.privatemessages.bungee.commands.ReloadCommand;
import pl.mirotcz.privatemessages.bungee.commands.ReplyCommand;
import pl.mirotcz.privatemessages.bungee.commands.SpyCommand;
import pl.mirotcz.privatemessages.bungee.commands.ToggleCommand;

public class CommandManager {
   private PrivateMessages instance;

   public CommandManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void registerCommands() {
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new ClearCommand("pmclear", "pm.clear", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new IgnoredCommand("pmignored", "pm.ignore", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new IgnoreCommand("pmignore", "pm.ignore", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new MessageCommand("message", "pm.message", new String[]{"msg", "m", "pm", "tell", "w", "whisper", "t"}, this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new ReadCommand("pmread", "pm.read", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new ReloadCommand("pmreload", "pm.reload", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new ReplyCommand("reply", "pm.reply", new String[]{"r"}, this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new SpyCommand("pmspy", "pm.spy", new String[0], this.instance));
      this.instance.getProxy().getPluginManager().registerCommand(this.instance, new ToggleCommand("pmtoggle", "pm.toggle", new String[0], this.instance));
   }
}
