
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

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

    public Task(String title, String description) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = Priority.MEDIUM;
        this.category = Category.GENERAL;
        this.status = Status.PENDING;
        this.createdDate = LocalDateTime.now();
        this.dueDate = null;
        this.completedDate = null;
    }

    public Task(String title, String description, Priority priority, Category category) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.status = Status.PENDING;
        this.createdDate = LocalDateTime.now();
        this.dueDate = null;
        this.completedDate = null;
    }

    public Task(int id, String title, String description, Priority priority, Category category,
                Status status, LocalDateTime createdDate, LocalDateTime dueDate, LocalDateTime completedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
        this.completedDate = LocalDateTime.now();
    }

    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
    }

    public void markPending() {
        this.status = Status.PENDING;
        this.completedDate = null;
    }

    public boolean isOverdue() {
        if (dueDate == null || status = Status.COMPLETED) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    public long getDaysUntilDue() {
        if (dueDate == null) {
            return Long.MAX_VALUE;
        }
        return Duration.between(LocalDateTime.now(), dueDate).toDays();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        } else if (status != Status.COMPLETED) {
            completedDate = null;
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id);
        sb.append(" | Title: ").append(title);
        sb.append(" | Status: ").append(status);
        sb.append(" | Priority: ").append(priority);
        sb.append(" | Category: ").append(category);
        if (dueDate != null) {
            sb.append(" | Due: ").append(dueDate.format(formatter));
            if (isOverdue()) {
                sb.append(" (OVERDUE)");
            }
        }
        sb.append("\nDescription: ").append(description);
        sb.append("\nCreated: ").append(createdDate.format(formatter));
        if (completedDate != null) {
            sb.append("\nCompleted: ").append(completedDate.format(formatter));
        }
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-3d", id));
        sb.append(" | ");
        sb.append(String.format("%-20s", title.length() > 20 ? title.substring(0, 17) + "..." : title));
        sb.append(" | ");
        sb.append(String.format("%-12s", status));
        sb.append(" | ");
        sb.append(String.format("%-6s", priority));
        sb.append(" | ");
        sb.append(String.format("%-10s", category));
        if (dueDate != null) {
            sb.append(" | ");
            sb.append(dueDate.format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));
            if (isOverdue()) {
                sb.append(" (!)");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id = task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
