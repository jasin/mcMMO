package jasin.mcmmo.utils.player;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.McMMOPlayer;
import jasin.mcmmo.utils.metadata.FixedMetadataValue;

import cn.nukkit.Player;

import java.util.HashSet;

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
}
