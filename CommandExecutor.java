import java.util.ArrayList;
class CommandExecutor{
    private Database currentDatabase;

    public CommandExecutor(Database database){
        this.currentDatabase = database;
    }
    public void executeCreateTable(Command command){
        String tableName = command.getTableName();
        ArrayList<String> attributes = command.getTableAtrributes();
        Table table = new Table(tableName);
        for(String attribute:attributes){
            table.addAttribute(attribute);
        };
        currentDatabase.addTable(table);
        
    }
    public void executeInsert(Command command){
        String tableName = command.getTableName();
        Table table = currentDatabase.findTable(tableName);
        ArrayList<Object> values = command.getValues();
        ArrayList<String> attributes = table.getAttributes();
        Row row = new Row();
        for (int i = 0 ; i < attributes.size() ; i++){
            row.addValue(attributes.get(i),values.get(i));
        }
        table.addRow(row);
    }
    public ArrayList<ArrayList<Object>> executeSelect(Command command){
        String tableName = command.getTableName();
        Table table = currentDatabase.findTable(tableName);
        ArrayList<String> attributes = command.getTableAtrributes();
        return table.select(attributes.toArray(new String[0]));
    }
}