package jasin.mcmmo.database;

import jasin.mcmmo.database.task.FlatFileReadTask;

import java.util.UUID;
import java.util.HashMap;
import java.io.*;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;

public class FlatFileDatabase implements Database {
    
    public static final int TYPE_YAML = 0;
    public static final int TYPE_JSON = 1;

    private String flatFileType;
    private String path;
    private HashMap<UUID, Integer> state = new HashMap<>();

    public FlatFileDatabase(String type, String path) {
        this.flatFileType = type;
        this.path = path;
        System.out.println(path);
    }

    public Integer getState(Player player) {
        return this.state.get(player.getUniqueId());
    }

    public void setState(Player player, Integer state) {
        this.setStateByPlayer(player.getUniqueId(), state);
    }

    public void setStateByPlayer(UUID uuid, Integer state) {
        this.state.put(uuid, state);
    }

    public boolean isLoading(Player player) {
        return this.getState(player) == STATE_LOADING;
    }

    public void load(Player player) {
        this.setState(player, STATE_LOADING);
        try {
            this.loadFromDatabase(player);
        } catch(FileNotFoundException e) { System.out.println(e); }
    }

    public  void onLoad(Player player, String[] loadedData) {
        this.setState(player, null);


    }

    public void save() {
         
    }

    public void saveAll() {

    }

    public void serialize() {

    }

    public void loadFromDatabase(Player player) throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(this.path));
        AsyncTask task = new FlatFileReadTask(player, in, 0);
        Server.getInstance().getScheduler().scheduleAsyncTask(Server.getInstance().getPluginManager().getPlugin("mcMMO"), task);
    }

    public void saveToDatabase() {

    }
}
