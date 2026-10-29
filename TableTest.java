
public class TableTest {

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

    private static Table createStudentsTable() {
        Table table = new Table("students");
        table.addAttribute(new Column("ID", "INT"));
        table.addAttribute(new Column("Name", "VARCHAR"));
        table.addAttribute(new Column("Year", "INT"));
        return table;
    }

    public static void main(String[] args) {
        check("Valid row insertion", () -> {
            Table table = createStudentsTable();
            Row row = new Row();
            row.addValue("ID", 1);
            row.addValue("Name", "Rahul");
            row.addValue("Year", 2);
            table.addRow(row);

            if (table.getRows().size() != 1) {
                throw new AssertionError("Expected one row");
            }
        });

        check("Reject wrong column type", () -> {
            Table table = createStudentsTable();
            Row row = new Row();
            row.addValue("ID", "wrong");
            row.addValue("Name", "Rahul");
            row.addValue("Year", 2);

            expectException(() -> table.addRow(row));
        });

        check("Reject missing value", () -> {
            Table table = createStudentsTable();
            Row row = new Row();
            row.addValue("ID", 1);
            row.addValue("Name", "Rahul");

            expectException(() -> table.addRow(row));
        });

        check("Reject duplicate column", () -> {
            Table table = createStudentsTable();
            expectException(
                () -> table.addAttribute(new Column("ID", "INT"))
            );
        });

        check("Reject wrong WHERE type", () -> {
            Table table = createStudentsTable();
            expectException(() -> table.selectWhere("Year", "Rahul"));
            expectException(() -> table.selectWhere("Name", 3));
        });

        check("Select matching rows", () -> {
            Table table = createStudentsTable();
            table.insert(new java.util.HashMap<>(java.util.Map.of(
            "ID", 1, "Name", "Rahul", "Year", 2
            )));
            table.insert(new java.util.HashMap<>(java.util.Map.of(
                "ID", 2, "Name", "Sohan", "Year", 3
            )));

            if (table.selectWhere("Year", 3).size() != 1) {
                throw new AssertionError("Expected one matching row");
            }
        });

        check("Delete matching rows", () -> {
            Table table = createStudentsTable();
            table.insert(new java.util.HashMap<>(java.util.Map.of(
                "ID", 1, "Name", "Rahul", "Year", 2
            )));
            table.deleteWhere("ID", 1);

            if (!table.getRows().isEmpty()) {
                throw new AssertionError("Expected empty table");
            }
        });

        check("Update matching rows", () -> {
            Table table = createStudentsTable();
            table.insert(new java.util.HashMap<>(java.util.Map.of(
                "ID", 1, "Name", "Rahul", "Year", 2
            )));
            table.updateWhere("ID", 1, "Year", 3);

            if (!Integer.valueOf(3).equals(
                    table.selectWhere("ID", 1).get(0).getValue("Year"))) {
                throw new AssertionError("Year was not updated");
            }
        });

        System.out.println("\nPassed: " + passed + ", Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Some tests failed");
        }
    }
}
