
/**
 * Represents a book in the Library Management System.
 *
 * @author Tayyab Zia & Mursaleen Ahmad
 */
public class Book {

    private int id;
    private String title;
    private String author;
    private String status;

    /**
     * Creates a new book with Available status.
     *
     * @param id unique book identifier
     * @param title title of the book
     * @param author author of the book
     */
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = "Available";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
