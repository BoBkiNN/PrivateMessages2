package pl.mirotcz.privatemessages.bungee.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class PluginMessagesListener implements Listener {
   private PrivateMessages instance;

   public PluginMessagesListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      this.instance.getProxy().registerChannel("privatemessages:sound");
   }

   public void sendMessage(String message, String server_name, String channel_name) {
      ServerInfo server = (ServerInfo)this.instance.getProxy().getServers().get(server_name);
      if (server != null) {
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(stream);

         try {
            out.writeUTF(message);
         } catch (IOException var8) {
            var8.printStackTrace();
         }

         server.sendData(channel_name, stream.toByteArray());
      }
   }

   public void sendSoundNotify(String player_name) {
      ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(player_name);
      if (pl != null) {
         Server server = pl.getServer();
         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(stream);

         try {
            out.writeUTF(player_name);
         } catch (IOException var7) {
            var7.printStackTrace();
         }

         server.getInfo().sendData("privatemessages:sound", stream.toByteArray());
      }
   }

   @EventHandler
   public void onPluginMessage(PluginMessageEvent ev) {
   }
}
