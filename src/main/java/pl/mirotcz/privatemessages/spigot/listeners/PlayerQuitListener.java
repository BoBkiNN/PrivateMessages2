package pl.mirotcz.privatemessages.spigot.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class PlayerQuitListener implements Listener {
   private PrivateMessages instance;

   public PlayerQuitListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent e) {
      Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
         this.instance.getManagers().getPlayerTempDataManger().removeData(this.instance.getManagers().getPlayerTempDataManger().getData(e.getPlayer()));
      });
   }
}
