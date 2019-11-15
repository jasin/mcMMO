package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.task.player.PlayerProfileSave;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

import cn.nukkit.Player;

import java.util.Map;
import java.util.HashMap;
import java.beans.Transient;

public class PlayerProfile {

    /* 
     * Properties being dumped by snakeyaml
     */
    private String name;
    private String UUID;

    private Player player;
    private boolean loaded;
    private volatile boolean changed;

    private final Map<PrimarySkillType, Integer> skills = new HashMap<PrimarySkillType, Integer>();

    public PlayerProfile() {}
    public PlayerProfile(Player player) {
        this.player = player;
    }

    public PlayerProfile(String name, String uuid) {
        this.name = name;
        this.UUID = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String uuid) {
        this.UUID = uuid;
    }

    @Transient
    public Player getPlayer() {
        return this.player;
    }

    @Transient
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
        PlayerProfile profileCopy = new PlayerProfile(name, UUID);
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
