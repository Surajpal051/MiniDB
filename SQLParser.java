import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        if (cleanedQuery.isEmpty() || cleanedQuery.contains(";")){
            throw new IllegalArgumentException("Invalid SQL Syntax");
        }
        if (startsWithKeyword(cleanedQuery,"SELECT")){
            return parseSelect(cleanedQuery);
        }
        if (startsWithKeyword(cleanedQuery,"INSERT")){
            return parseInsert(cleanedQuery);
        }
        if (startsWithKeyword(cleanedQuery,"UPDATE")){
            return parseUpdate(cleanedQuery);
        }
        if (startsWithKeyword(cleanedQuery,"DELETE")){
            return parseDelete(cleanedQuery);
        }
        if (startsWithKeyword(cleanedQuery,"CREATE")){
            return parseCreateTable(cleanedQuery);
        }
        throw new IllegalArgumentException("Unsupported SQL operation");

    }
    private boolean startsWithKeyword(String sql, String keyword){
        return sql.matches("(?is)^" + keyword + "\\b.*");
    }
    private Table requireTable(String tableName){
        Table table = database.findTable(tableName);
        if (table == null){
            throw new IllegalArgumentException("Table not found : "+tableName);
        }
        return table;
    }
    private Command parseSelect(String sql){
        Matcher matcher = Pattern.compile(
            "(?is)^SELECT\\s+(.+?)\\s+FROM\\s+" +
            "([A-Za-z_][A-Za-z0-9_]*)" +
            "(?:\\s+WHERE\\s+(.+))?$"
        ).matcher(sql);
        if (!matcher.matches()){
            throw new IllegalArgumentException("Invalid SELECT syntax");
        }
        String selectedColumns = matcher.group(1).trim();
        String tableName = matcher.group(2);
        String whereText = matcher.group(3);

        Table table = requireTable(tableName);
        ArrayList<String> attributes = new ArrayList<>();
        if (!selectedColumns.equals("*")){
            for (String column : selectedColumns.split(",",-1)){
                String name = column.trim();
                if (name.isEmpty() || table.getColumnType(name) == null){
                    throw new IllegalArgumentException("Unkown or Invalid column : "+name);
                }
                attributes.add(name);
            }
        }
        Command command = new Command(CommandType.SELECT, tableName, attributes);
        if (whereText != null){
            String[] condition = splitEquality(whereText);
            String key = condition[0];
            String type = table.getColumnType(key);
            if (type == null){
                throw new IllegalArgumentException("Unkown condition column : "+key);
            }
            command.setCondition(key,parseValue(condition[1],type));
        }
        return command;
    }
    private Command parseInsert(String sql){
        Matcher matcher = Pattern.compile(
            "(?is)^INSERT\\s+INTO\\s+" +
            "([A-Za-z_][A-Za-z0-9_]*)\\s+" +
            "VALUES\\s*\\((.*)\\)$"
        ).matcher(sql);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "Invalid INSERT syntax; use INSERT INTO table VALUES (...)"
            );
        }
        String tableName = matcher.group(1);
        String valuesText = matcher.group(2);
        Table table = requireTable(tableName);
        ArrayList<String> attributes = table.getAttributes();
        ArrayList<String> rawValues = splitCommaSeparated(valuesText);

        if (rawValues.size() != attributes.size()) {
            throw new IllegalArgumentException(
                "Column count does not match value count"
            );
        }
        ArrayList<Object> values = new ArrayList<>();

        for (int i = 0; i < attributes.size(); i++) {
            values.add(
                parseValue(
                    rawValues.get(i),
                    table.getColumnType(attributes.get(i))
                )
            );
        }
        Command command = new Command(
            CommandType.INSERT, tableName, null
        );
        command.setValues(values);

        return command;
    }
    private Command parseUpdate(String sql) {
        Matcher matcher = Pattern.compile(
            "(?is)^UPDATE\\s+" +
            "([A-Za-z_][A-Za-z0-9_]*)\\s+" +
            "SET\\s+(.+?)\\s+WHERE\\s+(.+)$"
        ).matcher(sql);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "Invalid UPDATE syntax; WHERE is required"
            );
        }

        String tableName = matcher.group(1);
        String assignment = matcher.group(2);
        String whereText = matcher.group(3);

        Table table = requireTable(tableName);

        String[] setParts = splitEquality(assignment);
        String updateKey = setParts[0];
        String updateType = table.getColumnType(updateKey);

        if (updateType == null) {
            throw new IllegalArgumentException(
                "Unknown column to update: " + updateKey
            );
        }

        Object updateValue = parseValue(setParts[1], updateType);

        String[] condition = splitEquality(whereText);
        String conditionKey = condition[0];
        String conditionType = table.getColumnType(conditionKey);

        if (conditionType == null) {
            throw new IllegalArgumentException(
                "Unknown condition column: " + conditionKey
            );
        }

        Object conditionValue = parseValue(
            condition[1], conditionType
        );

        Command command = new Command(
            CommandType.UPDATE, tableName, null
        );
        command.setUpdate(updateKey, updateValue);
        command.setCondition(conditionKey, conditionValue);

        return command;
    }

    // DELETE FROM students WHERE ID = 1
    private Command parseDelete(String sql) {
        Matcher matcher = Pattern.compile(
            "(?is)^DELETE\\s+FROM\\s+" +
            "([A-Za-z_][A-Za-z0-9_]*)\\s+" +
            "WHERE\\s+(.+)$"
        ).matcher(sql);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "Invalid DELETE syntax; WHERE is required"
            );
        }

        String tableName = matcher.group(1);
        Table table = requireTable(tableName);

        String[] condition = splitEquality(matcher.group(2));
        String key = condition[0];
        String type = table.getColumnType(key);

        if (type == null) {
            throw new IllegalArgumentException(
                "Unknown condition column: " + key
            );
        }

        Command command = new Command(
            CommandType.DELETE, tableName, null
        );
        command.setCondition(
            key, parseValue(condition[1], type)
        );

        return command;
    }

    private String[] splitEquality(String expression) {
        boolean inQuotes = false;
        int equalsIndex = -1;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '\'') {
                inQuotes = !inQuotes;
            } else if (ch == '=' && !inQuotes) {
                if (equalsIndex != -1) {
                    throw new IllegalArgumentException(
                        "Only one equality condition is supported"
                    );
                }
                equalsIndex = i;
            }
        }

        if (inQuotes || equalsIndex <= 0 ||
                equalsIndex == expression.length() - 1) {
            throw new IllegalArgumentException(
                "Invalid equality condition"
            );
        }

        String key = expression.substring(0, equalsIndex).trim();
        String value = expression.substring(equalsIndex + 1).trim();

        if (!key.matches("[A-Za-z_][A-Za-z0-9_]*") ||
                value.isEmpty()) {
            throw new IllegalArgumentException(
                "Invalid equality condition"
            );
        }

        return new String[] { key, value };
    }

    private ArrayList<String> splitCommaSeparated(String text) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '\'') {
                inQuotes = !inQuotes;
                current.append(ch);
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException(
                "Unclosed quoted string"
            );
        }

        if (current.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing value");
        }

        values.add(current.toString().trim());
        return values;
    }

    private Object parseValue(String rawValue, String type) {
        String value = rawValue.trim();

        if ("INT".equals(type)) {
            if (!value.matches("-?\\d+")) {
                throw new IllegalArgumentException(
                    "Expected an INT value, got: " + value
                );
            }

            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Integer value is out of range: " + value
                );
            }
        }

        if ("VARCHAR".equals(type)) {
            if (value.length() < 2 ||
                    value.charAt(0) != '\'' ||
                    value.charAt(value.length() - 1) != '\'') {
                throw new IllegalArgumentException(
                    "VARCHAR values must use single quotes"
                );
            }

            return value.substring(1, value.length() - 1);
        }

        throw new IllegalArgumentException(
            "Unsupported column type: " + type
        );
    }
    private Command parseCreateTable(String sql){
        Matcher matcher = Pattern.compile(
        "(?is)^CREATE\\s+TABLE\\s+" +
        "([A-Za-z_][A-Za-z0-9_]*)\\s*\\((.*)\\)$"
        ).matcher(sql);
        if (!matcher.matches()){
            throw new IllegalArgumentException(
                "Invalid CREATE TABLE syntax. Example: "+
                "CREATE TABLE employees (ID INT, Name VARCHAR)"
            );
        }
        String tableName = matcher.group(1);
        String columnsText = matcher.group(2).trim();
        if (columnsText.isEmpty()){
            throw new IllegalArgumentException(
                "CREATE TABLE requires at least one column"
            );
        }
        if (database.findTable(tableName) != null){
            throw new IllegalArgumentException(
                "Table already exists: " + tableName
            );
        }
        ArrayList<String> columnDefinitions = splitCommaSeparated(columnsText);
        ArrayList<String> attributes = new ArrayList<>();
        ArrayList<Column> columns = new ArrayList<>();
        for (String definition : columnDefinitions){
            String[] parts = definition.trim().split("\\s+");
            if (parts.length != 2){
                throw new IllegalArgumentException(
                    "Invalid column definition : " + definition
                );
            }
            String columnName = parts[0];
            String columnType = parts[1].toUpperCase();
            if (!columnName.matches("[A-Za-z_][A-Za-z0-9_]*")){
                throw new IllegalArgumentException(
                    "Invalid column name : "+columnName
                );
            }
            if (!columnType.equals("INT") && !columnType.equals("VARCHAR")){
                throw new IllegalArgumentException(
                    "Unsupported column type : "+columnType
                );
            }
            for (String existing : attributes) {
                if (existing.equalsIgnoreCase(columnName)){
                    throw new IllegalArgumentException(
                        "Duplicate column : " + columnName
                    );
                }
            }
            attributes.add(columnName);
            columns.add(new Column(columnName,columnType));
        }
        Command command = new Command(CommandType.CREATE_TABLE,tableName,attributes);
        command.setColumns(columns);
        return command;
    }
}