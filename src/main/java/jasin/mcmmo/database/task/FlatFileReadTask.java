package jasin.mcmmo.database.task;

import jasin.mcmmo.database.FlatFileDatabase;

import java.lang.*;
import java.util.UUID;
import java.io.*;
import org.yaml.snakeyaml.Yaml;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;

public class FlatFileReadTask extends AsyncTask {

    private UUID playerUUID;
    private InputStream file;
    private int type;

    public FlatFileReadTask(Player player, InputStream file, int type) {
        this.playerUUID = player.getUniqueId();
        this.file = file;
        this.type = type;
    }

    @Override
    public void onRun() {
        switch(this.type) {
            case FlatFileDatabase.TYPE_JSON:
                break;
            case FlatFileDatabase.TYPE_YAML:
                Yaml playerProfile = new Yaml();
                this.setResult(playerProfile.load(file));
                break;
        }
    }

    @Override
    public void onCompletion(Server server) {
        System.out.println("Completed");
    }
}
