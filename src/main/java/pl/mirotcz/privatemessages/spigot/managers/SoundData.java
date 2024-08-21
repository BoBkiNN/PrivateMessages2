package pl.mirotcz.privatemessages.spigot.managers;

import org.bukkit.Sound;

public class SoundData {
   private Sound sound = null;
   private float pitch = 1.0F;
   private float volume = 1.0F;

   public SoundData(Sound sound, float pitch, float volume) {
      this.sound = sound;
      this.pitch = pitch;
      this.volume = volume;
   }

   public Sound getSound() {
      return this.sound;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getVolume() {
      return this.volume;
   }
}
