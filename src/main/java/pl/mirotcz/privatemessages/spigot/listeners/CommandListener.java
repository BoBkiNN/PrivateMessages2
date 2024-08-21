package pl.mirotcz.privatemessages.spigot.listeners;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.commands.ClearCommand;
import pl.mirotcz.privatemessages.spigot.commands.HistoryCommand;
import pl.mirotcz.privatemessages.spigot.commands.IgnoreCommand;
import pl.mirotcz.privatemessages.spigot.commands.IgnoredCommand;
import pl.mirotcz.privatemessages.spigot.commands.MessageCommand;
import pl.mirotcz.privatemessages.spigot.commands.ReadCommand;
import pl.mirotcz.privatemessages.spigot.commands.ReloadCommand;
import pl.mirotcz.privatemessages.spigot.commands.ReplyCommand;
import pl.mirotcz.privatemessages.spigot.commands.SoundCommand;
import pl.mirotcz.privatemessages.spigot.commands.SpyCommand;
import pl.mirotcz.privatemessages.spigot.commands.ToggleCommand;
import pl.mirotcz.privatemessages.spigot.tabcompleters.IgnoreTabCompleter;
import pl.mirotcz.privatemessages.spigot.tabcompleters.MessageTabCompleter;
import pl.mirotcz.privatemessages.spigot.tabcompleters.ReadTabCompleter;
import pl.mirotcz.privatemessages.spigot.tabcompleters.ReplyTabCompleter;

public class CommandListener implements CommandExecutor {
   private PrivateMessages instance;

   public CommandListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
      switch (command.getName().toLowerCase()) {
         case "message":
            if (!this.instance.getSettings().BUNGEECORD || sender instanceof ConsoleCommandSender) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new MessageCommand(sender, args);
               });
            }
            break;
         case "reply":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new ReplyCommand(sender, args);
               });
            }
            break;
         case "pmhistory":
            Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
               new HistoryCommand(sender, args);
            });
            break;
         case "pmclear":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new ClearCommand(sender, args);
               });
            }
            break;
         case "pmsound":
            Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
               new SoundCommand(sender, args);
            });
            break;
         case "pmread":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new ReadCommand(sender, args);
               });
            }
            break;
         case "pmspy":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new SpyCommand(sender, args);
               });
            }
            break;
         case "pmignore":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new IgnoreCommand(sender, args);
               });
            }
            break;
         case "pmignored":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new IgnoredCommand(sender, args);
               });
            }
            break;
         case "pmtoggle":
            if (!this.instance.getSettings().BUNGEECORD) {
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  new ToggleCommand(sender, args);
               });
            }
            break;
         case "pmreload":
            Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
               new ReloadCommand(sender, args);
            });
      }

      return true;
   }

   public void registerCommands() {
      Iterator var1 = this.instance.getDescription().getCommands().keySet().iterator();

      while(var1.hasNext()) {
         String command = (String)var1.next();
         this.instance.getCommand(command).setExecutor(this);
      }

   }

   public void registerTabCompleters() {
      if (!this.instance.getSettings().BUNGEECORD) {
         this.instance.getCommand("message").setTabCompleter(new MessageTabCompleter());
         this.instance.getCommand("reply").setTabCompleter(new ReplyTabCompleter());
         this.instance.getCommand("pmread").setTabCompleter(new ReadTabCompleter());
         this.instance.getCommand("pmignore").setTabCompleter(new IgnoreTabCompleter());
      }

   }
}
