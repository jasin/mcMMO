package jasin.mcmmo.database;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.skills.SuperAbilityType;

import cn.nukkit.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.UUID;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        PreparedStatement stmt = null;

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

            // Finish
            stmt = connection.prepareStatement("UPDATE users SET last_login = datetime(?, 'unixepoch') WHERE id = ?");
            stmt.setString(1, String.valueOf(Instant.now().getEpochSecond()));
            stmt.setInt(2, id);
            success &= (stmt.executeUpdate() > 0);
            stmt.close();
            if(!success) {
                mcMMO.plugin.getLogger().info("Failed to update last_login, " + profile.getName() + " profile won't be saved.");
                return false;
            }

            stmt = connection.prepareStatement("UPDATE skill_levels SET "
                    + "mining = ?, excavation = ?, woodcutting = ? WHERE user_id = ?");
            stmt.setInt(1, profile.getSkillLevel(PrimarySkillType.MINING));
            stmt.setInt(2, profile.getSkillLevel(PrimarySkillType.EXCAVATION));
            stmt.setInt(3, profile.getSkillLevel(PrimarySkillType.WOODCUTTING));
            stmt.setInt(4, id);
            success &= (stmt.executeUpdate() > 0);
            stmt.close();
            if(!success) {
                mcMMO.plugin.getLogger().info("Failed to update " + profile.getName() + " skills table");
                return false;
            }

            stmt = connection.prepareStatement("UPDATE experience SET "
                    + "mining = ?, excavation = ?, woodcutting = ? WHERE user_id = ?");
            stmt.setInt(1, profile.getSkillXpLevel(PrimarySkillType.MINING));
            stmt.setInt(2, profile.getSkillXpLevel(PrimarySkillType.EXCAVATION));
            stmt.setInt(3, profile.getSkillXpLevel(PrimarySkillType.WOODCUTTING));
            stmt.setInt(4, id);
            success &= (stmt.executeUpdate() > 0);
            stmt.close();
            if(!success) {
                mcMMO.plugin.getLogger().info("Failed to update " + profile.getName() + " experience table");
                return false;
            }

            stmt = connection.prepareStatement("UPDATE cooldowns SET "
                    + "mining = ?, excavation = ?, woodcutting = ? WHERE user_id = ?");
            stmt.setInt(1, profile.getAbilityCooldown(SuperAbilityType.SUPER_BREAKER));
            stmt.setInt(2, profile.getAbilityCooldown(SuperAbilityType.GIGA_DRILL_BREAKER));
            stmt.setInt(3, profile.getAbilityCooldown(SuperAbilityType.TREE_FELLER));
            stmt.setInt(4, id);
            success &= (stmt.executeUpdate() > 0);
            stmt.close();
            if(!success) {
                mcMMO.plugin.getLogger().info("Failed to update " + profile.getName() + " cooldowns table");
                return false;
            }
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage());
        } finally {
            tryClose(stmt);
            tryClose(connection);
        }

        return success;
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create) {
        return loadPlayerProfile(name, uuid, create, true);
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create, boolean retry) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(connectString);
            int id = getUserID(connection, name, uuid);
            if(id < 0) {
                if(create) {
                    id = newUser(connection, name, uuid);
                    create = false;
                }
            }

            if(id < 0) {
                return new PlayerProfile(name, uuid, false);
            }

            // Make sure databases are linked
            linkDatabases(connection, id);

            stmt = connection.prepareStatement("SELECT "
                    + "s.mining, s.excavation, s.woodcutting, "
                    + "e.mining, e.excavation, e.woodcutting, "
                    + "c.mining, c.excavation, c.woodcutting, "
                    + "u.uuid, u.user "
                    + "FROM users u "
                    + "INNER JOIN skill_levels s ON (u.id = s.user_id) "
                    + "INNER JOIN experience e ON (u.id = e.user_id) "
                    + "INNER JOIN cooldowns c ON (u.id = c.user_id) "
                    + "WHERE u.id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if(rs.next()) {
                PlayerProfile profile = loadFromResults(name, rs);
                rs.close();
                stmt.close();
                return profile;
            }
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage());
        } finally {
            tryClose(rs);
            tryClose(stmt);
            tryClose(connection);
        }

        // Retry? return empty profile
        if(!retry) {
            return new PlayerProfile(name, uuid, false);
        }

        // Retry loading again
        return loadPlayerProfile(name, uuid, create, false);
    }

    private PlayerProfile loadFromResults(String name, ResultSet rs) throws SQLException {
        UUID uuid;
        Map<PrimarySkillType, Integer> skill_levels = new EnumMap<PrimarySkillType, Integer>(PrimarySkillType.class);
        Map<PrimarySkillType, Integer> experience = new EnumMap<PrimarySkillType, Integer>(PrimarySkillType.class);
        Map<SuperAbilityType, Integer> cooldowns = new EnumMap<SuperAbilityType, Integer>(SuperAbilityType.class);
        
        final int OFFSET_SKILL_LEVELS = 0;
        final int OFFSET_EXPERIENCE = 3;
        final int OFFSET_COOLDOWNS = 6;
        final int OFFSET_OTHER = 9;

        /* 
         * Player's skill level 
         */
        skill_levels.put(PrimarySkillType.MINING, rs.getInt(OFFSET_SKILL_LEVELS +1));
        skill_levels.put(PrimarySkillType.EXCAVATION, rs.getInt(OFFSET_SKILL_LEVELS +2));
        skill_levels.put(PrimarySkillType.WOODCUTTING, rs.getInt(OFFSET_SKILL_LEVELS +3));
        /*
         * Player's skill experience
         */
        experience.put(PrimarySkillType.MINING, rs.getInt(OFFSET_EXPERIENCE +1));
        experience.put(PrimarySkillType.EXCAVATION, rs.getInt(OFFSET_EXPERIENCE +2));
        experience.put(PrimarySkillType.WOODCUTTING, rs.getInt(OFFSET_EXPERIENCE +3));
        /*
         * Player's Super Ability cooldowns
         */
        cooldowns.put(SuperAbilityType.SUPER_BREAKER, rs.getInt(OFFSET_COOLDOWNS +1));
        cooldowns.put(SuperAbilityType.GIGA_DRILL_BREAKER, rs.getInt(OFFSET_COOLDOWNS +2));
        cooldowns.put(SuperAbilityType.TREE_FELLER, rs.getInt(OFFSET_COOLDOWNS +3));

        try {
            uuid = UUID.fromString(rs.getString(OFFSET_OTHER +1));
        } catch(Exception e) {
            uuid = null;
        }
        
        return new PlayerProfile(name, uuid, skill_levels, experience, cooldowns);
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
            linkDatabases(connection, id); 
            return id;
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage()); 
        } finally {
            tryClose(rs);
            tryClose(stmt);
        }

        return -1;
    }

    private void linkDatabases(Connection connection, int id) {
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

    private void debug(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            for (int i=1; i<=colNum; i++) {
                if(i > 1) System.out.print(", ");
                String colValue = rs.getString(i);
                System.out.print(colValue + " " + rsmd.getColumnName(i));
            }
        } catch(SQLException e) {
            mcMMO.plugin.getLogger().info(e.getMessage());
        } 
    }
}
