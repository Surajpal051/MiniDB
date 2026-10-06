import java.util.ArrayList;
class Command {
    private CommandType type;
    private String tableName;
    private ArrayList<String> attributes;
    private ArrayList<Object> values;

    public Command(CommandType type, String tableName, ArrayList<String> attributes){
        this.type = type;
        this.tableName = tableName;
        this.attributes = attributes;
    }
    public void setValues(ArrayList<Object> values){
        this.values = values;
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
}