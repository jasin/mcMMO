package jasin.mcmmo.datatypes.skills;

import cn.nukkit.item.Item;

public enum ToolType {
    AXE("Axes.Ability.Lower", "Axes.Ability.Ready"),
    PICKAXE("Mining.Ability.Lower", "Mining.Ability.Ready"),
    SHOVEL("Excavation.Ability.Lower", "Excavation.Ability.Ready");

    private String lowerTool;
    private String raiseTool;
    
    private ToolType(String lowerTool, String raiseTool) {
        this.lowerTool = lowerTool;
        this.raiseTool = raiseTool;
    }

    public String getLowerTool() {
        return lowerTool;
    }

    public String getRaiseTool() {
        return raiseTool;
    }

    public boolean inHand(Item itemTool) {
        switch(this) {
            case AXE:
                return itemTool.isAxe();
            case PICKAXE:
                return itemTool.isPickaxe();
            case SHOVEL:
                return itemTool.isShovel();
            default:
                return false;
        }
    }
}
