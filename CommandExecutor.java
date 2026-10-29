import java.util.ArrayList;
class CommandExecutor{
    private Database currentDatabase;

    public CommandExecutor(Database database){
        this.currentDatabase = database;
    }
    public void setCurrentDatabase(Database database){
        if (database == null) {
            throw new IllegalArgumentException(
                "Select a database first using USE"
            );
        }
        this.currentDatabase = database;
    }
    public void executeCreateTable(Command command){
        String tableName = command.getTableName();
        ArrayList<Column> columns = command.getColumns();
        if (columns == null || columns.isEmpty()){
            throw new IllegalArgumentException(
                "Table must have atleast one column"
            );
        }
        Table table = new Table(tableName);
        for(Column column : columns){
            table.addAttribute(column);
        };
        currentDatabase.addTable(table);
        
    }
    public void executeInsert(Command command){
        String tableName = command.getTableName();
        Table table = currentDatabase.findTable(tableName);
        if (table == null){
            throw new IllegalArgumentException(
                "Table not found : " + tableName
            );
        }
        ArrayList<Object> values = command.getValues();
        ArrayList<String> attributes = table.getAttributes();
        if (values == null || values.size() != attributes.size()){
            throw new IllegalArgumentException(
                "Column count does not match value count"
            );
        }
        Row row = new Row();
        for (int i = 0 ; i < attributes.size() ; i++){
            row.addValue(attributes.get(i),values.get(i));
        }
        table.addRow(row);
    }
    public ArrayList<ArrayList<Object>> executeSelect(Command command){
        String tableName = command.getTableName();
        Table table = currentDatabase.findTable(tableName);
        if (table == null) {
            throw new IllegalArgumentException(
                "Table not found : "+ tableName
            );
        }
        ArrayList<String> attributes = command.getTableAtrributes();
        if (attributes.isEmpty() || attributes == null){
            attributes = table.getAttributes();
        }
        if (command.getConditionKey() != null){
            ArrayList<Row> matchingRows = table.selectWhere(command.getConditionKey(),command.getConditionValue());
            ArrayList<ArrayList<Object>> result = new ArrayList<>();
            for (Row row : matchingRows) {
                ArrayList<Object> item = new ArrayList<>();
                for (String attribute : attributes) {
                    if (table.getColumnType(attribute) == null){
                        throw new IllegalArgumentException( "Unkown column : "+attribute);
                    }
                    item.add(row.getValue(attribute));
                    }
                    result.add(item);
            }
            return result;
        }
        return table.select(attributes.toArray(new String[0]));
    }
    public void executeDelete(Command command){
        Table table = currentDatabase.findTable(command.getTableName());
        if (table == null){
            throw new IllegalArgumentException("Table not found : "+command.getTableName());
        }
        if (command.getConditionKey() == null){
            throw new IllegalArgumentException("DELETE requires a WHERE condition");
        }
        table.deleteWhere(command.getConditionKey(),command.getConditionValue());
    }
    public void executeUpdate(Command command) {
    Table table = currentDatabase.findTable(command.getTableName());

    if (table == null) {
        throw new IllegalArgumentException(
            "Table not found: " + command.getTableName()
        );
    }

    if (command.getConditionKey() == null) {
        throw new IllegalArgumentException(
            "UPDATE requires a WHERE condition"
        );
    }

    if (command.getUpdateKey() == null) {
        throw new IllegalArgumentException(
            "Missing column to update"
        );
    }

    table.updateWhere(
        command.getConditionKey(),
        command.getConditionValue(),
        command.getUpdateKey(),
        command.getUpdateValue()
    );
}
    
public Object execute(Command command) {
    if (currentDatabase == null){
        throw new IllegalArgumentException (
            "No database selected. Use a database first."
        );
    }
    if (command == null) {
        throw new IllegalArgumentException("Command cannot be null");
    }

    switch (command.getType()) {
        case SELECT:
            return executeSelect(command);

        case INSERT:
            executeInsert(command);
            return null;

        case UPDATE:
            executeUpdate(command);
            return null;

        case DELETE:
            executeDelete(command);
            return null;

        case CREATE_TABLE:
            executeCreateTable(command);
            return null;

        default:
            throw new IllegalArgumentException(
                "Unsupported command type"
            );
    }
}

}