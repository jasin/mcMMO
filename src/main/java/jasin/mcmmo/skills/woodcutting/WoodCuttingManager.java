package jasin.mcmmo.skills.woodcutting;

import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

public class WoodCuttingManager extends SkillManager {
    
    public WoodCuttingManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, PrimarySkillType.WOODCUTTING);
    }
}
