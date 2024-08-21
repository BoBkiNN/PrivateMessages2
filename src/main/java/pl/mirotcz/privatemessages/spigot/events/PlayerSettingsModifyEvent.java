package pl.mirotcz.privatemessages.spigot.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;

public class PlayerSettingsModifyEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private PlayerSettings settings = null;

   public PlayerSettingsModifyEvent(PlayerSettings settings) {
      this.settings = settings;
   }

   public PlayerSettings getPlayerSettings() {
      return this.settings;
   }

   public HandlerList getHandlers() {
      return HANDLERS;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS;
   }
}
