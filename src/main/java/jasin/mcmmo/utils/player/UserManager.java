package jasin.mcmmo.utils.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.utils.metadata.FixedMetadataValue;

import cn.nukkit.Player;

import java.util.HashSet;
import com.google.common.collect.ImmutableList;

public class UserManager {

    private static HashSet<McMMOPlayer> playerDataSet;

    public static void track(McMMOPlayer mcMMOPlayer) {
        mcMMOPlayer.getPlayer().setMetadata(mcMMO.PLAYER_DATA_KEY, new FixedMetadataValue(mcMMO.plugin, mcMMOPlayer));
        
        if(playerDataSet == null) {
            playerDataSet = new HashSet<>();
        }

        playerDataSet.add(mcMMOPlayer);
    }

    public static McMMOPlayer getPlayer(Player player) {
        if(player != null && player.hasMetadata(mcMMO.PLAYER_DATA_KEY)) {
            return (McMMOPlayer) player.getMetadata(mcMMO.PLAYER_DATA_KEY).get(0).value();
        }

        return null;
    }

    public static void remove(Player player) {
        McMMOPlayer mcMMOPlayer = getPlayer(player);
        mcMMOPlayer.cleanup();
        player.removeMetadata(mcMMO.PLAYER_DATA_KEY, mcMMO.plugin);

        if(playerDataSet != null && playerDataSet.contains(mcMMOPlayer)) {
            playerDataSet.remove(mcMMOPlayer);
        }
    }

    public static boolean hasPlayerDataKey(Player player) {
        return player != null && player.hasMetadata(mcMMO.PLAYER_DATA_KEY);
    }

    public static void saveAll() {
        if(playerDataSet == null) {
            return;
        }

        ImmutableList<McMMOPlayer> playerSyncData = ImmutableList.copyOf(playerDataSet);

        for(McMMOPlayer playerData : playerSyncData) {
            try {
                mcMMO.plugin.getLogger().info("Saving player: " + playerData.getPlayerName());
                playerData.getProfile().save(true);
            } catch(Exception e) {
                mcMMO.plugin.getLogger().info("Problem saving: " + playerData.getPlayerName());
            }
        }
                
        mcMMO.plugin.getLogger().info("Finished saving player data.");
    }

    public static void clearAll() {
        for(Player player : mcMMO.plugin.getServer().getOnlinePlayers().values()) {
            remove(player);
        }

        if(playerDataSet != null) {
            playerDataSet.clear();
        }
    }
}
