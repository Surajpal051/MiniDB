import java.util.HashMap;
public class Row {
    private final HashMap<String, Object> rowData;
    public Row(){
        rowData = new HashMap<>();
    }
    public Row(HashMap<String, Object> data){
        if (data == null){
            throw new IllegalArgumentException("Row data cannot be null");
        }
        rowData = new HashMap<>(data);
    }
    public void addValue(String key, Object value){
        rowData.put(key,value);
    }
    public HashMap<String, Object> getRowData(){
        return new HashMap<>(rowData);
    }
    public Object getValue(String key){
        return rowData.get(key);
    }
    @Override
    public String toString() {
        return rowData.toString();
    }
}