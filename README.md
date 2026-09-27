# MiniDB

MiniDB is a small database management system built from scratch in Java.

The goal of this project is to understand how a database works internally by implementing its core components step by step.

## Current Status

The project is currently in active development.

### Implemented

- Java project setup
- Table representation
- Table name
- Table attributes/columns
- Row representation
- Row data using HashMap<String, Object>
- Adding values to a row
- Retrieving values from a row
- Adding rows to a table
- Retrieving table attributes
- Retrieving table rows
- Insert operation
- Basic select operation
- Select specific column
- Select multiple columns
- Select rows using a condition
- Delete rows using a condition
- Update rows using a condition

## Current Structure

### Main.java

Used to test and demonstrate MiniDB functionality.

### Table.java

Represents a database table.

Currently manages:

- Table name
- Attributes/columns
- Rows
- Insert operations
- Select operations
- Conditional selection
- Conditional deletion
- Conditional updates

### Row.java

Represents a single row in a table.

Row data is stored using HashMap<String, Object>.

This allows a row to store values of different types.

The class also provides methods to:

- Add or update values
- Retrieve values by column name

## Supported Operations

### Insert

students.insert(Map.of("ID", 2, "Name", "Sohan", "Year", 3));

### Select All Rows

students.select();

### Select Specific Column

students.select("Name");

### Select Multiple Columns

students.select("ID", "Name");

### Select With Condition

students.selectWhere("ID", 2);

This returns rows where the specified column matches the given value.

### Delete With Condition

students.deleteWhere("ID", 2);

This removes rows where the specified column matches the given value.

### Update With Condition

students.updateWhere("ID", 1, "Year", 3);

This updates the specified column for rows matching the given condition.

## Example

Table creation:

Table students = new Table("students");

Adding attributes:

students.addAttribute("ID");
students.addAttribute("Name");
students.addAttribute("Year");

Creating a row:

Row row1 = new Row();

row1.addValue("ID", 1);
row1.addValue("Name", "Rahul");
row1.addValue("Year", 2);

Adding the row to the table:

students.addRow(row1);

Selecting a specific column:

students.select("Name");

Example output:

[Rahul]

Selecting multiple columns:

students.select("ID", "Name");

Example output:

[[1, Rahul]]

Selecting with a condition:

students.selectWhere("ID", 1);

Example output:

[{Year=2, ID=1, Name=Rahul}]

Updating a row:

students.updateWhere("ID", 1, "Year", 3);

Example result:

[{Year=3, ID=1, Name=Rahul}]

Deleting a row:

students.deleteWhere("ID", 1);

The matching row is removed from the table.

## Implementation Notes

- Rows are stored using ArrayList<Row>.
- Row data is stored using HashMap<String, Object>.
- selectWhere() performs equality-based filtering.
- updateWhere() updates values through the Row abstraction.
- deleteWhere() uses Java's Iterator to safely remove matching rows while traversing the collection.
- The current conditional operations are implemented at the Java API level.
- A SQL-like query parser will be implemented in a later phase.

## Current Progress

### Phase 1 — Core Data Model

- [x] Table class
- [x] Row class
- [x] Table attributes
- [x] Add rows
- [x] Store row values
- [x] Multiple rows

### Phase 2 — CRUD Operations

- [x] Insert rows
- [x] Basic SELECT
- [x] SELECT specific column
- [x] SELECT multiple columns
- [x] SELECT with WHERE-style filtering
- [x] DELETE with WHERE-style filtering
- [x] UPDATE with WHERE-style filtering
- [ ] Validation

### Next Phase

The next major phase is implementing the Database layer for managing multiple tables.

Planned features include:

- Database class
- Multiple tables
- Create table
- Drop table
- Find table
- List tables

## Roadmap

- [x] Basic insert operation
- [x] Select operation
- [x] Update operation
- [x] Delete operation
- [ ] Database and multiple tables
- [ ] SQL-like query system
- [ ] Data persistence
- [ ] Command-line interface
- [ ] Validation and exception handling
- [ ] Testing
- [ ] Documentation

## Learning Goals

This project is being developed to understand:

- Object-Oriented Programming in Java
- Data structures
- Database internals
- CRUD operations
- Query processing
- Data persistence
- Software design
- Safe collection manipulation using Java Iterators