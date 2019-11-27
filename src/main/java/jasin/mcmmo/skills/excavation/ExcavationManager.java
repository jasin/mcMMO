package jasin.mcmmo.skills.excavation;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

public class ExcavationManager extends SkillManager {
    
    public ExcavationManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, PrimarySkillType.EXCAVATION);
    }
}
