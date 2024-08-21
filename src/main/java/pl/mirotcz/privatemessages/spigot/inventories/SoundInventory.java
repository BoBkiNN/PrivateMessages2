package pl.mirotcz.privatemessages.spigot.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.mirotcz.privatemessages.spigot.Caller;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.managers.SoundData;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;
import pl.mirotcz.privatemessages.spigot.utils.MaterialUtils;

import java.util.*;

public class SoundInventory implements Menu {
    private List<Inventory> pages = new ArrayList<>();
    private final List<Material> discs;
    private final PrivateMessages instance;

    public SoundInventory(PrivateMessages plugin) {
        this.instance = plugin;
        this.discs = MaterialUtils.getMusicDiscsMaterials();
    }

    public void load() {
        Inventory inv = Bukkit.getServer().createInventory(null, 54, this.instance.getMessages().INV_SOUND_TITLE);
        this.pages = new ArrayList<>();
        List<SoundData> sounds = this.instance.getManagers().getSoundDataManager().getAllData();
        int count = 1;
        int allCount = 1;
        int pagesCount = 1;
        Iterator<SoundData> it = sounds.iterator();
        Map<Integer, Caller> leftPageCallers = new HashMap<>();
        Map<Integer, Caller> rightPageCallers = new HashMap<>();
        ItemStack helpItem = new ItemStack(this.instance.getSettings().SOUND_HELP_ITEM_MATERIAL);
        ItemMeta helpItemMeta = helpItem.getItemMeta();
        helpItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.instance.getMessages().INV_SOUND_HELP_ITEM_NAME));
        helpItemMeta.setLore(MainUtils.getColorfulStringList(this.instance.getMessages().INV_SOUND_HELP_ITEM_LORE));
        helpItem.setItemMeta(helpItemMeta);
        inv.setItem(49, helpItem);

        while (it.hasNext()) {
            SoundData sound = it.next();
            ItemStack item = new ItemStack(this.discs.get(MainUtils.getRandomInt(0, this.discs.size() - 1)));
            ItemMeta meta = item.getItemMeta();
            List<String> lore = MainUtils.getColorfulStringList(this.instance.getMessages().INV_SOUND_ITEM_LORE);
            List<String> finalLore = new ArrayList<>(lore);

            finalLore.replaceAll(s -> s
                    .replaceAll("<volume>", String.valueOf(sound.getVolume()))
                    .replaceAll("<pitch>", String.valueOf(sound.getPitch()))
                    .replaceAll("<soundName>", sound.getSound().toString()));

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    this.instance.getMessages().INV_SOUND_ITEM_NAME
                            .replaceAll("<soundName>", sound.getSound().toString())
                            .replaceAll("<soundNumber>", String.valueOf(allCount))));
            meta.setLore(finalLore);
            item.setItemMeta(meta);
            inv.setItem(count - 1, item);
            int finalAllCount = allCount;
            Caller leftClick = (player) -> {
                Bukkit.getScheduler().runTask(this.instance, () -> {
                    this.instance.getManagers().getPlayerSettingsManager().getPlayerSettings(player.getName()).setMessageNotificationSoundNumber(finalAllCount);
                    player.closeInventory();
                    Messenger.send(player, this.instance.getMessages().INFO_SOUND_CHANGED);
                });
            };
            Caller rightClick = (player) -> {
                Bukkit.getScheduler().runTask(this.instance, () -> {
                    player.playSound(player.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
                });
            };
            leftPageCallers.put(count - 1, leftClick);
            rightPageCallers.put(count - 1, rightClick);
            ItemStack nextitem;
            ItemMeta pagemeta;
            ArrayList<String> pageitemlore;
            Caller left;
            if ((!it.hasNext() || count == 44) && pagesCount > 1) {
                nextitem = new ItemStack(Material.ARROW);
                pagemeta = nextitem.getItemMeta();
                pagemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.instance.getMessages().INV_PREVIOUS_PAGE));
                pageitemlore = new ArrayList<>();
                pageitemlore.add("" + (pagesCount - 1));
                pagemeta.setLore(MainUtils.getColorfulStringList(pageitemlore));
                nextitem.setItemMeta(pagemeta);
                int finalPagesCount1 = pagesCount;
                left = (player) -> {
                    Bukkit.getScheduler().runTask(this.instance, () -> {
                        player.openInventory(this.get(finalPagesCount1 - 1));
                    });
                };
                leftPageCallers.put(44, left);
                inv.setItem(44, nextitem);
            }

            if (!it.hasNext()) {
                this.pages.add(inv);
                leftClickCallers.put(inv, leftPageCallers);
                rightClickCallers.put(inv, rightPageCallers);
                return;
            }

            if (count == 44) {
                nextitem = new ItemStack(Material.ARROW);
                pagemeta = nextitem.getItemMeta();
                pagemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.instance.getMessages().INV_NEXT_PAGE));
                pageitemlore = new ArrayList<>();
                pageitemlore.add("" + (pagesCount + 1));
                pagemeta.setLore(MainUtils.getColorfulStringList(pageitemlore));
                nextitem.setItemMeta(pagemeta);
                int finalPagesCount = pagesCount;
                left = (player) -> {
                    Bukkit.getScheduler().runTask(this.instance, () -> {
                        player.openInventory(this.get(finalPagesCount + 1));
                    });
                };
                leftPageCallers.put(53, left);
                inv.setItem(53, nextitem);
                this.pages.add(inv);
                leftClickCallers.put(inv, leftPageCallers);
                rightClickCallers.put(inv, rightPageCallers);
                ++pagesCount;
                count = 0;
                ++allCount;
                inv = Bukkit.getServer().createInventory(null, 54, this.instance.getMessages().INV_SOUND_TITLE);
                inv.setItem(49, helpItem);
            } else {
                ++allCount;
                ++count;
            }
        }

    }

    public Caller getLeftClickCaller(Inventory inv, int slot) {
        if (leftClickCallers.containsKey(inv)) {
            Map<Integer, Caller> callers = leftClickCallers.get(inv);
            if (callers.containsKey(slot)) {
                return callers.get(slot);
            }
        }
        return null;
    }

    public Caller getRightClickCaller(Inventory inv, int slot) {
        if (rightClickCallers.containsKey(inv)) {
            Map<Integer, Caller> callers = rightClickCallers.get(inv);
            if (callers.containsKey(slot)) {
                return callers.get(slot);
            }
        }

        return null;
    }

    public Inventory get(int page) {
        return this.pages.size() >= page ? (Inventory) this.pages.get(page - 1) : null;
    }

    public List<Inventory> getPages() {
        return this.pages;
    }
}
