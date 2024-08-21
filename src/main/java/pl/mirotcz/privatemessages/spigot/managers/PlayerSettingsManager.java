package pl.mirotcz.privatemessages.spigot.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;

public class PlayerSettingsManager {
   private Map<String, PlayerSettings> playerSettings;
   private final BlockingQueue<PlayerSettings> pendingChanges = new ArrayBlockingQueue<>(256);
   private BukkitTask registeredTask = null;
   private final PrivateMessages instance;

   public PlayerSettingsManager(PrivateMessages plugin) {
      this.playerSettings = new HashMap<>();
      this.instance = plugin;
   }

   public void load() {
      this.playerSettings = PrivateMessages.get().getStorage().loadAllPlayerSettings();
   }

   public PlayerSettings getPlayerSettings(String playerName) {
       if (!this.playerSettings.containsKey(playerName)) {
           this.createNewSettings(Bukkit.getOfflinePlayer(playerName));
       }
       return this.playerSettings.get(playerName);
   }

   public void addPendingChanges(String playerName) {
      if (!this.pendingChanges.contains(this.getPlayerSettings(playerName))) {
         this.pendingChanges.add(this.getPlayerSettings(playerName));
      }

   }

   public void addPendingChanges(PlayerSettings data) {
      if (!this.pendingChanges.contains(data)) {
         this.pendingChanges.add(data);
      }

   }

   public Map<String, PlayerSettings> getAllSettings() {
      return this.playerSettings;
   }

   public synchronized void addPlayerSettings(String playerName, PlayerSettings settings) {
      this.playerSettings.put(playerName, settings);
   }

   public synchronized void createNewSettings(OfflinePlayer player) {
      PlayerSettings settings = new PlayerSettings();
      settings.setPlayerName(player.getName());
      this.playerSettings.put(player.getName(), settings);
      this.pendingChanges.add(settings);
   }

   public synchronized void removePlayerData(PlayerSettings settings) {
      Iterator<Map.Entry<String, PlayerSettings>> it = this.playerSettings.entrySet().iterator();

      Map.Entry<String, PlayerSettings> entry;
      do {
         if (!it.hasNext()) {
            return;
         }

         entry = it.next();
      } while(entry.getValue() != settings);

      it.remove();
   }

   public void savePendingChanges() {
      BlockingQueue<PlayerSettings> queue = new ArrayBlockingQueue<>(256);
      queue.addAll(this.pendingChanges);
      List<String> already_saved = new ArrayList<>();

       for (PlayerSettings data : queue) {
           if (already_saved.stream().noneMatch((x) -> x.equalsIgnoreCase(data.getPlayerName()))) {
               PrivateMessages.get().getManagers().getStorageManager().getStorage().savePlayerSettings(data);
               already_saved.add(data.getPlayerName());
           }
       }

      queue.clear();
      this.pendingChanges.clear();
   }

   public void registerSaveTask() {
      this.registeredTask = Bukkit.getScheduler().runTaskTimerAsynchronously(PrivateMessages.get(), this::savePendingChanges, 100L, 100L);
   }

   public void cancelSaveTask() {
      if (this.registeredTask != null) {
         this.registeredTask.cancel();
      }

   }

   public void reloadSettings(String player_name) {
      if (player_name != null) {
         PlayerSettings settings = this.getPlayerSettings(player_name);
         if (settings != null) {
            PlayerSettings updated_settings = PrivateMessages.get().getStorage().getPlayerSettings(player_name);
            if (updated_settings != null) {
               String name = updated_settings.getPlayerName();
               int sound_number = updated_settings.getMessageNotificationSoundNumber();
               boolean sound_enabled = updated_settings.isMessageNotificationSoundEnabled();
               boolean spy_enabled = updated_settings.isMessageSpyEnabled();
               Set ignored_players = updated_settings.getIgnoredPlayers();
               boolean msg_enabled = updated_settings.arePrivateMessagesEnabled();
               Bukkit.getScheduler().runTask(this.instance, () -> {
                  settings.load(name, sound_number, sound_enabled, spy_enabled, ignored_players, msg_enabled);
               });
            }

         }
      }
   }
}
