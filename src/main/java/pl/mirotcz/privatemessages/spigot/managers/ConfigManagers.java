package pl.mirotcz.privatemessages.spigot.managers;

import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class ConfigManagers {
   private ConfigManager langConfig;
   private ConfigManager mainConfig;
   private ConfigManager soundsConfig;

   public ConfigManager getMainConfigManager() {
      return this.mainConfig;
   }

   public ConfigManager getLangConfigManager() {
      return this.langConfig;
   }

   public ConfigManager getSoundsConfigManager() {
      return this.soundsConfig;
   }

   public void setupConfigs() {
      this.mainConfig = new ConfigManager(PrivateMessages.get(), "config.yml");
      this.mainConfig.getConfig();
      this.mainConfig.saveDefaultConfig();
      this.langConfig = new ConfigManager(PrivateMessages.get(), "lang.yml");
      this.langConfig.getConfig();
      this.langConfig.saveDefaultConfig();
      this.soundsConfig = new ConfigManager(PrivateMessages.get(), "sounds.yml");
      this.soundsConfig.getConfig();
      this.soundsConfig.saveDefaultConfig();
   }

   public void reloadConfigs() {
      this.mainConfig.reloadConfig();
      this.mainConfig.saveConfig();
      this.langConfig.reloadConfig();
      this.langConfig.saveConfig();
      this.soundsConfig.reloadConfig();
      this.soundsConfig.saveConfig();
   }
}
