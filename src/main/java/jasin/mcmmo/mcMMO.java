package jasin.mcmmo;

import jasin.mcmmo.database.*;
import jasin.mcmmo.listeners.PlayerEventListener;
import jasin.mcmmo.configuration.Configuration;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.lang.*;
import java.io.*;

public class mcMMO extends PluginBase {

    public static mcMMO plugin;
    private static Config databaseCfg;
    private static String pluginName = "mcMMO";
    private Configuration configuration;
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
        configuration = new Configuration();
        Configuration.setup();

        Config databaseCfg = configuration.getDatabase();
        String dbtype = databaseCfg.getString("type");
        String playerProfileDir = databaseCfg.getSection(dbtype.toUpperCase()).getString("datafolder");
        String playerProfilePath = this.getDataFolder() + File.separator + playerProfileDir + File.separator; 

        File dir = new File(playerProfilePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        
        this.database = DatabaseManager.getDatabaseClass(dbtype, playerProfilePath);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.RED + "Disabled " + pluginName);
    }

    public Database getDatabaseManager() {
        return this.database;
    }
}
