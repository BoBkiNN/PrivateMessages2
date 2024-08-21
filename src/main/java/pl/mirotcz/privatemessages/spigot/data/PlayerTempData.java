package pl.mirotcz.privatemessages.spigot.data;

import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.inventories.MessagesInventory;

public class PlayerTempData {
   private Player player = null;
   private String lastMessageSender = null;
   private MessagesInventory lastMessagesInv = null;
   private boolean pm_sound_tip_received = false;

   public PlayerTempData(Player pl) {
      this.player = pl;
   }

   public Player getPlayer() {
      return this.player;
   }

   public synchronized void setLastMessageSender(String name) {
      this.lastMessageSender = name;
   }

   public String getLastMessageSender() {
      return this.lastMessageSender;
   }

   public synchronized void setLastMessagesInventory(MessagesInventory inv) {
      this.lastMessagesInv = inv;
   }

   public MessagesInventory getLastMessagesInventory() {
      return this.lastMessagesInv;
   }

   public boolean hasReceivedPmSoundTip() {
      return this.pm_sound_tip_received;
   }

   public synchronized void setReceivedPmSoundTip(boolean value) {
      this.pm_sound_tip_received = value;
   }
}
