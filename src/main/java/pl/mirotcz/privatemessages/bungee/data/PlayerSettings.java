package pl.mirotcz.privatemessages.bungee.data;

import java.util.HashSet;
import java.util.Set;
import net.md_5.bungee.api.ProxyServer;
import pl.mirotcz.privatemessages.bungee.events.PlayerSettingsModifyEvent;

public class PlayerSettings {
   private String PLAYER_NAME = null;
   private boolean MESSAGE_SPY_ENABLED = false;
   private Set IGNORED_PLAYERS = new HashSet();
   private boolean PRIVATE_MESSAGES_ENABLED = true;

   public PlayerSettings() {
   }

   public PlayerSettings(String player_name, boolean spy_enabled, Set ignored_players, boolean msg_enabled) {
      this.PLAYER_NAME = player_name;
      this.MESSAGE_SPY_ENABLED = spy_enabled;
      this.IGNORED_PLAYERS = ignored_players;
      this.PRIVATE_MESSAGES_ENABLED = msg_enabled;
   }

   public String getPlayerName() {
      return this.PLAYER_NAME;
   }

   public void setPlayerName(String name) {
      this.PLAYER_NAME = name;
   }

   public boolean isMessageSpyEnabled() {
      return this.MESSAGE_SPY_ENABLED;
   }

   public synchronized void setMessageSpyEnabled(boolean value) {
      this.MESSAGE_SPY_ENABLED = value;
   }

   public Set getIgnoredPlayers() {
      return this.IGNORED_PLAYERS;
   }

   public synchronized void setIgnoredPlayers(Set value) {
      this.IGNORED_PLAYERS = value;
      ProxyServer.getInstance().getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
   }

   public boolean arePrivateMessagesEnabled() {
      return this.PRIVATE_MESSAGES_ENABLED;
   }

   public synchronized void setPrivateMessagesEnabled(boolean value) {
      this.PRIVATE_MESSAGES_ENABLED = value;
      ProxyServer.getInstance().getPluginManager().callEvent(new PlayerSettingsModifyEvent(this));
   }
}
