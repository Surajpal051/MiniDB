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
    }
}