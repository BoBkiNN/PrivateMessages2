package pl.mirotcz.privatemessages.spigot.managers;

import org.bukkit.Bukkit;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.listeners.CommandListener;
import pl.mirotcz.privatemessages.spigot.listeners.EssentialsVanishListener;
import pl.mirotcz.privatemessages.spigot.listeners.InventoryClickListener;
import pl.mirotcz.privatemessages.spigot.listeners.PlayerJoinListener;
import pl.mirotcz.privatemessages.spigot.listeners.PlayerQuitListener;
import pl.mirotcz.privatemessages.spigot.listeners.PlayerSettingsModifyListener;
import pl.mirotcz.privatemessages.spigot.listeners.PluginMessagesListener;
import pl.mirotcz.privatemessages.spigot.listeners.PremiumVanishListener;
import pl.mirotcz.privatemessages.spigot.listeners.PrivateMessageListener;
import pl.mirotcz.privatemessages.spigot.listeners.VanishNoPacketListener;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_Essentials;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_NoPacket;
import pl.mirotcz.privatemessages.spigot.vanish.Vanish_SuperVanish;

public class ListenersManager {
   private final PrivateMessages instance;
   private CommandListener commandListener;
   private PlayerJoinListener playerJoinListener;
   private PlayerQuitListener playerQuitListener;
   private InventoryClickListener inventoryClickListener;
   private PlayerSettingsModifyListener playerSettingsModifyListener;
   private PrivateMessageListener privateMessageListener;
   private PluginMessagesListener pluginMessagesListener;
   private PremiumVanishListener premiumVanishListener;
   private VanishNoPacketListener vanishNoPacketListener;
   private EssentialsVanishListener essVanishListener;

   public ListenersManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public CommandListener getCommandListener() {
      return this.commandListener;
   }

   public PlayerJoinListener getPlayerJoinListener() {
      return this.playerJoinListener;
   }

   public PlayerQuitListener getPlayerQuitListener() {
      return this.playerQuitListener;
   }

   public InventoryClickListener getInventoryClickListener() {
      return this.inventoryClickListener;
   }

   public PlayerSettingsModifyListener getPlayerSettingsModifyListener() {
      return this.playerSettingsModifyListener;
   }

   public PrivateMessageListener getPrivateMessageListener() {
      return this.privateMessageListener;
   }

   public PluginMessagesListener getPluginMessagesListener() {
      return this.pluginMessagesListener;
   }

   public PremiumVanishListener getPremiumVanishListener() {
      return this.premiumVanishListener;
   }

   public VanishNoPacketListener getVanishNoPacketListener() {
      return this.vanishNoPacketListener;
   }

   public EssentialsVanishListener getEssentialsVanishListener() {
      return this.essVanishListener;
   }

   public void setupListeners() {
      this.commandListener = new CommandListener(this.instance);
      this.commandListener.registerCommands();
      this.commandListener.registerTabCompleters();
      this.playerJoinListener = new PlayerJoinListener(this.instance);
      this.playerQuitListener = new PlayerQuitListener(this.instance);
      this.inventoryClickListener = new InventoryClickListener(this.instance);
      this.playerSettingsModifyListener = new PlayerSettingsModifyListener(this.instance);
      this.privateMessageListener = new PrivateMessageListener(this.instance);
      this.pluginMessagesListener = new PluginMessagesListener(this.instance);
      Bukkit.getPluginManager().registerEvents(this.playerJoinListener, this.instance);
      Bukkit.getPluginManager().registerEvents(this.playerQuitListener, this.instance);
      Bukkit.getPluginManager().registerEvents(this.inventoryClickListener, this.instance);
      Bukkit.getPluginManager().registerEvents(this.playerSettingsModifyListener, this.instance);
      Bukkit.getPluginManager().registerEvents(this.privateMessageListener, this.instance);
      if (this.instance.getSettings().BUNGEECORD) {
         this.pluginMessagesListener.load();
      }

      if (this.instance.getManagers().getVanishManager().isVanishSupported()) {
         Vanish vanish = this.instance.getVanish();
         if (!(vanish instanceof Vanish_SuperVanish)) {
            if (vanish instanceof Vanish_NoPacket) {
               this.vanishNoPacketListener = new VanishNoPacketListener(this.instance);
               Bukkit.getPluginManager().registerEvents(this.vanishNoPacketListener, this.instance);
            } else if (vanish instanceof Vanish_Essentials) {
               this.essVanishListener = new EssentialsVanishListener(this.instance);
               Bukkit.getPluginManager().registerEvents(this.essVanishListener, this.instance);
            }
         } else {
            this.premiumVanishListener = new PremiumVanishListener(this.instance);
            Bukkit.getPluginManager().registerEvents(this.premiumVanishListener, this.instance);
         }
      }

   }
}
