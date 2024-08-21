package pl.mirotcz.privatemessages.spigot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.mirotcz.privatemessages.Message;

public class PrivateMessageEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final Message message;

   public PrivateMessageEvent(Message msg) {
      this.message = msg;
   }

   public Message getMessage() {
      return this.message;
   }

   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }
}
