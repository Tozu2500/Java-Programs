import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Priority> priorityCombo;
    private JComboBox<Category> categoryCombo;
    private JComboBox<Status> statusCombo;
    private JTextField dueDateField;
    private JSpinner estimatedHoursSpinner;
    private JTextArea notesArea;
    private JButton saveButton;
    private JButton cancelButton;
    
    private Task task;
    private boolean isEditing;
    private boolean wasSaved;
    
    public TaskDialog(Frame parent, String title) {
        super(parent, title, true);
        this.isEditing = false;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public TaskDialog(Frame parent, String title, Task task) {
        super(parent, title, true);
        this.task = task;
        this.isEditing = true;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        populateFields();
    }
    
    private void initializeComponents() {
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        priorityCombo = new JComboBox<>(Priority.values());
        categoryCombo = new JComboBox<>(Category.values());
        statusCombo = new JComboBox<>(Status.values());
        
        dueDateField = new JTextField(20);
        dueDateField.setToolTipText("Format: yyyy-MM-dd HH:mm (e.g., 2024-12-25 15:30)");
        
        estimatedHoursSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        wasSaved = false;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(descriptionArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(priorityCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(categoryCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(dueDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Estimated Hours:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(estimatedHoursSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(notesArea), gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(getParent());
    }
    
    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTask();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        getRootPane().setDefaultButton(saveButton);
    }
    
    private void populateFields() {
        if (task != null) {
            titleField.setText(task.getTitle());
            descriptionArea.setText(task.getDescription());
            priorityCombo.setSelectedItem(task.getPriority());
            categoryCombo.setSelectedItem(task.getCategory());
            statusCombo.setSelectedItem(task.getStatus());
            
            if (task.getDueDate() != null) {
                dueDateField.setText(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            
            estimatedHoursSpinner.setValue(task.getEstimatedHours());
            notesArea.setText(task.getNotes());
        }
    }
    
    private void saveTask() {
        if (validateInput()) {
            if (isEditing) {
                updateExistingTask();
            } else {
                createNewTask();
            }
            wasSaved = true;
            dispose();
        }
    }
    
    private boolean validateInput() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return false;
        }
        
        String dueDateText = dueDateField.getText().trim();
        if (!dueDateText.isEmpty()) {
            try {
                LocalDateTime.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd HH:mm", "Validation Error", JOptionPane.ERROR_MESSAGE);
                dueDateField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private void createNewTask() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        Priority priority = (Priority) priorityCombo.getSelectedItem();
        Category category = (Category) categoryCombo.getSelectedItem();
        
        task = new Task(title, description, priority, category);
        updateTaskFromFields();
    }
    
    private void updateExistingTask() {
        task.setTitle(titleField.getText().trim());
        task.setDescription(descriptionArea.getText().trim());
        task.setPriority((Priority) priorityCombo.getSelectedItem());
        task.setCategory((Category) categoryCombo.getSelectedItem());
        updateTaskFromFields();
    }
    
    private void updateTaskFromFields() {
        task.setStatus((Status) statusCombo.getSelectedItem());
        task.setEstimatedHours((Integer) estimatedHoursSpinner.getValue());
        task.setNotes(notesArea.getText().trim());
        
        String dueDateText = dueDateField.getText().trim();
        if (!dueDateText.isEmpty()) {
            try {
                LocalDateTime dueDate = LocalDateTime.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                task.setDueDate(dueDate);
            } catch (DateTimeParseException e) {
                task.setDueDate(null);
            }
        } else {
            task.setDueDate(null);
        }
    }
    
    public Task getTask() {
        return task;
    }
    
    public boolean wasSaved() {
        return wasSaved;
    }
}