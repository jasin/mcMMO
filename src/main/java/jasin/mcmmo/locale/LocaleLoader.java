package jasin.mcmmo.locale;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.MissingResourceException;

public final class LocaleLoader {

    private static final String BUNDLE_PATH = "locale.locale";
    private static Map<String, String> bundleCache = new HashMap<String, String>();
    private static ResourceBundle bundle = null;

    public static String getString(String key, Object... msgArgs) {
        if(bundle == null)
            init();

        String rawMessage = bundleCache.computeIfAbsent(key, k -> LocaleLoader.getRawString(k));
        System.out.println("Message here");
        return formatString(rawMessage, msgArgs);
    }

    public static String formatString(String msg, Object... msgArgs) {
        if(msgArgs != null) {
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(msg.replace("'", "''"));
            msg = formatter.format(msgArgs);
        }

        return msg;
    }

    private static String getRawString(String key) {
        try {
            return bundle.getString(key);
        } catch(MissingResourceException e) {
            System.out.println(e.getMessage());
        }

        return "Oops";
    }

    private static void init() {
        Locale locale = new Locale("en", "US");
        bundle = ResourceBundle.getBundle(BUNDLE_PATH, locale);
    }
}
