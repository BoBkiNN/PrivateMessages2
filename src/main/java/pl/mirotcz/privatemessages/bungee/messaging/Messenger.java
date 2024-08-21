package pl.mirotcz.privatemessages.bungee.messaging;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
         if (recipient instanceof ProxiedPlayer && !((ProxiedPlayer)recipient).isConnected()) {
            return;
         }

         recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void send(CommandSender recipient, List messages) {
      if (recipient != null && messages != null) {
         if (recipient instanceof ProxiedPlayer && !((ProxiedPlayer)recipient).isConnected()) {
            return;
         }

         messages.forEach((msg) -> {
            recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
         });
      }

   }

   public static void sendCustomPrefix(CommandSender recipient, String prefix, String message) {
      if (recipient != null && message != null) {
         if (recipient instanceof ProxiedPlayer && !((ProxiedPlayer)recipient).isConnected()) {
            return;
         }

         recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void sendConsole(String message) {
      if (message != null) {
         ProxyServer.getInstance().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
      }

   }

   public static void sendSystem(String message) {
      if (message != null) {
         System.out.println(prefix + message);
      }

   }
}
