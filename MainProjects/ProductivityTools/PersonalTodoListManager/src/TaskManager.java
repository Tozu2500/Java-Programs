import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private List<TaskManagerListener> listeners;
    private static final String DATA_FILE = "tasks.dat";
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.listeners = new ArrayList<>();
        loadTasks();
    }
    
    public void addListener(TaskManagerListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(TaskManagerListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (TaskManagerListener listener : listeners) {
            listener.onTasksChanged();
        }
    }
    
    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        notifyListeners();
    }
    
    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                saveTasks();
                notifyListeners();
                return;
            }
        }
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks();
        notifyListeners();
    }
    
    public void removeTask(int taskId) {
        tasks.removeIf(task -> task.getId() == taskId);
        saveTasks();
        notifyListeners();
    }
    
    public Task getTaskById(int id) {
        return tasks.stream()
                   .filter(task -> task.getId() == id)
                   .findFirst()
                   .orElse(null);
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    public List<Task> getSortedTasks() {
        return tasks.stream()
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getTasksByStatus(Status status) {
        return tasks.stream()
                   .filter(task -> task.getStatus() == status)
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getTasksByCategory(Category category) {
        return tasks.stream()
                   .filter(task -> task.getCategory() == category)
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.stream()
                   .filter(task -> task.getPriority() == priority)
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getOverdueTasks() {
        return tasks.stream()
                   .filter(Task::isOverdue)
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getTasksDueToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        return tasks.stream()
                   .filter(task -> task.getDueDate() != null)
                   .filter(task -> task.getDueDate().isAfter(startOfDay) && task.getDueDate().isBefore(endOfDay))
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> getTasksDueThisWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfWeek = now.plusWeeks(1);
        
        return tasks.stream()
                   .filter(task -> task.getDueDate() != null)
                   .filter(task -> task.getDueDate().isAfter(now) && task.getDueDate().isBefore(endOfWeek))
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public List<Task> searchTasks(String query) {
        String lowerQuery = query.toLowerCase();
        return tasks.stream()
                   .filter(task -> task.getTitle().toLowerCase().contains(lowerQuery) ||
                                  task.getDescription().toLowerCase().contains(lowerQuery) ||
                                  task.getNotes().toLowerCase().contains(lowerQuery))
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public TaskStatistics getStatistics() {
        return new TaskStatistics(tasks);
    }
    
    public void markTaskCompleted(int taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(Status.COMPLETED);
            updateTask(task);
        }
    }
    
    public void markTaskInProgress(int taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(Status.IN_PROGRESS);
            updateTask(task);
        }
    }
    
    public void clearCompletedTasks() {
        tasks.removeIf(task -> task.getStatus() == Status.COMPLETED);
        saveTasks();
        notifyListeners();
    }
    
    public void clearAllTasks() {
        tasks.clear();
        saveTasks();
        notifyListeners();
    }
    
    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            tasks = (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>();
        }
    }
    
    public void exportTasks(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Title,Description,Priority,Category,Status,Created,Due,Completed,Estimated Hours,Notes");
            for (Task task : tasks) {
                writer.println(String.format("%d,\"%s\",\"%s\",%s,%s,%s,%s,%s,%s,%d,\"%s\"",
                    task.getId(),
                    task.getTitle().replace("\"", "\"\""),
                    task.getDescription().replace("\"", "\"\""),
                    task.getPriority().name(),
                    task.getCategory().name(),
                    task.getStatus().name(),
                    task.getFormattedCreatedDate(),
                    task.getFormattedDueDate(),
                    task.getFormattedCompletedDate(),
                    task.getEstimatedHours(),
                    task.getNotes().replace("\"", "\"\"")
                ));
            }
        }
    }
}