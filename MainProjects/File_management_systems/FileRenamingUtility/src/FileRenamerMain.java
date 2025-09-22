
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileRenamerMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                new MainWindow.setVisible(true);
            }
        });
    }

}
