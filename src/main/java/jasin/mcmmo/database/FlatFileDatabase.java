package jasin.mcmmo.database;

import jasin.mcmmo.mcMMO;
import jasin.mcmmo.datatypes.player.PlayerProfile;

import cn.nukkit.Player;
import cn.nukkit.Server;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.UUID;
import java.util.HashMap;
import java.io.*;
import java.util.List;
import java.util.Arrays;

public class FlatFileDatabase implements Database {
    
    private File file;
    private Yaml yaml;
    private String path;
    private PlayerProfile profile;
    private List<PlayerProfile> profileList;
    private Representer representer;
    private DumperOptions options;

    public FlatFileDatabase(String dir) {
        path = dir + File.separator;
        representer = new Representer();
        options = new DumperOptions();
        representer.addClassTag(PlayerProfile.class, Tag.MAP);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(new CustomClassLoaderConstructor(FlatFileDatabase.class.getClassLoader()), representer, options);
    }

    public PlayerProfile loadPlayerProfile(String name, UUID uuid, boolean create) {
        file = new File(this.path + name);
        if(file.exists()) {
            try {
                profile = yaml.loadAs(new FileInputStream(file), PlayerProfile.class);
            } catch(Exception e) {
                mcMMO.plugin.getLogger().info(e.toString());
            }
        } else if(create) {
            try {
                profile = new PlayerProfile();
                profile.setName(name);
                profile.setUUID(uuid.toString());
                //profileList = Arrays.asList(profile);
                FileWriter writer = new FileWriter(file, false);
                yaml.dump(profile, writer); 
            } catch(Exception e) { }
        }

        return profile;
    }

    public boolean savePlayerProfile(PlayerProfile profile) {
        file = new File(this.path + profile.getName());
        try {
            //profileList = Arrays.asList(profile);
            FileWriter writer = new FileWriter(file, false);
            yaml.dump(profile, writer);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public void cleanupUser(UUID uuid) {
        // Not used here
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
