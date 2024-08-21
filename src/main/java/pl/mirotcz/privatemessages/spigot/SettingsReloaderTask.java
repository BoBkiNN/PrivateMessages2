package pl.mirotcz.privatemessages.spigot;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.managers.PlayerSettingsManager;

public class SettingsReloaderTask {
   private PrivateMessages instance;
   private BukkitTask task = null;
   private volatile ConcurrentLinkedQueue players_waiting_for_reload = new ConcurrentLinkedQueue();
   private volatile boolean task_active = false;

   public SettingsReloaderTask(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void start() {
      this.task_active = true;
      PlayerSettingsManager manager = this.instance.getManagers().getPlayerSettingsManager();
      this.task = Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
         while(this.task_active) {
            Iterator it = this.players_waiting_for_reload.iterator();

            while(it.hasNext()) {
               String player_name = (String)it.next();
               PlayerSettings settings = manager.getPlayerSettings(player_name);
               if (settings != null) {
                  manager.reloadSettings(player_name);
                  it.remove();

                  try {
                     Thread.sleep(500L);
                  } catch (InterruptedException var7) {
                     var7.printStackTrace();
                  }
               }
            }

            try {
               Thread.sleep(2000L);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }

      });
   }

   public void stop() {
      this.task_active = false;
      if (this.task != null && Bukkit.getScheduler().isCurrentlyRunning(this.task.getTaskId())) {
         this.task.cancel();
      }

   }

   public void addPlayerWaitingForReload(String name) {
      if (!this.players_waiting_for_reload.contains(name)) {
         this.players_waiting_for_reload.add(name);
      }

   }
}
