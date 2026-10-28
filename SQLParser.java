import java.util.ArrayList;
class SQLParser {
    private Database database;
    public SQLParser(Database database){
        this.database = database;
    }
    public Command parse(String query){
        String[] parts = query.split("\\s+");
        String tableName = parts[3].replace(";","").trim();
        Table table = database.findTable(tableName);
        String selectedColumns = parts[1];
        String[] columns = selectedColumns.split(",");
        ArrayList<String> attributes = new ArrayList<>();
        if (!selectedColumns.equals("*")) {
            for (String column : columns) {
                attributes.add(column);
                }
            }
        Command command = new Command(CommandType.SELECT,tableName,attributes);
        if (query.contains("WHERE")){
            String conditionKey = parts[5];
            String conditionValue = parts[7].replace(";","");
            int value = Integer.parseInt(conditionValue);
            command.setCondition(conditionKey,value);
        }
        return command;

    }
}