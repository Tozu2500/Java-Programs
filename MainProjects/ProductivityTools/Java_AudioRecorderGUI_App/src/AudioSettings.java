
import javax.sound.sampled.Mixer;

public class AudioSettings {

    public static final float SAMPLE_RATE_8K = 8000.0f;
    public static final float SAMPLE_RATE_16K = 16000.0f;
    public static final float SAMPLE_RATE_22K = 22000.0f;
    public static final float SAMPLE_RATE_44K = 44000.0f;
    public static final float SAMPLE_RATE_48K = 48000.0f;
    public static final float SAMPLE_RATE_96K = 96000.0f;

    public static final int BIT_DEPTH_8 = 8;
    public static final int BIT_DEPTH_16 = 16;
    public static final int BIT_DEPTH_24 = 24;
    public static final int BIT_DEPTH_32 = 32;

    public static final int CHANNELS_MONO = 1;
    public static final int CHANNELS_STEREO = 2;

    private float sampleRate;
    private int bitDepth;
    private int channels;
    private Mixer.Info inputDevice;
    private Mixer.Info outputDevice;
    private boolean signed;
    private boolean bigEndian;
    private int bufferSize;
    private float gain;
    private boolean noiseReduction;
    private boolean autoGainControl;

    public AudioSettings() {
        this.sampleRate = SAMPLE_RATE_44K;
        this.bitDepth = BIT_DEPTH_16;
        this.channels = CHANNELS_STEREO;
        this.signed = true;
        this.bigEndian = false;
        this.bufferSize = 4096;
        this.gain = 1.0f;
        this.noiseReduction = false;
        this.autoGainControl = false;
    }

    public AudioSettings(float sampleData, int bitDepth, int channels) {
        this();
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        if (sampleRate <= 0) {
            throw new IllegalArgumentException("Sample rate must be positive");
        }
        this.sampleRate = sampleRate;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(int bitDepth) {
        if (bitDepth != 8 && bitDepth != 16 && bitDepth != 24 && bitDepth != 32) {
            throw new IllegalArgumentException("Bit depth must be 8, 16, 24 or 32");
        }
        this.bitDepth = bitDepth;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        if (channels < 1 || channels > 2) {
            throw new IllegalArgumentException("Channels must be 1 (mono) or 2 (stereo");
        }
        this.channels = channels;
    }

    public Mixer.Info getInputDevice() {
        return inputDevice;
    }

    public void setInputDevice(Mixer.Info inputDevice) {
        this.inputDevice = inputDevice;
    }

    public Mixer.Info getOutputDevice() {
        return outputDevice;
    }

    public void setOutputDevice(Mixer.Info outputDevice) {
        this.outputDevice = outputDevice;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        if (bufferSize <= 0 || (bufferSize & (bufferSize - 1)) != 0) {
            throw new IllegalArgumentException("Buffer size must be a positie power of 2");
        }
        this.bufferSize = bufferSize;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        if (gain < 0.0f || gain > 10.0f) {
            throw new IllegalArgumentException("Gain must be between 0.0 and 10.0");
        }
        this.gain = gain;
    }

    public boolean isNoiseReduction() {
        return noiseReduction;
    }

    public void setNoiseReduction(boolean noiseReduction) {
        this.noiseReduction = noiseReduction;
    }

    public boolean isAutoGainControl() {
        return autoGainControl;
    }

    public void setAutoGainControl(boolean autoGainControl) {
        this.autoGainControl = autoGainControl;
    }

    public String getQualityDescription() {
        StringBuilder sb = new StringBuilder();

        if (sampleRate >= 40000) {
            sb.append("High Quality");
        } else if (sampleRate >= 44100) {
            sb.append("CD Quality");
        } else if (sampleRate >= 22050) {
            sb.append("Radio Quality");
        } else {
            sb.append("Phone Quality");
        }

        sb.append("(").append(sampleRate).append("Hz, ");
        sb.append(bitDepth).append("-bit, ");
        sb.append(channels == 1 ? "Mono" : "Stereo").append(")");

        return sb.toString();
    }

    public long getFileSizeEstimate(int recordingSeconds) {
        long bytesPerSecond = (long) (sampleRate * (bitDepth / 8) * channels);
        return bytesPerSecond * recordingSeconds;
    }

    public String getFileSizeEstimateString(int recordingSeconds) {
        long bytes = getFileSizeEstimate(recordingSeconds);

        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    public AudioSettings copy() {
        AudioSettings copy = new AudioSettings();
        copy.sampleRate = this.sampleRate;
        copy.bitDepth = this.bitDepth;
        copy.channels = this.channels;
        copy.inputDevice = this.inputDevice;
        copy.outputDevice = this.outputDevice;
        copy.signed = this.signed;
        copy.bigEndian = this.bigEndian;
        copy.bufferSize = this.bufferSize;
        copy.gain = this.gain;
        copy.noiseReduction = this.noiseReduction;
        copy.autoGainControl = this.autoGainControl;
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AudioSettings that = (AudioSettings) obj;
        return Float.compare(that.sampleRate, sampleRate) == 0 &&
               bitDepth == that.bitDepth &&
               channels == that.channels &&
               signed == that.signed &&
               bigEndian == that.bigEndian &&
               bufferSize == that.bufferSize &&
               Float.compare(that.gain, gain) == 0 &&
               noiseReduction == that.noiseReduction &&
               autoGainControl == that.autoGainControl;
    }

    @Override
    public String toString() {
        return String.format("AudioSettings{sampleRate=%.0f, bitDepth=%d, channels=%d, " +
                           "bufferSize=%d, gain=%.2f, noiseReduction=%s, autoGainControl=%s}",
                           sampleRate, bitDepth, channels, bufferSize, gain, 
                           noiseReduction, autoGainControl);
    }
}