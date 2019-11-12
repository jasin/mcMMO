package jasin.mcmmo.datatypes.player;

import java.util.UUID;
import cn.nukkit.Player;

public class McMMOPlayer {

    private Player player;
    private PlayerProfile profile;
    private String playerName;

    public McMMOPlayer(Player player, PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
        this.playerName = player.getName();
        UUID uuid = player.getUniqueId();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Player getPlayer() {
        return this.player;
    }
}
