package jasin.mcmmo.skills.mining;

import jasin.mcmmo.config.experience.ConfigExperience;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

import cn.nukkit.block.Block;

public class Mining {
    
    public static int getBlockXp(Block block) {
        int xp = ConfigExperience.getInstance().getXp(PrimarySkillType.MINING, block.getName());
        return xp;
    }
}
