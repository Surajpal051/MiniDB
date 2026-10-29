import java.util.HashMap;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Table students = new Table("students");
        System.out.println(students.getName());
        students.addAttribute(new Column("ID", "INT"));
        students.addAttribute(new Column("Name", "VARCHAR"));
        students.addAttribute(new Column("Year", "INT"));
        System.out.println(students.getAttributes());
        System.out.println(students.getColumnType("Year"));
        System.out.println(students.getColumnType("Name"));
        Row row1 = new Row();
        row1.addValue("ID",1);
        row1.addValue("Name","Rahul");
        row1.addValue("Year",2);
        students.addRow(row1);
        System.out.println(row1.getRowData());
        System.out.println(students.getRows());
        HashMap<String, Object> map = new HashMap<>();
        map.put("ID", 2);
        map.put("Name","Sohan");
        map.put("Year",3);
        students.insert(map);
        System.out.println(students.getRows());
        System.out.println(students.select("ID","Name","Year"));
        System.out.println(row1.getValue("Name"));
        System.out.println(students.selectWhere("ID",2));
        System.out.println(students.selectWhere("ID",99));
        students.deleteWhere("ID", 2);
        System.out.println(students.getRows());
        students.updateWhere("ID", 1, "Year", 3);
        System.out.println(students.getRows());
        Database db = new Database("Students");
        db.addTable(students);
        Table table = db.findTable("students");
        if (table == null){
            System.out.println("Table not found");
        }
        else{
            System.out.println(table.getName());
        }
        Table employees = new Table("employees");
        db.addTable(employees);
        System.out.println(db.dropTable("employees"));
        db.findTable("employees");
        System.out.println(db.dropTable("employees"));
        db.addTable(employees);
        System.out.println(db.listTables());
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("ID");
        attributes.add("Name");
        attributes.add("Class");
        ArrayList<Column> columns = new ArrayList<>();
        columns.add(new Column("ID", "INT"));
        columns.add(new Column("Name", "VARCHAR"));
        columns.add(new Column("Class", "VARCHAR"));

        Command command = new Command(CommandType.CREATE_TABLE,
        "College",
        attributes);
        command.setColumns(columns);
        System.out.println(command.getType());
        System.out.println(command.getTableAtrributes());
        System.out.println(command.getTableName());
        DatabaseManager manager = new DatabaseManager();
        Database db1 = new Database("CollegeDB");
        Database db2 = new Database("SchoolDB");
        manager.addDatabase(db1);
        manager.addDatabase(db2);
        System.out.println(db1.getName());
        System.out.println(manager.findDatabase("CollegeDB"));
        manager.useDatabase("CollegeDB");
        System.out.println(manager.getCurrentDatabase().getName());
        CommandExecutor execute = new CommandExecutor(db);
        execute.executeCreateTable(command);
        System.out.println(db.listTables());
        System.out.println(db.findTable("College").getAttributes());
        ArrayList<Object> values = new ArrayList<>();
        values.add(1);
        values.add("Rahul");
        values.add(2);
        Command insertcommand = new Command(CommandType.INSERT,
        "students",
        null);
        insertcommand.setValues(values);
        System.out.println(insertcommand.getValues());
        execute.executeInsert(insertcommand);
        System.out.println(db.findTable("students").getRows());
        ArrayList<String> attributes1 = new ArrayList<>();
        Command selectCommand = new Command(CommandType.SELECT,"students",attributes1);
        System.out.println(execute.executeSelect(selectCommand));
        selectCommand.setCondition("Year", 3);
        System.out.println(selectCommand.getConditionKey());
        System.out.println(selectCommand.getConditionValue());
        System.out.println(db.findTable("students").getRows());
        System.out.println(execute.executeSelect(selectCommand));
        SQLParser parser = new SQLParser(db);
        Command command2 = parser.parse("SELECT * FROM students;");
        System.out.println(execute.executeSelect(command2));

try {
    Command c3 = parser.parse(
        "SELECT Name FROM students WHERE Year = Rahul;"
    );
    System.out.println(execute.executeSelect(c3));
} catch (IllegalArgumentException e) {
    System.out.println("c3 rejected: " + e.getMessage());
}

try {
    Command c4 = parser.parse(
        "SELECT Name FROM students WHERE Name = 3;"
    );
    System.out.println(execute.executeSelect(c4));
} catch (IllegalArgumentException e) {
    System.out.println("c4 rejected: " + e.getMessage());
}
    }
}