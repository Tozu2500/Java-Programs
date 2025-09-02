
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_FILE = "tasks.txt";
    private static final String SEPARATOR = "|";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveTasks(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Task task : tasks) {
                writer.write(taskToString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = stringToTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    public boolean exportTasks(List<Task> tasks, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Personal To-Do List Export");
            writer.newLine();
            writer.write("Export Date: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.newLine();
            writer.write("Total tasks: " + tasks.size());
            writer.newLine();
            writer.newLine();

            for (Task task : tasks) {
                writer.write("Task ID: " + task.getId());
                writer.newLine();
                writer.write("Title: " + task.getTitle());
                writer.newLine();
                writer.write("Description: " + task.getDescription());
                writer.newLine();
                writer.write("Priority: " + task.getPriority());
                writer.newLine();
                writer.write("Category: " + task.getCategory());
                writer.newLine();
                writer.write("Status: " + task.getStatus());
                writer.newLine();
                writer.write("Created: " + task.getCreatedDate().format(DATE_FORMATTER));
                writer.newLine();

                if (task.getDueDate() != null) {
                    writer.write("Due Date: " + task.getDueDate().format(DATE_FORMATTER));
                    writer.newLine();
                }

                if (task.getCompletedDate() != null) {
                    writer.write("Completed: " + task.getCompletedDate().format(DATE_FORMATTER));
                    writer.newLine();
                }

                writer.newLine();
                writer.write("----------------------------------------");
                writer.newLine();
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error exporting tasks: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> importTasks(String filename) {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            System.err.println("Import file does not exist: " + filename);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("Personal To-Do List") || 
                    line.startsWith("Export Date:") || line.startsWith("Total Tasks:") || 
                    line.startsWith("----")) {
                    continue;
                }
                
                Task task = parseExportedTask(reader, line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error importing tasks: " + e.getMessage());
            return null;
        }
        
        return tasks;
    }

    private Task parseExportedTask(BufferedReader reader, String firstLine) throws IOException {
        if (!firstLine.startsWith("Task ID:")) {
            return null;
        }

        try {
            int id = Integer.parseInt(firstLine.substring("Task ID:".length()).trim());
            String title = null;
            String description = null;
            Priority priority = Priority.MEDIUM;
            Category category = Category.GENERAL;
            Status status = Status.PENDING;
            LocalDateTime createdDate = LocalDateTime.now();
            LocalDateTime dueDate = null;
            LocalDateTime completedDate = null;

            String line;
            while ((line = reader.readLine()) != null && !line.startsWith("----")) {
                if (line.startsWith("Title:")) {
                    title = line.substring("Title:".length()).trim();
                } else if (line.startsWith("Description:")) {
                    description = line.substring("Description:".length()).trim();
                } else if (line.startsWith("Priority:")) {
                    priority = Priority.fromString(line.substring("Priority:".length()).trim());
                } else if (line.startsWith("Category:")) {
                    category = Category.fromString(line.substring("Category:".length()).trim());
                } else if (line.startsWith("Status:")) {
                    status = Status.fromString(line.substring("Status:".length()).trim());
                } else if (line.startsWith("Created")) {
                    createdDate = LocalDateTime.parse(line.substring("Created:".length()).trim(), DATE_FORMATTER);
                } else if (line.startsWith("Due Date:")) {
                    dueDate = LocalDateTime.parse(line.substring("Due Date:".length()).trim(), DATE_FORMATTER);
                } else if (line.startsWith("Completed:")) {
                    completedDate = LocalDateTime.parse(line.substring("Completed:".length()).trim(), DATE_FORMATTER);
                }
            }

            if (title != null && description != null) {
                return new Task(id, title, description, priority, category, status, createdDate, dueDate, completedDate);
            }
        } catch (Exception e) {
            System.err.println("Error parsin task: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(SEPARATOR);
        sb.append(task.getTitle()).append(SEPARATOR);
        sb.append(task.getDescription()).append(SEPARATOR);
        sb.append(task.getPriority().name()).append(SEPARATOR);
        sb.append(task.getCategory().name()).append(SEPARATOR);
        sb.append(task.getStatus().name()).append(SEPARATOR);
        sb.append(task.getCreatedDate().format(DATE_FORMATTER)).append(SEPARATOR);

        if (task.getDueDate() != null) {
            sb.append(task.getDueDate().format(DATE_FORMATTER));
        }
        sb.append(SEPARATOR);

        if (task.getCompletedDate() != null) {
            sb.append(task.getCompletedDate().format(DATE_FORMATTER));
        }

        return sb.toString();
    }

    private Task stringToTask(String line) {
        try {
            String[] parts = line.split("\\" + SEPARATOR, -1);

            if (parts.length < 9) {
                return null;
            }

            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String description = parts[2];
            Priority priority = Priority.valueOf(parts[3]);
            Category category = Category.valueOf(parts[4]);
            Status status = Status.valueOf(parts[5]);
            LocalDateTime createdDate = LocalDateTime.parse(parts[6], DATE_FORMATTER);

            LocalDateTime dueDate = null;
            if (!parts[7].isEmpty()) {
                dueDate = LocalDateTime.parse(parts[7], DATE_FORMATTER);
            }

            LocalDateTime completedDate = null;
            if (parts.length > 8 && !parts[8].isEmpty()) {
                completedDate = LocalDateTime.parse(parts[8], DATE_FORMATTER);
            }

            return new Task(id, title, description, priority, category, status, createdDate, dueDate, completedDate);
        } catch (Exception e) {
            System.err.println("Error parsin task line: " + line + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}