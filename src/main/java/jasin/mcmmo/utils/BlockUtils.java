package jasin.mcmmo.utils;

import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.config.experience.ConfigExperience;

import cn.nukkit.block.Block;

public final class BlockUtils {

    public static boolean affectedBySuperBreaker(Block block) {
        System.out.println("affected block: " + block.getName());
        if(ConfigExperience.getInstance().doesBlockGiveXP(PrimarySkillType.MINING, block)) {
            return true;
        }
        return false;
    }

    public static boolean affectedByGigaDrillBreaker(Block block) {
        if(ConfigExperience.getInstance().doesBlockGiveXP(PrimarySkillType.EXCAVATION, block)) {
            return true;
        }
        return false;
    }
}
