
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class VolumeMonitor extends JPanel {

    private WaveformDisplay waveformDisplay;
    private JProgressBar volumeBar;
    private JLabel volumeLabel;
    private Timer updateTimer;
    private AudioEngine audioEngine;

    public VolumeMonitor(WaveformDisplay waveformDisplay) {
        this.waveformDisplay = waveformDisplay;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        volumeBar = new JProgressBar(0, 100);
        volumeBar.setStringPainted(true);
        volumeBar.setString("0%");
        volumeBar.setPreferredSize(new Dimension(200, 20));

        volumeLabel = new JLabel("Input Level:");
    }

    private void setupLayout() {
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createTitledBorder("Audio Levels"));
        add(volumeLabel);
        add(volumeBar);
    }

    public void startMonitoring(AudioEngine engine) {
        this.audioEngine = engine;
        updateTimer = new Timer(100, e -> updateVolumeLevel());
        updateTimer.start();
    }

    public void stopMonitoring() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }

    private void updateVolumeLevel() {
        if (waveformDisplay != null) {
            float amplitude = waveformDisplay.getMaxAmplitde();
            int level = (int) (amplitude * 100);
            volumeBar.setValue(level);
            volumeBar.setString(level + "%");

            if (level > 80) {
                volumeBar.setForeground(Color.RED);
            } else if (level > 50) {
                volumeBar.setForeground(Color.ORANGE);
            } else {
                volumeBar.setForeground(Color.GREEN);
            }
        }
    }
}
