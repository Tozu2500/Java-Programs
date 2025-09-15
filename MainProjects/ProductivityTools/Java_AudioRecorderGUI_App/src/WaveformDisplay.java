
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class WaveformDisplay extends JPanel {

    private List<Float> waveformData;
    private List<Float> displayData;
    private boolean isRecording = false;
    private int maxSamples = 1000;
    private float maxAmplitude = 0;
    private Color waveformColor = new Color(0, 123, 255);
    private Color backgroundColor = Color.WHITE;
    private Color gridColor = new Color(230, 230, 230);
    private int updateCounter = 0;
    private final Object dataLock = new Object();
    private Timer repaintTimer;

    public WaveformDisplay() {
        waveformData = new CopyOnWriteArrayList<>();
        displayData = new ArrayList<>();
        initializeComponents();
        setupTimer();
    }

    private void initializeComponents() {
        setPreferredSize(new Dimension(600, 200));
        setMinimumSize(new Dimension(400, 150));
        setBorder(BorderFactory.createTitledBorder("Waveform"));
        setBackground(backgroundColor);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMaxSamples();
                repaint();
            }
        });
    }

    private void setupTimer() {
        repaintTimer = new Timer(50, e -> {
            if (isRecording && !waveformData.isEmpty()) {
                updateDisplayData();
                repaint();
            }
        });
        repaintTimer.start();
    }

    private void updateMaxSamples() {
        int width = getWidth();
        if (width > 0) {
            maxSamples = Math.max(100, width);
        }
    }

    public void updateWaveform(byte[] audioData) {
        if (audioData == null || audioData.length == 0) {
            return;
        }

        synchronized (dataLock) {
            for (int i = 0; i < audioData.length - 1; i += 2) {
                short sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
                float amplitude = sample / 32768.0f;

                waveformData.add(amplitude);

                if (Math.abs(amplitude) > maxAmplitude) {
                    maxAmplitude = Math.abs(amplitude);
                }

                if (waveformData.size() > maxSamples * 2) {
                    waveformData.remove(0);
                }
            }
        }

        updateCounter++;
        if (updateCounter % 10 == 0) {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    private void updateDisplayData() {
        synchronized (dataLock) {
            if (waveformData.isEmpty()) {
                return;
            }

            displayData.clear();

            int dataSize = waveformData.size();
            int width = getWidth();

            if (width <= 0 || dataSize == 0) {
                return;
            }

            if (dataSize <= width) {
                displayData.addAll(waveformData);
            } else {
                int step = dataSize / width;
                for (int i = 0; i < width && i * step < dataSize; i++) {
                    int index = i * step;
                    if (index < dataSize) {
                        float max = 0;
                        for (int j = 0; j < step && index + j < dataSize; j++) {
                            float value = Math.abs(waveformData.get(index + j));
                            if (value > max) {
                                max = value;
                            }
                        }
                        displayData.add(waveformData.get(index) >= 0 ? max : -max);
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerY = height / 2;

        drawGrid(g2d, width, height, centerY);

        if (displayData.isEmpty()) {
            drawNoDataMessage(g2d, width, height);
            g2d.dispose();
            return;
        }

        drawWaveform(g2d, width, height, centerY);
        drawAmplitudeScale(g2d, height, centerY);

        g2d.dispose();
    }

    private void drawGrid(Graphics2D g2d, int width, int height, int centerY) {
        g2d.setColor(gridColor);
        g2d.setStroke(new BasicStroke(1.0f));

        g2d.drawLine(0, centerY, width, centerY);

        for (int i = 0; i < width; i += 50) {
            g2d.drawLine(i, 0, i, height);
        }

        for (int i = 0; i < height; i += 25) {
            g2d.drawLine(0, i, width, i);
        }
    }

    private void drawNoDataMessage(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        String message = isRecording ? "Recording..." : "No audio data";

        FontMetrics fm = g2d.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        int x = (width - messageWidth) / 2;
        int y = height / 2;

        g2d.drawString(message, x, y);
    }

    private void drawWaveform(Graphics2D g2d, int width, int height, int centerY) {
        g2d.setColor(waveformColor);
        g2d.setStroke(new BasicStroke(1.5f));

        float scaleY = maxAmplitude > 0 ? (height / 2 - 10) / maxAmplitude : 1;

        int dataSize = displayData.size();
        if (dataSize < 2) {
            return;
        }

        int[] xPoints = new int[dataSize];
        int[] yPoints = new int[dataSize];

        for (int i = 0; i < dataSize; i++) {
            xPoints[i] = (int) ((float) i / dataSize * width);
            yPoints[i] = centerY - (int) (displayData.get(i) * scaleY);
        }

        for (int i = 0; i < dataSize - 1; i++) {
            g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        if (isRecording) {
            g2d.setColor(new Color(255, 0, 0, 128));
            g2d.fillOval(width - 20, 10, 10, 10);
        }
    }

    private void drawAmplitudeScale(Graphics2D g2d, int height, int centerY) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        String maxLabel = String.format("%.2f", maxAmplitude);
        String minLabel = String.format("%.2f", -maxAmplitude);
        String zeroLabel = "0.00";

        g2d.drawString(maxLabel, 5, 15);
        g2d.drawString(zeroLabel, 5, centerY + 5);
        g2d.drawString(minLabel, 5, height - 5);
    }

    public void startRecording() {
        isRecording = true;
        clear();
        repaint();
    }

    public void stopRecording() {
        isRecording = false;
        repaint();
    }

    public void clear() {
        synchronized (dataLock) {
            waveformData.clear();
            displayData.clear();
            maxAmplitude = 0;
            updateCounter = 0;
        }
        repaint();
    }

    public void setWaveformColor(Color color) {
        this.waveformColor = color;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        setBackground(color);
        repaint();
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
        repaint();
    }

    public List<Float> getWaveformData() {
        synchronized (dataLock) {
            return new ArrayList<>(waveformData);
        }
    }

    public float getMaxAmplitde() {
        return maxAmplitude;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void cleanup() {
        if (repaintTimer != null) {
            repaintTimer.stop();
        }
    }
}