package jasin.mcmmo.utils.player;

import jasin.mcmmo.locale.LocaleLoader;
import jasin.mcmmo.datatypes.interactions.NotificationType;
import jasin.mcmmo.events.skills.McMMOPlayerNotificationEvent;

import cn.nukkit.Player;
import cn.nukkit.Server;

// TODO this will need some fudging
// for now just get the values and format the string
// in LocaleLoader. In future possibly provide other
// languages. Won't be me adding them!!!
// There is no ChatMessageType or TextComponent for nukkit.
public class NotificationManager {
    
    public static void sendPlayerInformation(Player player, NotificationType notificationType, String key) {
    }

    public static void sendPlayerInformation(Player player, NotificationType notificationType, String key, String... values) {
        if(UserManager.getPlayer(player) == null)
            return;

        String message = LocaleLoader.getString(key, (Object[]) values);
        System.out.println(message + "msg");
        McMMOPlayerNotificationEvent customEvent = checkNotificationEvent(player, notificationType, message);

        sendNotification(player, customEvent);
    }

    private static void sendNotification(Player player, McMMOPlayerNotificationEvent customEvent) {
        // Send Message to player
        player.sendMessage(customEvent.getNotificationMessage());
    }

    private static McMMOPlayerNotificationEvent checkNotificationEvent(Player player, NotificationType notificationType, String message) {
        McMMOPlayerNotificationEvent customEvent = new McMMOPlayerNotificationEvent(player, notificationType, message);

        Server.getInstance().getPluginManager().callEvent(customEvent);
        return customEvent;
    }
}
