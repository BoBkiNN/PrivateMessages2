package pl.mirotcz.privatemessages.bungee;

import pl.mirotcz.privatemessages.bungee.utils.MainUtils;

public class Utils {
   private MainUtils main_utils;
   private final PrivateMessages instance;

   public Utils(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      this.main_utils = new MainUtils(this.instance);
   }

   public MainUtils getMainUtils() {
      return this.main_utils;
   }
}
