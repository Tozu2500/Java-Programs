package com.tozu.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.concurrent.CompletableFuture;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.tozu.core.FileOrganizer;
import com.tozu.core.OrganizationStats;

// Main GUI window or the application. Provides user friendly interface for file organization operations.
public class FileOrganizerGUI extends JFrame implements FileOrganizer.ProgressCallback {
	
	private static final String title ="Advanced File Organizer - Version 1.0 - Java, by @Tozu";
	
	private JTextField pathField;
	private JButton browseButton;
	private JRadioButton extensionRadio, dateRadio, sizeRadio, typeRadio, customRadio;
	private JCheckBox subdirCheckbox, dryRunCheckbox;
	private JButton organizeButton, cancelButton;
	private JProgressBar progressBar;
	private JLabel statusLabel;
	private JTextArea logArea;
	private JButton settingsButton, rulesButton;
	
	private FileOrganizer organizer;
	private CompletableFuture<OrganizationStats> currentTask;
	
	public FileOrganizerGUI() {
		this.organizer = new FileOrganizer();
		initializeComponents();
		setupLayout();
		setupEventHandlers();
		setupWindow();
	}
	
	private void initializeComponents() {
		pathField = new JTextField(30);
		browseButton = new JButton("Browse..");
		
		extensionRadio = new JRadioButton("By Extension", true);
		dateRadio = new JRadioButton("By Date");
		sizeRadio = new JRadioButton("By Size");
		typeRadio = new JRadioButton("By File Type");
		customRadio = new JRadioButton("Custom Rules");
	
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(extensionRadio);
		modeGroup.add(dateRadio);
		modeGroup.add(sizeRadio);
		modeGroup.add(typeRadio);
		modeGroup.add(customRadio);
		
		subdirCheckbox = new JCheckBox("Include Subdirectories", ConfigManager.shouldProcessSubdirectories());
		dryRunCheckbox = new JCheckBox("Dry Run (Preview Only)", ConfigManager.isDryRun());
		
		organizeButton = new JButton("Start Organizing");
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		
		settingsButton = new JButton("Settings");
		rulesButton = new JButton("Custom Rules");
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);
		statusLabel = new JLabel("Ready to start organizing your files");
		
