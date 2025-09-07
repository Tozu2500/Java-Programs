
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TaskPlannerMainWindow extends JFrame implements TaskManagerListener {

    private TaskManager taskManager;
    private TaskTableModel tableModel;
    private JTable taskTable;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JTextArea statisticsArea;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton markCompleteButton;
    private JButton markInProgressButton;
    private JButton clearCompletedButton;
    private JButton exportButton;
    private JButton refreshButton;

    public TaskPlannerMainWindow() {
        taskManager = new TaskManager();
        taskManager.addListener(this);

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupTable();
        updateDisplay();

        setTitle("Task Priority Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        tableModel = new TaskTableModel();
        taskTable = new JTable(tableModel);

        searchField = new JTextField(15);
        searchField.setToolTipText("Search tasks by title, description, or notes");

        String[] filterOptions = {
            "All Tasks", "Pending", "In Progress", "Completed", "Overdue",
            "Due Today", "Due This Week", "High Priority", "Medium Priority", "Low Priority"
        };
        filterCombo = new JComboBox<>(filterOptions);

        statusLabel = new JLabel("Ready");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        statisticsArea = new JTextArea(8, 20);
        statisticsArea.setEditable(false);
        statisticsArea.setBackground(getBackground());
        statisticsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        addButton = new JButton("Add Task");
        editButton = new JButton("Edit Task");
        deleteButton = new JButton("Delete Task");
        markCompleteButton = new JButton("Mark Complete");
        markInProgressButton = new JButton("Mark In Progress");
        clearCompletedButton = new JButton("Clear Completed");
        exportButton = new JButton("Export CSV");
        refreshButton = new JButton("Refresh");

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        markCompleteButton.setEnabled(false);
        markInProgressButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterCombo);
        searchPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(markCompleteButton);
        buttonPanel.add(markInProgressButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(clearCompletedButton);
        buttonPanel.add(exportButton);

        topPanel.add(buttonPanel, BorderLayout.CENTER);

        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 400));

        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new TitledBorder("Progress"));
        progressPanel.add(progressBar, BorderLayout.NORTH);

        JScrollPane statisticsScrollPane = new JScrollPane(statisticsArea);
        statisticsScrollPane.setBorder(new TitledBorder("Statistics"));
        statisticsScrollPane.setPreferredSize(new Dimension(250, 300));

        rightPanel.add(progressPanel, BorderLayout.NORTH);
        rightPanel.add(statisticsScrollPane, BorderLayout.CENTER);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, rightPanel);
        mainSplitPane.setDividerLocation(700);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> showAddTaskDialog());
        editButton.addActionListener(e -> showEditTaskDialog());
        deleteButton.addActionListener(e -> deleteSelectedTasks());
        markCompleteButton.addActionListener(e -> markSelectedTaskComplete());
        markInProgressButton.addActionListener(e -> markSelectedTaskInProgress());
        clearCompletedButton.addActionListener(e -> clearCompletedTasks());
        exportButton.addActionListener(e -> exportTasks());
        refreshButton.addActionListener(e -> updateDisplay());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        filterCombo.addActionListener(e -> applyFilter());

        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });

        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showEditTaskDialog();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void setupTable() {
        taskTable.setDefaultRenderer(Object.class, new TaskTableCellRenderer());
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setRowHeight(25);

        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        taskTable.getColumnModel().getColumn(6).setPreferredWidth(60);

        taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void updateButtonStates() {
        boolean hasSelection = taskTable.getSelectedRow() >= 0;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        markCompleteButton.setEnabled(hasSelection);
        markInProgressButton.setEnabled(hasSelection);
    }

    private void showAddTaskDialog() {
        TaskDialog dialog = new TaskDialog(this, "Add New Task");
        dialog.setVisible(true);

        if (dialog.wasSaved()) {
            Task newTask = dialog.getTask();
            taskManager.addTask(newTask);
            statusLabel.setText("Task added successfully");
        }
    }

    private void showEditTaskDialog() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task selectedTask = tableModel.getTaskAt(selectedRow);
            if (selectedTask != null) {
                TaskDialog dialog = new TaskDialog(this, "Edit Task", selectedTask);
                dialog.setVisible(true);

                if (dialog.wasSaved()) {
                    taskManager.updateTask(dialog.getTask());
                    statusLabel.setText("Task updated successfully");
                }
            }
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task selectedTask = tableModel.getTaskAt(selectedRow);
            if (selectedTask != null) {
                int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this task?\n" + selectedTask.getTitle(),
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );

                if (result == JOptionPane.YES_OPTION) {
                    taskManager.removeTask(selectedTask);
                    statusLabel.setText("Task deleted successfully");
                }
            }
        }
    }

    private void markSelectedTaskComplete() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task selectedTask = tableModel.getTaskAt(selectedRow);
            if (selectedTask != null) {
                taskManager.markTaskCompleted(selectedTask.getId());
                statusLabel.setText("Task marked as completed");
            }
        }
    }

    private void markSelectedTaskInProgress() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            Task selectedTask = tableModel.getTaskAt(selectedRow);
            if (selectedTask != null) {
                taskManager.markTaskInProgress(selectedTask.getId());
                statusLabel.setText("Task marked as in progress");
            }
        }
    }

    private void clearCompletedTasks() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear all completed tasks?",
            "Confirm Clear Completed",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            taskManager.clearCompletedTasks();
            statusLabel.setText("Completed tasks cleared");
        }
    }

    private void exportTasks() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("tasks.csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                taskManager.exportTasks(fileChooser.getSelectedFile().getAbsolutePath());
                statusLabel.setText("Tasks exported successfully");
                JOptionPane.showMessageDialog(this, "Tasks exported successfully to " + fileChooser.getSelectedFile().getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting tasks: " + e.getMessage(), "Exporting error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            applyFilter();
        } else {
            List<Task> searchResults = taskManager.searchTasks(searchText);
            tableModel.setTasks(searchResults);
            statusLabel.setText("Found " + searchResults.size() + " tasks");
        }
    }

    private void applyFilter() {
        String selectedFilter = (String) filterCombo.getSelectedItem();
        List<Task> filteredTasks;

        switch (selectedFilter) {
            case "Pending":
                filteredTasks = taskManager.getTasksByStatus(Status.PENDING);
                break;
            case "In Progress":
                filteredTasks = taskManager.getTasksByStatus(Status.IN_PROGRESS);
                break;
            case "Completed":
                filteredTasks = taskManager.getTasksByStatus(Status.COMPLETED);
                break;
            case "Overdue":
                filteredTasks = taskManager.getOverdueTasks();
                break;
            case "Due Today":
                filteredTasks = taskManager.getTasksDueToday();
                break;
            case "Due This Week":
                filteredTasks = taskManager.getTasksDueThisWeek();
                break;
            case "High Priority":
                filteredTasks = taskManager.getTasksByPriority(Priority.HIGH);
                break;
            case "Medium Priority":
                filteredTasks = taskManager.getTasksByPriority(Priority.MEDIUM);
                break;
            case "Low Priority":
                filteredTasks = taskManager.getTasksByPriority(Priority.LOW);
                break;
            default:
                filteredTasks = taskManager.getSortedTasks();
                break;
        }

        tableModel.setTasks(filteredTasks);
        statusLabel.setText("Showing " + filteredTasks.size() + " tasks");
    }

    private void updateStatistics() {
        TaskStatistics stats = taskManager.getStatistics();
        statisticsArea.setText(stats.getSummary());

        double completionPercentage = stats.getCompletionPercentage();
        progressBar.setValue((int) completionPercentage);
        progressBar.setString(String.format("%.1f%% Complete", completionPercentage));
    }

    private void updateDisplay() {
        applyFilter();
        updateStatistics();
        updateButtonStates();
    }

    @Override
    public void onTasksChanged() {
        SwingUtilities.invokeLater(() -> updateDisplay());
    }

}