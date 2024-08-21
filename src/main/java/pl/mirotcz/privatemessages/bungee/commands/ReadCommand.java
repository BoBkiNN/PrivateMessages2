package pl.mirotcz.privatemessages.bungee.commands;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;
import pl.mirotcz.privatemessages.bungee.utils.MathUtils;

public class ReadCommand extends Command implements TabExecutor {
   private PrivateMessages instance;

   public ReadCommand(String name, String permission, String[] aliases, PrivateMessages plugin) {
      super(name, permission, aliases);
      this.instance = plugin;
   }

   public Iterable onTabComplete(CommandSender sender, String[] args) {
      List complete = new ArrayList();
      if (!sender.hasPermission("pm.read")) {
         return complete;
      } else {
         if (args.length == 1 && args[0].length() == 0) {
            complete.add("[msg number]");
         }

         return complete;
      }
   }

   public void execute(CommandSender sender, String[] args) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         if (!(sender instanceof ProxiedPlayer)) {
            Messenger.send(sender, this.instance.getMessages().INFO_YOU_NOT_PLAYER);
         } else {
            ProxiedPlayer pl = (ProxiedPlayer)sender;
            if (args.length != 1) {
               Messenger.send(sender, this.instance.getMessages().HELP_COMMAND_PMREAD);
            } else if (!MathUtils.isInteger(args[0])) {
               Messenger.send(sender, this.instance.getMessages().INFO_INVALID_NUMBER);
            } else {
               int number = Integer.parseInt(args[0]);
               List stored_messages = this.instance.getStorage().getUnreadMessages(pl.getName());
               List all_pending_messages = this.instance.getManagers().getPendingMessagesManager().getPendingUnreadMessages();
               List all_messages = new ArrayList();

               for(int i = all_pending_messages.size() - 1; i >= 0; --i) {
                  Message m = (Message)all_pending_messages.get(i);
                  if (m.getRecipientName().equalsIgnoreCase(pl.getName())) {
                     all_messages.add(m);
                  }
               }

               all_messages.addAll(stored_messages);
               if (all_messages.isEmpty()) {
                  Messenger.send(sender, this.instance.getMessages().INFO_NO_MESSAGES);
               } else if (all_messages.size() < number) {
                  Messenger.send(sender, this.instance.getMessages().INFO_MESSAGE_NOT_FOUND);
               } else {
                  Message message = (Message)all_messages.get(number - 1);
                  Messenger.send(sender, this.instance.getMessages().INFO_MESSAGE_READ_NUMBER.replaceAll("<number>", String.valueOf(number)));
                  Messenger.send(sender, this.instance.getMessages().INFO_MESSAGE_READ_SENDER.replaceAll("<sender>", message.getSenderName()));
                  Messenger.send(sender, this.instance.getMessages().INFO_MESSAGE_READ_MESSAGE.replaceAll("<message>", message.getMessageContent()));
                  Messenger.send(sender, this.instance.getMessages().INFO_CLEAR_MESSAGES_TIP);
               }
            }
         }
      });
   }
}
