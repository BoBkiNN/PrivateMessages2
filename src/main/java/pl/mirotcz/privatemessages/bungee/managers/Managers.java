package pl.mirotcz.privatemessages.bungee.managers;

import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class Managers {
   private ConfigManagers configManagers;
   private PlayerSettingsManager playerSettingsManager;
   private ListenersManager listenersManager;
   private StorageManager storageManager;
   private SettingsManager settingsManager;
   private MessagesManager messagesManager;
   private PendingMessagesManager pendingMessagesManager;
   private PlayerTempDataManager playerTempDataManager;
   private VanishManager vanishManager;
   private KnownPlayersManager knownPlayersManager;
   private CommandManager commandManager;
   private PrivateMessages instance;

   public Managers(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      this.configManagers = new ConfigManagers(this.instance);
      this.configManagers.setupConfigs();
      this.knownPlayersManager = new KnownPlayersManager(this.instance);
      this.knownPlayersManager.loadNames(this.configManagers.getKnownPlayersConfigManager());
      this.knownPlayersManager.startTask();
      this.messagesManager = new MessagesManager(this.instance);
      this.messagesManager.load();
      this.settingsManager = new SettingsManager(this.instance);
      this.settingsManager.load();
      this.storageManager = new StorageManager(this.instance);
      this.storageManager.setupStorage();
      this.playerSettingsManager = new PlayerSettingsManager(this.instance);
      this.playerSettingsManager.load();
      this.playerSettingsManager.registerSaveTask();
      this.vanishManager = new VanishManager(this.instance);
      this.vanishManager.setupVanish();
      this.listenersManager = new ListenersManager(this.instance);
      this.listenersManager.setupListeners();
      this.pendingMessagesManager = new PendingMessagesManager(this.instance);
      this.pendingMessagesManager.startSaveTask();
      this.playerTempDataManager = new PlayerTempDataManager();
      this.commandManager = new CommandManager(this.instance);
      this.commandManager.registerCommands();
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

   public PlayerSettingsManager getPlayerSettingsManager() {
      return this.playerSettingsManager;
   }

   public PendingMessagesManager getPendingMessagesManager() {
      return this.pendingMessagesManager;
   }

   public PlayerTempDataManager getPlayerTempDataManger() {
      return this.playerTempDataManager;
   }

   public VanishManager getVanishManager() {
      return this.vanishManager;
   }

   public KnownPlayersManager getKnownPlayersManager() {
      return this.knownPlayersManager;
   }

   public ListenersManager getListenersManager() {
      return this.listenersManager;
   }
}
