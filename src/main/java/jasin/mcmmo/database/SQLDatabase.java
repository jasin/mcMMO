package jasin.mcmmo.database;

import java.util.UUID;

import cn.nukkit.Player;

public class SQLDatabase implements Database {
    
    public Integer getState(Player player) {
    
        return 0;
    }

    public void setState(Player player, Integer state) {

    }

    public void setStateByPlayer(UUID uuid, Integer state) {

    }

    public boolean isLoading(Player player) {
        
        return true;
    }

    public void load(Player player) {

    }

    public  void onLoad(Player player, String[] loadedData) {

    }

    public void save() {

    }

    public void saveAll() {

    }

    public void serialize() {

    }

    public void loadFromDatabase(Player player) {

    }

    public void saveToDatabase() {

    }
}
