package jasin.mcmmo.skills.mining;

import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

public class MiningManager extends SkillManager {
    
    public MiningManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, PrimarySkillType.MINING);
    }
}
