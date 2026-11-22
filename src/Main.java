
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.io.UncheckedIOException;

/**
 * Entry point for the Library Book Management System.
 *
 * @author Tayyab Zia
 * @author Muraleen Ahmad
 */
public class Main {

    /**
     * Prevents instantiation of the application entry class.
     */
    private Main() {
    }

    /**
     * Launches the application on the Swing event thread.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                LibraryGUI gui = new LibraryGUI();
                gui.setVisible(true);

            } catch (UncheckedIOException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Unable to load saved library data:\n"
                        + ex.getMessage(),
                    "Storage Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
