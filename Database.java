import java.util.ArrayList;
public class Database{
    private String name;
    private ArrayList<Table> tables = new ArrayList<>();

    public Database(String name){
        this.name = name;
    }
    public void addTable(Table table){
        tables.add(table);
    }
    public Table findTable(String key){
        for(Table table : tables){
            if (table.getName().equals(key)){
                return table;
            }
        }
        return null;
        }
    public String dropTable(String key){
        Table table = this.findTable(key);
        if (table == null){
            return "Table does not exist";
        }
        else{
            tables.remove(table);
            return "Table dropped";
        }
    }
    public ArrayList<String> listTables(){
        ArrayList<String> names = new ArrayList<>();
        tables.forEach(table ->{
            names.add(table.getName());
        });
        return names;
    }
    }