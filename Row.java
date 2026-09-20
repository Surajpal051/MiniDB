import java.util.HashMap;
public class Row {
    private HashMap<String, Object> rowData;
    public Row(){
        rowData = new HashMap<>();
    }
    public void addValue(String key, Object value){
        rowData.put(key,value);
    }
    public HashMap<String, Object> getRowData(){
        return rowData;
    }
    @Override
    public String toString() {
        return rowData.toString();
    }
}