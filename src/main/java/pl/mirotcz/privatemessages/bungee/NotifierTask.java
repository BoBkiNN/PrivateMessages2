package pl.mirotcz.privatemessages.bungee;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;

public class NotifierTask {
   private PrivateMessages instance;
   private ScheduledTask task = null;
   private volatile ConcurrentLinkedQueue players_waiting_for_notification = new ConcurrentLinkedQueue();
   private volatile boolean task_active = false;

   public NotifierTask(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void start() {
      this.task_active = true;
      this.task = this.instance.getProxy().getScheduler().runAsync(this.instance, () -> {
         while(this.task_active) {
            Iterator it = this.players_waiting_for_notification.iterator();

            while(it.hasNext()) {
               String player_name = (String)it.next();
               int count = 0;
               Iterator var4 = this.instance.getManagers().getPendingMessagesManager().getPendingUnreadMessages().iterator();

               while(var4.hasNext()) {
                  Message m = (Message)var4.next();
                  if (m.getRecipientName().equalsIgnoreCase(player_name)) {
                     ++count;
                  }
               }

               int stored_unread_messages = this.instance.getStorage().getUnreadMessages(player_name).size();
               count += stored_unread_messages;
               if (count > 0) {
                  Messenger.send(this.instance.getProxy().getPlayer(player_name), (String)this.instance.getMessages().INFO_YOU_HAVE_UNREAD_MESSAGES.replaceAll("<number>", String.valueOf(count)));
               }

               it.remove();

               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var7) {
                  var7.printStackTrace();
               }
            }

            try {
               Thread.sleep(2000L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }

      });
   }

   public void stop() {
      this.task_active = false;
      if (this.task != null) {
         this.task.cancel();
      }

   }

   public void addPlayerWaitingForNotification(String name) {
      if (!this.players_waiting_for_notification.contains(name)) {
         this.players_waiting_for_notification.add(name);
      }

   }
}
