package jasin.mcmmo.datatypes.skills;

public enum SuperAbilityType {

    SUPER_BREAKER,
    GIGA_DRILL_BREAKER,
	TREE_FELLER,
    BLAST_MINING;

    static {
        SUPER_BREAKER.subSkillTypeDefinition = SubSkillType.MINING_SUPER_BREAKER;
        GIGA_DRILL_BREAKER.subSkillTypeDefinition = SubSkillType.EXCAVATION_GIGA_DRILL_BREAKER;
        TREE_FELLER.subSkillTypeDefinition = SubSkillType.WOODCUTTING_TREE_FELLER;
        BLAST_MINING.subSkillTypeDefinition = SubSkillType.MINING_BLAST_MINING;
    }

    private SubSkillType subSkillTypeDefinition;

    public SubSkillType getSubSkillTypeDefinition() {
        return subSkillTypeDefinition;
    }
}
