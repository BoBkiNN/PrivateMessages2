package pl.mirotcz.privatemessages.spigot.inventories;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.Caller;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.DateUtils;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;
import pl.mirotcz.privatemessages.spigot.utils.StringUtils;

public class MessagesInventory implements Menu {
   private final PrivateMessages instance;
   private final List<Message> messages;
   private List<Inventory> pages = new ArrayList();
   private final UUID requesting_player;
   Inventory inv;
   Player player;
   String p1;
   String p2;
   private DateTimeFormatter formatter = null;
   private volatile long time;
   private volatile int a_count = 0;
   private volatile int t_count = 0;
   ItemStack nextpage;
   ItemStack previouspage;

   public MessagesInventory(PrivateMessages plugin, List<Message> msg, UUID requesting_player) {
      this.instance = plugin;
      this.messages = msg;
      this.formatter = this.instance.getSettings().DATE_FORMATTER;
      this.requesting_player = requesting_player;
   }

   public static boolean isValidInventory(InventoryClickEvent e) {
      if (e.getClickedInventory().getSize() != 54) {
         return false;
      } else if (e.getView().getTitle() == null) {
         return false;
      } else {
         return e.getView().getTitle().equals(PrivateMessages.get().getMessages().INV_MESSAGES_TITLE);
      }
   }

   public void load() {
      Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, this.instance.getMessages().INV_MESSAGES_TITLE);
      this.pages = new ArrayList<>();
      int count = 0;
      int pagesCount = 1;
      this.a_count = 0;
      this.t_count = 0;
      Iterator it = this.messages.iterator();
      Map leftPageCallers = new HashMap<>();
      Map rightPageCallers = new HashMap<>();
      this.time = System.currentTimeMillis();
      long part_duration = 0L;

