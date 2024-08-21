package pl.mirotcz.privatemessages.spigot.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

public class Vanish_NoPacket implements Vanish, Listener {
   public boolean isVanished(Player player) {
      try {
         return VanishNoPacket.getManager().isVanished(player);
      } catch (VanishNotLoadedException var3) {
         var3.printStackTrace();
         return false;
      }
   }
}
