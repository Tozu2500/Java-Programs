public class ExportSettings {

    private String format = "wav";
    private float volumeAdjustment = 1.0f;
    private boolean normalizeAudio = false;
    private boolean fadeIn = false;
    private boolean fadeOut = false;
    private float fadeInDuration = 1.0f;
    private float fadeOutDuration = 1.0f;
    private int bitRate = 320;
    private boolean stereoToMono = false;

    public ExportSettings() {}

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public float getVolumeAdjustment() {
        return volumeAdjustment;
    }

    public void setVolumeAdjustment(float volumeAdjustment) {
        this.volumeAdjustment = Math.max(0.0f, Math.min(2.0f, volumeAdjustment));
    }

    public boolean isNormalizeAudio() {
        return normalizeAudio;
    }

    public void setNormalizeAudio(boolean normalizeAudio) {
        this.normalizeAudio = normalizeAudio;
    }

    public boolean isFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(boolean fadeIn) {
        this.fadeIn = fadeIn;
    }

    public boolean isFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(boolean fadeOut) {
        this.fadeOut = fadeOut;
    }

    public float getFadeInDuration() {
        return fadeInDuration;
    }

    public void setFadeInDuration(float fadeInDuration) {
        this.fadeInDuration = Math.max(0.1f, Math.min(10.0f, fadeInDuration));
    }

    public float getFadeOutDuration() {
        return fadeOutDuration;
    }

    public void setFadeOutDuration(float fadeOutDuration) {
        this.fadeOutDuration = Math.max(0.1f, Math.min(10.0f, fadeOutDuration));
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public boolean isStereoToMono() {
        return stereoToMono;
    }

    public void setStereoToMono(boolean stereoToMono) {
        this.stereoToMono = stereoToMono;
    }

    @Override
    public String toString() {
        return String.format("ExportSettings{format='%s', volume=%.2f, normalize=%s, fadeIn=%s, fadeOut=%s}",
                format, volumeAdjustment, normalizeAudio, fadeIn, fadeOut);
    }

}
