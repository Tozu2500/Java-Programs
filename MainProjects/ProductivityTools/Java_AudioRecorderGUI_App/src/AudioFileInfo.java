public class AudioFileInfo {

    private String fileName;
    private float sampleRate;
    private int bitDepth;
    private int channels;
    private double duration;
    private long fileSize;

    public AudioFileInfo(String fileName, float sampleRate, int bitDepth, int channels, double duration, long fileSize) {
        this.fileName = fileName;
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
        this.duration = duration;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public int getChannels() {
        return channels;
    }

    public double getDuration() {
        return duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getDurationString() {
        int minutes = (int) (duration / 60);
        int seconds = (int) (duration % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

}