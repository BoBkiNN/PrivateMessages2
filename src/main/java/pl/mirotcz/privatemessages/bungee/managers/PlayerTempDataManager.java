package pl.mirotcz.privatemessages.bungee.managers;

import java.util.HashMap;
import java.util.Map;
import pl.mirotcz.privatemessages.bungee.data.PlayerTempData;

public class PlayerTempDataManager {
   private final Map<String, PlayerTempData> tempData = new HashMap<>();

   public void addData(String player_name, PlayerTempData data) {
      synchronized(this.tempData) {
         this.tempData.put(player_name, data);
      }
   }

   public void removeData(String player_name) {
      synchronized(this.tempData) {
          this.tempData.remove(player_name);
      }
   }

   public PlayerTempData getData(String player_name) {
      synchronized(this.tempData) {
         if (this.tempData.containsKey(player_name)) {
            return this.tempData.get(player_name);
         } else {
            PlayerTempData data = new PlayerTempData();
            this.addData(player_name, data);
            return data;
         }
      }
   }
}
