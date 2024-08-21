package pl.mirotcz.privatemessages.bungee.events;

import net.md_5.bungee.api.plugin.Event;
import pl.mirotcz.privatemessages.Message;

public class PrivateMessageEvent extends Event {
   private Message message = null;

   public PrivateMessageEvent(Message msg) {
      this.message = msg;
   }

   public Message getMessage() {
      return this.message;
   }
}
