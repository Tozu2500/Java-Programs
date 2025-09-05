
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TaskTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable, table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        TaskTableModel model = (TaskTableModel) table.getModel();
        Task task = model.getTaskAt(row);

        if (task != null) {
            if (!isSelected) {
                if (task.isOverdue()) {
                    component.setBackground(new Color(255, 230, 230));
                } else if (task.getStatus() == Status.COMPLETED) {
                    component.setBackground(new Color(230, 255, 230));
                } else if (task.getStatus() == Status.IN_PROGRESS) {
                    component.setBackground(new Color(230, 245, 255));
                } else {
                    component.setBackground(Color.WHITE);
                }
            }

            if (column == 2) {
                Priority priority = task.getPriority();
                component.setForeground(priority.getColor());
                setFont(getFont().deriveFont(Font.BOLD));
            } else if (column == 3) {
                Category category = task.getCategory();
                component.setForeground(category.getColor());
            } else if (column == 4) {
                Status status = task.getStatus();
                component.setForeground(status.getColor());
                setFont(getFont().deriveFont(Font.BOLD));
            } else {
                if (!isSelected) {
                    component.setForeground(Color.BLACK);
                }
                setFont(getFont().deriveFont(Font.PLAIN));
            }

            if (task.getStatus() == Status.COMPLETED) {
                setFont(getFont().deriveFont(Font.ITALIC));
            }
        }

        return component;
    }
}