package jasin.mcmmo.events.experience;

import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public abstract class McMMOPlayerExperienceEvent extends PlayerEvent implements Cancellable {
    
    private boolean cancelled;
    protected PrimarySkillType skill;
    protected int level;
    protected XPGainReason reason;
    private static final HandlerList handlers = new HandlerList();

    protected McMMOPlayerExperienceEvent(Player player, PrimarySkillType skill, XPGainReason reason) {
        this.skill = skill;
        this.level = UserManager.getPlayer(player).getSkillLevel(skill);
        this.reason = reason;
    }

    public PrimarySkillType getSkill() {
        return skill;
    }

    public int getSkillLevel() {
        return level;
    }

    public XPGainReason getXpGainReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    private static HandlerList getHandlers() {
        return handlers;
    }
}
