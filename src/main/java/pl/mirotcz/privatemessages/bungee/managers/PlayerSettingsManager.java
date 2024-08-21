package pl.mirotcz.privatemessages.bungee.managers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSettingsManager {
    private final BlockingQueue<PlayerSettings> pendingChanges = new ArrayBlockingQueue<>(256);
    private final PrivateMessages instance;
    private ConcurrentHashMap<String, PlayerSettings> playerSettings;
    private ScheduledTask registeredTask = null;
    private volatile boolean task_running = false;

    public PlayerSettingsManager(PrivateMessages plugin) {
        this.playerSettings = new ConcurrentHashMap<>();
        this.instance = plugin;
    }

    public void load() {
        this.playerSettings = this.instance.getStorage().loadAllPlayerSettings();
    }

    public PlayerSettings getPlayerSettings(String playerName) {
        if (!this.playerSettings.containsKey(playerName)) {
            this.createNewSettings(playerName);
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

    public synchronized void createNewSettings(String player_name) {
        PlayerSettings settings = new PlayerSettings();
        settings.setPlayerName(player_name);
        this.playerSettings.put(player_name, settings);
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
        } while (entry.getValue() != settings);

        it.remove();
    }

    public void savePendingChanges() {
        BlockingQueue<PlayerSettings> queue = new ArrayBlockingQueue(256);
        queue.addAll(this.pendingChanges);
        List<String> already_saved = new ArrayList<>();

        for (PlayerSettings data : queue) {
            if (already_saved.stream().noneMatch((x) -> x.equalsIgnoreCase(data.getPlayerName()))) {
                this.instance.getStorage().savePlayerSettings(data);
                this.instance.getStorage().addPlayerWatingForSettingsReload(data.getPlayerName());
                already_saved.add(data.getPlayerName());
            }
        }

        queue.clear();
        this.pendingChanges.clear();
    }

    public void registerSaveTask() {
        this.task_running = true;
        this.registeredTask = ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
            while (this.task_running) {
                this.savePendingChanges();

                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }
            }

        });
    }

    public void cancelSaveTask() {
        this.task_running = false;
        if (this.registeredTask != null) {
            this.registeredTask.cancel();
        }

    }
}
