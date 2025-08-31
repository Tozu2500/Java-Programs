import java.util.HashMap;
import java.util.Map;

public class LanguageDetectionResult {
    
    private String language;
    private double confidence;
    private Map<String, Double> languageScores;

    public LanguageDetectionResult() {
        this.language = "Unknown";
        this.confidence = 0.0;
        this.languageScores = new HashMap<>();
    }

    public LanguageDetectionResult(String language, double confidence) {
        this.language = language;
        this.confidence = confidence;
        this.languageScores = new HashMap<>();
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setConfidence(double confidence) {
        this.confidence = Math.max(0.0, Math.min(1.0, confidence));
    }

    public double getConfidence() {
        return confidence;
    }

    public void setLanguageScores(Map<String, Double> languageScores) {
        this.languageScores = new HashMap<>(languageScores);
    }

    public Map<String, Double> getLanguageScores() {
        return new HashMap<>(languageScores);
    }

    public void addLanguageScore(String language, double score) {
        languageScores.put(language, score);
    }

    public double getLanguageScore(String language) {
        return languageScores.getOrDefault(language, 0.0);
    }

    public String getConfidenceLevel() {
        if (confidence >= 0.9) return "Very High";
        else if (confidence >= 0.7) return "High";
        else if (confidence >= 0.5) return "Medium";
        else if (confidence >= 0.3) return "Low";
        else return "Very Low";
    }

    public boolean isReliable() {
        return confidence >= 0.6;
    }

    public String getSecondMostLikelyLanguage() {
        String secondBest = "Unknown";
        double secondBestScore = 0.0;
        String currentBest = language;

        for (Map.Entry<String, Double> entry : languageScores.entrySet()) {
            if (!entry.getKey().equals(currentBest) && entry.getValue() > secondBestScore) {
                secondBestScore = entry.getValue();
                secondBest = entry.getKey();
            }
        }

        return secondBest;
    }

    public double getSecondMostLikelyScore() {
        String secondBest = getSecondMostLikelyLanguage();
        return languageScores.getOrDefault(secondBest, 0.0);
    }

    public String formatResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("Detected Language: ").append(language).append("\n");
        sb.append("Confidence: ").append(String.format("%.2f", confidence * 100)).append("% (").append(getConfidenceLevel()).append(")\n");

        if (!languageScores.isEmpty()) {
            sb.append("Language Scores:\n");
            languageScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry ->
                    sb.append(" ").append(entry.getKey()).append(": ")
                    .append(String.format("%.2f", entry.getValue())).append("\n"));
        }

        return sb.toString();
    }
}
