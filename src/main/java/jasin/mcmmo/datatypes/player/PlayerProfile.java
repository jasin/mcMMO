package jasin.mcmmo.datatypes.player;

import cn.nukkit.Player;

public class PlayerProfile {

    public String name;
    public String UUID;
    public boolean loaded;
    private Player player;

    public PlayerProfile() {}
    public PlayerProfile(Player player) {
        this.player = player;
    }

    public PlayerProfile(String name, String uuid) {
        this.name = name;
        this.UUID = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String uuid) {
        this.UUID = uuid;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isLoaded() {
        return true;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
