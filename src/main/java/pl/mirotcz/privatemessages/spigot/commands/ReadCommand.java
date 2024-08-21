package pl.mirotcz.privatemessages.spigot.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;

public class ReadCommand {
   public ReadCommand(CommandSender sender, String[] args) {
      if (sender.hasPermission("pm.read")) {
         if (!(sender instanceof Player)) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_YOU_NOT_PLAYER);
            return;
         }

         Player pl = (Player)sender;
         if (args.length != 1) {
            Messenger.send(sender, PrivateMessages.get().getMessages().HELP_COMMAND_PMREAD);
            return;
         }

         if (!MainUtils.isInteger(args[0])) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_INVALID_NUMBER);
            return;
         }

         int number = Integer.parseInt(args[0]);
         List stored_messages = PrivateMessages.get().getStorage().getUnreadMessages(pl.getName());
         List all_pending_messages = PrivateMessages.get().getManagers().getPendingMessagesManager().getPendingUnreadMessages();
         List all_messages = new ArrayList();

         for(int i = all_pending_messages.size() - 1; i >= 0; --i) {
            Message m = (Message)all_pending_messages.get(i);
            if (m.getRecipientName().equalsIgnoreCase(pl.getName())) {
               all_messages.add(m);
            }
         }

         all_messages.addAll(stored_messages);
         if (all_messages.isEmpty()) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_MESSAGES);
            return;
         }

         if (all_messages.size() < number) {
            Messenger.send(sender, PrivateMessages.get().getMessages().INFO_MESSAGE_NOT_FOUND);
            return;
         }

         Message message = (Message)all_messages.get(number - 1);
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_MESSAGE_READ_NUMBER.replaceAll("<number>", String.valueOf(number)));
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_MESSAGE_READ_SENDER.replaceAll("<sender>", message.getSenderName()));
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_MESSAGE_READ_MESSAGE.replaceAll("<message>", message.getMessageContent()));
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_CLEAR_MESSAGES_TIP);
      } else {
         Messenger.send(sender, PrivateMessages.get().getMessages().INFO_NO_PERMISSION);
      }

   }
}
