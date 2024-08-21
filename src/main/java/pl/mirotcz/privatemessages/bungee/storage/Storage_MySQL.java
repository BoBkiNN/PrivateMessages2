package pl.mirotcz.privatemessages.bungee.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pl.mirotcz.privatemessages.Message;
import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.data.PlayerSettings;
import pl.mirotcz.privatemessages.bungee.utils.MathUtils;
import pl.mirotcz.privatemessages.bungee.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Storage_MySQL implements Storage {
    private final PrivateMessages instance;
    private final HikariConfig config;
    private final HikariDataSource ds;
    private final String sql = "CREATE TABLE IF NOT EXISTS pmsg_unread_messages (\n\tID INT PRIMARY KEY AUTO_INCREMENT,\n\tSENDER VARCHAR(16) NOT NULL,\n\tRECIPIENT VARCHAR(16) NOT NULL,\n\tMESSAGE VARCHAR(256) NOT NULL,\n DATE BIGINT(20) NOT NULL\n);";
    private final String sql2 = "CREATE TABLE IF NOT EXISTS pmsg_player_settings (\n\tID INT PRIMARY KEY AUTO_INCREMENT,\n\tPLAYER_NAME VARCHAR(16) NOT NULL,\n\tSOUND_ENABLED INT(1) DEFAULT 1,\n\tSOUND_NUMBER INT(3) DEFAULT 1, \n IGNORED_PLAYERS TEXT, \n SPY_ENABLED INT(1), \n PRIVATE_MESSAGES_ENABLED INT(1)\n);";
    private final String sql3 = "CREATE TABLE IF NOT EXISTS pmsg_messages_history (\n\tID INT PRIMARY KEY AUTO_INCREMENT,\n\tSENDER VARCHAR(16) NOT NULL,\n\tRECIPIENT VARCHAR(16) NOT NULL,\n\tMESSAGE VARCHAR(256) NOT NULL,\n\tDATE BIGINT(20) NOT NULL\n);";
    private final String sql4 = "CREATE TABLE IF NOT EXISTS pmsg_players_waiting_settings_reload (\n\tID INT PRIMARY KEY AUTO_INCREMENT,\n\tPLAYER_NAME VARCHAR(16) NOT NULL,\n\tDATE BIGINT(20) NOT NULL\n);";
    private final String sql5 = "CREATE TABLE IF NOT EXISTS pmsg_vanished_players (\n\tPLAYER_NAME VARCHAR(16) NOT NULL,\n\tDATE BIGINT(20) NOT NULL\n);";
    private final String dbaddress;
    private final String dbname;
    private final String dbuser;
    private final String dbpass;
    private final int dbport;
    volatile Connection conn;

    public Storage_MySQL(PrivateMessages plugin) {
        this.instance = plugin;
        this.dbaddress = this.instance.getSettings().DB_HOST;
        this.dbname = this.instance.getSettings().DB_NAME;
        this.dbuser = this.instance.getSettings().DB_USER;
        this.dbpass = this.instance.getSettings().DB_PASS;
        this.dbport = this.instance.getSettings().DB_PORT;
        this.config = new HikariConfig();
        this.config.setJdbcUrl("jdbc:mysql://" + this.dbaddress + ":" + this.dbport + "/" + this.dbname + "?useSSL=false");
        this.config.setUsername(this.dbuser);
        this.config.setPassword(this.dbpass);
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.config.addDataSourceProperty("useServerPrepStmts", true);
        this.config.addDataSourceProperty("characterEncoding", "utf8");
        this.config.addDataSourceProperty("useUnicode", true);
        this.config.setLeakDetectionThreshold(10000L);
        this.config.setMaximumPoolSize(3);
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
            ps = conn.prepareStatement(this.sql4);
            ps.executeUpdate();
            ps = conn.prepareStatement(this.sql5);
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
            } catch (SQLException ignored) {
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ignored) {
            }
        }

        if (res != null) {
            try {
                res.close();
            } catch (SQLException ignored) {
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
            this.close(conn, ps, null);
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
            this.close(conn, ps, null);
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
            this.close(conn, ps, null);
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
            this.close(conn, ps, null);
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
            this.close(conn, ps, null);
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
            this.close(conn, ps, null);
        }

        return messages;
    }

    public List<Message> getMessagesFromHistory() {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
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
            this.close(conn, ps, null);
        }

        return messages;
    }

    public ConcurrentHashMap<String, PlayerSettings> loadAllPlayerSettings() {
        ConcurrentHashMap<String, PlayerSettings> settingsData = new ConcurrentHashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        String query = "SELECT * FROM pmsg_player_settings";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("PLAYER_NAME");
                Set<String> ignored_players = StringUtils.getSetFromString(rs.getString("IGNORED_PLAYERS"));
                boolean spy_enabled = MathUtils.getBoolVal(rs.getInt("SPY_ENABLED"));
                boolean msg_enabled = MathUtils.getBoolVal(rs.getInt("PRIVATE_MESSAGES_ENABLED"));
                PlayerSettings data = new PlayerSettings(name, spy_enabled, ignored_players, msg_enabled);
                settingsData.put(data.getPlayerName(), data);
            }
        } catch (SQLException var14) {
            var14.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

        return settingsData;
    }

    public void savePlayerSettings(PlayerSettings data) {
        Connection conn = null;
        PreparedStatement ps = null;
        String query;
        if (this.playerSettingsExists(data.getPlayerName())) {
            query = "UPDATE pmsg_player_settings SET SPY_ENABLED = ?, IGNORED_PLAYERS = ?, PRIVATE_MESSAGES_ENABLED = ? WHERE PLAYER_NAME = ?";
        } else {
            query = "INSERT INTO pmsg_player_settings (SPY_ENABLED, IGNORED_PLAYERS, PRIVATE_MESSAGES_ENABLED, PLAYER_NAME) VALUES (?, ?, ?, ?)";
        }

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, MathUtils.getIntVal(data.isMessageSpyEnabled()));
            ps.setString(2, StringUtils.getStringFromSet(data.getIgnoredPlayers()));
            ps.setInt(3, MathUtils.getIntVal(data.arePrivateMessagesEnabled()));
            ps.setString(4, data.getPlayerName());
            ps.executeUpdate();
        } catch (SQLException var9) {
            var9.printStackTrace();
        } finally {
            this.close(conn, ps, null);
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
                this.close(conn, ps, null);
            }
        }

    }

    public boolean playerSettingsExists(String playerName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
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
            this.close(conn, ps, null);
        }

        return result;
    }

    public void clearPlayerWaitingForSettingsReloadTable() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(this.sql4);
            ps.executeUpdate();
            ps = conn.prepareStatement(this.sql5);
            ps.executeUpdate();
        } catch (SQLException var7) {
            var7.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

    }

    public boolean hasPlayerWaitingForSettingsReload(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        boolean result = false;
        String query = "SELECT * FROM pmsg_players_waiting_settings_reload WHERE PLAYER_NAME = ?";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            result = rs.isBeforeFirst();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

        return result;
    }

    public void addPlayerWatingForSettingsReload(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        long time = System.currentTimeMillis();
        String sql;
        if (this.hasPlayerWaitingForSettingsReload(name)) {
            sql = "UPDATE pmsg_players_waiting_settings_reload SET DATE = ? WHERE PLAYER_NAME = ?";
        } else {
            sql = "INSERT INTO pmsg_players_waiting_settings_reload (DATE, PLAYER_NAME) VALUES (?, ?)";
        }

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, time);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

    }

    public void clearVansihedPlayersInfo() {
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "TRUNCATE TABLE pmsg_vanished_players";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException var8) {
            var8.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

    }

    public Map<String, Long> getVanishedPlayersInfo() {
        Map<String, Long> players = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        String query = "SELECT * FROM pmsg_vanished_players ORDER BY DATE DESC";

        try {
            conn = this.ds.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String player_name = rs.getString("PLAYER_NAME");
                    long time = rs.getLong("DATE");
                    if (!players.containsKey(player_name)) {
                        players.put(player_name, time);
                    }
                }
            }

            return players;
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            this.close(conn, ps, null);
        }

        return players;
    }
}
