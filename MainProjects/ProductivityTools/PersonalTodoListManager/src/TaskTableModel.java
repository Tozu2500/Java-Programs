
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Title", "Priority", "Category", "Status", "Due Date", "Hours"};
    private List<Task> tasks;

    public TaskTableModel() {
        this.tasks = new ArrayList<>();
    }

    public TaskTableModel(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return Priority.class;
            case 3: return Category.class;
            case 4: return Status.class;
            case 5: return String.class;
            case 6: return Integer.class;
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        fireTableDataChanged();
    }

    public void addTask(Task task) {
        tasks.add(task);
        fireTableRowsInserted(tasks.size() - 1, tasks.size() - 1);
    }

    public void removeTask(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tasks.size()) {
            tasks.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    public Task getTaskAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < tasks.size()) {
            return tasks.get(rowIndex);
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public void clear() {
        int size = tasks.size();
        tasks.clear();
        if (size > 0) {
            fireTableRowsDeleted(0, size - 1);
        }
    }

    public void refresh() {
        fireTableDataChanged();
    }
}