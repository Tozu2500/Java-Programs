
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class FileManager {

    private static final String DEFAULT_RECORDINGS_DIR = "recordings";
    private String recordingsDirectory;
    private SimpleDateFormat dateFormat;

    public FileManager() {
        this.recordingsDirectory = DEFAULT_RECORDINGS_DIR;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        createRecordingsDirectory();
    }

    public FileManager(String recordingsDirectory) {
        this.recordingsDirectory = recordingsDirectory;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        createRecordingsDirectory();
    }

    private void createRecordingsDirectory() {
        File dir = new File(recordingsDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void saveRecording(byte[] audioData, File file) throws IOException {
        if (audioData == null || audioData.length == 0) {
            throw new IllegalArgumentException("Audio data can't be null or empty");
        }

        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".wav")) {
            saveAsWav(audioData, file);
        } else if (fileName.endsWith(".aiff") || fileName.endsWith(".aif")) {
            saveAsAiff(audioData, file);
        } else {
            saveAsWav(audioData, new File(file.getAbsolutePath() + ".wav"));
        }
    }

    private void saveAsWav(byte[] audioData, File file) throws IOException {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
        audioInputStream.close();
    }

    private void saveAsAiff(byte[] audioData, File file) throws IOException {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.AIFF, file);
        audioInputStream.close();
    }

    public void exportRecording(byte[] audioData, File file, ExportSettings settings) throws IOException {
        if (audioData == null || audioData.length == 0) {
            throw new IllegalArgumentException("Audio data cannot be null or empty.");
        }

        byte[] processedData = processAudioData(audioData, settings);

        String format = settings.getFormat().toLowerCase();
        switch (format) {
            case "wav":
                saveAsWav(processedData, file);
                break;
            case "aiff":
            case "aif":
                saveAsAiff(processedData, file);
                break;
            default:
                saveAsWav(processedData, new File(file.getAbsolutePath() + ".wav"));
                break;
        }
    }

    private byte[] processAudioData(byte[] originalData, ExportSettings settings) {
        byte[] processedData = originalData.clone();

        if (settings.isNormalizeAudio()) {
            processedData = normalizeAudio(processedData);
        }

        if (settings.getVolumeAdjustment() != 1.0f) {
            processedData = adjustVolume(processedData, settings.getVolumeAdjustment());
        }

        if (settings.isFadeIn() || settings.isFadeOut()) {
            processedData = applyFades(processedData, settings);
        }

        return processedData;
    }

    private byte[] normalizeAudio(byte[] audioData) {
        short maxAmplitude = 0;

        for (int i = 0; i < audioData.length - 1; i += 2) {
            short sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
            maxAmplitude = (short) Math.max(maxAmplitude, Math.abs(sample));
        }

        if (maxAmplitude == 0) {
            return audioData;
        }

        float normalizationFactor = 32767.0f / maxAmplitude;
        byte[] normalizedData = new byte[audioData.length];

        for (int i = 0; i < audioData.length - 1; i += 2) {
            short sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
            short normalizedSample = (short) (sample * normalizationFactor);

            normalizedData[i] = (byte) (normalizedSample & 0xFF);
            normalizedData[i + 1] = (byte) (normalizedSample >> 8);
        }

        return normalizedData;
    }

    private byte[] adjustVolume(byte[] audioData, float volumeMultiplier) {
        byte[] adjustedData = new byte[audioData.length];

        for (int i = 0; i < audioData.length - 1; i += 2) {
            short sample = (short) ((audioData[i + 1] << 8) | (audioData[i] & 0xFF));
            short adjustedSample = (short) Math.max(-32768, Math.min(32767, sample * volumeMultiplier));

            adjustedData[i] = (byte) (adjustedSample & 0xFF);
            adjustedData[i + 1] = (byte) (adjustedSample >> 8);
        }

        return adjustedData;
    }

    private byte[] applyFades(byte[] audioData, ExportSettings settings) {
        byte[] fadedData = audioData.clone();
        int frameSize = 4;
        int totalFrames = audioData.length / frameSize;

        if (settings.isFadeIn()) {
            int fadeInFrames = (int) (settings.getFadeInDuration() * 44100);
            fadeInFrames = Math.min(fadeInFrames, totalFrames / 2);

            for (int frame = 0; frame < fadeInFrames; frame++) {
                float fadeMultiplier = (float) frame / fadeInFrames;
                int byteIndex = frame * frameSize;

                for (int channel = 0; channel < 2; channel++) {
                    int sampleIndex = byteIndex + (channel * 2);
                    short sample = (short) ((fadedData[sampleIndex + 1] << 8) | (fadedData[sampleIndex] & 0xFF));
                    short fadedSample = (short) (sample * fadeMultiplier);

                    fadedData[sampleIndex] = (byte) (fadedSample & 0xFF);
                    fadedData[sampleIndex + 1] = (byte) (fadedSample >> 8);
                }
            }
        }

        if (settings.isFadeOut()) {
            int fadeOutFrames = (int) (settings.getFadeOutDuration() * 44100);
            fadeOutFrames = Math.min(fadeOutFrames, totalFrames / 2);
            int fadeStartFrame = totalFrames - fadeOutFrames;

            for (int frame = fadeStartFrame; frame < totalFrames; frame++) {
                float fadeMultiplier = (float) (totalFrames - frame) / fadeOutFrames;
                int byteIndex = frame * frameSize;

                for (int channel = 0; channel < 2; channel++) {
                    int sampleIndex = byteIndex + (channel * 2);
                    if (sampleIndex + 1 < fadedData.length) {
                        short sample = (short) ((fadedData[sampleIndex + 1] << 8) | (fadedData[sampleIndex] & 0xFF));
                        short fadedSample = (short) (sample * fadeMultiplier);

                        fadedData[sampleIndex] = (byte) (fadedSample & 0xFF);
                        fadedData[sampleIndex + 1] = (byte) (fadedSample >> 8);
                    }
                }
            }
        }

        return fadedData;
    }

    public byte[] loadAudioFile(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;

        while ((bytesRead = audioInputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        audioInputStream.close();
        return buffer.toByteArray();
    }

    public List<File> getRecordingFiles() {
        File dir = new File(recordingsDirectory);
        List<File> audioFiles = new ArrayList<>();

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((file, name) -> {
                String lowerName = name.toLowerCase();
                return lowerName.endsWith(".wav") ||
                        lowerName.endsWith(".aiff") ||
                        lowerName.endsWith(".aif") ||
                        lowerName.endsWith(.mp3);
            });

            if (files != null) {
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                audioFiles.addAll(Arrays.asList(files));
            }
        }

        return audioFiles;
    }

    public String generateUniqueFileName(String baseName, String extension) {
        String timestamp = dateFormat.format(new Date());
        String fileName = baseName + "_" + timestamp + "." + extension;

        File file = new File(recordingsDirectory, fileName);
        int counter = 1;

        while (file.exists()) {
            fileName = baseName + "_" + timestamp + "_" + counter + "." + extension;
            file = new File(recordingsDirectory, fileName);
            counter++;
        }

        return fileName;
    }

    public File getRecordingFile(String fileName) {
        return new File(recordingsDirectory, fileName);
    }

    public boolean deleteRecording(File file) {
        return file.exists() && file.delete();
    }

    public long getRecordingFileSize(File file) {
        return file.exists() ? file.length() : 0;
    }

    public String getRecordingFileSizeString(File file) {
        long bytes = getRecordingFileSize(file);

        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    public AudioFileInfo getAudioFileInfo(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long frameLength = audioInputStream.getFrameLength();
        double durationInSeconds = frameLength / format.getFrameRate();

        audioInputStream.close();

        return new AudioFileInfo(
            file.getName(),
            format.getSampleRate(),
            format.getSampleSizeInBits(),
            format.getChannels(),
            durationInSeconds,
            file.length()
        );
    }

    public void setRecordingsDirectory(String directory) {
        this.recordingsDirectory = directory;
        createRecordingsDirectory();
    }

    public String getRecordingsDirectory() {
        return recordingsDirectory;
    }

    public void cleanupOldRecordings(int maxFiles) {
        List<File> files = getRecordingFiles();

        if (files.size() > maxFiles) {
            for (int i = maxFiles; i < files.size(); i++) {
                files.get(i).delete();
            }
        }
    }

    public long getTotalRecordingsSize() {
        List<File> files = getRecordingFiles();
        long totalSize = 0;

        for (File file : files) {
            totalSize += file.length();
        }

        return totalSize;
    }

    public String getTotalRecordingsSizeString() {
        long bytes = getTotalRecordingsSize();

        if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}