package jasin.mcmmo.events.player;

import jasin.mcmmo.datatypes.player.PlayerProfile;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class McMMOPlayerProfileLoadEvent extends Event implements Cancellable {

    private boolean cancelled;
    private PlayerProfile profile;
    private Player player;
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public McMMOPlayerProfileLoadEvent(Player player, PlayerProfile profile) {
        this.cancelled = false;
        this.profile = profile;
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PlayerProfile getProfile() {
        return this.profile;
    }
}
