import java.util.ArrayList;
class DatabaseManager {
    private ArrayList<Database> databases;
    private Database currentDatabase;

    public DatabaseManager() {
        databases = new ArrayList<>();
        currentDatabase = null;
    }
    public void addDatabase(Database database){
        databases.add(database);
    }

    public Database findDatabase(String name){
        for(Database database:databases){
            if (database.getName().equals(name)){
                return database;
            }
        };
        return null;
    }
    public Database getCurrentDatabase(){
        return currentDatabase;
    }
    public void useDatabase(String name){
        currentDatabase = findDatabase(name);
    }
}