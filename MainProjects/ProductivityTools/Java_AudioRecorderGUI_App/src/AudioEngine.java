import javax.sound.sampled.*;
import java.io.*;
import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioEngine {
    private TargetDataLine targetLine;
    private SourceDataLine sourceLine;
    private AudioFormat audioFormat;
    private ByteArrayOutputStream recordingStream;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private ExecutorService executorService;
    private StatusListener statusListener;
    private WaveformListener waveformListener;
    private float volume = 1.0f;
    private byte[] currentRecording;
    private AudioInputStream currentAudioStream;
    private Thread recordingThread;
    private Thread playbackThread;
    private long pausePosition = 0;
    
    public interface StatusListener {
        void onStatusChange(String status);
    }
    
    public interface WaveformListener {
        void onWaveformData(byte[] data);
    }
    
    public AudioEngine() {
        executorService = Executors.newCachedThreadPool();
        recordingStream = new ByteArrayOutputStream();
    }
    
    public void startRecording(AudioSettings settings) throws Exception {
        if (isRecording) {
            stopRecording();
        }
        
        audioFormat = new AudioFormat(
            settings.getSampleRate(),
            settings.getBitDepth(),
            settings.getChannels(),
            true,
            false
        );
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        
        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Audio format not supported");
        }
        
        targetLine = (TargetDataLine) AudioSystem.getLine(info);
        targetLine.open(audioFormat);
        targetLine.start();
        
        recordingStream.reset();
        isRecording = true;
        
        recordingThread = new Thread(this::recordingLoop);
        recordingThread.start();
        
        if (statusListener != null) {
            statusListener.onStatusChange("Recording started");
        }
    }

    private void recordingLoop() {
        byte[] buffer = new byte[4096];

        while (isRecording && targetLine != null) {
            try {
                int bytesRead = targetLine.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    recordingStream.write(buffer, 0, bytesRead);

                    if (waveformListener != null) {
                        byte[] waveformData = new byte[bytesRead];
                        System.arraycopy(buffer, 0, waveformData, 0, bytesRead);
                        waveformListener.onWaveformData(waveformData);
                    }
                }
            } catch (Exception e) {
                if (statusListener != null) {
                    statusListener.onStatusChange("Recording error: " + e.getMessage());
                }
                break;
            }
        }
    }

    public void stopRecording() throws Exception {
        if (isRecording) {
            isRecording = false;

            if (recordingThread != null) {
                recordingThread.interrupt();
            }

            if (targetLine != null) {
                targetLine.stop();
                targetLine.close();
                targetLine = null;
            }

            currentRecording = recordingStream.toByteArray();

            if (statusListener != null) {
                statusListener.onStatusChange("Recording stopped");
            }
        }
    }

    public void playRecording() throws Exception {
        if (!hasRecording()) {
            throw new Exception("No recording available");
        }

        if (isPlaying) {
            stopPlayback();
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(currentRecording);
        currentAudioStream = new AudioInputStream(inputStream, audioFormat, currentRecording.length / audioFormat.getFrameSize());

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(audioFormat);
        sourceLine.start();

        isPlaying = true;
        isPaused = false;
        pausePosition = 0;

        playbackThread = new Thread(this::playbackLoop);
        playbackThread.start();

        if (statusListener != null) {
            statusListener.onStatusChange("Playback started");
        }
    }

    private void playbackLoop() {
        byte[] buffer = new byte[4096];

        try {
            if (pausePosition > 0) {
                currentAudioStream.skip(pausePosition);
                pausePosition = 0;
            }

            int bytesRead;
            while ((bytesRead = currentAudioStream.read(buffer)) != -1 && isPlaying) {
                if (!isPaused) {
                    if (volume != 1.0f) {
                        applyVolume(buffer, bytesRead);
                    }
                    sourceLine.write(buffer, 0, bytesRead);
                } else {
                    synchronized (this) {
                        while (isPaused && isPlaying) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (statusListener != null) {
                statusListener.onStatusChange("Playback error: " + e.getMessage());
            }
        } finally {
            if (sourceLine != null) {
                sourceLine.drain();
                sourceLine.close();
            }
            isPlaying = false;
            if (statusListener != null) {
                statusListener.onStatusChange("Playback finished");
            }
        }
    }

    private void applyVolume(byte[] buffer, int length) {
        for (int i = 0; i < length - 1; i += 2) {
            short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
            sample = (short) (sample * volume);
            buffer[i] = (byte) (sample & 0xFF);
            buffer[i + 1] = (byte) (sample >> 8);
        }
    }

    public void stopPlayback() throws Exception {
        if (isPlaying) {
            isPlaying = false;
            isPaused = false;

            if (playbackThread != null) {
                playbackThread.interrupt();
            }

            if (sourceLine != null) {
                sourceLine.stop();
                sourceLine.close();
                sourceLine = null;
            }

            if (currentAudioStream != null) {
                currentAudioStream.close();
                currentAudioStream = null;
            }

            if (statusListener != null) {
                statusListener.onStatusChange("Playback stopped");
            }
        }
    }

    public void pausePlayback() throws Exception {
        if (isPlaying && !isPaused) {
            isPaused = true;
            if (statusListener != null) {
                statusListener.onStatusChange("Playback paused");
            }
        }
    }

    public synchronized void resumePlayback() throws Exception {
        if (isPlaying && isPaused) {
            isPaused = false;
            notifyAll();
            if (statusListener != null) {
                statusListener.onStatusChange("Playback resumed");
            }
        }
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public float getVolume() {
        return volume;
    }

    public boolean hasRecording() {
        return currentRecording != null && currentRecording.length > 0;
    }

    public byte[] getRecordingData() {
        return currentRecording != null ? currentRecording.clone() : null;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void clearRecording() {
        currentRecording = null;
        recordingStream.reset();
        if (statusListener != null) {
            statusListener.onStatusChange("Recording cleared");
        }
    }

    public void loadAudioFile(File file) throws IOException, UnsupportedAudioFileException {
        final long MAX_MEMORY_LOAD_SIZE = 50 * 1024 * 1024;  // 50 MB limit for the audio file
        
        if (file.length() > MAX_MEMORY_LOAD_SIZE) {
            throw new IOException("The file is too large, it exceeds (50MB): " + file.length() + " bytes");
        }

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            
            audioFormat = audioInputStream.getFormat();
            byte[] data = new byte[4096];
            int bytesRead;

            while ((bytesRead = audioInputStream.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            currentRecording = buffer.toByteArray();

            if (statusListener != null) {
                statusListener.onStatusChange("Audio file loaded: " + file.getName());
            }
        }
    }

    public List<Mixer.Info> getAvailableInputDevices() {
        List<Mixer.Info> devices = new ArrayList<>();
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] targetLines = mixer.getTargetLineInfo();
            if (targetLines.length > 0) {
                devices.add(mixerInfo);
            }
        }

        return devices;
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        List<Mixer.Info> devices = new ArrayList<>();
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        for (Mixer.Info mixerInfo : mixers) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] sourceLines = mixer.getSourceLineInfo();
            if (sourceLines.length > 0) {
                devices.add(mixerInfo);
            }
        }

        return devices;
    }

    public double getRecordingLength() {
        if (currentRecording == null || audioFormat == null) {
            return 0;
        }
        // Using floating point division for accurate length calc
        return (long) ((double) currentRecording.length / (audioFormat.getFrameSize() * audioFormat.getFrameRate()));
    }

    public double getRecordingLengthInSeconds() {
        if (currentRecording == null || audioFormat == null) {
            return 0.0;
        }

        return (double) currentRecording.length / (audioFormat.getFrameSize() * audioFormat.getFrameRate());
    }

    public void setStatusListener(StatusListener listener) {
        this.statusListener = listener;
    }

    public void setWaveformListener(WaveformListener listener) {
        this.waveformListener = listener;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void cleanup() {
        try {
            if (isRecording) {
                stopRecording();
            }
            if (isPlaying) {
                stopPlayback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}