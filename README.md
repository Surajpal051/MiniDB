# MiniDB

MiniDB is a small database management system built from scratch in Java.

The goal of this project is to understand how a database works internally by implementing its core components step by step.

## Current Status

The project is currently in the early development stage.

### Implemented

- Java project setup
- Table representation
- Table name
- Table attributes/columns
- Row representation
- Row data using HashMap<String, Object>
- Adding values to a row
- Adding rows to a table
- Retrieving table attributes
- Retrieving table rows
- Implement insert operation

## Current Structure

### Main.java

Used to test and demonstrate MiniDB functionality.

### Table.java

Represents a database table.

Currently manages:
- Table name
- Attributes/columns
- Rows

### Row.java

Represents a single row in a table.

Row data is stored using HashMap<String, Object>.

This allows a row to store values of different types.

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

## Roadmap

- [ ] Basic insert operation
- [ ] Select operation
- [ ] Update operation
- [ ] Delete operation
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