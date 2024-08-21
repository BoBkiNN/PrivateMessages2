package pl.mirotcz.privatemessages.bungee.managers;

import java.lang.reflect.Field;
import java.util.List;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;

public class MessagesManager {
   public String PLUGIN_PREFIX;
   public String MESSAGE_TO_FORMAT;
   public String MESSAGE_FROM_FORMAT;
   public String MESSAGE_TO_OFFLINE_FORMAT;
   public String SPY_MESSAGE_FORMAT;
   public String INV_NEXT_PAGE;
   public String INV_PREVIOUS_PAGE;
   public String INFO_PLAYER_IGNORED;
   public String INFO_PLAYER_NOW_IGNORED;
   public String INFO_PLAYER_IGNORES_YOU;
   public String INFO_NO_PERMISSION;
   public String INFO_SPY_ENABLED;
   public String INFO_SPY_DISABLED;
   public String INFO_YOU_NOT_PLAYER;
   public String INFO_CONFIG_RELOADED;
   public String INFO_PLAYER_NOW_NOT_IGNORED;
   public String INFO_NONE_TO_REPLY;
   public String INFO_CANNOT_MESSAGE_SELF;
   public String INFO_OFFLINE_MESSAGING_DISABLED;
   public String INFO_IGNORED_PLAYERS;
   public String INFO_NO_MESSAGES;
   public String INFO_MESSAGE_NOT_FOUND;
   public String INFO_PLEASE_WAIT;
   public String INFO_YOU_HAVE_UNREAD_MESSAGES;
   public String INFO_CLEAR_MESSAGES_TIP;
   public String INFO_MESSAGES_CLEARED;
   public String INFO_MESSAGE_READ_NUMBER;
   public String INFO_MESSAGE_READ_SENDER;
   public String INFO_MESSAGE_READ_MESSAGE;
   public String INFO_SOUND_CHANGE_TIP;
   public String INFO_SOUND_CHANGED;
   public String INFO_INVALID_NUMBER;
   public String INFO_PLAYER_NOT_FOUND;
   public String INFO_MESSAGES_NOW_ENABLED;
   public String INFO_MESSAGES_NOW_DISABLED;
   public String INFO_MESSAGES_DISABLED;
   public String INFO_PLAYER_MESSAGES_DISABLED;
   public String INV_MESSAGES_TITLE;
   public String INV_SOUND_HELP_ITEM_NAME;
   public String INV_SOUND_TITLE;
   public String INV_SOUND_ITEM_NAME;
   public List HELP_COMMAND_MESSAGE;
   public List HELP_COMMAND_REPLY;
   public List HELP_COMMAND_IGNORE;
   public List HELP_COMMAND_IGNORED;
   public List HELP_COMMAND_PMREAD;
   public List HELP_COMMAND_PMCLEAR;
   public List HELP_COMMAND_PMHISTORY;
   public List HELP_COMMAND_PMHISTORY_ADMIN;
   public List INV_SOUND_HELP_ITEM_LORE;
   public List INV_SOUND_ITEM_LORE;
   private PrivateMessages instance;

   public MessagesManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public String getStr(String name) {
      if (name == null) {
         return null;
      } else {
         Field[] fields = this.getClass().getFields();
         Field[] var3 = fields;
         int var4 = fields.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (field.getType().equals(String.class) && field.getName().equalsIgnoreCase(name)) {
               try {
                  return (String)field.get(this);
               } catch (IllegalArgumentException var8) {
                  var8.printStackTrace();
               } catch (IllegalAccessException var9) {
                  var9.printStackTrace();
               }
            }
         }

         return null;
      }
   }

   public void load() {
      ConfigManager lang = this.instance.getManagers().getConfigManagers().getLangConfigManager();
      Field[] fields = this.getClass().getFields();
      Field[] var3 = fields;
      int var4 = fields.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (field.getType().equals(String.class)) {
            String name = field.getName();

            try {
               field.set(this, lang.getConfig().getString(name));
            } catch (IllegalArgumentException var9) {
               var9.printStackTrace();
            } catch (IllegalAccessException var10) {
               var10.printStackTrace();
            }
         }
      }

      this.HELP_COMMAND_MESSAGE = lang.getConfig().getStringList("HELP_COMMAND_MESSAGE");
      this.HELP_COMMAND_REPLY = lang.getConfig().getStringList("HELP_COMMAND_REPLY");
      this.HELP_COMMAND_IGNORED = lang.getConfig().getStringList("HELP_COMMAND_IGNORED");
      this.HELP_COMMAND_PMREAD = lang.getConfig().getStringList("HELP_COMMAND_PMREAD");
      this.HELP_COMMAND_PMHISTORY = lang.getConfig().getStringList("HELP_COMMAND_PMHISTORY");
      this.HELP_COMMAND_PMHISTORY_ADMIN = lang.getConfig().getStringList("HELP_COMMAND_PMHISTORY_ADNIN");
      this.HELP_COMMAND_IGNORE = lang.getConfig().getStringList("HELP_COMMAND_IGNORE");
      this.INV_SOUND_HELP_ITEM_LORE = lang.getConfig().getStringList("INV_SOUND_HELP_ITEM_LORE");
      this.INV_SOUND_ITEM_LORE = lang.getConfig().getStringList("INV_SOUND_ITEM_LORE");
   }
}
