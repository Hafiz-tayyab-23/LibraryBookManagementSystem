
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * Handles permanent storage of library books using a properties file.
 *
 * <p>Book records are saved to disk and loaded when the application
 * starts. A temporary file is used during saving to reduce the
 * risk of corrupting existing data.</p>
 *
 * @author Tayyab Zia
 * @author Muraleen Ahmad
 */
public class BookStorage {

    private final Path filePath;

    /**
     * Creates storage using the default data directory.
     */
    public BookStorage() {
        this(Paths.get("data", "books.properties"));
    }

    /**
     * Creates storage at a specified file path.
     *
     * @param filePath location of the storage file
     */
    public BookStorage(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException(
                "Storage file path cannot be null."
            );
        }
        this.filePath = filePath;
    }

    /**
     * Loads previously saved books from disk.
     *
     * @return list of stored books, or an empty list
     *         if the storage file does not exist
     * @throws IOException if the file cannot be read
     *         or contains invalid book data
     */
    public List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return books;
        }

        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(filePath)) {
            properties.load(input);
        }

        int count;
        try {
            count = Integer.parseInt(
                properties.getProperty("book.count", "")
            );

            if (count < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            throw new IOException("Invalid book count in storage.", ex);
        }

        for (int i = 0; i < count; i++) {
            String prefix = "book." + i + ".";

            try {
                int id = Integer.parseInt(
                    properties.getProperty(prefix + "id", "")
                );

                String title =
                    properties.getProperty(prefix + "title");

                String author =
                    properties.getProperty(prefix + "author");

                String status =
                    properties.getProperty(prefix + "status");

                if (id <= 0 || title == null ||
                    title.trim().isEmpty() ||
                    author == null || author.trim().isEmpty() ||
                    status == null ||
                    !(status.equals("Available") ||
                      status.equals("Issued") ||
                      status.equals("Reserved"))) {

                    throw new IllegalArgumentException(
                        "Invalid stored book information."
                    );
                }

                for (Book existing : books) {
                    if (existing.getId() == id) {
                        throw new IllegalArgumentException(
                            "Duplicate stored book ID."
                        );
                    }
                }

                Book book = new Book(id, title, author);
                book.setStatus(status);
                books.add(book);

            } catch (IllegalArgumentException ex) {
                throw new IOException(
                    "Invalid stored book at index " + i, ex
                );
            }
        }

        return books;
    }

    /**
     * Saves all book records to disk.
     *
     * <p>The data is written to a temporary file first.
     * The temporary file then replaces the existing
     * storage file.</p>
     *
     * @param books collection of books to save
     * @throws IOException if saving fails
     */
    public void saveBooks(List<Book> books) throws IOException {
        if (books == null) {
            throw new IllegalArgumentException(
                "Book collection cannot be null."
            );
        }

        Path absolutePath = filePath.toAbsolutePath();
        Path directory = absolutePath.getParent();

        Files.createDirectories(directory);

        Properties properties = new Properties();

        properties.setProperty(
            "book.count", String.valueOf(books.size())
        );

        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort(Comparator.comparingInt(Book::getId));

        for (int i = 0; i < sortedBooks.size(); i++) {
            Book book = sortedBooks.get(i);
            String prefix = "book." + i + ".";

            properties.setProperty(
                prefix + "id", String.valueOf(book.getId())
            );
            properties.setProperty(prefix + "title", book.getTitle());
            properties.setProperty(prefix + "author", book.getAuthor());
            properties.setProperty(prefix + "status", book.getStatus());
        }

        Path temporaryFile = Files.createTempFile(
            directory, "books-", ".tmp"
        );

        try {
            try (OutputStream output =
                    Files.newOutputStream(temporaryFile)) {
                properties.store(
                    output, "Library Book Management System"
                );
            }

            try {
                Files.move(
                    temporaryFile,
                    absolutePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
                );
            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                Files.move(
                    temporaryFile,
                    absolutePath,
                    StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
