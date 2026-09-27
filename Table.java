import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
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
    public ArrayList<ArrayList<Object>> select(String... keys){
        ArrayList<ArrayList<Object>> result = new ArrayList<>();
        rows = getRows();
        rows.forEach(row -> {
            ArrayList<Object> item = new ArrayList<>();
            for (String key : keys) {
        item.add(row.getValue(key));
    }
            result.add(item);
        });
        return result;
    }
    public ArrayList<Row> selectWhere(String key, Object value){
        ArrayList<Row> result = new ArrayList<>();
        rows.forEach(row -> {
            if (row.getValue(key).equals(value)){
                result.add(row);
            }
        });
        return result;
    }
    public void deleteWhere(String key, Object value){
        Iterator<Row> iterator = rows.iterator();
        while(iterator.hasNext()){
            Row row = iterator.next();
            if(row.getValue(key).equals(value)){
                iterator.remove();
                System.out.println("Removed succesfully");
            }
        }
    }
    public void updateWhere(String key, Object value, String newKey, Object newValue){
        rows.forEach(row ->{
            if(row.getValue(key).equals(value)){
                row.addValue(newKey,newValue);
            }
        });
    }
}