
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class ExportDialog extends JDialog {

    private JComboBox<String> formatCombo;
    private JSlider volumeSlider;
    private JCheckBox normalizeCheck;
    private JCheckBox fadeInCheck;
    private JCheckBox fadeOutCheck;
    private JSpinner fadeInSpinner;
    private JSpinner fadeOutSpinner;
    private JButton okButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private ExportSettings exportSettings;

    public ExportDialog(JFrame parent) {
        super(parent, "Export Settings", true);
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        formatCombo = new JComboBox<>(new String[]{"WAV", "AIFF"});

        volumeSlider = new JSlider(0, 200, 100);
        volumeSlider.setMajorTickSpacing(50);
        volumeSlider.setMinorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        normalizeCheck = new JCheckBox("Normalize Audio");
        fadeInCheck = new JCheckBox("Fade In");
        fadeOutCheck = new JCheckBox("Fade Out");

        fadeInSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));
        fadeOutSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));

        fadeInSpinner.setEnabled(false);
        fadeOutSpinner.setEnabled(false);

        okButton = new JButton("Export");
        cancelButton = new JButton("Cancel");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Format:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(formatCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Volume:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainPanel.add(volumeSlider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        mainPanel.add(normalizeCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        mainPanel.add(fadeInCheck, gbc);

        gbc.gridx = 1;
        mainPanel.add(new JLabel("Duration (s):"), gbc);

        gbc.gridx = 2;
        mainPanel.add(fadeInSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(fadeOutCheck, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(new JLabel("Duration (s):"), gbc);

        gbc.gridx = 2;
        mainPanel.add(fadeOutSpinner, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        fadeInCheck.addActionListener(e -> fadeInSpinner.setEnabled(fadeInCheck.isSelected()));
        fadeOutCheck.addActionListener(e -> fadeOutSpinner.setEnabled(fadeOutCheck.isSelected()));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                exportSettings = createExportSettings();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private void configureDialog() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private ExportSettings createExportSettings() {
        ExportSettings settings = new ExportSettings();
        settings.setFormat(formatCombo.getSelectedItem().toString().toLowerCase());
        settings.setVolumeAdjustment(volumeSlider.getValue() / 100.0f);
        settings.setNormalizeAudio(normalizeCheck.isSelected());
        settings.setFadeIn(fadeInCheck.isSelected());
        settings.setFadeOut(fadeOutCheck.isSelected());
        settings.setFadeInDuration(((Double) fadeInSpinner.getValue()).floatValue());
        settings.setFadeOutDuration(((Double) fadeOutSpinner.getValue()).floatValue());
        return settings;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public ExportSettings getExportSettings() {
        return exportSettings;
    }
}