import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Serializable, Comparable<Task> {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Status status;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
    private int estimatedHours;
    private String notes;
    
    public Task(String title, String description) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = Priority.MEDIUM;
        this.category = Category.GENERAL;
        this.status = Status.PENDING;
        this.createdDate = LocalDateTime.now();
        this.estimatedHours = 1;
        this.notes = "";
    }
    
    public Task(String title, String description, Priority priority, Category category) {
        this(title, description);
        this.priority = priority;
        this.category = category;
    }

    public Task(int id, String title, String description, Priority priority, 
                Category category, Status status, LocalDateTime createdDate, 
                LocalDateTime dueDate, LocalDateTime completedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.category = category != null ? category : Category.GENERAL;
        this.status = status != null ? status : Status.PENDING;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.estimatedHours = 1;
        this.notes = "";
        
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public Task(int id, String title, String description, Priority priority, 
                Category category, Status status, LocalDateTime createdDate, 
                LocalDateTime dueDate, LocalDateTime completedDate, 
                int estimatedHours, String notes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.category = category != null ? category : Category.GENERAL;
        this.status = status != null ? status : Status.PENDING;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        this.estimatedHours = estimatedHours > 0 ? estimatedHours : 1;
        this.notes = notes != null ? notes : "";
        
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.COMPLETED && completedDate == null) {
            completedDate = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDateTime getCompletedDate() {
        return completedDate;
    }
    
    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
    
    public int getEstimatedHours() {
        return estimatedHours;
    }
    
    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && status != Status.COMPLETED;
    }
    
    public String getFormattedDueDate() {
        if (dueDate == null) {
            return "No due date";
        }
        return dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    public String getFormattedCreatedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    public String getFormattedCompletedDate() {
        if (completedDate == null) {
            return "Not completed";
        }
        return completedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    @Override
    public int compareTo(Task other) {
        int priorityComparison = other.priority.getWeight() - this.priority.getWeight();
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        
        if (this.dueDate == null && other.dueDate == null) {
            return this.createdDate.compareTo(other.createdDate);
        }
        if (this.dueDate == null) {
            return 1;
        }
        if (other.dueDate == null) {
            return -1;
        }
        
        return this.dueDate.compareTo(other.dueDate);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", priority.name(), title, status.name());
    }
}