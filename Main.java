import java.util.HashMap;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Table students = new Table("students");
        System.out.println(students.getName());
        students.addAttribute("ID");
        students.addAttribute("Name");
        students.addAttribute("Year");
        System.out.println(students.getAttributes());
        Row row1 = new Row();
        students.addRow(row1);
        row1.addValue("ID",1);
        row1.addValue("Name","Rahul");
        row1.addValue("Year",2);
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
        Command command = new Command(CommandType.CREATE_TABLE,
        "College",
        attributes);
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
    }
}