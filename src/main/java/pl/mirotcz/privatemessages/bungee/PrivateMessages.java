package pl.mirotcz.privatemessages.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import pl.mirotcz.privatemessages.bungee.managers.Managers;
import pl.mirotcz.privatemessages.bungee.managers.MessagesManager;
import pl.mirotcz.privatemessages.bungee.managers.SettingsManager;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;
import pl.mirotcz.privatemessages.bungee.storage.Storage;
import pl.mirotcz.privatemessages.bungee.vanish.Vanish;

public class PrivateMessages extends Plugin {
    private static PrivateMessages instance;
    private Managers managers;
    private MessageSending message_sending;
    private Utils utils;
    private NotifierTask notifier_task;
    private VanishInfoUpdaterTask vanish_task;

    public void onEnable() {
        instance = this;
        this.managers = new Managers(instance);
        this.managers.load();
        this.message_sending = new MessageSending(instance);
        Messenger.setPrefix(this.getMessages().PLUGIN_PREFIX);
        this.utils = new Utils(instance);
        this.utils.load();
        this.notifier_task = new NotifierTask(instance);
        this.notifier_task.start();
        if (instance.getManagers().getVanishManager().isVanishSupported()) {
            this.vanish_task = new VanishInfoUpdaterTask(instance);
            this.vanish_task.startTask();
        }

        if (this.getSettings().METRICS) {
            new Metrics(instance);
        }

    }

    public void onDisable() {
        this.getManagers().getPendingMessagesManager().savePendingMessagesToStorage();
        this.getManagers().getPendingMessagesManager().savePendingUnreadMessagesToStorage();
        this.getManagers().getPendingMessagesManager().cancelSaveTask();
        this.getManagers().getPlayerSettingsManager().savePendingChanges();
        this.getManagers().getPlayerSettingsManager().cancelSaveTask();
        this.getManagers().getKnownPlayersManager().saveKnownPlayers(this.getManagers().getConfigManagers().getKnownPlayersConfigManager());
        this.getManagers().getKnownPlayersManager().stopTask();
        this.getStorage().clearVansihedPlayersInfo();
        this.getStorage().clearPlayerWaitingForSettingsReloadTable();
        this.getStorage().closePool();
        if (this.notifier_task != null) {
            this.notifier_task.stop();
        }

        if (this.vanish_task != null) {
            this.vanish_task.stopTask();
        }

    }

    public Managers getManagers() {
        return this.managers;
    }

    public MessagesManager getMessages() {
        return this.getManagers().getMessagesManager();
    }

    public SettingsManager getSettings() {
        return this.getManagers().getSettingsManager();
    }

    public Storage getStorage() {
        return this.getManagers().getStorageManager().getStorage();
    }

    public Vanish getVanish() {
        return this.getManagers().getVanishManager().getVanish();
    }

    public MessageSending getMessageSending() {
        return this.message_sending;
    }

    public Utils getUtils() {
        return this.utils;
    }

    public NotifierTask getNotifierTask() {
        return this.notifier_task;
    }

    public VanishInfoUpdaterTask getVanishTask() {
        return this.vanish_task;
    }
}
