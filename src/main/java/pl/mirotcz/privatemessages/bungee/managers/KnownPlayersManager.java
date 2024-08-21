package pl.mirotcz.privatemessages.bungee.managers;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class KnownPlayersManager {
   private List known_names;
   private ScheduledTask task;
   private PrivateMessages instance;

   public KnownPlayersManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void loadNames(ConfigManager config) {
      this.known_names = config.getConfig().getStringList("Names");
   }

   public boolean isKnownPlayerExact(String name) {
      return this.known_names.contains(name);
   }

   public String getKnownPlayer(String name) {
      Iterator var2 = this.known_names.iterator();

      String k_name;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         k_name = (String)var2.next();
      } while(!k_name.equalsIgnoreCase(name));

      return k_name;
   }

   public void addKnownPlayer(String name) {
      this.known_names.add(name);
   }

   public void saveKnownPlayers(ConfigManager config) {
      config.getConfig().set("Names", this.known_names);
      config.saveConfig();
   }

   public void startTask() {
      this.task = ProxyServer.getInstance().getScheduler().schedule(this.instance, () -> {
         this.saveKnownPlayers(this.instance.getManagers().getConfigManagers().getKnownPlayersConfigManager());
      }, 10L, 10L, TimeUnit.SECONDS);
   }

   public void stopTask() {
      if (this.task != null) {
         this.task.cancel();
      }

   }
}
