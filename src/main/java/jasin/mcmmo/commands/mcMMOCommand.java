package jasin.mcmmo.commands;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.utils.Lang;
import jasin.mcmmo.utils.Message;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;

public abstract class mcMMOCommand extends Command {
    
    private Message pm;
    private boolean consoleCanUse;
    private String permission;

    mcMMOCommand(String name, String permission, boolean consoleCanUse) {
        super(name);
        this.setPermission(permission);
        this.permission = permission;
        this.consoleCanUse = consoleCanUse;
    }

    protected abstract void run(CommandSender sender, String[] args);

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!consoleCanUse) {
            if(sender instanceof ConsoleCommandSender) {
                pm.consoleSend(Lang.notaplayer());
                return false;
            }
        }
        if(sender instanceof Player) {
            if(!sender.hasPermission(permission)) {
                pm.playerSend((Player)sender, Lang.permission());
                return false;
            }
        }
        run(sender, args);
        return true;
    }

    public static void registerCommands() {
        final CommandMap map = (CommandMap)mcMMO.getInstance().getServer().getCommandMap();
    }
}
