package jasin.mcmmo.database;

import jasin.mcmmo.datatypes.player.PlayerProfile;

import java.lang.*;
import java.io.*;
import java.util.UUID;

import cn.nukkit.Player;

public interface Database {

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create);
    public void savePlayerProfile(PlayerProfile profile);
}
