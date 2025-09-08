import java.util.*;

public class TaskStatistics {
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private int inProgressTasks;
    private int overdueTasks;
    private Map<Priority, Integer> tasksByPriority;
    private Map<Category, Integer> tasksByCategory;
    private Map<Status, Integer> tasksByStatus;
    private int totalEstimatedHours;
    private int completedEstimatedHours;
    
    public TaskStatistics(List<Task> tasks) {
        this.totalTasks = tasks.size();
        this.tasksByPriority = new EnumMap<>(Priority.class);
        this.tasksByCategory = new EnumMap<>(Category.class);
        this.tasksByStatus = new EnumMap<>(Status.class);
        
        for (Priority priority : Priority.values()) {
            tasksByPriority.put(priority, 0);
        }
        
        for (Category category : Category.values()) {
            tasksByCategory.put(category, 0);
        }
        
        for (Status status : Status.values()) {
            tasksByStatus.put(status, 0);
        }
        
        calculateStatistics(tasks);
    }
    
    private void calculateStatistics(List<Task> tasks) {
        for (Task task : tasks) {
            tasksByPriority.put(task.getPriority(), tasksByPriority.get(task.getPriority()) + 1);
            tasksByCategory.put(task.getCategory(), tasksByCategory.get(task.getCategory()) + 1);
            tasksByStatus.put(task.getStatus(), tasksByStatus.get(task.getStatus()) + 1);
            
            totalEstimatedHours += task.getEstimatedHours();
            
            if (task.getStatus() == Status.COMPLETED) {
                completedTasks++;
                completedEstimatedHours += task.getEstimatedHours();
            } else if (task.getStatus() == Status.PENDING) {
                pendingTasks++;
            } else if (task.getStatus() == Status.IN_PROGRESS) {
                inProgressTasks++;
            }
            
            if (task.isOverdue()) {
                overdueTasks++;
            }
        }
    }
    
    public int getTotalTasks() {
        return totalTasks;
    }
    
    public int getCompletedTasks() {
        return completedTasks;
    }
    
    public int getPendingTasks() {
        return pendingTasks;
    }
    
    public int getInProgressTasks() {
        return inProgressTasks;
    }
    
    public int getOverdueTasks() {
        return overdueTasks;
    }
    
    public double getCompletionPercentage() {
        if (totalTasks == 0) return 0.0;
        return (double) completedTasks / totalTasks * 100.0;
    }
    
    public Map<Priority, Integer> getTasksByPriority() {
        return new EnumMap<>(tasksByPriority);
    }
    
    public Map<Category, Integer> getTasksByCategory() {
        return new EnumMap<>(tasksByCategory);
    }
    
    public Map<Status, Integer> getTasksByStatus() {
        return new EnumMap<>(tasksByStatus);
    }
    
    public int getTotalEstimatedHours() {
        return totalEstimatedHours;
    }
    
    public int getCompletedEstimatedHours() {
        return completedEstimatedHours;
    }
    
    public int getRemainingEstimatedHours() {
        return totalEstimatedHours - completedEstimatedHours;
    }
    
    public double getHoursCompletionPercentage() {
        if (totalEstimatedHours == 0) return 0.0;
        return (double) completedEstimatedHours / totalEstimatedHours * 100.0;
    }
    
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total Tasks: %d%n", totalTasks));
        sb.append(String.format("Completed: %d (%.1f%%)%n", completedTasks, getCompletionPercentage()));
        sb.append(String.format("In Progress: %d%n", inProgressTasks));
        sb.append(String.format("Pending: %d%n", pendingTasks));
        sb.append(String.format("Overdue: %d%n", overdueTasks));
        sb.append(String.format("Total Estimated Hours: %d%n", totalEstimatedHours));
        sb.append(String.format("Completed Hours: %d (%.1f%%)%n", completedEstimatedHours, getHoursCompletionPercentage()));
        sb.append(String.format("Remaining Hours: %d%n", getRemainingEstimatedHours()));
        return sb.toString();
    }
}