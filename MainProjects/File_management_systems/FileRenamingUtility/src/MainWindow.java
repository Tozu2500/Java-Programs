
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {

    private FileListPanel fileListPanel;
    private RenameOptionsPanel renameOptionsPanel;
    private PreviewPanel previewPanel;
    private StatusPanel statusPanel;
    private FileManager fileManager;
    private RenameEngine renameEngine;

    public MainWindow() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("File Renamer Batch Tool - Java by Tozu");
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        fileManager = new FileManager();
        renameEngine = new RenameEngine();

        fileListPanel = new FileListPanel();
        renameOptionsPanel = new RenameOptionsPanel();
        previewPanel = new PreviewPanel();
        statusPanel = new StatusPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(fileListPanel, BorderLayout.CENTER);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, topPanel, renameOptionsPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.6);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(previewPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

}
