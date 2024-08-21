package pl.mirotcz.privatemessages.spigot.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class PendingMessagesManager {
   private final Set<Message> pending_messages = new HashSet<>();
   private final List<Message> pending_unread_messages = new ArrayList<>();
   private final PrivateMessages instance;
   private BukkitTask task = null;

   public PendingMessagesManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public Set<Message> getPendingMessages() {
      return this.pending_messages;
   }

   public List<Message> getPendingUnreadMessages() {
      return this.pending_unread_messages;
   }

   public void addPendingMessage(Message message) {
      synchronized(this.pending_messages) {
         this.pending_messages.add(message);
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
      this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.instance, () -> {
         this.savePendingMessagesToStorage();
         this.savePendingUnreadMessagesToStorage();
      }, this.instance.getSettings().MESSAGE_SAVE_INTERVAL_SECONDS * 20L, this.instance.getSettings().MESSAGE_SAVE_INTERVAL_SECONDS * 20L);
   }

   public void cancelSaveTask() {
      if (Bukkit.getScheduler().isCurrentlyRunning(this.task.getTaskId())) {
         Bukkit.getScheduler().cancelTask(this.task.getTaskId());
      }

   }
}
