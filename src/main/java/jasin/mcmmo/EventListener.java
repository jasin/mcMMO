package jasin.mcmmo;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

class EventListener implements Listener {

    private mcMMO plugin;

    public EventListener(mcMMO plugin) {
        this.plugin = plugin;
    }

    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        this.plugin.getDatabase().load(player);
    }

    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
    }

    public void onPlayerInteract(PlayerInteractEvent e) {
        
    }
}
