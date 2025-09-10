import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AudioRecorderGUI extends JFrame {
    private AudioEngine audioEngine;
    private AudioControlPanel controlPanel;
    private WaveformDisplay waveformDisplay;
    private FileManager fileManager;
    private SettingsPanel settingsPanel;
    private StatusBar statusBar;
    private RecordingTimer recordingTimer;
    private VolumeMonitor volumeMonitor;
    private AudioFileList audioFileList;
    
    private JTabbedPane tabbedPane;
    private JPanel mainPanel;
    private JPanel recordingPanel;
    private JPanel playbackPanel;
    private JPanel settingsTabPanel;
    
    private boolean isRecording = false;
    private boolean isPlaying = false;
    
    public AudioRecorderGUI() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureWindow();
        startVolumeMonitoring();
    }
    
    private void initializeComponents() {
        audioEngine = new AudioEngine();
        fileManager = new FileManager();
        controlPanel = new AudioControlPanel(this);
        waveformDisplay = new WaveformDisplay();
        settingsPanel = new SettingsPanel(this);
        statusBar = new StatusBar();
        recordingTimer = new RecordingTimer();
        volumeMonitor = new VolumeMonitor(waveformDisplay);
        audioFileList = new AudioFileList(this);
        
        tabbedPane = new JTabbedPane();
        mainPanel = new JPanel(new BorderLayout());
        recordingPanel = new JPanel(new BorderLayout());
        playbackPanel = new JPanel(new BorderLayout());
        settingsTabPanel = new JPanel(new BorderLayout());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(recordingTimer, BorderLayout.EAST);
        
        recordingPanel.add(topPanel, BorderLayout.NORTH);
        recordingPanel.add(waveformDisplay, BorderLayout.CENTER);
        recordingPanel.add(volumeMonitor, BorderLayout.SOUTH);
        
        playbackPanel.add(audioFileList, BorderLayout.CENTER);
        
        settingsTabPanel.add(settingsPanel, BorderLayout.CENTER);
        
        tabbedPane.addTab("Recording", recordingPanel);
        tabbedPane.addTab("Playback", playbackPanel);
        tabbedPane.addTab("Settings", settingsTabPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem newRecording = new JMenuItem("New Recording");
        JMenuItem openFile = new JMenuItem("Open Audio File");
        JMenuItem saveRecording = new JMenuItem("Save Recording");
        JMenuItem exportRecording = new JMenuItem("Export Recording");
        JMenuItem exit = new JMenuItem("Exit");
        
        newRecording.addActionListener(e -> newRecording());
        openFile.addActionListener(e -> openAudioFile());
        saveRecording.addActionListener(e -> saveRecording());
        exportRecording.addActionListener(e -> exportRecording());
        exit.addActionListener(e -> exitApplication());
        
        fileMenu.add(newRecording);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveRecording);
        fileMenu.add(exportRecording);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        
        JMenu editMenu = new JMenu("Edit");
        JMenuItem clearRecording = new JMenuItem("Clear Recording");
        JMenuItem refreshFileList = new JMenuItem("Refresh File List");
        
        clearRecording.addActionListener(e -> clearRecording());
        refreshFileList.addActionListener(e -> refreshFileList());
        
        editMenu.add(clearRecording);
        editMenu.add(refreshFileList);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> showAbout());
        helpMenu.add(about);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void setupEventHandlers() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        audioEngine.setStatusListener(status -> SwingUtilities.invokeLater(() -> {
            statusBar.setStatus(status);
        }));
        
        audioEngine.setWaveformListener(data -> SwingUtilities.invokeLater(() -> {
            waveformDisplay.updateWaveform(data);
        }));
    }
    
    private void configureWindow() {
        setTitle("Audio Recorder Professional");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }
    
    private void startVolumeMonitoring() {
        volumeMonitor.startMonitoring(audioEngine);
    }
    
    public void startRecording() {
        if (!isRecording) {
            try {
                AudioSettings settings = settingsPanel.getAudioSettings();
                audioEngine.startRecording(settings);
                isRecording = true;
                recordingTimer.startTimer();
                controlPanel.setRecordingState(true);
                statusBar.setStatus("Recording...");
                waveformDisplay.startRecording();
            } catch (Exception e) {
                showError("Failed to start recording: " + e.getMessage());
            }
        }
    }
    
    public void stopRecording() {
        if (isRecording) {
            try {
                audioEngine.stopRecording();
                isRecording = false;
                recordingTimer.stopTimer();
                controlPanel.setRecordingState(false);
                statusBar.setStatus("Recording stopped");
                waveformDisplay.stopRecording();
            } catch (Exception e) {
                showError("Failed to stop recording: " + e.getMessage());
            }
        }
    }
    
    public void playRecording() {
        if (!isPlaying && audioEngine.hasRecording()) {
            try {
                audioEngine.playRecording();
                isPlaying = true;
                controlPanel.setPlayingState(true);
                statusBar.setStatus("Playing...");
            } catch (Exception e) {
                showError("Failed to play recording: " + e.getMessage());
            }
        }
    }
    
    public void stopPlayback() {
        if (isPlaying) {
            try {
                audioEngine.stopPlayback();
                isPlaying = false;
                controlPanel.setPlayingState(false);
                statusBar.setStatus("Playback stopped");
            } catch (Exception e) {
                showError("Failed to stop playback: " + e.getMessage());
            }
        }
    }
    
    public void pausePlayback() {
        if (isPlaying) {
            try {
                audioEngine.pausePlayback();
                controlPanel.setPausedState(true);
                statusBar.setStatus("Paused");
            } catch (Exception e) {
                showError("Failed to pause playback: " + e.getMessage());
            }
        }
    }
    
    public void resumePlayback() {
        if (isPlaying) {
            try {
                audioEngine.resumePlayback();
                controlPanel.setPausedState(false);
                statusBar.setStatus("Playing...");
            } catch (Exception e) {
                showError("Failed to resume playback: " + e.getMessage());
            }
        }
    }
    
    public void setVolume(float volume) {
        audioEngine.setVolume(volume);
        statusBar.setStatus("Volume: " + Math.round(volume * 100) + "%");
    }
    
    private void newRecording() {
        if (isRecording) {
            stopRecording();
        }
        if (isPlaying) {
            stopPlayback();
        }
        audioEngine.clearRecording();
        waveformDisplay.clear();
        recordingTimer.reset();
        statusBar.setStatus("Ready for new recording");
    }
    
    private void openAudioFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Audio Files", "wav", "mp3", "aiff"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                audioEngine.loadAudioFile(selectedFile);
                statusBar.setStatus("Loaded: " + selectedFile.getName());
                audioFileList.refreshList();
            } catch (Exception e) {
                showError("Failed to load audio file: " + e.getMessage());
            }
        }
    }
    
    private void saveRecording() {
        if (!audioEngine.hasRecording()) {
            showError("No recording to save");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
        fileChooser.setSelectedFile(new File("recording.wav"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            try {
                fileManager.saveRecording(audioEngine.getRecordingData(), saveFile);
                statusBar.setStatus("Recording saved: " + saveFile.getName());
                audioFileList.refreshList();
            } catch (Exception e) {
                showError("Failed to save recording: " + e.getMessage());
            }
        }
    }
    
    private void exportRecording() {
        if (!audioEngine.hasRecording()) {
            showError("No recording to export");
            return;
        }
        
        ExportDialog exportDialog = new ExportDialog(this);
        exportDialog.setVisible(true);
        
        if (exportDialog.isConfirmed()) {
            ExportSettings exportSettings = exportDialog.getExportSettings();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("export." + exportSettings.getFormat()));
            
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File exportFile = fileChooser.getSelectedFile();
                try {
                    fileManager.exportRecording(audioEngine.getRecordingData(), exportFile, exportSettings);
                    statusBar.setStatus("Recording exported: " + exportFile.getName());
                } catch (Exception e) {
                    showError("Failed to export recording: " + e.getMessage());
                }
            }
        }
    }
    
    private void clearRecording() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear the current recording?", 
            "Clear Recording", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            newRecording();
        }
    }
    
    private void refreshFileList() {
        audioFileList.refreshList();
        statusBar.setStatus("File list refreshed");
    }
    
    private void showAbout() {
        String aboutText = "Audio Recorder Professional\n\n" +
                          "A comprehensive audio recording application\n" +
                          "Features:\n" +
                          "- High quality audio recording\n" +
                          "- Waveform visualization\n" +
                          "- File management\n" +
                          "- Export capabilities\n\n" +
                          "Built with Java Swing";
        
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitApplication() {
        if (isRecording) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Recording is in progress. Stop recording and exit?", 
                "Exit Application", 
                JOptionPane.YES_NO_OPTION);
            
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
            stopRecording();
        }
        
        if (isPlaying) {
            stopPlayback();
        }
        
        audioEngine.cleanup();
        volumeMonitor.stopMonitoring();
        dispose();
        System.exit(0);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public AudioEngine getAudioEngine() {
        return audioEngine;
    }
    
    public FileManager getFileManager() {
        return fileManager;
    }
    
    public boolean isRecording() {
        return isRecording;
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
}