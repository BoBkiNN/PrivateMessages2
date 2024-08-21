package pl.mirotcz.privatemessages.spigot.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.inventories.Menu;
import pl.mirotcz.privatemessages.spigot.inventories.MessagesInventory;

public class InventoryClickListener implements Listener {
   private PrivateMessages instance = null;

   public InventoryClickListener(PrivateMessages plugin) {
      this.instance = plugin;
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent e) {
      if (e.getWhoClicked() instanceof Player) {
         Player player = (Player)e.getWhoClicked();
         Inventory inv = e.getClickedInventory();
         if (inv != null) {
            Menu menu = this.instance.getManagers().getMenuManager().getMatchingMenu(e);
            if (menu != null || MessagesInventory.isValidInventory(e)) {
               e.setCancelled(true);
               Bukkit.getScheduler().runTaskAsynchronously(this.instance, () -> {
                  Menu finalMenu = null;
                  if (menu == null) {
                     finalMenu = this.instance.getManagers().getPlayerTempDataManger().getData(player).getLastMessagesInventory();
                  } else {
                     finalMenu = menu;
                  }

                  if (e.isLeftClick()) {
                     if (((Menu)finalMenu).getLeftClickCaller(inv, e.getSlot()) != null) {
                        ((Menu)finalMenu).getLeftClickCaller(inv, e.getSlot()).start(player);
                     }

                  } else if (e.isRightClick()) {
                     if (((Menu)finalMenu).getRightClickCaller(inv, e.getSlot()) != null) {
                        ((Menu)finalMenu).getRightClickCaller(inv, e.getSlot()).start(player);
                     }

                  }
               });
            }
         }
      }
   }
}
