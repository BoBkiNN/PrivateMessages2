package pl.mirotcz.privatemessages.spigot.vanish;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Vanish_Essentials implements Vanish, Listener {
   private Essentials ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");

   public boolean isVanished(Player player) {
      return this.ess.getUser(player).isVanished();
   }
}
