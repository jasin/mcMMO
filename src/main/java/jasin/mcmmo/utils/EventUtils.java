package jasin.mcmmo.utils;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.events.player.McMMOPlayerProfileLoadEvent;

import cn.nukkit.Player;

public class EventUtils {

    public static McMMOPlayerProfileLoadEvent callPlayerProfileLoadEvent(Player player, PlayerProfile profile) {
        McMMOPlayerProfileLoadEvent event = new McMMOPlayerProfileLoadEvent(player, profile);
        mcMMO.plugin.getServer().getPluginManager().callEvent(event);

        return event;
    }
}