		logArea = new JTextArea(10, 50);
		logArea.setEditable(false);
		logArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		logArea.setBackground(Color.black);
		logArea.setForeground(Color.green);
	}
	
	private void setupLayout() {
		setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel pathPanel = createPathPanel();
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(createModePanel(), BorderLayout.NORTH);
		centerPanel.add(createOptionsPanel(), BorderLayout.CENTER);
		
		JPanel bottomPanel = createBottomPanel();
		
		JPanel logPanel = createLogPanel();
		
		mainPanel.add(pathPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(logPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createPathPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Source Directory"));
		
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(pathField, BorderLayout.CENTER);
		inputPanel.add(browseButton, BorderLayout.EAST);
		
		panel.add(inputPanel, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createModePanel() {
		JPanel panel = new JPanel(new GridLayout(2, 3, 10, 5));
		panel.setBorder(new TitledBorder("Organization Mode"));
		
		panel.add(extensionRadio);
		panel.add(dateRadio);
		panel.add(sizeRadio);
		panel.add(typeRadio);
		panel.add(customRadio);
		panel.add(new JLabel()); // Empty cell
		
		return panel;
	}
	
	private JPanel createOptionsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBorder(new TitledBorder("Options"));
		
		panel.add(subdirCheckbox);
		panel.add(dryRunCheckbox);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(settingsButton);
		panel.add(rulesButton);
		
		return panel;
	}
	
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(organizeButton);
		buttonPanel.add(cancelButton);
		
		JPanel progressPanel = new JPanel(new BorderLayout());
		progressPanel.add(progressBar, BorderLayout.CENTER);
		progressPanel.add(statusLabel, BorderLayout.SOUTH);
		
		panel.add(buttonPanel, BorderLayout.NORTH);
		panel.add(progressPanel, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createLogPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Activity Log"));
		
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setPreferredSize(new Dimension(0, 200));
		
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}
	
	private void setupEventHandlers() {
		browseButton.addActionListener(e -> browseForDirectory());
		
		organizeButton.addActionListener(e -> startOrganization());
		
		cancelButton.addActionListener(e -> cancelOrganization());
		
		settingsButton.addActionListener(e -> openSettings());
		
		rulesButton.addActionListener(e -> openCustomRules());
		
		// Update config when checkboxes change
		subdirCheckbox.addActionListener(e ->
				ConfigManager.setProcessSubdirectories(subdirCheckbox.isSelected()));
		
		dryRunCheckbox.addActionListener(e ->
				ConfigManager.setDryRun(dryRunCheckbox.isSelected()));
	
		// Enable or disable custom rules button based on selection (if it's on use or not)
		customRadio.addActionListener(e -> updateCustomRulesButton());
		extensionRadio.addActionListener(e -> updateCustomRulesButton());
		dateRadio.addActionListener(e -> updateCustomRulesButton());
		sizeRadio.addActionListener(e -> updateCustomRulesButton());
		typeRadio.addActionListener(e -> updateCustomRulesButton());
	}
	
	private void setupWindow() {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		
		setMinimumSize(new Dimension(600, 500));
		
		// Load initial directory from config
		String lastDir = ConfigManager.getLastDirectory();
		if (lastDir != null && !lastDir.isEmpty()) {
			pathField.setText(lastDir);
		}
	}
	
	private void browseForDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		String currentPath = pathField.getText();
		if (!currentPath.isEmpty()) {
			chooser.setCurrentDirectory(new File(currentPath));
		}
		
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedDir = chooser.getSelectedFile();
			pathField.setText(selectedDir.getAbsolutePath());
			ConfigManager.setLastDirectory(selectedDir.getAbsolutePath());
		}
	}
	
	private void startOrganization() {
		String path = pathField.getText().trim();
		if (path.isEmpty()) {
			showError("Please select a directory that is not empty");
			return;
		}
		
		File sourceDir = new File(path);
		if (!sourceDir.exists() || !sourceDir.isDirectory()) {
			showError("Selected path must be a system file directory, a regular file");
			return;
		}
		
		setOrganizationInProgress(true);
		logArea.setTabSize("");
		appendToLog("Starting file organization...");
		
		if (extensionRadio.isSelected()) {
			currentTask = organizer.organizeByExtension(sourceDir, this);
		} else if (dateRadio.isSelected()) {
			currentTask = organizer.organizeByDate(sourceDir, this);
		} else if (sizeRadio.isSelected()) {
			currentTask = organizer.organizeBySide(sourceDir, this);
		} else if (typeRadio.isSelected()) {
			currentTask = organizer.organizeByFileType(sourceDir, this);
		} else if (customRadio.isSelected()) {
			currentTask = organizer.organizeByCustomRules(sourceDir, this);
		}
		
		// Handle completion
		if (currentTask != null) {
			currentTask.whenComplete((stats, throwable) -> {
				SwingUtilities.invokeLater(() -> {
					setOrganizationInProgress(false);
					if (throwable != null) {
						onError("Organization failed: " + throwable.getMessage());
					}
				});
			});
		}
	}
	
	private void cancelOrganization() {
		if (currentTask != null && !currentTask.isDone()) {
			organizer.cancel();
			currentTask.cancel(true);
			setOrganizationInProgress(false);
			appendToLog("Organization cancelled by user");
			statusLabel.setText("Organization cancelled");
		}
	}
	
	private void openSettings() {
		SettingsDialog dialog = new SettingsDialog(this);
		dialog.setVisible(true);
	}
	
	private void openCustomRules() {
		CustomRulesDialog dialog = new CustomRulesDialog(this, organizer);
		dialog.setVisible(true);
	}
	
	private void updateCustomRulesButton() {
		rulesButton.setEnabled(customRadio.isSelected());
	}
	
	private void setOrganizationInProgress(boolean inProgress) {
		organizeButton.setEnabled(!inProgress);
		cancelButton.setEnabled(inProgress);
		browseButton.setEnabled(!inProgress);
		settingsButton.setEnabled(!inProgress);
		
		if (!inProgress) {
			progressBar.setValue(0);
			progressBar.setString("");
		}
	}
	
	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void showInfo(String message) {
		JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void appendToLog(String message) {
		SwingUtilities.invokeLater(() -> {
			logArea.append(message + "\n");
			logArea.setCaretPosition(logArea.getDocument().getLength());
		});
	}
	
	@Override
	public void onProgress(int current, int total, String currentFile) {
		SwingUtilities.invokeLater(() -> {
			int percent = total > 0 ? (current * 100) / total : 0;
			progressBar.setValue(percent);
			progressBar.setString(percent + "%");
			statusLabel.setText("Processing: " + currentFile + " (" + current  + "/" + total + ")");
		});
	}

	@Override
	public void onComplete(OrganizationStats stats) {
		SwingUtilities.invokeLater(() -> {
			progressBar.setValue(100);
			progressBar.setString("Complete");
			statusLabel.setText("Organization of the files completed!");
			
			String message = String.format(
						"Organization completed!\n\n" +
							"Files processed: %d\n" +
							"Files moved: %d\n" +
							"Errors: %d\n" +
							"Duration: %.2f seconds",
							stats.getTotalFiles(),
							stats.getProcessedFiles(),
							stats.getErrors(),
							stats.getDurationSeconds()
					);
			
			appendToLog(message);
			showInfo(message);
		});
	}

	@Override
	public void onError(String error) {
		SwingUtilities.invokeLater(() -> {
			statusLabel.setText("Error occurred");
			appendToLog("ERROR: " + error);
			showError(error);
		});
	}
	
	@Override
	public void dispose() {
		if (organizer != null) {
			organizer.shutdown();
		}
		ConfigManager.saveConfig();
		super.dispose();
	}
	
}
