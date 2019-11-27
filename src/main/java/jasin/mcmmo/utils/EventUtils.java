package jasin.mcmmo.utils;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.player.UserManager;
import jasin.mcmmo.datatypes.player.PlayerProfile;
import jasin.mcmmo.datatypes.skills.PrimarySkillType;
import jasin.mcmmo.datatypes.experience.XPGainReason;
import jasin.mcmmo.events.player.McMMOPlayerProfileLoadEvent;
import jasin.mcmmo.events.experience.McMMOPlayerXpGainEvent;

import cn.nukkit.Player;

public class EventUtils {

    public static McMMOPlayerProfileLoadEvent callPlayerProfileLoadEvent(Player player, PlayerProfile profile) {
        McMMOPlayerProfileLoadEvent event = new McMMOPlayerProfileLoadEvent(player, profile);
        mcMMO.plugin.getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static boolean handleXpGainEvent(Player player, PrimarySkillType skill, float xp, XPGainReason reason) {
        McMMOPlayerXpGainEvent event = new McMMOPlayerXpGainEvent(player, skill, xp, reason);
        mcMMO.plugin.getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if(!isCancelled) {
            UserManager.getPlayer(player).getProfile().addXp(skill, event.getRawXpGained());
            UserManager.getPlayer(player).getProfile().registerXpGain(skill, event.getRawXpGained());
        }

        return !isCancelled;
    }
}
