import java.awt.*;
import java.util.List;
import javax.sound.sampled.Mixer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsPanel extends JPanel {
    private AudioRecorderGUI parent;
    private JComboBox<String> sampleRateCombo;
    private JComboBox<String> bitDepthCombo;
    private JComboBox<String> channelsCombo;
    private JComboBox<String> inputDeviceCombo;
    private JComboBox<String> outputDeviceCombo;
    private JSlider bufferSizeSlider;
    private JSlider gainSlider;
    private JCheckBox noiseReductionCheck;
    private JCheckBox autoGainControlCheck;
    private JLabel qualityLabel;
    private JLabel fileSizeLabel;
    private JLabel bufferSizeLabel;
    private JLabel gainLabel;
    private JButton applyButton;
    private JButton resetButton;
    private AudioSettings currentSettings;
    private List<Mixer.Info> inputDevices;
    private List<Mixer.Info> outputDevices;
    
    public SettingsPanel(AudioRecorderGUI parent) {
        this.parent = parent;
        this.currentSettings = new AudioSettings();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDevices();
        updateLabels();
    }
    
    private void initializeComponents() {
        sampleRateCombo = new JComboBox<>(new String[]{
            "8000 Hz", "16000 Hz", "22050 Hz", "44100 Hz", "48000 Hz", "96000 Hz"
        });
        sampleRateCombo.setSelectedIndex(3);
        
        bitDepthCombo = new JComboBox<>(new String[]{
            "8-bit", "16-bit", "24-bit", "32-bit"
        });
        bitDepthCombo.setSelectedIndex(1);
        
        channelsCombo = new JComboBox<>(new String[]{
            "Mono", "Stereo"
        });
        channelsCombo.setSelectedIndex(1);
        
        inputDeviceCombo = new JComboBox<>();
        outputDeviceCombo = new JComboBox<>();
        
        bufferSizeSlider = new JSlider(512, 8192, 4096);
        bufferSizeSlider.setMajorTickSpacing(1024);
        bufferSizeSlider.setMinorTickSpacing(512);
        bufferSizeSlider.setPaintTicks(true);
        bufferSizeSlider.setPaintLabels(false);
        
        gainSlider = new JSlider(0, 1000, 100);
        gainSlider.setMajorTickSpacing(250);
        gainSlider.setMinorTickSpacing(50);
        gainSlider.setPaintTicks(true);
        gainSlider.setPaintLabels(false);
        
        noiseReductionCheck = new JCheckBox("Enable Noise Reduction");
        autoGainControlCheck = new JCheckBox("Enable Auto Gain Control");
        
        qualityLabel = new JLabel();
        fileSizeLabel = new JLabel();
        bufferSizeLabel = new JLabel("Buffer Size: 4096 bytes");
        gainLabel = new JLabel("Gain: 1.0x");
        
        applyButton = new JButton("Apply Settings");
        resetButton = new JButton("Reset to Defaults");
        
        applyButton.setPreferredSize(new Dimension(120, 30));
        resetButton.setPreferredSize(new Dimension(120, 30));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Sample Rate:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(sampleRateCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Bit Depth:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(bitDepthCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Channels:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(channelsCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Input Device:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainPanel.add(inputDeviceCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Output Device:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainPanel.add(outputDeviceCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Buffer Size:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(bufferSizeSlider, gbc);
        gbc.gridx = 2;
        mainPanel.add(bufferSizeLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Input Gain:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(gainSlider, gbc);
        gbc.gridx = 2;
        mainPanel.add(gainLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        mainPanel.add(noiseReductionCheck, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 8;
        mainPanel.add(autoGainControlCheck, gbc);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Quality Information"));
        infoPanel.add(qualityLabel);
        infoPanel.add(fileSizeLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        sampleRateCombo.addActionListener(e -> updateLabels());
        bitDepthCombo.addActionListener(e -> updateLabels());
        channelsCombo.addActionListener(e -> updateLabels());
        
        bufferSizeSlider.addChangeListener(e -> {
            int value = bufferSizeSlider.getValue();
            bufferSizeLabel.setText("Buffer Size: " + value + " bytes");
        });
        
        gainSlider.addChangeListener(e -> {
            float gain = gainSlider.getValue() / 100.0f;
            gainLabel.setText("Gain: " + String.format("%.1f", gain) + "x");
        });
        
        applyButton.addActionListener(e -> applySettings());
        resetButton.addActionListener(e -> resetSettings());
    }
    
    private void loadDevices() {
        try {
            inputDevices = parent.getAudioEngine().getAvailableInputDevices();
            outputDevices = parent.getAudioEngine().getAvailableOutputDevices();
            
            inputDeviceCombo.removeAllItems();
            inputDeviceCombo.addItem("Default Input Device");
            for (Mixer.Info device : inputDevices) {
                inputDeviceCombo.addItem(device.getName());
            }
            
            outputDeviceCombo.removeAllItems();
            outputDeviceCombo.addItem("Default Output Device");
            for (Mixer.Info device : outputDevices) {
                outputDeviceCombo.addItem(device.getName());
            }
        } catch (Exception e) {
            showError("Failed to load audio devices: " + e.getMessage());
        }
    }
    
    private void updateLabels() {
        AudioSettings tempSettings = createSettingsFromUI();
        qualityLabel.setText("Quality: " + tempSettings.getQualityDescription());
        fileSizeLabel.setText("Estimated size (1 min): " + tempSettings.getFileSizeEstimateString(60));
    }
    
    private AudioSettings createSettingsFromUI() {
        AudioSettings settings = new AudioSettings();
        
        String[] sampleRates = {"8000", "16000", "22050", "44100", "48000", "96000"};
        float sampleRate = Float.parseFloat(sampleRates[sampleRateCombo.getSelectedIndex()]);
        settings.setSampleRate(sampleRate);
        
        int[] bitDepths = {8, 16, 24, 32};
        int bitDepth = bitDepths[bitDepthCombo.getSelectedIndex()];
        settings.setBitDepth(bitDepth);
        
        int channels = channelsCombo.getSelectedIndex() + 1;
        settings.setChannels(channels);
        
        settings.setBufferSize(bufferSizeSlider.getValue());
        settings.setGain(gainSlider.getValue() / 100.0f);
        settings.setNoiseReduction(noiseReductionCheck.isSelected());
        settings.setAutoGainControl(autoGainControlCheck.isSelected());
        
        int inputIndex = inputDeviceCombo.getSelectedIndex();
        if (inputIndex > 0 && inputIndex <= inputDevices.size()) {
            settings.setInputDevice(inputDevices.get(inputIndex - 1));
        }
        
        int outputIndex = outputDeviceCombo.getSelectedIndex();
        if (outputIndex > 0 && outputIndex <= outputDevices.size()) {
            settings.setOutputDevice(outputDevices.get(outputIndex - 1));
        }
        
        return settings;
    }
    
    private void applySettings() {
        try {
            currentSettings = createSettingsFromUI();
            JOptionPane.showMessageDialog(this, 
                "Settings applied successfully!\nNew recordings will use these settings.", 
                "Settings Applied", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showError("Failed to apply settings: " + e.getMessage());
        }
    }
    
    private void resetSettings() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Reset all settings to default values?", 
            "Reset Settings", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            currentSettings = new AudioSettings();
            updateUIFromSettings(currentSettings);
            updateLabels();
        }
    }
    
    private void updateUIFromSettings(AudioSettings settings) {
        float sampleRate = settings.getSampleRate();
        if (sampleRate == 8000) sampleRateCombo.setSelectedIndex(0);
        else if (sampleRate == 16000) sampleRateCombo.setSelectedIndex(1);
        else if (sampleRate == 22050) sampleRateCombo.setSelectedIndex(2);
        else if (sampleRate == 44100) sampleRateCombo.setSelectedIndex(3);
        else if (sampleRate == 48000) sampleRateCombo.setSelectedIndex(4);
        else if (sampleRate == 96000) sampleRateCombo.setSelectedIndex(5);
        
        int bitDepth = settings.getBitDepth();
        if (bitDepth == 8) bitDepthCombo.setSelectedIndex(0);
        else if (bitDepth == 16) bitDepthCombo.setSelectedIndex(1);
        else if (bitDepth == 24) bitDepthCombo.setSelectedIndex(2);
        else if (bitDepth == 32) bitDepthCombo.setSelectedIndex(3);
        
        channelsCombo.setSelectedIndex(settings.getChannels() - 1);
        bufferSizeSlider.setValue(settings.getBufferSize());
        gainSlider.setValue((int) (settings.getGain() * 100));
        noiseReductionCheck.setSelected(settings.isNoiseReduction());
        autoGainControlCheck.setSelected(settings.isAutoGainControl());
        
        bufferSizeLabel.setText("Buffer Size: " + settings.getBufferSize() + " bytes");
        gainLabel.setText("Gain: " + String.format("%.1f", settings.getGain()) + "x");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public AudioSettings getAudioSettings() {
        return currentSettings.copy();
    }
    
    public void setAudioSettings(AudioSettings settings) {
        this.currentSettings = settings.copy();
        updateUIFromSettings(settings);
        updateLabels();
    }
    
    public void refreshDevices() {
        loadDevices();
    }
}