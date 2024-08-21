package pl.mirotcz.privatemessages.spigot.messaging;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messenger {
   private static String prefix;

   public static void setPrefix(String newPrefix) {
      prefix = newPrefix;
   }

   public static String getPrefix() {
      return prefix;
   }

   public static void send(CommandSender recipient, String message) {
      if (recipient != null && message != null) {
         if (recipient instanceof Player && !((Player)recipient).isOnline()) {
            return;
         }

         recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void send(CommandSender recipient, List messages) {
      if (recipient != null && messages != null) {
         if (recipient instanceof Player && !((Player)recipient).isOnline()) {
            return;
         }

         messages.forEach((msg) -> {
            recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
         });
      }

   }

   public static void sendCustomPrefix(CommandSender recipient, String prefix, String message) {
      if (recipient != null && message != null) {
         if (recipient instanceof Player && !((Player)recipient).isOnline()) {
            return;
         }

         recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void sendConsole(String message) {
      if (message != null) {
         Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void sendSystem(String message) {
      if (message != null) {
         System.out.println(prefix + message);
      }

   }
}
