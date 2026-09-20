import java.util.ArrayList;
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
}