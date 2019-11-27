package jasin.mcmmo.datatypes.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.Misc;
import jasin.mcmmo.utils.EventUtils;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.utils.player.NotificationManager;
import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.skills.mining.MiningManager;
import jasin.mcmmo.skills.excavation.ExcavationManager;
import jasin.mcmmo.skills.woodcutting.WoodcuttingManager;
import jasin.mcmmo.datatypes.skills.ToolType;
import jasin.mcmmo.datatypes.skills.SuperAbilityType;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;
import jasin.mcmmo.datatypes.experience.XPGainSource;
import jasin.mcmmo.datatypes.interactions.NotificationType;
import jasin.mcmmo.api.exceptions.InvalidSkillException;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class McMMOPlayer {

    //private mcMMO plugin;
    private Player player;
    private PlayerProfile profile;
    private String playerName;
    
    private boolean abilityUse = true; 

    private final Map<PrimarySkillType, SkillManager> skillManagerMap = new HashMap<PrimarySkillType, SkillManager>();
    private final Map<SuperAbilityType, Boolean> superAbilityModeMap = new HashMap<SuperAbilityType, Boolean>();
    private final Map<SuperAbilityType, Boolean> superAbilityInformedMap = new HashMap<SuperAbilityType, Boolean>();
    private final Map<ToolType, Boolean> toolModeMap = new HashMap<ToolType, Boolean>();

    public McMMOPlayer(Player player, PlayerProfile profile) {
        //this.plugin = plugin;
        this.player = player;
        this.profile = profile;
        this.playerName = player.getName();
        UUID uuid = player.getUniqueId();

        initSkillManagers();

        for(SuperAbilityType superAbilityType : SuperAbilityType.values()) {
            superAbilityModeMap.put(superAbilityType, false);
            superAbilityInformedMap.put(superAbilityType, true);
        }

        for(ToolType toolType : ToolType.values()) {
            toolModeMap.put(toolType, false);
        }
    }

    private void initSkillManagers() {
        for(PrimarySkillType primarySkillType : PrimarySkillType.values()) {
            try {
                initManager(primarySkillType);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initManager(PrimarySkillType primarySkillType) throws InvalidSkillException {
        switch(primarySkillType) {
            case EXCAVATION:
                skillManagerMap.put(primarySkillType, new ExcavationManager(this));
                break;
            case MINING:
                skillManagerMap.put(primarySkillType, new MiningManager(this));
                break;
            case WOODCUTTING:
                skillManagerMap.put(primarySkillType, new WoodcuttingManager(this));
                break;
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
        
        applyXpGain(skill, xp, reason, source);
    }

    // Unused presently
    public void beginUnsharedXpGain(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        
        applyXpGain(skill, xp, reason, source);
    }
    
    /*
     * TODO finish this
     * Checks for childskill and adjust xp before calling applyXpGain();
     */
    public void isChildSkill(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        applyXpGain(skill, xp, reason, source);
    }

    public void applyXpGain(PrimarySkillType skill, float xp, XPGainReason reason, XPGainSource source) {
        if(!EventUtils.handleXpGainEvent(player, skill, xp, reason)) {
            return;
        }

        checkLevelGain(skill, reason, source);
    }

    public void checkLevelGain(PrimarySkillType skill, XPGainReason reason, XPGainSource source ) {

    }

    public void resetAbilityMode() {
        // Finish this function
    }

    public boolean getAllowAbilityUse() {
        return abilityUse;
    }

    public boolean getSuperAbilityMode(SuperAbilityType ability) {
        return superAbilityModeMap.get(ability);
    }

    public boolean getToolPreparationMode(ToolType toolType) {
        return toolModeMap.get(toolType);
    }

    public void setToolPreparationMode(ToolType tool, boolean isPrepared) {
        toolModeMap.put(tool, isPrepared);
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
        return (MiningManager) skillManagerMap.get(PrimarySkillType.MINING);
    }

    public ExcavationManager getExcavationManager() {
        return (ExcavationManager) skillManagerMap.get(PrimarySkillType.EXCAVATION);
    }

    public WoodcuttingManager getWoodCuttingManager() {
        return (WoodcuttingManager) skillManagerMap.get(PrimarySkillType.WOODCUTTING);
    }

    public void processAbilityActivation(PrimarySkillType skill) {
        Item inHand = player.getInventory().getItemInHand();
        
        if(!getAllowAbilityUse()) {
            return;
        }

        for(SuperAbilityType superAbilityType : SuperAbilityType.values()) {
            if(getSuperAbilityMode(superAbilityType)) {
                return;
            }
        }

        SuperAbilityType ability = skill.getSuperAbility();
        ToolType tool = skill.getPrimarySkillToolType();

        // TODO finish this as needed
        if(tool.inHand(inHand) && !getToolPreparationMode(tool)) {
            int timeRemaining = calculateTimeRemaining(ability);

            if(!getSuperAbilityMode(ability) && timeRemaining > 0) {
                mcMMO.plugin.getNotificationManager().sendPlayerInformation(player, NotificationType.ABILITY_COOLDOWN, "Skills.TooTired", String.valueOf(timeRemaining));
                return;
            }

            setToolPreparationMode(tool, true);
        }
    }
}
