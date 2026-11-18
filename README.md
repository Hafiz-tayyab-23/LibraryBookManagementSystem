
# Library Book Management System

A desktop-based Library Book Management System developed
using Java Swing as part of the Software Construction
Open-Ended Lab (OEL).

## Team Members

- Tayyab Zia
- Muraleen Ahmad

## Project Objective

To develop a functional Java desktop application using
object-oriented programming principles, graphical user
interface development, Git version control, and Javadoc.

## Features

- Add new books with unique IDs
- View all books in a table
- Update book titles and authors
- Delete books with confirmation
- Change book status:
  - Available
  - Issued
  - Reserved
- Validate empty titles and author names
- Prevent duplicate book IDs
- Handle invalid and non-numeric book IDs
- Display success and error messages
- Select books directly from the table

## Technologies Used

- Java
- Java Swing
- Java Collections Framework (ArrayList)
- JDK
- Visual Studio Code
- Git and GitHub
- Javadoc

## Project Structure

```text
LibraryBookManagementSystem/
├── src/
│   ├── Main.java
│   ├── Book.java
│   ├── LibraryManager.java
│   └── LibraryGUI.java
├── docs/
│   └── index.html
├── .gitignore
└── README.md
```

The docs folder contains additional generated Javadoc
HTML files and supporting resources.

## Class Descriptions

### Book.java
Represents a book with an ID, title, author, and status.
Uses encapsulation through private fields and getter
and setter methods.

### LibraryManager.java
Manages the book collection using ArrayList.
Implements CRUD operations, status management,
and input validation.

### LibraryGUI.java
Provides the Java Swing interface using JFrame,
JTable, JTextField, JComboBox, and JButton.

### Main.java
Contains the main method and launches the GUI
on the Swing Event Dispatch Thread.

## How to Run

### Requirements

- JDK 17 or newer
- Visual Studio Code or another Java-compatible IDE

### Compilation

Open the terminal in the project root:

```bash
javac -d out src/*.java
```

### Execution

```bash
java -cp out Main
```

## Generate Javadoc

```bash
javadoc -author -d docs -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 src/*.java
```

Open `docs/index.html` in a browser to view
the generated API documentation.

## Testing

The application supports testing of:

1. Adding valid books
2. Rejecting empty book titles
3. Rejecting empty author names
4. Preventing duplicate IDs
5. Rejecting non-numeric book IDs
6. Viewing books and empty-library messages
7. Updating existing books
8. Handling updates to nonexistent books
9. Deleting existing books
10. Changing book availability status

## Data Structure

The system uses ArrayList<Book> for in-memory storage.

Book lookup is performed using linear search.

Time complexity:
- Book lookup: O(n)
- Duplicate ID checking: O(n)
- Adding a book: O(n), including duplicate validation
- Updating a book: O(n)
- Deleting a book: O(n)

## Limitations

Book records are stored in memory and do not persist
after the application is closed.

The application does not require a database or
external Java libraries.

## Version Control

Git is used to track project development through
multiple meaningful commits.

The complete project source code and generated
Javadoc documentation are maintained on GitHub.

## Authors

Tayyab Zia and Muraleen Ahmad

Software Construction — Open-Ended Lab
```
