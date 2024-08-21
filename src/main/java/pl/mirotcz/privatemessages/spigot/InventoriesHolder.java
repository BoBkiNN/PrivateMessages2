package pl.mirotcz.privatemessages.spigot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class InventoriesHolder {
   public HashMap messageinventories;
   public HashMap messageinventoriestime;
   public JavaPlugin plugin;
   BukkitTask task;

   public InventoriesHolder(JavaPlugin plugin) {
      this.plugin = plugin;
      this.messageinventories = new HashMap();
      this.messageinventoriestime = new HashMap();
      Runnable cleartask = new Runnable() {
         public void run() {
            Iterator iterator = InventoriesHolder.this.messageinventories.entrySet().iterator();

            while(iterator.hasNext()) {
               Map.Entry entry = (Map.Entry)iterator.next();
               Player pl = (Player)entry.getKey();
               if (InventoriesHolder.this.messageinventoriestime.containsKey(pl)) {
                  long storedtime = (Long)InventoriesHolder.this.messageinventoriestime.get(pl);
                  if (System.currentTimeMillis() >= storedtime + 600000L) {
                     InventoriesHolder.this.messageinventories.remove(pl);
                     InventoriesHolder.this.messageinventoriestime.remove(pl);
                  }
               }
            }

         }
      };
      this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, cleartask, 100L, 1000L);
   }

   public void saveInventory(Player player, HashMap inventories) {
      this.messageinventories.put(player, inventories);
      this.messageinventoriestime.put(player, System.currentTimeMillis());
   }

   public void deleteInventory(Player player) {
      this.messageinventories.remove(player);
      this.messageinventoriestime.remove(player);
   }

   public boolean isInventory(Player player, int invnumber) {
      if (this.messageinventories.containsKey(player)) {
         HashMap playerinventories = (HashMap)this.messageinventories.get(player);
         if (playerinventories.containsKey(invnumber)) {
            return true;
         }
      }

      return false;
   }

   public boolean getInventory(Player player, int invnumber) {
      if (this.isInventory(player, invnumber)) {
         HashMap playerinventories = (HashMap)this.messageinventories.get(player);
         player.openInventory((Inventory)playerinventories.get(invnumber));
         return true;
      } else {
         return false;
      }
   }

   public void cancelTasks() {
      this.task.cancel();
   }
}
