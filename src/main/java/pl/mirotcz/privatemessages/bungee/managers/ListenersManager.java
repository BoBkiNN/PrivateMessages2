package pl.mirotcz.privatemessages.bungee.managers;

import net.md_5.bungee.api.ProxyServer;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.listeners.PlayerDisconnectListener;
import pl.mirotcz.privatemessages.bungee.listeners.PlayerSettingsModifyListener;
import pl.mirotcz.privatemessages.bungee.listeners.PluginMessagesListener;
import pl.mirotcz.privatemessages.bungee.listeners.PostLoginListener;
import pl.mirotcz.privatemessages.bungee.listeners.PrivateMessageListener;

public class ListenersManager {
   private final PrivateMessages instance;
   private PostLoginListener postLoginListener;
   private PlayerDisconnectListener playerDisconnectListener;
   private PlayerSettingsModifyListener playerSettingsModifyListener;
   private PrivateMessageListener privateMessageListener;
   private PluginMessagesListener pluginMessagesListener;

   public ListenersManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public PostLoginListener getPostLoginListener() {
      return this.postLoginListener;
   }

   public PlayerDisconnectListener getPlayerDisconnectListener() {
      return this.playerDisconnectListener;
   }

   public PlayerSettingsModifyListener getPlayerSettingsModifyListener() {
      return this.playerSettingsModifyListener;
   }

   public PrivateMessageListener getPrivateMessageListener() {
      return this.privateMessageListener;
   }

   public PluginMessagesListener getPluginMessagesListener() {
      return this.pluginMessagesListener;
   }

   public void setupListeners() {
      this.postLoginListener = new PostLoginListener(this.instance);
      this.playerDisconnectListener = new PlayerDisconnectListener(this.instance);
      this.playerSettingsModifyListener = new PlayerSettingsModifyListener(this.instance);
      this.privateMessageListener = new PrivateMessageListener(this.instance);
      this.pluginMessagesListener = new PluginMessagesListener(this.instance);
      this.pluginMessagesListener.load();
      ProxyServer server = ProxyServer.getInstance();
      server.getPluginManager().registerListener(this.instance, this.pluginMessagesListener);
      server.getPluginManager().registerListener(this.instance, this.postLoginListener);
      server.getPluginManager().registerListener(this.instance, this.playerDisconnectListener);
      server.getPluginManager().registerListener(this.instance, this.playerSettingsModifyListener);
      server.getPluginManager().registerListener(this.instance, this.privateMessageListener);
   }
}
