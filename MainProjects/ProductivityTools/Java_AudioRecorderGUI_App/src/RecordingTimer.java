
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RecordingTimer extends JPanel {

    private JLabel timeLabel;
    private Timer timer;
    private long startTime;
    private boolean isRunning = false;

    public RecordingTimer() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        timeLabel = new JLabel("00:00");
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        timeLabel.setForeground(Color.RED);
    }

    private void setupLayout() {
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createTitledBorder("Recording Time"));
        add(timeLabel);
    }

    public void startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;

            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTime();
                }
            });
            timer.start();
        }
    }

    public void stopTimer() {
        if (isRunning) {
            isRunning = false;
            if (timer != null) {
                timer.stop();
            }
        }
    }

    public void reset() {
        stopTimer();
        timeLabel.setText("00:00");
        timeLabel.setForeground(Color.RED);
    }

    public void updateTime() {
        if (isRunning) {
            long elapsed = System.currentTimeMillis() - startTime;
            int minutes = (int) (elapsed / 60000);
            int seconds = (int) ((elapsed & 60000) / 1000);

            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }
    }

    public long getElapsedTime() {
        return isRunning ? System.currentTimeMillis() - startTime : 0;
    }

}
