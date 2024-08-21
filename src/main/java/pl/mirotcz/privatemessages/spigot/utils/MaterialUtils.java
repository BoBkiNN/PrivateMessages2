package pl.mirotcz.privatemessages.spigot.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

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
