import java.util.ArrayList;
class CommandExecutor{
    private Database currentDatabase;

    public CommandExecutor(Database database){
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
}