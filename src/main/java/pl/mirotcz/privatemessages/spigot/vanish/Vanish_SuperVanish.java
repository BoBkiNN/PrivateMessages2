package pl.mirotcz.privatemessages.spigot.vanish;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Vanish_SuperVanish implements Vanish, Listener {
   public boolean isVanished(Player player) {
      return VanishAPI.isInvisible(player);
   }
}
