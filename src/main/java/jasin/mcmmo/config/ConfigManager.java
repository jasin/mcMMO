package jasin.mcmmo.config;

import jasin.mcmmo.mcMMO;

import cn.nukkit.utils.Config;

import java.io.*;

public class ConfigManager {

    private static File database;
    private static File experience;
    private static File extra_drops;
    private static File helpMsgs;
    private static Config configDatabase;
    private static Config configExperience;
    private static Config configExtras;
    
    public static void setup() {

        File dataDir = mcMMO.plugin.getDataFolder();
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }

        saveResources();

        database = new File(dataDir, "database.yml");
        experience = new File(dataDir, "experience.yml");
        extra_drops = new File(dataDir, "extras.yml");
        helpMsgs = new File(dataDir, "help.ini");

        loadConfigs();
    }

    public static void saveResources() {
        mcMMO.plugin.saveResource("database.yml", true);
        mcMMO.plugin.saveResource("extras.yml", true);
        mcMMO.plugin.saveResource("experience.yml", true);
    }

    public static void loadConfigs() {
        configDatabase = new Config(database, Config.YAML);
        configExperience = new Config(experience, Config.YAML);
        configExtras = new Config(extra_drops, Config.YAML);
    }

    public Config getDatabaseCfg() {
        return configDatabase;
    }

    public Config getExperienceCfg() {
        return configExperience;
    }

    public Config getExtrasCfg() {
        return configExtras;
    }
}
