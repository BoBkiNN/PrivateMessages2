package pl.mirotcz.privatemessages.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.mirotcz.privatemessages.spigot.MessageSending;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class PluginMessagesListener implements PluginMessageListener {
   private PrivateMessages instance;

   public PluginMessagesListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      try {
         this.instance.getServer().getMessenger().registerIncomingPluginChannel(this.instance, "privatemessages:sound", this);
      } catch (IllegalArgumentException var2) {
         var2.printStackTrace();
      }

   }

   public void onPluginMessageReceived(String channel, Player player, byte[] message) {
      if (channel.equals("privatemessages:sound")) {
         ByteArrayDataInput in = ByteStreams.newDataInput(message);
         String player_name = in.readUTF();
         MessageSending.soundNotifyPlayer(player_name);
      }

   }
}
