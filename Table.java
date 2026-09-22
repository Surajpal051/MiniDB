import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class Table {
    private String name;
    private ArrayList<String> attributes = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();
    public Table(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void addAttribute(String name){
        attributes.add(name);
    }
    public ArrayList<String> getAttributes(){
        return attributes;
    }
    public void addRow(Row row){
        rows.add(row);
    }
    public ArrayList<Row> getRows(){
        return rows;
    }
    public void insert(HashMap<String, Object> map){
        Row row = new Row();
        for (Map.Entry<String, Object> entry : map.entrySet()){
            row.addValue(entry.getKey(), entry.getValue());
        }
        addRow(row);
    }
}