package pl.mirotcz.privatemessages.bungee.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;

public interface Storage {
   void saveUnreadMessage(Message var1);

   List getUnreadMessages(String var1);

   boolean clearUnreadMessages(String var1);

   void saveMessage(Message var1);

   List getMessagesFromHistory(String var1, String var2);

   List getMessagesFromHistory(String var1);

   List getMessagesFromHistory();

   void close(Connection var1, PreparedStatement var2, ResultSet var3);

   void closePool();

   void removePlayerSettings(PlayerSettings var1);

   boolean playerSettingsExists(String var1);

   void savePlayerSettings(PlayerSettings var1);

   ConcurrentHashMap<String, PlayerSettings> loadAllPlayerSettings();

   void clearPlayerWaitingForSettingsReloadTable();

   void addPlayerWatingForSettingsReload(String var1);

   void clearVansihedPlayersInfo();

   Map getVanishedPlayersInfo();
}
