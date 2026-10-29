import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
public class Table {
    private String name;
    private ArrayList<Column> columns = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();
    public Table(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void addAttribute(Column column){
        if (column == null){
            throw new IllegalArgumentException("Column cannot be null");
        }
        if (column.getName() == null || column.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Column name cannot be empty");
        }
        if (getColumnType(column.getName()) != null){
            throw new IllegalArgumentException(
                "Duplicate column : " + column.getName()
            );
        }
        String type = column.getType();
        if (!"INT".equals(type) && !"VARCHAR".equals(type)){
            throw new IllegalArgumentException(
                "Unsupported column type : " + column.getType()
            );
        }
        columns.add(column);
    }
    public ArrayList<String> getAttributes(){
        ArrayList<String> attributes = new ArrayList<>();
        for (Column column : columns){
            attributes.add(column.getName());
        }
        return attributes;
    }
    public void addRow(Row row){
        if (row == null) {
            throw new IllegalArgumentException("Row cannot be null");
        }
        HashMap<String, Object> rowData = row.getRowData();
        for (String key : rowData.keySet()){
            if (!getAttributes().contains(key)){
                throw new IllegalArgumentException (
                    "Unkown Column : "+ key
                );
            }
        }
        for (String columnName : getAttributes()){
            Object value = row.getValue(columnName);
            if (!isValidValue(columnName,value)){
                throw new IllegalArgumentException(
                    "Invalid or missing value or column : "+ columnName
                );
            }
        }
        rows.add(new Row(rowData));
    }
    public ArrayList<Row> getRows(){
        ArrayList<Row> result = new ArrayList<>();
        for (Row row : rows){
            result.add(new Row(row.getRowData()));
        }
        return result;
    }
    public void insert(HashMap<String, Object> map){
        if (map == null){
            throw new IllegalArgumentException("Row data cannot be null");
        }
        for (String key : map.keySet()){
            if (!getAttributes().contains(key)){
                throw new IllegalArgumentException("Unkown Column : "+ key);
            }
        }
        for (Column column : columns){
            String columnName = column.getName();
            Object value = map.get(columnName);
            if (!map.containsKey(columnName) || !isValidValue(columnName, value)){
                throw new IllegalArgumentException (
                    "Invalid or missing value for column : " + columnName
                );
            }
        }
        Row row = new Row();
        for (Column column : columns){
            String columnName = column.getName();
            row.addValue(columnName, map.get(columnName));
        }
        addRow(row);
    }
    public ArrayList<ArrayList<Object>> select(String... keys){
        for (String key : keys){
            if (getColumnType(key) == null){
                throw new IllegalArgumentException(
                    "Unkown column : " + key
                );
            }
        }
        ArrayList<ArrayList<Object>> result = new ArrayList<>();
        for (Row row : rows) {
            ArrayList<Object> item = new ArrayList<>();
            for (String key : keys) {
                item.add(row.getValue(key));
            }
            result.add(item);
        };
        return result;
    }
    public ArrayList<Row> selectWhere(String key, Object value){
        if (getColumnType(key) == null){
            throw new IllegalArgumentException(
                "Unkown condition column : "+key
            );
        }
        if (!isValidValue(key,value)){
            throw new IllegalArgumentException(
                "Invalid condition Value for Column : "+key
            );
        }
        ArrayList<Row> result = new ArrayList<>();
        for (Row row : rows){
            Object currentValue = row.getValue(key);
            if (value.equals(currentValue)){
                result.add(new Row(row.getRowData()));
            }
        }
        return result;
    }
    public void deleteWhere(String key, Object value){
        if (getColumnType(key) == null){
            throw new IllegalArgumentException(
                "Unkown condition column : "+key
            );
        }
        if (!isValidValue(key, value)){
            throw new IllegalArgumentException(
                "Invalid condition for the column : "+key
            );
        }
        Iterator<Row> iterator = rows.iterator();
        while(iterator.hasNext()){
            Row row = iterator.next();
            Object currentValue = row.getValue(key);
            if (value.equals(currentValue)){
                iterator.remove();
            }
        }
    }
    public void updateWhere(String key, Object value, String newKey, Object newValue){
        if (getColumnType(key) == null){
            throw new IllegalArgumentException(
                "Unkown condition column : " + key
            );
        }
        if (!isValidValue(key, value)){
            throw new IllegalArgumentException(
                "Invalid condition value for column : "+ key
            );
        }
        if (getColumnType(newKey) == null){
            throw new IllegalArgumentException(
                "Unkown column to update : "+ newKey
            );
        }
        if (!isValidValue(newKey,newValue)){
            throw new IllegalArgumentException(
                "Invalid value for column : " + newKey
            );
        }
        for (Row row : rows){
            Object currentValue = row.getValue(key);
            if (currentValue != null && currentValue.equals(value)){
                row.addValue(newKey,newValue);
            }
        }
    }
    public String getColumnType(String name){
        if (name == null){
            return null;
        }
        for (Column column : columns){
            if (column.getName().equals(name)){
                return column.getType();
            }
        }
        return null;
    }
    public boolean isValidValue(String columnName, Object value){
        String type = getColumnType(columnName);
        if (type == null || value == null){
            return false;
        }
        if (type.equals("INT")){
            return value instanceof Integer;
        }
        if (type.equals("VARCHAR")){
            return value instanceof String;
        }
        return false;
    }
}