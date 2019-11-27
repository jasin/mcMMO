package jasin.mcmmo.config.experience;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;

import cn.nukkit.block.Block;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.util.Set;

public class ConfigExperience {
   
    private static String xp = "Experience_XP";
    private static String formula = "Experience_Formula";
    private static String curve;
    private static ConfigExperience instance;
    private static Config config;

    private ConfigExperience() {
        config = mcMMO.plugin.getConfigManager().getExperienceCfg();
        curve = config.getSection(formula).getString("Curve");
    }

    public static ConfigExperience getInstance() {
        if(instance == null)
            instance = new ConfigExperience();

        return instance;
    }

    public int getBase() {
        return config.getSection(formula).getSection(curve).getInt("base");
    }

    public double getMultiplier() {
        return config.getSection(xp).getSection(curve).getInt("multiplier");

    }

    public int getXp(PrimarySkillType skill, String blockName) {
        return config.getSection(xp).getSection(formatString(skill.getName())).getInt(blockName);
    }

    public boolean doesBlockGiveXP(PrimarySkillType skill, Block block) {
        System.out.println("Block: " + block.getName());
        String skillName = formatString(skill.getName());
        String blockName = block.getName();
        return config.getSection(xp).getSection(skillName).exists(blockName, true);
    }

    private String formatString(String str) {
        return str.substring(0,1) + str.substring(1).toLowerCase();
    }
}
