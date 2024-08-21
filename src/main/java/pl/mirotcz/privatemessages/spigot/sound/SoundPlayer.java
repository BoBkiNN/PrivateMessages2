package pl.mirotcz.privatemessages.spigot.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer {
   public void play(Player player, String name, float volume, float pitch) {
      player.playSound(player.getLocation(), Sound.valueOf(name), volume, pitch);
   }
}
