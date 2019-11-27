package jasin.mcmmo.utils;

public class StringUtils {
    
    public static String getCapitalized(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
