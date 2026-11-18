
import javax.swing.SwingUtilities;

/**
 * Entry point for the Library Book Management System.
 *
 * @author Tayyab Zia & Mursaleen Ahmad
 */
public class Main {

    /**
     * Launches the application on the Swing event thread.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}
