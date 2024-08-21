package pl.mirotcz.privatemessages.spigot.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.data.PlayerSettings;
import pl.mirotcz.privatemessages.spigot.messaging.Messenger;
import pl.mirotcz.privatemessages.spigot.utils.MainUtils;
import pl.mirotcz.privatemessages.spigot.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Storage_SQLite implements Storage {
    volatile Connection conn;
    private HikariConfig config;
    private HikariDataSource ds;
    private String sql = "CREATE TABLE IF NOT EXISTS pmsg_unread_messages (\n\tID INT PRIMARY KEY,\n\tSENDER VARCHAR(16) NOT NULL,\n\tRECIPIENT VARCHAR(16) NOT NULL,\n\tMESSAGE VARCHAR(256) NOT NULL,\n DATE BIGINT(20) NOT NULL\n);";
    private String sql2 = "CREATE TABLE IF NOT EXISTS pmsg_player_settings (\n\tID INT PRIMARY KEY,\n\tPLAYER_NAME VARCHAR(16) NOT NULL,\n\tSOUND_ENABLED INT(1),\n\tSOUND_NUMBER INT(3), \n IGNORED_PLAYERS TEXT, \n SPY_ENABLED INT(1), \n PRIVATE_MESSAGES_ENABLED INT(1)\n);";
    private String sql3 = "CREATE TABLE IF NOT EXISTS pmsg_messages_history (\n\tID INT PRIMARY KEY,\n\tSENDER VARCHAR(16) NOT NULL,\n\tRECIPIENT VARCHAR(16) NOT NULL,\n\tMESSAGE VARCHAR(256) NOT NULL,\n\tDATE BIGINT(20) NOT NULL\n);";

    public Storage_SQLite(PrivateMessages plugin) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException var3) {
            Messenger.sendConsole(var3.getMessage());
        }

        this.config = new HikariConfig();
        this.config.setPoolName("PrivateMessagesSQLitePool");
        this.config.setDriverClassName("org.sqlite.JDBC");
        this.config.setJdbcUrl("jdbc:sqlite:plugins/PrivateMessages/Storage.db");
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.config.addDataSourceProperty("useServerPrepStmts", true);
        this.config.addDataSourceProperty("characterEncoding", "utf8");
        this.config.addDataSourceProperty("useUnicode", true);
        this.config.setLeakDetectionThreshold(10000L);
        this.ds = new HikariDataSource(this.config);
        this.setupTables();
    }

    private void setupTables() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(this.sql);
            ps.executeUpdate();
            ps = conn.prepareStatement(this.sql2);
            ps.executeUpdate();
            ps = conn.prepareStatement(this.sql3);
            ps.executeUpdate();
            this.close(conn, ps, null);
        } catch (SQLException var4) {
            this.close(conn, ps, null);
            var4.printStackTrace();
        }

    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException var7) {
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException var6) {
            }
        }

        if (res != null) {
            try {
                res.close();
            } catch (SQLException var5) {
            }
        }

    }

    public void closePool() {
        if (this.ds != null && !this.ds.isClosed()) {
            this.ds.close();
        }

    }

    public void saveUnreadMessage(Message message) {
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO pmsg_unread_messages (SENDER, RECIPIENT, MESSAGE, DATE) VALUES (?, ?, ?, ?)";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, message.getSenderName());
            ps.setString(2, message.getRecipientName());
            ps.setString(3, message.getMessageContent());
            ps.setLong(4, message.getDate());
            ps.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

    }

    public List getUnreadMessages(String recipient_name) {
        List messages = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_unread_messages WHERE RECIPIENT = ? ORDER BY DATE DESC";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, recipient_name);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Message message = new Message(rs.getString("SENDER"), rs.getString("RECIPIENT"), rs.getLong("DATE"), rs.getString("MESSAGE"));
                    messages.add(message);
                }
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return messages;
    }

    public boolean clearUnreadMessages(String recipient_name) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement("DELETE FROM pmsg_unread_messages WHERE RECIPIENT = ?");
            ps.setString(1, recipient_name);
            result = ps.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return result > 0;
    }

    public void saveMessage(Message message) {
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO pmsg_messages_history (SENDER, RECIPIENT, MESSAGE, DATE) VALUES (?, ?, ?, ?)";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, message.getSenderName());
            ps.setString(2, message.getRecipientName());
            ps.setString(3, message.getMessageContent());
            ps.setLong(4, message.getDate());
            ps.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

    }

    public List getMessagesFromHistory(String player1, String player2) {
        List messages = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_messages_history WHERE (SENDER = ? AND RECIPIENT = ?) OR (SENDER = ? AND RECIPIENT = ?) ORDER BY DATE DESC";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, player1);
            ps.setString(2, player2);
            ps.setString(3, player2);
            ps.setString(4, player1);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Message message = new Message(rs.getString("SENDER"), rs.getString("RECIPIENT"), rs.getLong("DATE"), rs.getString("MESSAGE"));
                    messages.add(message);
                }
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
            messages = null;
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return messages;
    }

    public List getMessagesFromHistory(String player) {
        List messages = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_messages_history WHERE SENDER = ? OR RECIPIENT = ? ORDER BY DATE DESC";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, player);
            ps.setString(2, player);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Message message = new Message(rs.getString("SENDER"), rs.getString("RECIPIENT"), rs.getLong("DATE"), rs.getString("MESSAGE"));
                    messages.add(message);
                }
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
            messages = null;
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return messages;
    }

    public List getMessagesFromHistory() {
        List messages = new ArrayList();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_messages_history ORDER BY DATE DESC";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Message message = new Message(rs.getString("SENDER"), rs.getString("RECIPIENT"), rs.getLong("DATE"), rs.getString("MESSAGE"));
                    messages.add(message);
                }
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
            messages = null;
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return messages;
    }

    public Map loadAllPlayerSettings() {
        Map settingsData = new HashMap();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_player_settings";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String player_name = rs.getString("PLAYER_NAME");
                int notification_sound_number = rs.getInt("SOUND_NUMBER");
                Set ignored_players = StringUtils.getSetFromString(rs.getString("IGNORED_PLAYERS"));
                boolean sound_enabled = MainUtils.getBoolVal(rs.getInt("SOUND_ENABLED"));
                boolean spy_enabled = MainUtils.getBoolVal(rs.getInt("SPY_ENABLED"));
                boolean pmsg_enabled = MainUtils.getBoolVal(rs.getInt("PRIVATE_MESSAGES_ENABLED"));
                PlayerSettings data = new PlayerSettings();
                data.load(player_name, notification_sound_number, sound_enabled, spy_enabled, ignored_players, pmsg_enabled);
                settingsData.put(data.getPlayerName(), data);
            }
        } catch (SQLException var16) {
            var16.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return settingsData;
    }

    public void savePlayerSettings(PlayerSettings data) {
        Connection conn = null;
        PreparedStatement ps = null;
        String query;
        if (this.playerSettingsExists(data.getPlayerName())) {
            query = "UPDATE pmsg_player_settings SET SOUND_NUMBER = ?, SOUND_ENABLED = ?, SPY_ENABLED = ?, IGNORED_PLAYERS = ?, PRIVATE_MESSAGES_ENABLED = ? WHERE PLAYER_NAME = ?";
        } else {
            query = "INSERT INTO pmsg_player_settings (SOUND_NUMBER, SOUND_ENABLED, SPY_ENABLED, IGNORED_PLAYERS, PRIVATE_MESSAGES_ENABLED, PLAYER_NAME) VALUES (?, ?, ?, ?, ?, ?)";
        }

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, data.getMessageNotificationSoundNumber());
            ps.setInt(2, MainUtils.getIntVal(data.isMessageNotificationSoundEnabled()));
            ps.setInt(3, MainUtils.getIntVal(data.isMessageSpyEnabled()));
            ps.setString(4, StringUtils.getStringFromSet(data.getIgnoredPlayers()));
            ps.setInt(5, MainUtils.getIntVal(data.arePrivateMessagesEnabled()));
            ps.setString(6, data.getPlayerName());
            ps.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

    }

    public void removePlayerSettings(PlayerSettings data) {
        Connection conn = null;
        PreparedStatement ps = null;
        if (this.playerSettingsExists(data.getPlayerName())) {
            String query = "DELETE FROM pmsg_player_settings WHERE PLAYER_NAME = ?";

            try {
                conn = this.ds.getConnection();
                ps = conn.prepareStatement(query);
                ps.setString(1, data.getPlayerName());
                ps.executeUpdate();
            } catch (SQLException var9) {
                var9.printStackTrace();
            } finally {
                this.close(conn, ps, (ResultSet) null);
            }
        }

    }

    public boolean playerSettingsExists(String playerName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;
        String query = "SELECT * FROM pmsg_player_settings WHERE PLAYER_NAME = ?";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, playerName);
            rs = ps.executeQuery();
            result = rs.isBeforeFirst();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return result;
    }

    public PlayerSettings getPlayerSettings(String player_name) {
        PlayerSettings data = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM pmsg_player_settings WHERE PLAYER_NAME = ?";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, player_name);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                data = new PlayerSettings();
                int sound_number = rs.getInt("SOUND_NUMBER");
                String name = rs.getString("PLAYER_NAME");
                Set ignored_players = StringUtils.getSetFromString(rs.getString("IGNORED_PLAYERS"));
                boolean sound_enabled = MainUtils.getBoolVal(rs.getInt("SOUND_ENABLED"));
                boolean spy_enabled = MainUtils.getBoolVal(rs.getInt("SPY_ENABLED"));
                boolean msg_enabled = MainUtils.getBoolVal(rs.getInt("PRIVATE_MESSAGES_ENABLED"));
                data.load(name, sound_number, sound_enabled, spy_enabled, ignored_players, msg_enabled);
            }
        } catch (SQLException var16) {
            var16.printStackTrace();
        } finally {
            this.close(conn, ps, (ResultSet) null);
        }

        return data;
    }
}
