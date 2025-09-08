
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AudioRecorderMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            AudioRecorderGUI gui = new AudioRecorderGUI();
            gui.setVisible(true);
        });
    }
}
