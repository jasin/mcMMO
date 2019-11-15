package jasin.mcmmo.task.player;

import jasin.mcmmo.datatypes.player.PlayerProfile;

import cn.nukkit.scheduler.NukkitRunnable;

public class PlayerProfileSave extends NukkitRunnable {
    
    private PlayerProfile profile;
    private boolean syncSave;

    public PlayerProfileSave(PlayerProfile profile, boolean syncSave) {
        this.profile = profile;
        this.syncSave = syncSave;
    }

    @Override
    public void run() {
        profile.save(syncSave);
    }
}
