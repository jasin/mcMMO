package jasin.mcmmo.configuration;

import jasin.mcmmo.mcMMO;

import cn.nukkit.utils.Config;

import java.io.*;

public class Configuration {

    private static File database;
    private static File xpRewards;
    private static File extraDrops;
    private static File helpMsgs;
    private static Config databaseCfg;
    private static Config xpRewardsCfg;
    private static Config extraDropsCfg;
    
    public static void setup() {

        File dataDir = mcMMO.plugin.getDataFolder();
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }

        saveResources();

        database = new File(dataDir, "database.yml");
        xpRewards = new File(dataDir, "xprewards.yml");
        extraDrops = new File(dataDir, "drops.yml");
        helpMsgs = new File(dataDir, "help.ini");

        loadConfigs();
    }

    public static void saveResources() {
        mcMMO.plugin.saveResource("database.yml", true);
        mcMMO.plugin.saveResource("drops.yml", true);
        mcMMO.plugin.saveResource("xprewards.yml", true);
    }

    public static void loadConfigs() {
        databaseCfg = new Config(database, Config.YAML);
        xpRewardsCfg = new Config(xpRewards, Config.YAML);
        extraDropsCfg = new Config(extraDrops, Config.YAML);
    }

    public Config getDatabase() {
        return databaseCfg;
    }

    public Config getXpRewards() {
        return xpRewardsCfg;
    }

    public Config getExtraDrops() {
        return extraDropsCfg;
    }
}
