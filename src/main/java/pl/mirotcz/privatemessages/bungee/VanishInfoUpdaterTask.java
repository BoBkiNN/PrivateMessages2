package pl.mirotcz.privatemessages.bungee;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.bungee.managers.VanishManager;
import pl.mirotcz.privatemessages.bungee.storage.Storage_MySQL;

import java.util.Map;

public class VanishInfoUpdaterTask {
    private final PrivateMessages instance;
    private ScheduledTask task = null;

    public VanishInfoUpdaterTask(PrivateMessages plugin) {
        this.instance = plugin;
    }

    public void startTask() {
        this.task = this.instance.getProxy().getScheduler().runAsync(this.instance, this::loadAndUpdateVanishedPlayers);
    }

    public void stopTask() {
        if (this.task != null) {
            this.task.cancel();
        }

    }

    public void loadAndUpdateVanishedPlayers() {
        Storage_MySQL storage = (Storage_MySQL) this.instance.getStorage();
        Map<String, Long> players = storage.getVanishedPlayersInfo();
        long time_now = System.currentTimeMillis();
        VanishManager manager = this.instance.getManagers().getVanishManager();

        for (Map.Entry<String, Long> entry : players.entrySet()) {
            String player_name = entry.getKey();
            long time = entry.getValue();
            if (time_now - time > 15000L) {
                manager.removeVanishedSomewhere(player_name);
            } else {
                manager.addVanishedSomewhere(player_name);
            }
        }

    }
}
