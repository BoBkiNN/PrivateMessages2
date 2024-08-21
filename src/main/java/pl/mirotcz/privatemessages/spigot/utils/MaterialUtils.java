package pl.mirotcz.privatemessages.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class MaterialUtils {
   public static final List<Material> MUSIC_DISCS = getMusicDiscsMaterials();

   private static List<Material> getMusicDiscsMaterials() {
      List<Material> materials = new ArrayList<>();
      for (Material m : Material.values()) {
         if (m.isRecord()) materials.add(m);
      }
      return materials;
   }
}
