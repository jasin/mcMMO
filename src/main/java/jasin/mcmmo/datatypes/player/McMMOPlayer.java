package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.Misc;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.utils.player.NotificationManager;
import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.skills.mining.MiningManager;
import jasin.mcmmo.datatypes.skills.ToolType;
import jasin.mcmmo.datatypes.skills.SuperAbilityType;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;
import jasin.mcmmo.datatypes.experience.XPGainSource;
import jasin.mcmmo.datatypes.interactions.NotificationType;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class McMMOPlayer {

    private Player player;
    private PlayerProfile profile;
    private String playerName;
    
    private boolean abilityUse = true; 

    private final Map<PrimarySkillType, SkillManager> skillManagers = new HashMap<PrimarySkillType, SkillManager>();
    private final Map<SuperAbilityType, Boolean> abilityMode = new HashMap<SuperAbilityType, Boolean>();
    private final Map<ToolType, Boolean> toolMode = new HashMap<ToolType, Boolean>();

    public McMMOPlayer(Player player, PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
        this.playerName = player.getName();
        UUID uuid = player.getUniqueId();

        try {
            for(PrimarySkillType primarySkillType : PrimarySkillType.values()) {
                skillManagers.put(primarySkillType, primarySkillType.getManagerClass().getConstructor(McMMOPlayer.class).newInstance(this));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(SuperAbilityType superAbilityType : SuperAbilityType.values()) {
            abilityMode.put(superAbilityType, false);
        }

        for(ToolType toolType : ToolType.values()) {
            toolMode.put(toolType, false);
        }
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

    public boolean getAbilityUse() {
        return abilityUse;
    }

    public boolean getAbilityMode(SuperAbilityType ability) {
        return abilityMode.get(ability);
    }

    public void setToolPreparationMode(ToolType tool, boolean isPrepared) {
        toolMode.put(tool, isPrepared);
    }

    // TODO make cooldown time dynamic
    public int calculateTimeRemaining(SuperAbilityType ability) {
        int timestamp = profile.getAbilityCooldown(ability);
        return (int) (((timestamp + 240) * Misc.TIME_CONVERSION_FACTOR) - System.currentTimeMillis()) / Misc.TIME_CONVERSION_FACTOR;
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

    public MiningManager getMiningManager() {
        return (MiningManager) skillManagers.get(PrimarySkillType.MINING);
    }

    public void processAbilityActivation(PrimarySkillType skill) {
        Item inHand = player.getInventory().getItemInHand();
        
        if(!getAbilityUse()) {
            return;
        }

        for(SuperAbilityType superAbilityType : SuperAbilityType.values()) {
            if(getAbilityMode(superAbilityType)) {
                return;
            }
        }

        SuperAbilityType ability = skill.getAbility();
        ToolType tool = skill.getTool();

        // TODO finish this as needed
        if(tool.inHand(inHand)) {
            int timeRemaining = calculateTimeRemaining(ability);

            //if(!getAbilityMode(ability) && timeRemaining > 0) {
            if(true) {
                System.out.println("NotificationManager called");
                NotificationManager.sendPlayerInformation(player, NotificationType.ABILITY_COOLDOWN, "Skills.TooTired", String.valueOf(timeRemaining));
                return;
            }

            setToolPreparationMode(tool, true);
        }
    }
}
