
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Graphical interface for the Library Book Management System.
 * Provides controls for managing books using Java Swing.
 *
 * @author Tayyab Zia & Mursaleen Ahmad
 */
public class LibraryGUI extends JFrame {

    private final LibraryManager manager;

    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> statusBox;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    /**
     * Initializes and displays the library GUI.
     */
    public LibraryGUI() {
        manager = new LibraryManager();

        setTitle("Library Book Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        refreshTable();
    }

    /**
     * Creates the form, buttons, and table.
     */
    private void initializeComponents() {

        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
            "Library Book Management System",
            SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBorder(
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        );

        add(heading, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 20, 20, 20)
        );

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        idField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();

        statusBox = new JComboBox<>(
            new String[]{"Available", "Issued", "Reserved"}
        );

        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Book Title:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Book");
        JButton updateButton = new JButton("Update Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton statusButton = new JButton("Set Status");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Author", "Status"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(28);
        bookTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        bookTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Library Books")
        );

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addBook());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        statusButton.addActionListener(e -> updateStatus());
        clearButton.addActionListener(e -> clearFields());

        // Populate fields when a row is selected
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateSelectedBook();
            }
        });
    }

    /**
     * Reads and validates the ID field.
     *
     * @return the entered book ID
     */
    private int readBookId() {
        try {
            return Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Please enter a valid numeric Book ID."
            );
        }
    }

    /**
     * Adds a new book to the library.
     */
    private void addBook() {
        try {
            int id = readBookId();

            manager.addBook(
                id,
                titleField.getText(),
                authorField.getText()
            );

            refreshTable();
            clearFields();

            showMessage("Book added successfully!");

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Updates the title and author of an existing book.
     */
    private void updateBook() {
        try {
            int id = readBookId();

            boolean updated = manager.updateBook(
                id,
                titleField.getText(),
                authorField.getText()
            );

            if (updated) {
                refreshTable();
                clearFields();
                showMessage("Book updated successfully!");
            } else {
                showError("Book not found!");
            }

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Deletes an existing book after user confirmation.
     */
    private void deleteBook() {
        try {
            int id = readBookId();

            if (manager.findBookById(id) == null) {
                showError("Book not found!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this book?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteBook(id);
                refreshTable();
                clearFields();
                showMessage("Book deleted successfully!");
            }

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Changes the status of a book.
     */
    private void updateStatus() {
        try {
            int id = readBookId();

            String status =
                (String) statusBox.getSelectedItem();

            boolean updated =
                manager.updateBookStatus(id, status);

            if (updated) {
                refreshTable();
                clearFields();
                showMessage("Book status updated!");
            } else {
                showError("Book not found!");
            }

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Displays the current book collection in the table.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Book> books = manager.getAllBooks();

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus()
            });
        }

        if (books.isEmpty()) {
            tableModel.addRow(new Object[]{
                "", "No books available", "", ""
            });
        }
    }

    /**
     * Copies the selected book's details into the form.
     */
    private void populateSelectedBook() {

        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        int modelRow =
            bookTable.convertRowIndexToModel(selectedRow);

        Object idValue = tableModel.getValueAt(modelRow, 0);

        if (!(idValue instanceof Integer)) {
            return;
        }

        idField.setText(idValue.toString());
        titleField.setText(
            tableModel.getValueAt(modelRow, 1).toString()
        );
        authorField.setText(
            tableModel.getValueAt(modelRow, 2).toString()
        );
        statusBox.setSelectedItem(
            tableModel.getValueAt(modelRow, 3)
        );
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        statusBox.setSelectedIndex(0);
        bookTable.clearSelection();
    }

    /**
     * Displays a success or information message.
     *
     * @param message text to display
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Displays an error message.
     *
     * @param message error description
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
