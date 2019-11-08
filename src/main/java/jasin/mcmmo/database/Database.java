package jasin.mcmmo.database;

import java.lang.*;
import java.io.*;
import java.util.UUID;

import cn.nukkit.Player;

public interface Database {

    public static final int STATE_LOADING = 0;

    Integer getState(Player player);
    void setState(Player player, Integer state);
    void setStateByPlayer(UUID uuid, Integer state);
    boolean isLoading(Player player);
    void load(Player player);
    void onLoad(Player player, String[] loadedData);
    void save();
    void saveAll();
    void serialize();
    void loadFromDatabase(Player player) throws FileNotFoundException;
    void saveToDatabase();
}
