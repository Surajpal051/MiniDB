
import java.util.ArrayList;

public class IntegrationTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + name);
        } catch (Throwable e) {
            failed++;
            System.out.println("FAIL: " + name);
            System.out.println("      " + e.getMessage());
        }
    }

    private static void assertEquals(
            Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(
                "Expected: " + expected + ", Actual: " + actual
            );
        }
    }

    private static Database setupDatabase() {
        Database db = new Database("TestDB");

        Table students = new Table("students");
        students.addAttribute(new Column("ID", "INT"));
        students.addAttribute(new Column("Name", "VARCHAR"));
        students.addAttribute(new Column("Year", "INT"));

        db.addTable(students);

        java.util.HashMap<String, Object> first =
            new java.util.HashMap<>();
        first.put("ID", 1);
        first.put("Name", "Rahul");
        first.put("Year", 2);
        students.insert(first);

        java.util.HashMap<String, Object> second =
            new java.util.HashMap<>();
        second.put("ID", 2);
        second.put("Name", "Sohan");
        second.put("Year", 3);
        students.insert(second);

        return db;
    }

    public static void main(String[] args) {
        Database db = setupDatabase();
        SQLParser parser = new SQLParser(db);
        CommandExecutor executor = new CommandExecutor(db);

        check("SELECT * returns all rows", () -> {
            Command command = parser.parse(
                "SELECT * FROM students;"
            );
            Object result = executor.executeSelect(command);

            assertEquals(2, ((ArrayList<?>) result).size());
        });

        check("SELECT specific column", () -> {
            Command command = parser.parse(
                "SELECT Name FROM students;"
            );
            Object result = executor.executeSelect(command);

            assertEquals(
                java.util.Arrays.asList(
                    java.util.Arrays.asList("Rahul"),
                    java.util.Arrays.asList("Sohan")
                ),
                result
            );
        });

        check("SELECT with WHERE condition", () -> {
            Command command = parser.parse(
                "SELECT Name FROM students WHERE Year = 3;"
            );
            Object result = executor.executeSelect(command);

            assertEquals(
                java.util.Arrays.asList(
                    java.util.Arrays.asList("Sohan")
                ),
                result
            );
        });

        check("WHERE with no matching rows", () -> {
            Command command = parser.parse(
                "SELECT Name FROM students WHERE Year = 99;"
            );
            Object result = executor.executeSelect(command);

            assertEquals(
                new ArrayList<ArrayList<Object>>(),
                result
            );
        });

        check("Invalid WHERE value type is rejected", () -> {
            try {
                Command command = parser.parse(
                    "SELECT Name FROM students WHERE Year = Rahul;"
                );
                executor.executeSelect(command);
            } catch (IllegalArgumentException e) {
                return;
            }

            throw new AssertionError(
                "Expected invalid WHERE value to be rejected"
            );
        });

        System.out.println(
            "\nPassed: " + passed + ", Failed: " + failed
        );

        if (failed > 0) {
            throw new AssertionError("Some integration tests failed");
        }
    }
}
