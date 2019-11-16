package jasin.mcmmo.database;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;

import cn.nukkit.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.Instant;

public class SQLDatabase implements Database { 

    private String connectString;

    private final Map<UUID, Integer> cachedUserIDs = new HashMap<UUID, Integer>();

    public SQLDatabase(String path) {
        connectString = "jdbc:sqlite:" + path + "user.db";

        checkStructure();
    }

    public void cleanupUser(UUID uuid) {

    }

    public boolean savePlayerProfile(PlayerProfile profile) {
        boolean success = true;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectString);
            int id = getUserID(connection, profile.getName(), profile.getUUID());
            if(id < 0) {
                id = newUser(connection, profile.getName(), profile.getUUID());
                if(id < 0) {
                    mcMMO.plugin.getLogger().info("Failed to add new user(" + profile.getName() +") to database.");
                    return false;
                }
            }
        } catch(SQLException e) {

        }

        return success;
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create) {
        return new PlayerProfile(name, uuid);
    }

    private int newUser(Connection connection, String name, UUID uuid) {
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("INSERT INTO users (user, uuid, last_login) VALUES(?, ?, datetime(?, 'unixepoch'))", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, uuid != null ? uuid.toString() : null);
            stmt.setString(3, String.valueOf(Instant.now().getEpochSecond()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(!rs.next()) {
                mcMMO.plugin.getLogger().info("Failed to add new user to database.");
                return -1;
            }
            
            int id = rs.getInt(1);
            fillNewUserDatabase(connection, id); 
            return id;
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage()); 
        } finally {
            tryClose(rs);
            tryClose(stmt);
        }

        return -1;
    }

    private void fillNewUserDatabase(Connection connection, int id) {
        PreparedStatement stmt = null;

        List<String> sqlStrings = new ArrayList<String>();
        sqlStrings.add("INSERT OR IGNORE INTO skill_levels (user_id) VALUES(?)");
        sqlStrings.add("INSERT OR IGNORE INTO experience (user_id) VALUES(?)");
        sqlStrings.add("INSERT OR IGNORE INTO cooldowns (user_id) VALUES(?)");

        try {
            for(String sql : sqlStrings) {
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            }
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage());
        } finally {
            tryClose(stmt);
        }
    }

    private int getUserID(final Connection connection, final String playerName, final UUID uuid) {
        if(cachedUserIDs.containsKey(uuid))
            return cachedUserIDs.get(uuid);

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("SELECT id, user FROM users WHERE uuid = ? OR (uuid = null AND user = ?)");
            stmt.setString(1, uuid.toString());
            stmt.setString(2, playerName);
            rs = stmt.executeQuery();

            if(rs.next()) {
                int id = rs.getInt("id");
                cachedUserIDs.put(uuid, id);
                return id;
            }
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage());
        } finally {
            tryClose(rs);
            tryClose(stmt);
        }

        return -1;
    }

    private void checkStructure() {
         
        Statement statement = null;
        Connection connection = null;

        List<String> tables = new ArrayList<String>();
        /* Users table */
        tables.add("CREATE TABLE IF NOT EXISTS users " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "user CHAR(40) NOT NULL, " +
                    "uuid CHAR(36) UNIQUE NULL, " +
                    "last_login INTEGER NOT NULL)");
        /* Cooldown table */
        tables.add("CREATE TABLE IF NOT EXISTS cooldowns " +
                    "(user_id INTEGER PRIMARY KEY NOT NULL, " +
                    "mining INTEGER NOT NULL DEFAULT 0, " +
                    "excavation INTEGER NOT NULL DEFAULT 0, " +
                    "woodcutting INTEGER NOT NULL DEFAULT 0)");
        /* Skill level table */
        tables.add("CREATE TABLE IF NOT EXISTS skill_levels " +
                    "(user_id INTEGER PRIMARY KEY NOT NULL, " +
                    "mining INTEGER NOT NULL DEFAULT 0, " +
                    "excavation INTEGER NOT NULL DEFAULT 0, " +
                    "woodcutting INTEGER NOT NULL DEFAULT 0)");
        /* Skill experience table */
        tables.add("CREATE TABLE IF NOT EXISTS experience " +
                    "(user_id INTEGER PRIMARY KEY NOT NULL, " +
                    "mining INTEGER NOT NULL DEFAULT 0, " +
                    "excavation INTEGER NOT NULL DEFAULT 0, " +
                    "woodcutting INTEGER NOT NULL DEFAULT 0)");
        
        try {
            connection = DriverManager.getConnection(connectString);
            statement = connection.createStatement();
            for(String table : tables) {
                statement.executeUpdate(table);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            tryClose(statement);
            tryClose(connection);
        }
    }

    private void tryClose(AutoCloseable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch(Exception e) {
                // ignore
            }
        }
    }
}
