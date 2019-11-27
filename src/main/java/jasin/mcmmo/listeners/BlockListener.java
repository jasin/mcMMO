package jasin.mcmmo.listeners;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.BlockUtils;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.skills.mining.MiningManager;
import jasin.mcmmo.skills.excavation.ExcavationManager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

    private final mcMMO plugin;

    public BlockListener(final mcMMO plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakEvent(BlockBreakEvent e) {
    
        Player player = e.getPlayer();
        
        if(!UserManager.hasPlayerDataKey(player)) {
            return;
        }
        
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        if(mcMMOPlayer == null) {
            return;
        }
        
        Block block = e.getBlock();
        Item heldItem = player.getInventory().getItemInHand();

        /**
         * Call appropriate SkillManager to process block
         */

        /* Mining */
        if(BlockUtils.affectedBySuperBreaker(block) && heldItem.isPickaxe()) {
            MiningManager miningMgr = mcMMOPlayer.getMiningManager();
            miningMgr.processMiningBlock(block);
        }
        
        /* Excavation */
        else if(BlockUtils.affectedByGigaDrillBreaker(block) && heldItem.isShovel()) {
            ExcavationManager excavationMgr = mcMMOPlayer.getExcavationManager();
            //excavationMgr.processExcavationBlock(block);
        }
        
        /* Woodcutting */
        else if(false) {

        }
    }
}
