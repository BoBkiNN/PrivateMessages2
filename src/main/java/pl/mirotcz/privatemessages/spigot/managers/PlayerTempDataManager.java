package pl.mirotcz.privatemessages.spigot.managers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.entity.Player;
import pl.mirotcz.privatemessages.spigot.data.PlayerTempData;

public class PlayerTempDataManager {
   private final Set<PlayerTempData> tempData = new HashSet<>();

   public void addData(PlayerTempData data) {
      synchronized(this.tempData) {
         this.tempData.add(data);
      }
   }

   public void removeData(PlayerTempData data) {
      synchronized(this.tempData) {
         this.tempData.remove(data);
      }
   }

   public PlayerTempData getData(Player player) {
      synchronized(this.tempData) {
         Iterator<PlayerTempData> var3 = this.tempData.iterator();

         PlayerTempData data;
         do {
            if (!var3.hasNext()) {
               data = new PlayerTempData(player);
               this.addData(data);
               return data;
            }

            data = var3.next();
         } while(data.getPlayer() != player);

         return data;
      }
   }
}
