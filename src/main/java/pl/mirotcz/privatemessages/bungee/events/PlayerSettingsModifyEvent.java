package pl.mirotcz.privatemessages.bungee.events;

import net.md_5.bungee.api.plugin.Event;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;

public class PlayerSettingsModifyEvent extends Event {
   private PlayerSettings settings = null;

   public PlayerSettingsModifyEvent(PlayerSettings settings) {
      this.settings = settings;
   }

   public PlayerSettings getPlayerSettings() {
      return this.settings;
   }
}
