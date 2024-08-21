package pl.mirotcz.privatemessages.bungee.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class MaterialUtils {
   public static List getMusicDiscsMaterials() {
      List materials = new ArrayList();
      if (!PrivateMessages.checkVersion().equals("v1_15_R1") && !PrivateMessages.checkVersion().equals("v1_14_R1") && !PrivateMessages.checkVersion().equals("v1_13_R2") && !PrivateMessages.checkVersion().equals("v1_13_R1")) {
         materials.add(Material.valueOf("RECORD_11"));
         materials.add(Material.valueOf("RECORD_12"));
         materials.add(Material.valueOf("RECORD_10"));
         materials.add(Material.valueOf("RECORD_3"));
         materials.add(Material.valueOf("RECORD_4"));
         materials.add(Material.valueOf("RECORD_5"));
         materials.add(Material.valueOf("RECORD_6"));
         materials.add(Material.valueOf("RECORD_7"));
         materials.add(Material.valueOf("RECORD_8"));
         materials.add(Material.valueOf("RECORD_9"));
         materials.add(Material.valueOf("GOLD_RECORD"));
         materials.add(Material.valueOf("GREEN_RECORD"));
      } else {
         materials.add(Material.valueOf("MUSIC_DISC_11"));
         materials.add(Material.valueOf("MUSIC_DISC_13"));
         materials.add(Material.valueOf("MUSIC_DISC_BLOCKS"));
         materials.add(Material.valueOf("MUSIC_DISC_CAT"));
         materials.add(Material.valueOf("MUSIC_DISC_CHIRP"));
         materials.add(Material.valueOf("MUSIC_DISC_FAR"));
         materials.add(Material.valueOf("MUSIC_DISC_MALL"));
         materials.add(Material.valueOf("MUSIC_DISC_MELLOHI"));
         materials.add(Material.valueOf("MUSIC_DISC_STAL"));
         materials.add(Material.valueOf("MUSIC_DISC_STRAD"));
         materials.add(Material.valueOf("MUSIC_DISC_WAIT"));
         materials.add(Material.valueOf("MUSIC_DISC_WARD"));
      }

      return materials;
   }
}
