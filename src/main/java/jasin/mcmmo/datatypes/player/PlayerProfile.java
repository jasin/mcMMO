package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.task.player.PlayerProfileSave;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

import cn.nukkit.Player;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.beans.Transient;

public class PlayerProfile {

    /* 
     * Properties being dumped by snakeyaml
     */
    private String name;
    private UUID uuid;

    private Player player;
    private boolean loaded;
    private volatile boolean changed;

    private Map<PrimarySkillType, Integer> skills = new HashMap<PrimarySkillType, Integer>();

    public PlayerProfile(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isLoaded() {
        return true;
    }

    public int getSkillLevel(PrimarySkillType skill) {
        return skills.get(skill);
    }

    public void scheduleSyncSave() {
        new PlayerProfileSave(this, true).runTask(mcMMO.plugin);
    }

    public void scheduleAsyncSave() {
        new PlayerProfileSave(this, false).runTaskAsynchronously(mcMMO.plugin);
    }

    public void save(boolean useSync) {
        PlayerProfile profileCopy = new PlayerProfile(name, uuid);
        changed = mcMMO.plugin.getDatabaseManager().savePlayerProfile(profileCopy);

        if(!changed) {
            if(useSync) {
                scheduleSyncSave();
            } else {
                scheduleAsyncSave();
            }
        }
    }
}
