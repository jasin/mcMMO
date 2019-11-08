package jasin.mcmmo.utils;

import jasin.mcmmo.mcMMO;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;

public class Message {
    
    private String cc(String msg) {
        return TextFormat.colorize('&', msg);
    }

    public void playerSend(Player player, String msg) {
        player.sendMessage(cc(msg));
    }

    public void consoleSend(String msg) {
        mcMMO.getInstance().getServer().getConsoleSender().sendMessage(cc(msg));
    }
}
