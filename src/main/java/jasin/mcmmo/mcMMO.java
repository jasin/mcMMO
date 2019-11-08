package jasin.mcmmo;

import jasin.mcmmo.database.*;
import jasin.mcmmo.configuration.Configuration;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.lang.*;
import java.io.*;

public class mcMMO extends PluginBase {

    private static mcMMO instance;
    private static Config databaseCfg;
    private static String pluginName = "mcMMO";
    private Configuration configuration;
    private Database database;
    
    @Override
    public void onLoad() {
        instance = this;
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
        this.database = DatabaseManager.getDatabaseClass(dbtype, playerProfilePath);
        System.out.println(this.database);

        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.RED + "Disabled " + pluginName);
    }

    public Database getDatabase() {
        return this.database;
    }

    public static mcMMO getInstance() {
        return instance;
    }
}
