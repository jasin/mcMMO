package jasin.mcmmo;

import jasin.mcmmo.database.Database;
import jasin.mcmmo.database.SQLDatabase;
import jasin.mcmmo.database.DatabaseManager;
import jasin.mcmmo.listeners.BlockListener;
import jasin.mcmmo.listeners.PlayerEventListener;
import jasin.mcmmo.config.ConfigManager;
import jasin.mcmmo.config.experience.ConfigExperience;
import jasin.mcmmo.utils.experience.FormulaManager;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.task.player.PlayerProfileLoading;
import jasin.mcmmo.utils.player.NotificationManager;

import cn.nukkit.Server;
import cn.nukkit.Player;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.block.Block;
import cn.nukkit.scheduler.NukkitRunnable;

import java.lang.*;
import java.io.*;

public class mcMMO extends PluginBase {

    public static mcMMO plugin;
    private static String pluginName = "mcMMO";

    /* Managers */
    private ConfigManager configManager;
    private FormulaManager formulaManager;
    private NotificationManager notificationManager;

    private Database database;

    // Metadata constants
    public final static String PLAYER_DATA_KEY = "mcMMO: Player Data";
    
    @Override
    public void onLoad() {
        plugin = this;
        this.getLogger().info(TextFormat.WHITE + "Loaded " + pluginName);
    }

    @Override
    public void onEnable() {
        /* Load Configuration files */
        loadConfigFiles();

        /* Init Managers */
        formulaManager = new FormulaManager();

        /* Init Database */
        Config configDatabase = configManager.getDatabaseCfg();
        String dbtype = configDatabase.getString("type");
        String playerProfileDir = configDatabase.getSection(dbtype.toUpperCase()).getString("datafolder");
        String playerProfilePath = this.getDataFolder() + File.separator + playerProfileDir + File.separator; 

        File dir = new File(playerProfilePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        
        database = DatabaseManager.getDatabaseClass(dbtype, playerProfilePath);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        /* 
         * For server Reload event ONLY
         * Load players already online during a server reload
         * as they will not fire a PlayerJoin event.
         */
        for(Player player : getServer().getOnlinePlayers().values()) {
            new PlayerProfileLoading(player).runTaskLaterAsynchronously(mcMMO.plugin, 1);
        }
    }

    @Override
    public void onDisable() {
        try {
            UserManager.saveAll();
            UserManager.clearAll();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        this.getLogger().info(TextFormat.RED + "Disabled " + pluginName);
    }

    public Database getDatabaseManager() {
        return database;
    }

    public FormulaManager getFormulaManager() {
        return formulaManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ConfigExperience getExperienceConfig() {
        return ConfigExperience.getInstance();
    }

    private void loadConfigFiles() {
        configManager = new ConfigManager();
        ConfigManager.setup();
    }
}
