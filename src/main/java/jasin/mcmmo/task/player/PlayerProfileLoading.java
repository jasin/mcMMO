package jasin.mcmmo.task.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.EventUtils;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.datatypes.player.McMMOPlayer;

import cn.nukkit.Player;
import cn.nukkit.scheduler.NukkitRunnable;

public class PlayerProfileLoading extends NukkitRunnable {

    private final Player player;

    public PlayerProfileLoading(Player player) {
        this.player = player;
    }

    @Override
    public void run() {

        // Quit if they logged out
        if(!player.isOnline()) {
            mcMMO.plugin.getLogger().info("Aborting loading of " + player.getName() + " profile.");
            return;
        }

        PlayerProfile profile = mcMMO.plugin.getDatabaseManager().loadPlayerProfile(player.getName(), player.getUniqueId(), true);

        if(profile.isLoaded()) {
            new ApplySuccessfulProfile(new McMMOPlayer(player, profile)).runTask(mcMMO.plugin);
            EventUtils.callPlayerProfileLoadEvent(player, profile);
        }
    }

    private class ApplySuccessfulProfile extends NukkitRunnable {
        
        private final McMMOPlayer mcMMOPlayer;

        private ApplySuccessfulProfile(McMMOPlayer mcMMOPlayer) {
            this.mcMMOPlayer = mcMMOPlayer;
        }

        @Override
        public void run() {
            if(!player.isOnline()) {
                mcMMO.plugin.getLogger().info("Aborting loading of " + player.getName() + " profile.");
            }

            UserManager.track(mcMMOPlayer);
        }
    }
}
