package pl.mirotcz.privatemessages.spigot.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.Inventory;
import pl.mirotcz.privatemessages.spigot.Caller;

public interface Menu {
   Map<Inventory, Map<Integer, Caller>> leftClickCallers = new HashMap<>();
   Map<Inventory, Map<Integer, Caller>> rightClickCallers = new HashMap<>();

   void load();

   Inventory get(int var1);

   List<Inventory> getPages();

   Caller getLeftClickCaller(Inventory var1, int var2);

   Caller getRightClickCaller(Inventory var1, int var2);
}
