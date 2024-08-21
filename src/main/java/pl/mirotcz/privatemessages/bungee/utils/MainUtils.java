package pl.mirotcz.privatemessages.bungee.utils;

import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class MainUtils {
   private PrivateMessages instance;

   public MainUtils(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void reloadPlugin() {
      this.instance.getManagers().getConfigManagers().reloadConfigs();
      this.instance.getMessages().load();
      this.instance.getSettings().load();
   }
}
