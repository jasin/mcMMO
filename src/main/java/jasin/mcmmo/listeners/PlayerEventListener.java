package jasin.mcmmo.listeners;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.task.player.PlayerProfileLoading;
import jasin.mcmmo.skills.mining.MiningManager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerBlockPickEvent;

public class PlayerEventListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        new PlayerProfileLoading(player).runTaskAsynchronously(mcMMO.plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        if(mcMMOPlayer == null) {
            return;
        }

        mcMMOPlayer.logout(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteractLowest(PlayerInteractEvent e) {
    
        Player player = e.getPlayer();

        if(!UserManager.hasPlayerDataKey(player) || UserManager.getPlayer(player) == null) {
            return;
        }

        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);
        MiningManager miningManager = mcMMOPlayer.getMiningManager();
        Block block = e.getBlock();
        Item heldItem = e.getItem();

        // TODO: finish
        switch(e.getAction()) {
            case RIGHT_CLICK_BLOCK:
                if(true) {
                    System.out.println(block.toString());    
                }
                return;
            case LEFT_CLICK_BLOCK:
                if(player.isSneaking()) {
                    System.out.println(block.toString());    
                }
                break;
            default:
                break;
        }

        return;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractMonitor(PlayerInteractEvent e) {
        
        Player player = e.getPlayer();

        if(!UserManager.hasPlayerDataKey(player) || UserManager.getPlayer(player) == null) {
            return;
        }

        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);
        Item heldItem = e.getItem();

        switch(e.getAction()) {
            case RIGHT_CLICK_BLOCK:
                if(player.isSneaking()) {
                    mcMMOPlayer.processAbilityActivation(PrimarySkillType.MINING);
                    mcMMOPlayer.processAbilityActivation(PrimarySkillType.EXCAVATION);
                    mcMMOPlayer.processAbilityActivation(PrimarySkillType.WOODCUTTING);
                }
                break;
            default:
                break;
        }
    }
}
