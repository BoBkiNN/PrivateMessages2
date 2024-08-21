package pl.mirotcz.privatemessages.bungee;

import java.util.Iterator;
import java.util.Map;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.bungee.managers.VanishManager;
import pl.mirotcz.privatemessages.bungee.storage.Storage_MySQL;

public class VanishInfoUpdaterTask {
   private PrivateMessages instance;
   private ScheduledTask task = null;

   public VanishInfoUpdaterTask(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void startTask() {
      this.task = this.instance.getProxy().getScheduler().runAsync(this.instance, () -> {
         this.loadAndUpdateVanishedPlayers();
      });
   }

   public void stopTask() {
      if (this.task != null) {
         this.task.cancel();
      }

   }

   public void loadAndUpdateVanishedPlayers() {
      Storage_MySQL storage = (Storage_MySQL)this.instance.getStorage();
      Map players = storage.getVanishedPlayersInfo();
      long time_now = System.currentTimeMillis();
      VanishManager manager = this.instance.getManagers().getVanishManager();
      Iterator var6 = players.entrySet().iterator();

      while(var6.hasNext()) {
         Map.Entry entry = (Map.Entry)var6.next();
         String player_name = (String)entry.getKey();
         long time = (Long)entry.getValue();
         if (time_now - time > 15000L) {
            manager.removeVanishedSomewhere(player_name);
         } else {
            manager.addVanishedSomewhere(player_name);
         }
      }

   }
}
