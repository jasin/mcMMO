package jasin.mcmmo.datatypes.skills;

import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.skills.mining.MiningManager;
import jasin.mcmmo.skills.excavation.ExcavationManager;
import jasin.mcmmo.skills.woodcutting.WoodCuttingManager;

public enum PrimarySkillType {
    
    EXCAVATION(ExcavationManager.class, SuperAbilityType.GIGA_DRILL_BREAKER, ToolType.SHOVEL),
    MINING(MiningManager.class, SuperAbilityType.SUPER_BREAKER, ToolType.PICKAXE),
    WOODCUTTING(WoodCuttingManager.class, SuperAbilityType.TREE_FELLER, ToolType.AXE);

    private Class<? extends SkillManager> managerClass;
    private SuperAbilityType ability;
    private ToolType tool;

    private PrimarySkillType(Class<? extends SkillManager> managerClass, SuperAbilityType ability, ToolType tool) {
        this.managerClass = managerClass;
        this.ability = ability;
        this.tool = tool;
    }
}
