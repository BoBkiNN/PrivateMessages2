package pl.mirotcz.privatemessages.spigot.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

public class MainUtils {
   public static void reloadPlugin() {
      PrivateMessages.get().getManagers().getConfigManagers().reloadConfigs();
      PrivateMessages.get().getMessages().load();
      PrivateMessages.get().getSettings().load();
   }

   public static int getIntVal(boolean value) {
      return value ? 1 : 0;
   }

   public static boolean getBoolVal(int value) {
      return value == 1;
   }

   public static int getRandomInt(int min, int max) {
      if (min >= max) {
         throw new IllegalArgumentException("max must be greater than min");
      } else {
         Random r = new Random();
         return r.nextInt(max - min + 1) + min;
      }
   }

   public static List<String> getColorfulStringList(List<String> list) {
      List<String> lore = new ArrayList<>();

       for (String line : list) {
           lore.add(ChatColor.translateAlternateColorCodes('&', line));
       }

      return lore;
   }

   public static boolean isInteger(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static OfflinePlayer getOfflinePlayer(String name, OfflinePlayer[] players) {
      OfflinePlayer[] var2 = players;
      int var3 = players.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         OfflinePlayer op = var2[var4];
         if (op.getName().equalsIgnoreCase(name)) {
            return op;
         }
      }

      return null;
   }

   public static boolean containsIgnoreCase(List list, String value) {
      Iterator var2 = list.iterator();

      String text;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         text = (String)var2.next();
      } while(!text.equalsIgnoreCase(value));

      return true;
   }

   public static double round(double value, int places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         long factor = (long)Math.pow(10.0, (double)places);
         value *= (double)factor;
         long tmp = Math.round(value);
         return (double)tmp / (double)factor;
      }
   }
}
