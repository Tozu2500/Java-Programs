
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class StatusBar extends JPanel {

    private JLabel statusLabel;
    private JLabel timeLabel;
    private JProgressBar progressBar;
    private Timer timeUpdateTimer;
    private SimpleDateFormat timeFormat;

    public StatusBar() {
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        initializeComponents();
        setupLayout();
        startTimeUpdate();
    }

    private void initializeComponents() {
        statusLabel = new JLabel("Ready");
        timeLabel = new JLabel(timeFormat.format(new Date()));
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());
        setPreferredSize(new Dimension(0, 25));

        add(statusLabel, BorderLayout.WEST);
        add(progressBar, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.EAST);
    }

    private void startTimeUpdate() {
        timeUpdateTimer = new Timer(1000, e -> timeLabel.setText(timeFormat.format(new Date())));
        timeUpdateTimer.start();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void showProgress(boolean show) {
        progressBar.setVisible(show);
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    public void cleanup() {
        if (timeUpdateTimer != null) {
            timeUpdateTimer.stop();
        }
    }
}