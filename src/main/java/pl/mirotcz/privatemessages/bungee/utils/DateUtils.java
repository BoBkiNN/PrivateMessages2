package pl.mirotcz.privatemessages.bungee.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
   public static long getDays(long miliseconds) {
      return miliseconds / 86400000L;
   }

   public static long getHours(long miliseconds) {
      return miliseconds / 3600000L;
   }

   public static long getMinutes(long miliseconds) {
      return miliseconds / 60000L;
   }

   public static String getFormattedDate(long miliseconds, DateTimeFormatter formatter) {
      LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(miliseconds), formatter.getZone());
      return time.format(formatter);
   }
}
