
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskSorter {

    public enum SortBy {
        ID,
        TITLE,
        PRIORITY,
        CATEGORY,
        STATUS,
        CREATED_DATE,
        DUE_DATE,
        COMPLETION_DATE
    }

    public static List<Task> sortTasks(List<Task> tasks, SortBy sortBy, boolean ascending) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        Comparator<Task> comparator = getComparator(sortBy);

        if (!ascending) {
            comparator = comparator.reversed();
        }

        sortedTasks.sort(comparator);
        return sortedTasks;
    }

    private static Comparator<Task> getComparator(SortBy sortBy) {
        switch (sortBy) {
            case ID:
                return Comparator.comparingInt(Task::getId);

            case TITLE:
                return Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);

            case PRIORITY:
                return Comparator.comparing(task -> task.getPriority().getLevel(), Comparator.reverseOrder());

            case CATEGORY:
                return Comparator.comparing(task -> task.getCategory().getDisplayName(), String.CASE_INSENSITIVE_ORDER);

            case STATUS:
                return Comparator.comparing(task -> task.getStatus().getDisplayName(), String.CASE_INSENSITIVE_ORDER);

            case CREATED_DATE:
                return Comparator.comparing(Task::getCreatedDate);

            case DUE_DATE:
                return Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));

            case COMPLETION_DATE:
                return Comparator.comparing(Task::getCompletedDate, Comparator.nullsLast(Comparator.naturalOrder()));

            default:
                return Comparator.comparingInt(Task::getId);
        }
    }

    public static List<Task> sortByPriorityAndDueDate(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator
            .comparing((Task task) -> task.getPriority().getLevel(), Comparator.reverseOrder())
            .thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
        );
        return sortedTasks;
    }

    public static List<Task> sortByStatusAndPriority(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator
            .comparing(Task::getStatus)
            .thenComparing((Task task) -> task.getPriority().getLevel(), Comparator.reverseOrder())
        );
        return sortedTasks;
    }

    public static List<Task> sortByDueDateUrgency(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort((task1, task2) -> {
            if (task1.getDueDate() == null && task2.getDueDate() == null) {
                return 0;
            }

            if (task1.getDueDate() == null) {
                return 1;
            }

            if (task2.getDueDate() == null) {
                return -1;
            }

            boolean task1Overdue = task1.isOverdue();
            boolean task2Overdue = task2.isOverdue();

            if (task1Overdue && !task2Overdue) {
                return -1;
            }

            if (!task1Overdue && task2Overdue) {
                return 1;
            }

            return task1.getDueDate().compareTo(task2.getDueDate());
        });
        return sortedTasks;
    }
}