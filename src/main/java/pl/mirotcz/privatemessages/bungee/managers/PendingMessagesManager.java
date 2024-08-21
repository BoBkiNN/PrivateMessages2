package pl.mirotcz.privatemessages.bungee.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class PendingMessagesManager {
   private final Set<Message> pending_messages = new HashSet<>();
   private final List<Message> pending_unread_messages = new ArrayList<>();
   private final PrivateMessages instance;
   private ScheduledTask task = null;
   private volatile boolean task_running = false;

   public PendingMessagesManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public Set<Message> getPendingMessages() {
      synchronized(this.pending_messages) {
         return this.pending_messages;
      }
   }

   public void addPendingMessage(Message message) {
      synchronized(this.pending_messages) {
         this.pending_messages.add(message);
      }
   }

   public List<Message> getPendingUnreadMessages() {
      synchronized(this.pending_unread_messages) {
         return this.pending_unread_messages;
      }
   }

   public void addPendingUnreadMessage(Message message) {
      synchronized(this.pending_unread_messages) {
         if (!this.pending_unread_messages.contains(message)) {
            this.pending_unread_messages.add(message);
         }
      }
   }

   public void removePendingMessage(Message message) {
      synchronized(this.pending_messages) {
         this.pending_messages.remove(message);
      }
   }

   public void removePendingUnreadMessage(Message message) {
      synchronized(this.pending_unread_messages) {
          this.pending_unread_messages.remove(message);
      }
   }

   public void savePendingMessagesToStorage() {
      synchronized(this.pending_messages) {
         this.pending_messages.forEach((message) -> {
            this.instance.getStorage().saveMessage(message);
         });
         this.pending_messages.clear();
      }
   }

   public void savePendingUnreadMessagesToStorage() {
      synchronized(this.pending_unread_messages) {
         this.pending_unread_messages.forEach((message) -> {
            this.instance.getStorage().saveUnreadMessage(message);
         });
         this.pending_unread_messages.clear();
      }
   }

   public void startSaveTask() {
      this.task_running = true;
      this.task = ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         while(this.task_running) {
            this.savePendingMessagesToStorage();
            this.savePendingUnreadMessagesToStorage();

            try {
               Thread.sleep(this.instance.getSettings().MESSAGE_SAVE_INTERVAL_SECONDS * 1000L);
            } catch (InterruptedException var2) {
               var2.printStackTrace();
            }
         }

      });
   }

   public void cancelSaveTask() {
      this.task_running = false;
      this.task.cancel();
   }
}
