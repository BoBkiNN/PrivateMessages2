package pl.mirotcz.privatemessages.spigot.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.inventories.MessagesInventory;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;

public class HistoryCommand {
   public HistoryCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.history")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         Player pl = (Player)sender;
         boolean global_history = false;
         if (pl.isSleeping()) {
            return;
         }

         if (args.length > 2) {
            Messenger.send(sender, PrivateMessages.get().getMessages().HELP_COMMAND_PMHISTORY);
            return;
         }

         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_PLEASE_WAIT);
         List<Message> messages = new ArrayList<>();
         if (args.length == 0) {
            messages = PrivateMessages.get().getStorage().getMessagesFromHistory(pl.getName());
         } else if (args.length == 1) {
            messages = PrivateMessages.get().getStorage().getMessagesFromHistory(pl.getName(), args[0]);
         } else if (args[0].equalsIgnoreCase("*") && args[1].equalsIgnoreCase("*")) {
            if (!sender.hasPermission("pm.admin")) {
               Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
               return;
            }

            global_history = true;
            if (PrivateMessages.get().getGlobalHistoryInv() == null) {
               messages = PrivateMessages.get().getStorage().getMessagesFromHistory();
            }
         } else if (args[1].equalsIgnoreCase("*")) {
            if (!sender.hasPermission("pm.admin")) {
               Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
               return;
            }

            messages = PrivateMessages.get().getStorage().getMessagesFromHistory(args[0]);
         } else {
            messages = PrivateMessages.get().getStorage().getMessagesFromHistory(args[0], args[1]);
         }

         MessagesInventory inv;
         boolean from_cache = false;
         if (global_history && PrivateMessages.get().getGlobalHistoryInv() != null) {
            from_cache = true;
            inv = PrivateMessages.get().getGlobalHistoryInv();
         } else {
            if (messages.isEmpty()) {
               Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_MESSAGES);
               return;
            }

            inv = new MessagesInventory(PrivateMessages.get(), messages, pl.getUniqueId());
            inv.load();
            inv.addExtraPageItems();
         }

         if (global_history && !from_cache) {
            PrivateMessages.get().setGlobalHistoryInv(inv);
            PrivateMessages.get().setLastGlobalHistoryInvGen(System.currentTimeMillis());
         }

         PrivateMessages.get().getManagers().getPlayerTempDataManger().getData(pl).setLastMessagesInventory(inv);
         Bukkit.getScheduler().runTask(PrivateMessages.get(), () -> pl.openInventory(inv.get(1)));
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
