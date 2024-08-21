package pl.mirotcz.privatemessages.spigot;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import pl.mirotcz.privatemessages.spigot.managers.VanishManager;
import pl.mirotcz.privatemessages.spigot.storage.Storage_MySQL;

public class BungeeVanishNotifierTask {
   private BukkitTask task = null;
   private PrivateMessages instance;

   public BungeeVanishNotifierTask(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void startTask() {
      this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.instance, () -> {
         VanishManager vanish = this.instance.getManagers().getVanishManager();
         Storage_MySQL storage = (Storage_MySQL)this.instance.getStorage();
         Iterator var3 = Bukkit.getOnlinePlayers().iterator();

         while(var3.hasNext()) {
            Player pl = (Player)var3.next();
            boolean vanished = vanish.isVanishSupported() && vanish.getVanish().isVanished(pl);
            if (vanished) {
               storage.sendVanishedPlayerInfo(pl.getName());
            }
         }

      }, 100L, 100L);
   }

   public void stopTask() {
      if (this.task != null && Bukkit.getScheduler().isCurrentlyRunning(this.task.getTaskId())) {
         this.task.cancel();
      }

   }
}
