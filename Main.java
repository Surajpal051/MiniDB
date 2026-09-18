public class Main {
    public static void main(String[] args) {
        Table students = new Table("students");
        System.out.println(students.getName());
        students.addAttribute("ID");
        students.addAttribute("Name");
        students.addAttribute("Year");
        System.out.println(students.getAttributes());
    }
}