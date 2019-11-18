package jasin.mcmmo.database;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;

import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.UUID;
import java.util.HashMap;
import java.io.*;
import java.util.List;
import java.util.Arrays;

public class FlatFileDatabase implements Database {
    
    private File file;
    private String path;
    private PlayerProfile profile;
    private List<PlayerProfile> profileList;

    public FlatFileDatabase(String dir) {
        path = dir + File.separator;
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create) {
        return new PlayerProfile(name, uuid, false);
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create, boolean retry) {
        return new PlayerProfile(name, uuid, false);
    }

    public boolean savePlayerProfile(PlayerProfile profile) {
        return false;
    }

    @Override
    public void cleanupUser(UUID uuid) {
        // Not used here
    }
}
