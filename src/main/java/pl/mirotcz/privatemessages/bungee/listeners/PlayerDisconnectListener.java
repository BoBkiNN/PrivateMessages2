package pl.mirotcz.privatemessages.bungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class PlayerDisconnectListener implements Listener {
   private PrivateMessages instance;

   public PlayerDisconnectListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onQuit(PlayerDisconnectEvent e) {
      ProxyServer.getInstance().getScheduler().runAsync(this.instance, () -> {
         this.instance.getManagers().getPlayerTempDataManger().removeData(e.getPlayer().getName());
      });
   }
}
