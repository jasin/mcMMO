package jasin.mcmmo.skills.mining;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.skills.SkillManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;

import cn.nukkit.Player;
import cn.nukkit.block.Block;

public class MiningManager extends SkillManager {
    
    public MiningManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, PrimarySkillType.MINING);
    }

    public void processMiningBlock(Block block) {
        Player player = getPlayer();

        applyXpGain(Mining.getBlockXp(block), XPGainReason.PVE);
    }
}
