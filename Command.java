import java.util.ArrayList;
class Command {
    private CommandType type;
    private String tableName;
    private ArrayList<String> attributes;
    private ArrayList<Column> columns;
    private ArrayList<Object> values;
    private String conditionKey;
    private Object conditionValue;
    private String updateKey;
    private Object updateValue;

    public Command(CommandType type, String tableName, ArrayList<String> attributes){
        this.type = type;
        this.tableName = tableName;
        this.attributes = attributes;
    }
    public void setUpdate(String key, Object value){
        this.updateKey = key;
        this.updateValue = value;
    }
    public void setValues(ArrayList<Object> values){
        this.values = values;
    }
    public void setColumns(ArrayList<Column> columns){
        this.columns = columns;
    }
    public String getUpdateKey(){
        return updateKey;
    }
    public Object getUpdateValue(){
        return updateValue;
    }
    public ArrayList<Column> getColumns(){
        return columns;
    }
    public CommandType getType(){
        return type;
    }
    public String getTableName(){
        return tableName;
    }
    public ArrayList<String> getTableAtrributes(){
        return attributes;
    }
    public ArrayList<Object> getValues(){
        return values;
    }
    public void setCondition(String key, Object value){
        this.conditionKey = key;
        this.conditionValue = value;
    }
    public String getConditionKey(){
        return conditionKey;
    }
    public Object getConditionValue(){
        return conditionValue;
    }
}