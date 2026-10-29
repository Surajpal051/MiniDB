import java.util.ArrayList;
class SQLParser {
    private final Database database;
    public SQLParser(Database database){
        if (database == null){
            throw new IllegalArgumentException("Database cannot be null");
        }
        this.database = database;
    }
    public Command parse(String query){
        if (query == null || query.trim().isEmpty()){
            throw new IllegalArgumentException("Query cannot be empty");
        }
        String cleanedQuery = query.trim();
        if (cleanedQuery.endsWith(";")){
            cleanedQuery = cleanedQuery.substring(0,cleanedQuery.length()-1).trim();
        }
        String[] parts = cleanedQuery.split("\\s+");
        if (parts.length < 4 || !parts[0].equalsIgnoreCase("SELECT") || !parts[2].equalsIgnoreCase("FROM")){
            throw new IllegalArgumentException("Invalid SELECT Query");
        }
        String tableName = parts[3];
        Table table = database.findTable(tableName);
        if (table == null){
            throw new IllegalArgumentException("Table not found : "+ tableName);
        }
        String selectedColumns = parts[1];
        ArrayList<String> attributes = new ArrayList<>();
        if (!selectedColumns.equals("*")) {
            for (String column : selectedColumns.split(",")) {
                attributes.add(column.trim());
                }
            }
        Command command = new Command(CommandType.SELECT,tableName,attributes);
        if (cleanedQuery.toUpperCase().contains(" WHERE ")){
            int whereIndex = cleanedQuery.toUpperCase().indexOf(" WHERE ");
            String conditionText = cleanedQuery.substring(whereIndex + 7).trim();
            String[] condition = conditionText.split("=",2);
            if (condition.length != 2){
                throw new IllegalArgumentException("Invalid WHERE Condition");
            }
            String conditionKey = condition[0].trim();
            String rawValue = condition[1].trim();
            if (rawValue.isEmpty()){
                throw new IllegalArgumentException("Missing WHERE condition value");
            }
            String type = table.getColumnType(conditionKey);
            if (type == null){
                throw new IllegalArgumentException("Unkown condition column : "+conditionKey);
            }
            Object value;
            if (rawValue.startsWith("'") && rawValue.endsWith("'") && rawValue.length() >= 2){
                value = rawValue.substring(1, rawValue.length()-1);
            }else {
                try {
                    value = Integer.parseInt(rawValue);
                }catch (NumberFormatException e){
                    value = rawValue;
                }
            }
            command.setCondition(conditionKey,value);
        }
        return command;

    }
}