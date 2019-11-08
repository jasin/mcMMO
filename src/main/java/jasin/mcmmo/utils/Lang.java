package jasin.mcmmo.utils;

import jasin.mcmmo.mcMMO;

import cn.nukkit.utils.Config;

public class Lang {

    private static Config messagesCfg;
    
    public static void get() {
//        messagesCfg = mcMMO.getInstance().getConfigManager().getMessagesCfg();
    }

    public static String getMessage(String cmd, String sub_cmd) {
        get();
        return "a message";
    }

    public static String permission() {
        get();
        return "permission";
    }

    public static String notaplayer() {
        get();
        return "notaplayer";
    }
}
