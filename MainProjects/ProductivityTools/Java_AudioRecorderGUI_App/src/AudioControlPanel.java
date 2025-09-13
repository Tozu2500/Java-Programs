
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AudioControlPanel extends JPanel {

    private AudioRecorderGUI parent;
    private JButton recordButton;
    private JButton stopButton;
    private JButton playButton;
    private JButton pauseButton;
    private JSlider volumeSlider;
    private JLabel volumeLabel;
    private JLabel statusLabel;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    public AudioControlPanel(AudioRecorderGUI parent) {
        this.parent = parent;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        recordButton = new JButton("Record");
        stopButton = new JButton("Stop");
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        volumeSlider = new JSlider(0, 100, 100);
        volumeLabel = new JLabel("Volume: 100%");
        statusLabel = new JLabel("Ready");

        recordButton.setPreferredSize(new Dimension(80, 30));
        stopButton.setPreferredSize(new Dimension(80, 30));
        playButton.setPreferredSize(new Dimension(80, 30));
        pauseButton.setPreferredSize(new Dimension(80, 30));

        recordButton.setBackground(new Color(220, 53, 69));
        recordButton.setForeground(Color.WHITE);
        recordButton.setFocusPainted(false);
        recordButton.setBorderPainted(false);

        playButton.setBackground(new Color(40, 167, 69));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);

        stopButton.setBackground(new Color(108, 117, 125));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setBorderPainted(false);

        pauseButton.setBackground(new Color(255, 193, 7));
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFocusPainted(false);
        pauseButton.setBorderPainted(false);

        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(BorderFactory.createTitledBorder("Controls"));

        add(recordButton);
        add(stopButton);
        add(playButton);
        add(pauseButton);

        add(Box.createHorizontalStrut(20));

        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);

        add(volumePanel);

        add(Box.createHorizontalStrut(20));
        add(statusLabel);
    }

    private void setupEventHandlers() {
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRecording) {
                    parent.startRecording();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRecording) {
                    parent.stopRecording();
                } else if (isPlaying) {
                    parent.stopPlayback();
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPlaying) {
                    parent.playRecording();    
                } else if (isPaused) {
                    parent.resumePlayback();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && !isPaused) {
                    parent.pausePlayback();
                }
            }
        });

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volumeSlider.getValue();
                float volume = value / 100.0f;
                parent.setVolume(volume);
                volumeLabel.setText("Volume: " + value + "%");
            }
        });
    }

    public void setRecordingState(boolean recording) {
        this.isRecording = recording;
        recordButton.setEnabled(!recording);
        stopButton.setEnabled(recording || isPlaying);
        playButton.setEnabled(!recording);

        if (recording) {
            recordButton.setBackground(new Color(169, 169, 169));
            statusLabel.setText("Recording...");
        } else {
            recordButton.setBackground(new Color(220, 53, 69));
            statusLabel.setText("Ready");
        }
    }

    public void setPlayingState(boolean playing) {
        this.isPlaying = playing;
        playButton.setEnabled(!isRecording);
        stopButton.setEnabled(playing || isRecording);
        pauseButton.setEnabled(playing);
        recordButton.setEnabled(!playing);

        if (playing) {
            playButton.setText("Resume");
            playButton.setBackground(new Color(23, 162, 184));
            statusLabel.setText("Playing...");
        } else {
            playButton.setText("Play");
            playButton.setBackground(new Color(40, 167, 69));
            statusLabel.setText("Ready");
        }
    }

    public void setPausedState(boolean paused) {
        this.isPaused = paused;

        if (paused) {
            playButton.setText("Resume");
            statusLabel.setText("Paused");
        } else {
            playButton.setText("Pause");
            statusLabel.setText("Playing...");
        }
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public int getVolumeValue() {
        return volumeSlider.getValue();
    }

    public void setVolumeValue(int value) {
        volumeSlider.setValue(value);
        volumeLabel.setText("Volume " + value + "%");
    }

}
