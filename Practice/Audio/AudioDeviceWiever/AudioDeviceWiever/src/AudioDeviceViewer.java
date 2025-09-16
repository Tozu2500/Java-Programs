
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AudioDeviceViewer extends JFrame {

    private JTextArea inputArea;
    private JTextArea outputArea;

    public AudioDeviceViewer() {
        setTitle("Audio device viewer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(1, 2));

        inputArea = new JTextArea();
        inputArea.setEditable(false);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Audio Input Devices (Microphones)", SwingConstants.CENTER), BorderLayout.NORTH);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Audio Output Devices (Speakers/Headphones)", SwingConstants.CENTER), BorderLayout.NORTH);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        add(inputPanel);
        add(outputPanel);

        listAudioDevices();
    }

    private void listAudioDevices() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        inputArea.setText("");
        outputArea.setText("");

        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);

            if (mixer.getTargetLineInfo().length > 0) {
                inputArea.append("- " + info.getName() + " (" + info.getDescription() + ")\n");
            }

            if (mixer.getSourceLineInfo().length > 0) {
                outputArea.append("- " + info.getName() + " (" + info.getDescription() + ")\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AudioDeviceViewer viewer = new AudioDeviceViewer();
            viewer.setVisible(true);
        });
    }

}
