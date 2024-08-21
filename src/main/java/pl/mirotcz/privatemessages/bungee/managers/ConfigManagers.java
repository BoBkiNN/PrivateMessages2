package pl.mirotcz.privatemessages.bungee.managers;

import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class ConfigManagers {
   private ConfigManager langConfig;
   private ConfigManager mainConfig;
   private ConfigManager soundsConfig;
   private ConfigManager knownPlayersConfig;
   private PrivateMessages instance;

   public ConfigManagers(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public ConfigManager getMainConfigManager() {
      return this.mainConfig;
   }

   public ConfigManager getLangConfigManager() {
      return this.langConfig;
   }

   public ConfigManager getSoundsConfigManager() {
      return this.soundsConfig;
   }

   public ConfigManager getKnownPlayersConfigManager() {
      return this.knownPlayersConfig;
   }

   public void setupConfigs() {
      this.mainConfig = new ConfigManager(this.instance, "config.yml");
      this.mainConfig.getConfig();
      this.mainConfig.saveDefaultConfig();
      this.langConfig = new ConfigManager(this.instance, "lang.yml");
      this.langConfig.getConfig();
      this.langConfig.saveDefaultConfig();
      this.soundsConfig = new ConfigManager(this.instance, "sounds.yml");
      this.soundsConfig.getConfig();
      this.soundsConfig.saveDefaultConfig();
      this.knownPlayersConfig = new ConfigManager(this.instance, "known_players.yml");
      this.knownPlayersConfig.getConfig();
      this.knownPlayersConfig.saveDefaultConfig();
   }

   public void reloadConfigs() {
      this.mainConfig.reloadConfig();
      this.mainConfig.saveConfig();
      this.langConfig.reloadConfig();
      this.langConfig.saveConfig();
      this.soundsConfig.reloadConfig();
      this.soundsConfig.saveConfig();
      this.knownPlayersConfig.reloadConfig();
      this.knownPlayersConfig.saveConfig();
   }
}
