
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the library's collection of books.
 * Supports adding, viewing, updating, deleting,
 * and changing the status of books.
 *
 * @author Library Management Team
 */
public class LibraryManager {

    private final ArrayList<Book> books;

    /**
     * Initializes an empty library.
     */
    public LibraryManager() {
        books = new ArrayList<>();
    }

    /**
     * Adds a new book after validating its details.
     *
     * @param id unique book identifier
     * @param title title of the book
     * @param author author of the book
     * @return true if the book was successfully added
     */
    public boolean addBook(int id, String title, String author) {

        validateBook(id, title, author);

        if (findBookById(id) != null) {
            throw new IllegalArgumentException(
                "A book with this ID already exists."
            );
        }

        books.add(new Book(id, title.trim(), author.trim()));
        return true;
    }

    /**
     * Returns all books in the library.
     *
     * @return a copy of the book list
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Finds a book using its unique ID.
     *
     * @param id book identifier
     * @return matching book or null if not found
     */
    public Book findBookById(int id) {

        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }

        return null;
    }

    /**
     * Updates the title and author of an existing book.
     *
     * @param id identifier of the book to update
     * @param title new title
     * @param author new author
     * @return true if updated, false if not found
     */
    public boolean updateBook(int id, String title, String author) {

        validateBook(id, title, author);

        Book book = findBookById(id);

        if (book == null) {
            return false;
        }

        book.setTitle(title.trim());
        book.setAuthor(author.trim());

        return true;
    }

    /**
     * Deletes an existing book.
     *
     * @param id identifier of the book
     * @return true if deleted, false if not found
     */
    public boolean deleteBook(int id) {

        Book book = findBookById(id);

        if (book == null) {
            return false;
        }

        books.remove(book);
        return true;
    }

    /**
     * Changes a book's availability status.
     *
     * @param id identifier of the book
     * @param status Available, Issued, or Reserved
     * @return true if the status was changed
     */
    public boolean updateBookStatus(int id, String status) {

        if (status == null ||
            !(status.equals("Available") ||
              status.equals("Issued") ||
              status.equals("Reserved"))) {

            throw new IllegalArgumentException(
                "Status must be Available, Issued, or Reserved."
            );
        }

        Book book = findBookById(id);

        if (book == null) {
            return false;
        }

        book.setStatus(status);
        return true;
    }

    /**
     * Validates the book ID, title, and author.
     *
     * @param id book identifier
     * @param title book title
     * @param author book author
     * @throws IllegalArgumentException for invalid input
     */
    private void validateBook(int id, String title, String author) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                "Book ID must be a positive number."
            );
        }

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Book title cannot be empty."
            );
        }

        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Author name cannot be empty."
            );
        }
    }
}
