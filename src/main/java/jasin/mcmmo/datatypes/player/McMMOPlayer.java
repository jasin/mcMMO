package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;
import jasin.mcmmo.datatypes.experience.XPGainSource;

import cn.nukkit.Player;

import java.util.UUID;

public class McMMOPlayer {

    private Player player;
    private PlayerProfile profile;
    private String playerName;

    public McMMOPlayer() {}
    public McMMOPlayer(Player player, PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
        this.playerName = player.getName();
        UUID uuid = player.getUniqueId();
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public void beginXpGain(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        
        beginUnsharedXpGain(skill, xp, reason, source);
    }

    public void beginUnsharedXpGain(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        
        applyXpGain(skill, xp, reason, source);
    }

    public void applyXpGain(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        // finish this, not important now
    }

    public void resetAbilityMode() {
        // Finish this function
    }

    public void logout(boolean syncSave) {
        Player thisPlayer = getPlayer();
        cleanup();

        if(syncSave) {
            getProfile().scheduleSyncSave();
        } else {
            getProfile().scheduleAsyncSave();
        }

        UserManager.remove(thisPlayer);

        mcMMO.plugin.getDatabaseManager().cleanupUser(thisPlayer.getUniqueId());
    }

    public void cleanup() {
        resetAbilityMode();
    }

    public int getSkillLevel(PrimarySkillType skill) {
        return profile.getSkillLevel(skill);
    }
}
