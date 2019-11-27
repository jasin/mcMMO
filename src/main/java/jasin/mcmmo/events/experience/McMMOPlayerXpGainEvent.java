package jasin.mcmmo.events.experience;

import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

public class McMMOPlayerXpGainEvent extends McMMOPlayerExperienceEvent {

    private float xp;

    public McMMOPlayerXpGainEvent(Player player, PrimarySkillType skill, float xp, XPGainReason reason) {
        super(player, skill, reason);
        this.xp = xp;
    }

    public float getRawXpGained() {
        return xp;
    }

    public void setRawXpGained(float xp) {
        this.xp = xp;
    }
}
