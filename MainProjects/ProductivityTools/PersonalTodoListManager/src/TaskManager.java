
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    private List<Task> tasks;
    private FileManager fileManager;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.fileManager = new FileManager();
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void addTask(String title, String description) {
        Task task = new Task(title, description);
        addTask(task);
    }

    public void addTask(String title, String description, Priority priority, Category category) {
        Task task = new Task(title, description, priority, category);
        addTask(task);
    }

    public boolean removeTask(int id) {
        Task taskToRemove = getTaskById(id);
        if (taskToRemove != null) {
            tasks.remove(taskToRemove);
            saveTasks();
            return true;
        }
        return false;
    }

    public Task getTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean updateTask(int id, String newTitle, String newDescription) {
        Task task = getTaskById(id);
        if (task != null) {
            if (newTitle != null && !newTitle.trim().isEmpty()) {
                task.setTitle(newTitle);
            }
            if (newDescription != null && !newDescription.trim().isEmpty()) {
                task.setDescription(newDescription);
            }
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean updateTaskPriority(int id, Priority priority) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setPriority(priority);
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean updateTaskCategory(int id, Category category) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setCategory(category);
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean updateTaskStatus(int id, Status status) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean setTaskDueDate(int id, LocalDateTime dueDate) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setDueDate(dueDate);
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean markTaskCompleted(int id) {
        Task task = getTaskById(id);
        if (task != null) {
            task.markCompleted();
            saveTasks();
            return true;
        }
        return false;
    }

    public boolean markTaskInProgress(int id) {
        Task task = getTaskById(id);
        if (task != null) {
            task.markInProgress();
            saveTasks();
            return true;
        }
        return false;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>();
    }

    public List<Task> getTasksByStatus(Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByCategory(Category category) {
        return tasks.stream()
                .filter(task -> task.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        return tasks.stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Task> getTasksDueToday() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return tasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> task.getDueDate().isBefore(startOfDay))
                .filter(task -> task.getDueDate().isBefore(endOfDay))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksDueThisWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekFromNow = now.plusWeeks(1);

        return tasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> !task.getDueDate().isBefore(now))
                .filter(task -> task.getDueDate().isBefore(weekFromNow))
                .collect(Collectors.toList());
    }

    public List<Task> searchTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(lowerKeyword) ||
                            task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<Task> getSortedTasks(TaskSorter.SortBy sortBy, boolean ascending) {
        return TaskSorter.sortTasks(tasks, sortBy, ascending);
    }

    public TaskStatistics getStatistics() {
        return new TaskStatistics(tasks);
    }

    public void clearCompletedTasks() {
        tasks.removeIf(task -> task.getStatus() == Status.COMPLETED);
        saveTasks();
    }

    public void clearAllTasks() {
        tasks.clear();
        saveTasks();
    }

    private void saveTasks() {
        fileManager.saveTasks(tasks);
    }

    private void loadTasks() {
        List<Task> loadedTasks = fileManager.loadTasks();
        if (loadedTasks != null) {
            this.tasks = loadedTasks;
        }
    }

    public boolean exportTasks(String filename) {
        return fileManager.exportTasks(tasks, filename);
    }

    public boolean importTasks(String filename) {
        List<Task> importedTasks = fileManager.importTasks(filename);
        if (importedTasks != null) {
            tasks.addAll(importedTasks);
            saveTasks();
            return true;
        }
        return false;
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(task -> task.getStatus() == Status.COMPLETED).count();
    }

    public int getPendingTaskCount() {
        return (int) tasks.stream().filter(task -> task.getStatus() == Status.PENDING).count();
    }

    public int getInProgressTaskCount() {
        return (int) tasks.stream().filter(task -> task.getStatus() == Status.IN_PROGRESS).count();
    }

    public int getOverdueTaskCount() {
        return (int) tasks.stream().filter(Task::isOverdue).count();
    }
}