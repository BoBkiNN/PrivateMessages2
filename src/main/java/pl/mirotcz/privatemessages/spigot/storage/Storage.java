package pl.mirotcz.privatemessages.spigot.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;

public interface Storage {
   void saveUnreadMessage(Message var1);

   List<Message> getUnreadMessages(String var1);

   boolean clearUnreadMessages(String var1);

   void saveMessage(Message var1);

   List<Message> getMessagesFromHistory(String var1, String var2);

   List<Message> getMessagesFromHistory(String var1);

   List<Message> getMessagesFromHistory();

   void close(Connection var1, PreparedStatement var2, ResultSet var3);

   void closePool();

   void removePlayerSettings(PlayerSettings var1);

   boolean playerSettingsExists(String var1);

   void savePlayerSettings(PlayerSettings var1);

   PlayerSettings getPlayerSettings(String var1);

   Map<String, PlayerSettings> loadAllPlayerSettings();
}
