
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class AudioFileList extends JPanel {

    private AudioRecorderGUI parent;
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;
    private JPopupMenu contextMenu;
    private List<File> audioFiles;

    public AudioFileList(AudioRecorderGUI parent) {
        this.parent = parent;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshList();
    }

    private void initializeComponents() {
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(fileList);

        contextMenu = new JPopupMenu();
        JMenuItem playItem = new JMenuItem("Play");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem infoItem = new JMenuItem("Info");

        playItem.addActionListener(e -> playSelectedFile());
        deleteItem.addActionListener(e -> deleteSelectedFile());
        infoItem.addActionListener(e -> showFileInfo());

        contextMenu.add(playItem);
        contextMenu.add(deleteItem);
        contextMenu.add(infoItem);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Audio Files"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    playSelectedFile();
                }
            }
        });
    }

    public void refreshList() {
        listModel.clear();
        audioFiles = parent.getFileManager().getRecordingFiles();

        for (File file : audioFiles) {
            listModel.addElement(file.getName());
        }
    }

    private void showContextMenu(MouseEvent e) {
        int index = fileList.locationToIndex(e.getPoint());
        if (index >= 0) {
            fileList.setSelectedIndex(index);
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void playSelectedFile() {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < audioFiles.size()) {
            try {
                File selectedFile = audioFiles.get(selectedIndex);
                parent.getAudioEngine().loadAudioFile(selectedFile);
                parent.playRecording();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to play file: " + e.getMessage(),
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedFile() {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < audioFiles.size()) {
            File selectedFile = audioFiles.get(selectedIndex);

            int result = JOptionPane.showConfirmDialog(this,
                    "Delete " + selectedFile.getName() + "?",
                    "Delete File?",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (parent.getFileManager().deleteRecording(selectedFile)) {
                    refreshList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete the file",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showFileInfo() {
        int selectedIndex = fileList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < audioFiles.size()) {
            try {
                File selectedFile = audioFiles.get(selectedIndex);
                AudioFileInfo info = parent.getFileManager().getAudioFileInfo(selectedFile);

                String infoText = String.format(
                    "File: %s\n" +
                    "Size: %s\n" +
                    "Sample Rate: %.0f Hz\n" +
                    "Bit Depth: %d bits\n" +
                    "Channels: %d\n" +
                    "Duration: %.2f seconds",
                    info.getFileName(),
                    parent.getFileManager().getRecordingFileSizeString(selectedFile),
                    info.getSampleRate(),
                    info.getBitDepth(),
                    info.getChannels(),
                    info.getDuration()
                );

                JOptionPane.showMessageDialog(this, infoText, "File Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Failed to get file info: " + e.getMessage(),
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}