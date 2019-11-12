package jasin.mcmmo.database;

public class DatabaseManager {

    public static Database getDatabaseClass(String type, String path) {
        
        switch(type) {
            case "YAML":
                return new FlatFileDatabase(path);
            case "SQL":
                //return new SQLDatabase();
            default:
                return null;
        }
    }
}
