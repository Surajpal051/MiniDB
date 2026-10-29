import java.util.ArrayList;
class DatabaseManager {
    private final ArrayList<Database> databases;
    private Database currentDatabase;

    public DatabaseManager() {
        databases = new ArrayList<>();
        currentDatabase = null;
    }
    public void addDatabase(Database database){
        if (database == null){
            throw new IllegalArgumentException(
                "Database cannot be null"
            );
        }
        if (findDatabase(database.getName()) != null){
            throw new IllegalArgumentException(
                "Databse already exists : "+database.getName()
            );
        }
        databases.add(database);
    }

    public Database findDatabase(String name){
        for(Database database:databases){
            if (database.getName().equalsIgnoreCase(name)){
                return database;
            }
        };
        return null;
    }
    public Database getCurrentDatabase(){
        return currentDatabase;
    }
    public void useDatabase(String name){
        Database database = findDatabase(name);
        if (database == null){
            throw new IllegalArgumentException(
                "Database not found: " + name
            );
        }
        currentDatabase = findDatabase(name);
    }
    public ArrayList<String> listDatabases(){
        ArrayList<String> names = new ArrayList<>();
        for (Database database : databases){
            names.add(database.getName());
        }
        return names;
    }
}