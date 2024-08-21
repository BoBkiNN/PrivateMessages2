package pl.mirotcz.privatemessages.spigot.managers;

import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_Essentials;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_NoPacket;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_SuperVanish;

public class VanishManager {
   private Vanish vanish = null;
   private PrivateMessages instance = null;

   public VanishManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public boolean setupVanish() {
      if (this.instance.getServer().getPluginManager().getPlugin("SuperVanish") != null) {
         this.vanish = new Vanish_SuperVanish();
         return true;
      } else if (this.instance.getServer().getPluginManager().getPlugin("VanishNoPacket") != null) {
         this.vanish = new Vanish_NoPacket();
         return true;
      } else if (this.instance.getServer().getPluginManager().getPlugin("Essentials") != null) {
         this.vanish = new Vanish_Essentials();
         return true;
      } else {
         return false;
      }
   }

   public Vanish getVanish() {
      return this.vanish;
   }

   public boolean isVanishSupported() {
      return this.vanish != null;
   }
}
