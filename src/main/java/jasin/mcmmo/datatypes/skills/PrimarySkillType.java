package jasin.mcmmo.datatypes.skills;

import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.skills.mining.MiningManager;
import jasin.mcmmo.skills.excavation.ExcavationManager;
import jasin.mcmmo.skills.woodcutting.WoodcuttingManager;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.skills.SuperAbilityType;
import jasin.mcmmo.datatypes.skills.SubSkillType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.google.common.collect.ImmutableList;

public enum PrimarySkillType {
    EXCAVATION(ExcavationManager.class, SuperAbilityType.GIGA_DRILL_BREAKER, ToolType.SHOVEL,
            ImmutableList.of(SubSkillType.EXCAVATION_GIGA_DRILL_BREAKER, SubSkillType.EXCAVATION_ARCHAEOLOGY)),
    MINING(MiningManager.class, SuperAbilityType.SUPER_BREAKER, ToolType.PICKAXE,
            ImmutableList.of(SubSkillType.MINING_SUPER_BREAKER, SubSkillType.MINING_BLAST_MINING)),
    WOODCUTTING(WoodcuttingManager.class, SuperAbilityType.TREE_FELLER, ToolType.AXE,
            ImmutableList.of(SubSkillType.WOODCUTTING_TREE_FELLER, SubSkillType.WOODCUTTING_LEAF_BLOWER));

    private Class<? extends SkillManager> managerClass;
    private SuperAbilityType superAbilityType;
    private ToolType toolType;
    private List<SubSkillType> subSkillTypes;

    private static final List<String> SKILL_NAMES;
    private static final List<String> SUBSKILL_NAMES;

    private static final List<PrimarySkillType> CHILD_SKILLS;
    private static final List<PrimarySkillType> NON_CHILD_SKILLS;

    static {
        List<PrimarySkillType> childSkills = new ArrayList<PrimarySkillType>();
        List<PrimarySkillType> nonChildSkills = new ArrayList<PrimarySkillType>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> subSkillNames = new ArrayList<String>();
        
        for(PrimarySkillType primarySkillType : values()) {
            if(primarySkillType.isChildSkill()) {
                childSkills.add(primarySkillType);
            } else {
                nonChildSkills.add(primarySkillType);
            }

            for(SubSkillType subSkillType : primarySkillType.subSkillTypes) {
                subSkillNames.add(subSkillType.getNiceNameNoSpace(subSkillType));
            }

            names.add(primarySkillType.getName());
        }

        Collections.sort(names);
        SKILL_NAMES = ImmutableList.copyOf(names);
        SUBSKILL_NAMES = ImmutableList.copyOf(subSkillNames);
        CHILD_SKILLS = ImmutableList.copyOf(childSkills);
        NON_CHILD_SKILLS = ImmutableList.copyOf(nonChildSkills);
    }

    private PrimarySkillType(Class<? extends SkillManager> managerClass, SuperAbilityType superAbilityType, ToolType toolType, List<SubSkillType> subSkillTypes) {
        this.managerClass = managerClass;
        this.superAbilityType = superAbilityType;
        this.toolType = toolType;
        this.subSkillTypes = subSkillTypes;
    }

    public String getName() {
        return this.toString();
    }

    public SuperAbilityType getSuperAbility() {
        return superAbilityType;
    }

    public ToolType getPrimarySkillToolType() {
        return toolType;
    }

    public List<SubSkillType> getSkillAbilities() {
        return subSkillTypes;
    }

    public boolean isChildSkill() {
        switch(this) {
            default:
                return false;
        }
    }

    public static PrimarySkillType bySecondaryAbility(SubSkillType subSkillType) {
        for(PrimarySkillType skill : values()) {
            if(skill.getSkillAbilities().contains(subSkillType)) {
                return skill;
            }
        }

        return null;
    }
} 
