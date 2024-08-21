package pl.mirotcz.privatemessages.bungee.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MathUtils {
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

   public static boolean isInteger(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }
}
