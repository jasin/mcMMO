package jasin.mcmmo.skills;

import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;
import jasin.mcmmo.datatypes.experience.XPGainSource;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;

public abstract class SkillManager {

    protected McMMOPlayer mcMMOPlayer;
    protected PrimarySkillType skill;

    public SkillManager(McMMOPlayer mcMMOPlayer, PrimarySkillType skill) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.skill = skill;
    }

    public Player getPlayer() {
        return mcMMOPlayer.getPlayer();
    }

    public int getSkillLevel() {
        return mcMMOPlayer.getSkillLevel(skill);
    }

    public void applyXpGain(float xp, XPGainReason xpGainReason, XPGainSource xpGainSource) {
        mcMMOPlayer.beginXpGain(skill, xp, xpGainReason, xpGainSource);
    }

    public XPGainReason getXPGainReason(EntityLiving target, Entity damager) {
        return (damager instanceof Player && target instanceof Player) ? XPGainReason.PVP : XPGainReason.PVE;
    }
}
