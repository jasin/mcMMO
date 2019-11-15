package jasin.mcmmo.listeners;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.task.player.PlayerProfileLoading;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

public class PlayerEventListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        new PlayerProfileLoading(player).runTaskAsynchronously(mcMMO.plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        if(mcMMOPlayer == null) {
            return;
        }

        mcMMOPlayer.logout(true);
    }

    public void onPlayerInteract(PlayerInteractEvent e) {
        
    }
}
