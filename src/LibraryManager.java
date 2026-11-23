import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the library collection and permanent storage.
 *
 * <p>Provides CRUD operations, input validation,
 * and automatic file persistence.</p>
 *
 * @author Tayyab Zia
 * @author Muraleen Ahmad
 */
public class LibraryManager {

  private final ArrayList<Book> books;
  private final BookStorage storage;

  /**
   * Initializes the library and loads previously saved books.
   *
   * @throws UncheckedIOException if stored books cannot be loaded
   */
  public LibraryManager() {
    storage = new BookStorage();

    try {
      books = new ArrayList<>(storage.loadBooks());
    } catch (IOException ex) {
      throw new UncheckedIOException("Unable to load saved library data.", ex);
    }
  }

  /**
   * Saves a proposed collection before accepting changes.
   *
   * @param updatedBooks proposed book collection
   * @throws UncheckedIOException if saving fails
   */
  private void saveChanges(List<Book> updatedBooks) {
    try {
      storage.saveBooks(updatedBooks);

      books.clear();
      books.addAll(updatedBooks);
    } catch (IOException ex) {
      throw new UncheckedIOException("Unable to save library data.", ex);
    }
  }

  /**
   * Adds a new book and saves it permanently.
   *
   * @param id unique book identifier
   * @param title book title
   * @param author book author
   * @return true when successfully added
   * @throws IllegalArgumentException for invalid details
   * @throws UncheckedIOException if saving fails
   */
  public boolean addBook(int id, String title, String author) {
    validateBook(id, title, author);

    if (findBookById(id) != null) {
      throw new IllegalArgumentException("A book with this ID already exists.");
    }

    if (title.trim().matches("\\d+")) {
      throw new IllegalArgumentException(
        "Book title cannot contain only numbers."
      );
    }

    List<Book> updatedBooks = new ArrayList<>(books);

    updatedBooks.add(new Book(id, title.trim(), author.trim()));

    saveChanges(updatedBooks);
    return true;
  }

  /**
   * Returns a copy of the book collection.
   *
   * @return list of library books
   */
  public List<Book> getAllBooks() {
    return new ArrayList<>(books);
  }

  /**
   * Finds a book using its ID.
   *
   * @param id unique book identifier
   * @return matching book, or null if not found
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
   * Updates an existing book and saves the changes.
   *
   * @param id book identifier
   * @param title updated title
   * @param author updated author
   * @return true if updated, false if not found
   * @throws IllegalArgumentException for invalid details
   * @throws UncheckedIOException if saving fails
   */
  public boolean updateBook(int id, String title, String author) {
    validateBook(id, title, author);

    Book existing = findBookById(id);

    if (existing == null) {
      return false;
    }

    List<Book> updatedBooks = new ArrayList<>(books);

    for (int i = 0; i < updatedBooks.size(); i++) {
      Book book = updatedBooks.get(i);

      if (book.getId() == id) {
        Book replacement = new Book(id, title.trim(), author.trim());

        replacement.setStatus(existing.getStatus());

        updatedBooks.set(i, replacement);
        break;
      }
    }

    saveChanges(updatedBooks);
    return true;
  }

  /**
   * Deletes a book and updates permanent storage.
   *
   * @param id book identifier
   * @return true if deleted, false if not found
   * @throws UncheckedIOException if saving fails
   */
  public boolean deleteBook(int id) {
    if (findBookById(id) == null) {
      return false;
    }

    List<Book> updatedBooks = new ArrayList<>(books);

    updatedBooks.removeIf(book -> book.getId() == id);

    saveChanges(updatedBooks);
    return true;
  }

  /**
   * Updates the status of an existing book.
   *
   * @param id book identifier
   * @param status Available, Issued, or Reserved
   * @return true if updated, false if not found
   * @throws IllegalArgumentException for invalid status
   * @throws UncheckedIOException if saving fails
   */
  public boolean updateBookStatus(int id, String status) {
    if (
      status == null ||
      !(status.equals("Available") ||
        status.equals("Issued") ||
        status.equals("Reserved"))
    ) {
      throw new IllegalArgumentException(
        "Status must be Available, Issued, or Reserved."
      );
    }

    Book existing = findBookById(id);

    if (existing == null) {
      return false;
    }

    List<Book> updatedBooks = new ArrayList<>(books);

    for (int i = 0; i < updatedBooks.size(); i++) {
      Book book = updatedBooks.get(i);

      if (book.getId() == id) {
        Book replacement = new Book(
          book.getId(),
          book.getTitle(),
          book.getAuthor()
        );

        replacement.setStatus(status);

        updatedBooks.set(i, replacement);
        break;
      }
    }

    saveChanges(updatedBooks);
    return true;
  }

  /**
   * Validates book details.
   *
   * @param id unique identifier
   * @param title book title
   * @param author book author
   * @throws IllegalArgumentException if details are invalid
   */
  private void validateBook(int id, String title, String author) {
    if (id <= 0) {
      throw new IllegalArgumentException("Book ID must be a positive number.");
    }

    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Book title cannot be empty.");
    }

    if (author == null || author.trim().isEmpty()) {
      throw new IllegalArgumentException("Author name cannot be empty.");
    }
  }
}
