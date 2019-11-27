package jasin.mcmmo.skills.woodcutting;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

public class WoodcuttingManager extends SkillManager {
    
    public WoodcuttingManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, PrimarySkillType.WOODCUTTING);
    }
}
