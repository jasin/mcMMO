package jasin.mcmmo.database;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;

import java.util.UUID;
import java.util.HashMap;
import java.io.*;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.List;
import java.util.Arrays;

public class FlatFileDatabase implements Database {
    
    private File file;
    private Yaml yaml;
    private String path;
    private PlayerProfile profile;
    private List<PlayerProfile> profileList;
    private PlayerProfileRepresenter representer;

    public FlatFileDatabase(String path) {
        this.path = path + File.separator;
        this.representer = new PlayerProfileRepresenter();
        representer.addClassTag(PlayerProfile.class, Tag.MAP);
        this.yaml = new Yaml(new CustomClassLoaderConstructor(FlatFileDatabase.class.getClassLoader()), representer);
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create) {
        file = new File(this.path + name);
        if(file.exists()) {
            try {
                profile = yaml.loadAs(new FileInputStream(file), PlayerProfile.class);
            System.out.println(profile.getName());
            } catch(Exception e) {
                System.out.println(e);
            }
        } else if(create) {
            try {
                profile = new PlayerProfile();
                profile.setName(name);
                profile.setUUID(uuid.toString());
                profileList = Arrays.asList(profile);
                FileWriter writer = new FileWriter(file, false);
                yaml.dumpAll(profileList.iterator(), writer); 
            } catch(Exception e) { }
        }

        return profile;
    }

    public void savePlayerProfile(PlayerProfile profile) {
        file = new File(this.path + profile.getName());
        try {
            profileList = Arrays.asList(profile);
            FileWriter writer = new FileWriter(file, false);
            yaml.dumpAll(profileList.iterator(), writer);
        } catch(Exception e) { }
    }

    private class PlayerProfileRepresenter extends Representer {

        private List<String> skipList = Arrays.asList("loaded", "player");

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
            if(skipList.contains(property.getName())) {
                return null;
            } else {
                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }
}
