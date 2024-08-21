package pl.mirotcz.privatemessages.spigot.managers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.inventories.Menu;
import pl.mirotcz.privatemessages.spigot.inventories.SoundInventory;

public class MenuManager {
   private Set menuSet;
   private PrivateMessages instance;

   public MenuManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void load() {
      this.menuSet = new HashSet();
      SoundInventory soundsMenu = new SoundInventory(this.instance);
      soundsMenu.load();
      this.menuSet.add(soundsMenu);
   }

   public Menu getMatchingMenu(InventoryClickEvent e) {
      Iterator var2 = this.menuSet.iterator();

      while(var2.hasNext()) {
         Menu menu = (Menu)var2.next();
         Iterator var4 = menu.getPages().iterator();

         while(var4.hasNext()) {
            Inventory page = (Inventory)var4.next();
            if (page.getSize() == e.getClickedInventory().getSize()) {
               if (page == e.getClickedInventory()) {
                  return menu;
               }

               if (page.getContents() == e.getClickedInventory().getContents()) {
                  return menu;
               }

               if (page.getItem(e.getSlot()) != null && e.getClickedInventory().getItem(e.getSlot()) != null && page.getItem(e.getSlot()).isSimilar(e.getClickedInventory().getItem(e.getSlot()))) {
                  return menu;
               }
            }
         }
      }

      return null;
   }

   public SoundInventory getSoundsMenu() {
      Iterator var1 = this.menuSet.iterator();

      Menu menu;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         menu = (Menu)var1.next();
      } while(!(menu instanceof SoundInventory));

      return (SoundInventory)menu;
   }
}
