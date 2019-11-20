package jasin.mcmmo.events.skills;

import jasin.mcmmo.datatypes.interactions.NotificationType;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class McMMOPlayerNotificationEvent extends Event implements Cancellable {

    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

    protected String message;
    protected final NotificationType notificationType;

    public McMMOPlayerNotificationEvent(Player player, NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
        isCancelled = false;
    }

    public String getNotificationMessage() {
        return message;
    }

    public void setNotificationMessage(String message) {
        this.message = message;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean bValue) {
        isCancelled = bValue;
    }
}
