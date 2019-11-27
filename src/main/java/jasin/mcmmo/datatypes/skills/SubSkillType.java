package jasin.mcmmo.datatypes.skills;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.StringUtils;

public enum SubSkillType {

    /* Mining */
    MINING_SUPER_BREAKER(1),
    MINING_BLAST_MINING(8),

    /* Excavation */
    EXCAVATION_GIGA_DRILL_BREAKER(1),
    EXCAVATION_ARCHAEOLOGY(8),

    /* Woodcutting */
    WOODCUTTING_LEAF_BLOWER(1),
    WOODCUTTING_TREE_FELLER(1);

    private final int numRanks;

    SubSkillType(int ranks) {
        numRanks = ranks;
    }

    SubSkillType() {
        numRanks = 0;
    }

    public PrimarySkillType getParentSkill(mcMMO plugin) {
        return PrimarySkillType.bySecondaryAbility(this);
    }

    public String getNiceNameNoSpace(SubSkillType subSkillType) {
        return getConfigName(subSkillType.toString());
    }

    private String getConfigName(String subSkillName) {
        String endResult = "";
        int subStringIndex = getSubStringIndex(subSkillName);

        /* Split the string */
        String subSkillNameWithoutPrefix = subSkillName.substring(subStringIndex);
        if(subSkillNameWithoutPrefix.contains("_")) {
            String splitString[] = subSkillNameWithoutPrefix.split("_");

            for(String str : splitString) {
                endResult += StringUtils.getCapitalized(str);
            }
        } else {
            endResult += StringUtils.getCapitalized(subSkillNameWithoutPrefix);
        }

        return endResult;
    }

    private int getSubStringIndex(String subSkillName) {
        char[] charArray = subSkillName.toCharArray();
        int subStringIndex = 0;

        for(int i = 0; i < charArray.length; i++) {
            if(charArray[i] == '_') {
                subStringIndex = i + 1;
                break;
            }
        }

        return subStringIndex;
    }
}
