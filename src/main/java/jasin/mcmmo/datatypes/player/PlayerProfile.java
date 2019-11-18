package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.task.player.PlayerProfileSave;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.skills.SuperAbilityType;

import cn.nukkit.Player;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.beans.Transient;

public class PlayerProfile {

    private String name;
    private UUID uuid;

    private Player player;
    private boolean loaded;
    private volatile boolean changed;

    private Map<PrimarySkillType, Integer> skill_levels = new HashMap<PrimarySkillType, Integer>();
    private Map<PrimarySkillType, Integer> experience = new HashMap<PrimarySkillType, Integer>();
    private Map<SuperAbilityType, Integer> cooldowns = new HashMap<SuperAbilityType, Integer>();

    public PlayerProfile(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public PlayerProfile(String name, UUID uuid, boolean loaded) {
        this.name = name;
        this.uuid = uuid;
        this.loaded = loaded;
    }

    public PlayerProfile(String name, UUID uuid, Map<PrimarySkillType, Integer> lvls, Map<PrimarySkillType, Integer> xp, Map<SuperAbilityType, Integer> timers) {
        this.name = name;
        this.uuid = uuid;
        
        skill_levels.putAll(lvls);
        experience.putAll(xp);
        cooldowns.putAll(timers);

        loaded = true;
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
        return skill_levels.get(skill);
    }

    public int getSkillXpLevel(PrimarySkillType skill) {
        return experience.get(skill);
    }

    public int getAbilityCooldown(SuperAbilityType ability) {
        return cooldowns.get(ability);
    }

    public void scheduleSyncSave() {
        new PlayerProfileSave(this, true).runTask(mcMMO.plugin);
    }

    public void scheduleAsyncSave() {
        new PlayerProfileSave(this, false).runTaskAsynchronously(mcMMO.plugin);
    }

    public void save(boolean useSync) {
        PlayerProfile profileCopy = new PlayerProfile(name, uuid, skill_levels, experience, cooldowns);
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
