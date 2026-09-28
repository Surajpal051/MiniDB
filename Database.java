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
}