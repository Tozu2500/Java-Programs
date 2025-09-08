
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TaskPlannerApp {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Task Priority Planner");

        SwingUtilities.invokeLater(() -> {
            setupUIDefaults();
            TaskPlannerMainWindow mainWindow = new TaskPlannerMainWindow();
            mainWindow.setVisible(true);
            createSampleTasks(mainWindow);
        });
    }

    private static void setupUIDefaults() {
        UIManager.put("Table.gridColor", new Color(220, 220, 220));
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.intercellSpacing", new Dimension(1, 1));
        UIManager.put("Table.selectionBackground", new Color(184, 207, 229));
        UIManager.put("Table.selectionForeground", Color.BLACK);

        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put("ProgressBar.selectionForeground", Color.WHITE);
    }

    private static void createSampleTasks(TaskPlannerMainWindow mainWindow) {
        int response = JOptionPane.showConfirmDialog(
            mainWindow,
            "Would you like to create some sample tasks to get started?",
            "Create Sample Tasks",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            TaskManager taskManager = new TaskManager();

            Task task1 = new Task("Complete project proposal", "Write and submit the Q4 project proposal document");
            task1.setPriority(Priority.HIGH);
            task1.setCategory(Category.WORK);
            task1.setEstimatedHours(4);
            task1.setDueDate(LocalDateTime.now().plusDays(2));
            taskManager.addTask(task1);

            Task task2 = new Task("Schedule doctor's appointment", "Annual health checkup appointment");
            task2.setPriority(Priority.MEDIUM);
            task2.setCategory(Category.HEALTH);
            task2.setEstimatedHours(1);
            task2.setDueDate(LocalDateTime.now().plusWeeks(1));
            taskManager.addTask(task2);

            Task task3 = new Task("Learn Java Swing", "Study advanced Swing components and layouts");
            task3.setPriority(Priority.LOW);
            task3.setCategory(Category.EDUCATION);
            task3.setEstimatedHours(10);
            task3.setStatus(Status.IN_PROGRESS);
            taskManager.addTask(task3);

            Task task4 = new Task("Buy groceries", "Weekly grocery shopping for the family");
            task4.setPriority(Priority.MEDIUM);
            task4.setCategory(Category.SHOPPING);
            task4.setEstimatedHours(2);
            task4.setDueDate(LocalDateTime.now().plusDays(1));
            taskManager.addTask(task4);

            Task task5 = new Task("Fix kitchen faucet", "Replace the leaky kitchen faucet");
            task5.setPriority(Priority.HIGH);
            task5.setCategory(Category.HOUSEHOLD);
            task5.setEstimatedHours(3);
            task5.setDueDate(LocalDateTime.now().minusDays(1));
            taskManager.addTask(task5);

            Task task6 = new Task("Plan vacation", "Research and book summer vacation destinations");
            task6.setPriority(Priority.LOW);
            task6.setCategory(Category.TRAVEL);
            task6.setEstimatedHours(5);
            task6.setDueDate(LocalDateTime.now().plusMonths(1));
            taskManager.addTask(task6);

            Task task7 = new Task("Update resume", "Refresh resume with recent work experience");
            task7.setPriority(Priority.MEDIUM);
            task7.setCategory(Category.WORK);
            task7.setEstimatedHours(2);
            task7.setStatus(Status.COMPLETED);
            taskManager.addTask(task7);

            Task task8 = new Task("Watch new movie", "Check out the latest releases on streaming");
            task8.setPriority(Priority.MINIMAL);
            task8.setCategory(Category.ENTERTAINMENT);
            task8.setEstimatedHours(2);
            taskManager.addTask(task8);

            Task task9 = new Task("Pay monthly bills", "Pay utilities, rent, and credit card bills");
            task9.setPriority(Priority.URGENT);
            task9.setCategory(Category.FINANCE);
            task9.setEstimatedHours(1);
            task9.setDueDate(LocalDateTime.now().plusDays(3));
            taskManager.addTask(task9);

            Task task10 = new Task("Clean garage", "Organize and clean out the garage storage");
            task10.setPriority(Priority.LOW);
            task10.setCategory(Category.HOUSEHOLD);
            task10.setEstimatedHours(6);
            task10.setStatus(Status.ON_HOLD);
            task10.setNotes("Waiting for better weather and free weekend");
            taskManager.addTask(task10);
        }
    }
}
