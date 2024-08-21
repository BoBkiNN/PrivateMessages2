package pl.mirotcz.privatemessages.spigot.managers;

import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class Managers {
   private ConfigManagers configManagers;
   private SoundDataManager soundDataManager;
   private PlayerSettingsManager playerSettingsManager;
   private ListenersManager listenersManager;
   private StorageManager storageManager;
   private SettingsManager settingsManager;
   private MessagesManager messagesManager;
   private VanishManager vanishManager;
   private MenuManager menuManager;
   private PendingMessagesManager pendingMessagesManager;
   private PlayerTempDataManager playerTempDataManager;
   private final PrivateMessages instance;

   public Managers(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      this.configManagers = new ConfigManagers();
      this.configManagers.setupConfigs();
      this.messagesManager = new MessagesManager();
      this.messagesManager.load();
      this.settingsManager = new SettingsManager();
      this.settingsManager.load();
      this.storageManager = new StorageManager(this.instance);
      this.storageManager.setupStorage();
      this.soundDataManager = new SoundDataManager();
      this.soundDataManager.load();
      this.playerSettingsManager = new PlayerSettingsManager(this.instance);
      this.playerSettingsManager.load();
      this.playerSettingsManager.registerSaveTask();
      this.vanishManager = new VanishManager(this.instance);
      this.vanishManager.setupVanish();
      this.listenersManager = new ListenersManager(this.instance);
      this.listenersManager.setupListeners();
      this.pendingMessagesManager = new PendingMessagesManager(this.instance);
      this.pendingMessagesManager.startSaveTask();
      this.menuManager = new MenuManager(this.instance);
      this.menuManager.load();
      this.playerTempDataManager = new PlayerTempDataManager();
   }

   public ConfigManagers getConfigManagers() {
      return this.configManagers;
   }

   public SettingsManager getSettingsManager() {
      return this.settingsManager;
   }

   public MessagesManager getMessagesManager() {
      return this.messagesManager;
   }

   public StorageManager getStorageManager() {
      return this.storageManager;
   }

   public SoundDataManager getSoundDataManager() {
      return this.soundDataManager;
   }

   public VanishManager getVanishManager() {
      return this.vanishManager;
   }

   public PlayerSettingsManager getPlayerSettingsManager() {
      return this.playerSettingsManager;
   }

   public PendingMessagesManager getPendingMessagesManager() {
      return this.pendingMessagesManager;
   }

   public MenuManager getMenuManager() {
      return this.menuManager;
   }

   public PlayerTempDataManager getPlayerTempDataManger() {
      return this.playerTempDataManager;
   }

   public ListenersManager getListenersManager() {
      return this.listenersManager;
   }
}
