
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseManager manager = new DatabaseManager();
        CommandExecutor executor = new CommandExecutor(null);
        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("          MiniDB v0.2");
        System.out.println("================================");
        System.out.println("Commands: CREATE DATABASE, SHOW DATABASES,");
        System.out.println("          USE, SHOW TABLES, exit");
        System.out.println();

        while (true) {
            Database current = manager.getCurrentDatabase();

            if (current == null) {
                System.out.print("minidb> ");
            } else {
                System.out.print(
                    "minidb[" + current.getName() + "]> "
                );
            }

            if (!scanner.hasNextLine()) {
                break;
            }

            String query = scanner.nextLine().trim();

            if (query.isEmpty()) {
                continue;
            }

            if (query.equalsIgnoreCase("exit")
                    || query.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                String sql = query;

                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1).trim();
                }

                if (sql.matches("(?is)^CREATE\\s+DATABASE\\s+.*")) {
                    String name = sql.replaceFirst(
                        "(?is)^CREATE\\s+DATABASE\\s+", ""
                    ).trim();

                    if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
                        throw new IllegalArgumentException(
                            "Invalid database name"
                        );
                    }

                    manager.addDatabase(new Database(name));
                    System.out.println(
                        "Database created: " + name
                    );
                } else if (sql.equalsIgnoreCase("SHOW DATABASES")) {
                    System.out.println(manager.listDatabases());
                } else if (sql.matches("(?is)^USE\\s+.*")) {
                    String name = sql.replaceFirst(
                        "(?is)^USE\\s+", ""
                    ).trim();

                    manager.useDatabase(name);
                    executor.setCurrentDatabase(
                        manager.getCurrentDatabase()
                    );

                    System.out.println(
                        "Using database: " + name
                    );
                } else if (sql.equalsIgnoreCase("SHOW TABLES")) {
                    if (manager.getCurrentDatabase() == null) {
                        throw new IllegalArgumentException(
                            "Select a database first using USE"
                        );
                    }

                    System.out.println(
                        manager.getCurrentDatabase().listTables()
                    );
                } else {
                    if (manager.getCurrentDatabase() == null) {
                        throw new IllegalArgumentException(
                            "Create and USE a database first"
                        );
                    }

                    SQLParser parser = new SQLParser(
                        manager.getCurrentDatabase()
                    );

                    Command command = parser.parse(query);
                    Object result = executor.execute(command);

                    if (command.getType() == CommandType.SELECT) {
                        System.out.println(result);
                    } else {
                        System.out.println("Query executed successfully.");
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(
                    "Unexpected error: " + e.getMessage()
                );
            }

            System.out.println();
        }

        scanner.close();
    }
}
