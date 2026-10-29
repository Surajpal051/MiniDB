
import java.util.ArrayList;

public class SQLParserTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String testName, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + testName);
        } catch (Throwable e) {
            failed++;
            System.out.println("FAIL: " + testName);
            System.out.println("      " + e.getMessage());
        }
    }

    private static void expectException(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
    }

    private static Database createDatabase() {
        Database db = new Database("TestDB");

        Table students = new Table("students");
        students.addAttribute(new Column("ID", "INT"));
        students.addAttribute(new Column("Name", "VARCHAR"));
        students.addAttribute(new Column("Year", "INT"));

        db.addTable(students);
        return db;
    }

    public static void main(String[] args) {
        Database db = createDatabase();
        SQLParser parser = new SQLParser(db);

        check("Parse SELECT *", () -> {
            Command command = parser.parse("SELECT * FROM students;");

            if (command.getType() != CommandType.SELECT) {
                throw new AssertionError("Expected SELECT command");
            }
            if (!"students".equals(command.getTableName())) {
                throw new AssertionError("Incorrect table name");
            }
            if (!command.getTableAtrributes().isEmpty()) {
                throw new AssertionError("SELECT * should have no explicit attributes");
            }
        });

        check("Parse selected columns", () -> {
            Command command = parser.parse(
                "SELECT Name,ID FROM students;"
            );
            ArrayList<String> attributes = command.getTableAtrributes();

            if (attributes.size() != 2
                    || !"Name".equals(attributes.get(0))
                    || !"ID".equals(attributes.get(1))) {
                throw new AssertionError("Selected columns parsed incorrectly");
            }
        });

        check("Parse integer WHERE condition", () -> {
            Command command = parser.parse(
                "SELECT Name FROM students WHERE Year = 3;"
            );

            if (!"Year".equals(command.getConditionKey())
                    || !Integer.valueOf(3).equals(command.getConditionValue())) {
                throw new AssertionError("Integer condition parsed incorrectly");
            }
        });

        check("Parse quoted VARCHAR condition", () -> {
            Command command = parser.parse(
                "SELECT ID FROM students WHERE Name = 'Rahul';"
            );

            if (!"Name".equals(command.getConditionKey())
                    || !"Rahul".equals(command.getConditionValue())) {
                throw new AssertionError("String condition parsed incorrectly");
            }
        });

        check("Reject unknown table", () -> {
            expectException(() ->
                parser.parse("SELECT * FROM teachers;")
            );
        });

        check("Reject unknown WHERE column", () -> {
            expectException(() ->
                parser.parse(
                    "SELECT Name FROM students WHERE Age = 3;"
                )
            );
        });

        check("Reject malformed WHERE condition", () -> {
            expectException(() ->
                parser.parse(
                    "SELECT Name FROM students WHERE Year;"
                )
            );
        });

        check("Reject empty query", () -> {
            expectException(() -> parser.parse("   "));
        });

        check("Reject invalid SELECT syntax", () -> {
            expectException(() ->
                parser.parse("SHOW students;")
            );
        });

        System.out.println(
            "\nPassed: " + passed + ", Failed: " + failed
        );

        if (failed > 0) {
            throw new AssertionError("Some tests failed");
        }
    }
}
