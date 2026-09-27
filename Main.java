import java.util.HashMap;
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
    }
}