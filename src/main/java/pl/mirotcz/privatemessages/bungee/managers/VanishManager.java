package pl.mirotcz.privatemessages.bungee.managers;

import java.util.ArrayList;
import java.util.List;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.messaging.Messenger;
import pl.mirotcz.privatemessages.bungee.vanish.Vanish;

public class VanishManager {
   private final Vanish vanish = null;
   private PrivateMessages instance;
   private final List<String> spigot_vanished_names = new ArrayList<>();

   public VanishManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public boolean setupVanish() {
       if (this.instance.getProxy().getPluginManager().getPlugin("PremiumVanish") == null) {
           Messenger.sendConsole("None vanish plugins found. Will try to determine vanish info from backend servers.");
       }
       return false;
   }

   public Vanish getVanish() {
      return this.vanish;
   }

   public boolean isVanishSupported() {
      return this.vanish != null;
   }

   public boolean isVanishedSomewhere(String player_name) {
      return this.spigot_vanished_names.contains(player_name);
   }

   public void addVanishedSomewhere(String player_name) {
      if (!this.spigot_vanished_names.contains(player_name)) {
         this.spigot_vanished_names.add(player_name);
      }

   }

   public synchronized void addVanishedSomewhere(List<String> names) {
       for (String name : names) {
           if (!this.isVanishedSomewhere(name)) {
               this.addVanishedSomewhere(name);
           }
       }

   }

   public void removeVanishedSomewhere(String player_name) {
       this.spigot_vanished_names.remove(player_name);
   }
}
