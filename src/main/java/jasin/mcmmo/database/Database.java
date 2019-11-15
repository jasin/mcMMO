package jasin.mcmmo.database;

import jasin.mcmmo.datatypes.player.PlayerProfile;

import java.lang.*;
import java.io.*;
import java.util.UUID;

import cn.nukkit.Player;

public interface Database {

    public void cleanupUser(UUID uuid);
    public boolean savePlayerProfile(PlayerProfile profile);
    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create);
}
