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
}