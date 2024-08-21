package pl.mirotcz.privatemessages.bungee.data;

public class PlayerTempData {
   private String lastMessageSender = null;

   public synchronized void setLastMessageSender(String name) {
      this.lastMessageSender = name;
   }

   public String getLastMessageSender() {
      return this.lastMessageSender;
   }
}