      while(it.hasNext()) {
         long start = System.currentTimeMillis();
         ++this.a_count;
         ++this.t_count;
         long current_time = System.currentTimeMillis();
         if (this.t_count >= this.instance.getSettings().MAX_HISTORY_MESSAGES_PER_SECOND && part_duration >= 1000L || this.t_count >= this.instance.getSettings().MAX_HISTORY_MESSAGES_PER_SECOND && current_time - this.time <= 1000L) {
            if (part_duration > 1000L) {
               try {
                  Thread.sleep(part_duration - 1000L);
               } catch (InterruptedException var26) {
                  var26.printStackTrace();
               }
            } else {
               try {
                  Thread.sleep(1000L - (current_time - this.time));
               } catch (InterruptedException var25) {
                  var25.printStackTrace();
               }
            }

            if (this.requesting_player != null && Bukkit.getPlayer(this.requesting_player) != null) {
               Messenger.send(Bukkit.getPlayer(this.requesting_player), PrivateMessages.get().getMessages().INFO_PLEASE_WAIT + " " + (int)MainUtils.round((double)this.a_count / (double)this.messages.size() * 100.0, 2) + "%");
            }

            this.t_count = 0;
            this.time = System.currentTimeMillis();
            part_duration = 0L;
         }

         Message message = (Message)it.next();
         ItemStack item = new ItemStack(Material.PAPER);
         ItemMeta meta = item.getItemMeta();
         List<String> lore = new ArrayList<>();
         lore.add(ChatColor.RED + "" + message.getSenderName() + ChatColor.WHITE + " -> " + ChatColor.GREEN + message.getRecipientName());
         String message_content = message.getMessageContent();
         if (message_content.length() > 30) {
            List<String> words = StringUtils.getWordsFromMessage(message_content);
            String line = "";

            for(int i = 1; i <= words.size(); ++i) {
               line = line + words.get(i - 1);
               if (i + 1 <= words.size()) {
                  if (line.length() + ((String)words.get(i)).length() >= 30) {
                     lore.add(ChatColor.YELLOW + "" + line);
                     line = "";
                  }
               } else if (i == words.size()) {
                  lore.add(ChatColor.YELLOW + "" + line);
                  line = "";
               } else {
                  lore.add(ChatColor.YELLOW + "" + line);
                  line = "";
               }
            }
         } else {
            lore.add(ChatColor.YELLOW + "" + message_content);
         }

         lore = MainUtils.getColorfulStringList(lore);
         meta.setLore(lore);
         meta.setDisplayName(ChatColor.GRAY + DateUtils.getFormattedDate(message.getDate(), this.formatter));
         item.setItemMeta(meta);
         inv.setItem(count, item);
         if ((!it.hasNext() || count == 44) && pagesCount > 1) {
            this.addPreviousPageItem(inv, pagesCount - 1, 45, 1, leftPageCallers);
         }

         long end;
         HashMap finalLeftPageCallers;
         HashMap finalRightPageCallers;
         if (!it.hasNext()) {
            this.pages.add(inv);
            finalLeftPageCallers = new HashMap(leftPageCallers);
            finalRightPageCallers = new HashMap(rightPageCallers);
            leftClickCallers.put(inv, finalLeftPageCallers);
            rightClickCallers.put(inv, finalRightPageCallers);
            end = System.currentTimeMillis();
            long var10000 = part_duration + (end - start);
            return;
         }

         if (count == 44) {
            this.addNextPageItem(inv, pagesCount + 1, 53, 1, leftPageCallers);
            this.pages.add(inv);
            finalLeftPageCallers = new HashMap(leftPageCallers);
            finalRightPageCallers = new HashMap(rightPageCallers);
            leftClickCallers.put(inv, finalLeftPageCallers);
            rightClickCallers.put(inv, finalRightPageCallers);
            ++pagesCount;
            count = 0;
            inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, this.instance.getMessages().INV_MESSAGES_TITLE);
            end = System.currentTimeMillis();
            part_duration += end - start;
         } else {
            end = System.currentTimeMillis();
            part_duration += end - start;
            ++count;
         }
      }

   }

   public void addNextPageItem(Inventory inv, int nextPageNumber, int slot, int itemCount, Map leftPageCallers) {
      ItemStack nextitem = new ItemStack(Material.ARROW);
      ItemMeta pagemeta = nextitem.getItemMeta();
      pagemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.instance.getMessages().INV_NEXT_PAGE));
      List pageitemlore = new ArrayList();
      pageitemlore.add("" + nextPageNumber);
      pagemeta.setLore(MainUtils.getColorfulStringList(pageitemlore));
      nextitem.setItemMeta(pagemeta);
      nextitem.setAmount(itemCount);
      Caller left = (player) -> {
         Bukkit.getScheduler().runTask(this.instance, () -> {
            player.openInventory(this.get(nextPageNumber));
         });
      };
      leftPageCallers.put(slot, left);
      inv.setItem(slot, nextitem);
   }

   public void addPreviousPageItem(Inventory inv, int previousPageNumber, int slot, int itemCount, Map leftPageCallers) {
      ItemStack backitem = new ItemStack(Material.ARROW);
      ItemMeta pagemeta = backitem.getItemMeta();
      pagemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.instance.getMessages().INV_PREVIOUS_PAGE));
      List pageitemlore = new ArrayList();
      pageitemlore.add("" + previousPageNumber);
      pagemeta.setLore(MainUtils.getColorfulStringList(pageitemlore));
      backitem.setItemMeta(pagemeta);
      backitem.setAmount(itemCount);
      Caller left = (player) -> {
         Bukkit.getScheduler().runTask(this.instance, () -> {
            player.openInventory(this.get(previousPageNumber));
         });
      };
      leftPageCallers.put(slot, left);
      inv.setItem(slot, backitem);
   }

   public void addExtraPageItems() {
      for(int i = 1; i <= this.pages.size(); ++i) {
         Inventory inv = this.pages.get(i - 1);
         Map leftPageCallers = (Map)leftClickCallers.get(inv);
         Map rightPageCallers = (Map)rightClickCallers.get(inv);
         if (i - 10 > 0) {
            this.addPreviousPageItem(inv, i - 10, 46, 10, leftPageCallers);
         }

         if (i + 10 <= this.pages.size()) {
            this.addNextPageItem(inv, i + 10, 52, 10, leftPageCallers);
         }

         if (i - 50 > 0) {
            this.addPreviousPageItem(inv, i - 50, 47, 50, leftPageCallers);
         }

         if (i + 50 <= this.pages.size()) {
            this.addNextPageItem(inv, i + 50, 51, 50, leftPageCallers);
         }

         Map finalLeftPageCallers = new HashMap(leftPageCallers);
         Map finalRightPageCallers = new HashMap(rightPageCallers);
         leftClickCallers.put(inv, finalLeftPageCallers);
         rightClickCallers.put(inv, finalRightPageCallers);
      }

   }

   public Map getLeftClickCallers() {
      return leftClickCallers;
   }

   public Map getRightClickCallers() {
      return rightClickCallers;
   }

   public Caller getLeftClickCaller(Inventory inv, int slot) {
      if (leftClickCallers.containsKey(inv)) {
         Map callers = (Map)leftClickCallers.get(inv);
         if (callers.containsKey(slot)) {
            return (Caller)callers.get(slot);
         }
      }

      return null;
   }

   public Caller getRightClickCaller(Inventory inv, int slot) {
      if (rightClickCallers.containsKey(inv)) {
         Map callers = (Map)rightClickCallers.get(inv);
         if (callers.containsKey(slot)) {
            return (Caller)callers.get(slot);
         }
      }

      return null;
   }

   public Inventory get(int page) {
      return this.pages.size() >= page ? (Inventory)this.pages.get(page - 1) : null;
   }

   public List<Inventory> getPages() {
      return this.pages;
   }
}
