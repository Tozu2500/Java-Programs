import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EnhancedStatisticsFormatter extends StatisticsFormatter {
    private DecimalFormat percentFormat;
    
    public EnhancedStatisticsFormatter() {
        super();
        this.percentFormat = new DecimalFormat("#.##%");
    }
    
    public String formatAdvancedStatistics(AdvancedTextStatistics stats) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n=== ADVANCED TEXT ANALYSIS ===\n\n");
        
        formatStructuralElements(sb, stats);
        formatSpecialPatterns(sb, stats);
        formatLexicalComplexity(sb, stats);
        formatLanguageFeatures(sb, stats);
        formatComplexityAnalysis(sb, stats);
        
        return sb.toString();
    }
    
    private void formatStructuralElements(StringBuilder sb, AdvancedTextStatistics stats) {
        sb.append("Structural Elements:\n");
        sb.append("  Code Blocks: ").append(stats.getCodeBlocks()).append("\n");
        sb.append("  Quotes/Citations: ").append(stats.getQuotes()).append("\n");
        sb.append("  Lists: ").append(stats.getLists()).append("\n");
        sb.append("  Headers/Titles: ").append(stats.getHeaders()).append("\n");
        sb.append("  Total Structural Elements: ").append(stats.getTotalStructuralElements()).append("\n");
        sb.append("  Has Structure: ").append(stats.hasStructuralElements() ? "Yes" : "No").append("\n\n");
    }
    
    private void formatSpecialPatterns(StringBuilder sb, AdvancedTextStatistics stats) {
        sb.append("Special Patterns:\n");
        
        List<String> emails = stats.getEmails();
        sb.append("  Email Addresses: ").append(emails.size()).append("\n");
        if (!emails.isEmpty() && emails.size() <= 5) {
            for (String email : emails) {
                sb.append("    - ").append(email).append("\n");
            }
        } else if (emails.size() > 5) {
            for (int i = 0; i < 5; i++) {
                sb.append("    - ").append(emails.get(i)).append("\n");
            }
            sb.append("    ... and ").append(emails.size() - 5).append(" more\n");
        }
        
        List<String> urls = stats.getUrls();
        sb.append("  URLs: ").append(urls.size()).append("\n");
        if (!urls.isEmpty() && urls.size() <= 3) {
            for (String url : urls) {
                String displayUrl = url.length() > 50 ? url.substring(0, 50) + "..." : url;
                sb.append("    - ").append(displayUrl).append("\n");
            }
        } else if (urls.size() > 3) {
            for (int i = 0; i < 3; i++) {
                String displayUrl = urls.get(i).length() > 50 ? urls.get(i).substring(0, 50) + "..." : urls.get(i);
                sb.append("    - ").append(displayUrl).append("\n");
            }
            sb.append("    ... and ").append(urls.size() - 3).append(" more\n");
        }
        
        List<String> phoneNumbers = stats.getPhoneNumbers();
        sb.append("  Phone Numbers: ").append(phoneNumbers.size()).append("\n");
        if (!phoneNumbers.isEmpty() && phoneNumbers.size() <= 5) {
            for (String phone : phoneNumbers) {
                sb.append("    - ").append(phone).append("\n");
            }
        }
        
        List<Double> numbers = stats.getNumbers();
        sb.append("  Numeric Values: ").append(numbers.size()).append("\n");
        if (!numbers.isEmpty()) {
            sb.append("    Sum: ").append(String.format("%.2f", stats.getNumberStatisticsSum())).append("\n");
            sb.append("    Average: ").append(String.format("%.2f", stats.getNumberStatisticsAverage())).append("\n");
            sb.append("    Min: ").append(String.format("%.2f", stats.getNumberStatisticsMin())).append("\n");
            sb.append("    Max: ").append(String.format("%.2f", stats.getNumberStatisticsMax())).append("\n");
        }
        
        sb.append("  Has Contact Info: ").append(stats.hasContactInformation() ? "Yes" : "No").append("\n");
        sb.append("  Has URLs: ").append(stats.hasUrls() ? "Yes" : "No").append("\n");
        sb.append("  Has Numeric Data: ").append(stats.hasNumericData() ? "Yes" : "No").append("\n\n");
    }

    public void formatLexicalComplexity(StringBuilder sb, AdvancedTextStatistics stats) {
        sb.append("Lexical Complexity:\n");
        sb.append("  Lexical Diversity: ").append(percentFormat.format(stats.getLexicalDiversity())).append("\n");
        sb.append("  Lexical Richness: ").append(String.format("%.2f", stats.getLexicalRichness())).append("%\n");
        sb.append("  Content Word Ratio: ").append(percentFormat.format(stats.getContentWordRatio())).append("\n");
        sb.append("  Long Words (7+ characters): ").append(stats.getLongWords()).append("\n");
        sb.append("  Complex Words (3+ syllables): ").append(stats.getComplexWords()).append("\n");
        sb.append("  Hapax Legomena (words appearing once): ").append(stats.getHapaxLegomena()).append("\n");
        sb.append("  Dis Legomena (words appearing twice): ").append(stats.getDisLegomena()).append("\n\n");
    }

    private void formatLanguageFeatures(StringBuilder sb, AdvancedTextStatistics stats) {
        sb.append("Language Features:\n");
        sb.append("  Questions: ").append(stats.getQuestions()).append("\n");
        sb.append("  Exclamations: ").append(stats.getExclamations()).append("\n");
        sb.append("  ALL CAPS Words: ").append(stats.getAllCapsWords()).append("\n");
        sb.append("  Contractions: ").append(stats.getContractions()).append("\n");
        sb.append("  Abbreviations: ").append(stats.getAbbreviations()).append("\n\n");

        sb.append("Sentence Complexity:\n");
        sb.append("  Simple Sentences: ").append(stats.getSimpleSentences()).append("\n");
        sb.append("  Complex Sentences: ").append(stats.getComplexSentences()).append("\n");
        sb.append("  Average Words Per Sentence: ").append(String.format("%.2f", stats.getAverageWordsPerSentence())).append("\n\n");
    }

    private void formatComplexityAnalysis(StringBuilder sb, AdvancedTextStatistics stats) {
        sb.append("Complexity Analysis:\n");
        sb.append("  Complexity Score: ").append(String.format("%.2f", stats.getComplexityScore())).append("/5.0\n");
        sb.append("  Complexity Level: ").append(stats.getComplexityLevel()).append("\n");

        sb.append("  Analysis:\n");
        if (stats.getComplexityScore() >= 4.0) {
            sb.append("    This text is really complex with sophisticated vocabulary and sentence structures\n");
        } else if (stats.getComplexityScore() >= 3.0) {
            sb.append("    This text has a complexity above average with varied vocabulary and sentence patterns\n");
        } else if (stats.getComplexityScore() >= 2.0) {
            sb.append("    This text has a moderate complexity, suitable for general audiences\n");
        } else if (stats.getComplexityScore() >= 1.0) {
            sb.append("    This is a relatively simple text with straightforward language\n");
        } else {
            sb.append("    This text is simple with basic vocabulary and short sentences\n");
        }

        sb.append("\n");

        formatWordFrequencyDistribution(sb, stats);
    }

    private void formatWordFrequencyDistribution(StringBuilder sb, AdvancedTextStatistics stats) {
        Map<Integer, Integer> distribution = stats.getWordFrequencyDistribution();
        if (!distribution.isEmpty()) {
            sb.append("Word Frequency Distribution:\n");
            distribution.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed())
                .limit(10)
                .forEach(entry -> {
                    sb.append("  Words appearing ").append(entry.getKey())
                      .append(" time(s): ").append(entry.getValue()).append("\n");
                });
            sb.append("\n");
        }
    }

    public String formatLanguageDetection(LanguageDetectionResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== LANGUAGE DETECTION ===\n\n");
        sb.append("Detected Type: ").append(result.getLanguage()).append("\n");
        sb.append("Confidence: ").append(String.format("%.1f%%", result.getConfidence() * 100));
        sb.append(" (").append(result.getConfidenceLevel()).append(")\n");
        sb.append("Reliable Detection: ").append(result.isReliable() ? "Yes" : "No").append("\n");

        if (!result.getSecondMostLikelyLanguage().equals("Unknown")) {
            sb.append("Second Most Likely: ").append(result.getSecondMostLikelyLanguage());
            sb.append(" (").append(String.format("%.1f", result.getSecondMostLikelyScore())).append(")\n");
        }

        sb.append("\nDetection Scores:\n");
        Map<String, Double> scores = result.getLanguageScores();
        scores.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .forEach(entry ->
                sb.append(" ").append(entry.getKey()).append(": ")
                  .append(String.format("%.2f", entry.getValue())).append("\n"));

        return sb.toString();
    }

    public String formatCompleteAdvancedAnalysis(FileStatistics fileStats, CharacterStatistics charStats,
                                                WordStatistics wordStats, ReadabilityMetrics readability,
                                                AdvancedTextStatistics advancedStats,
                                                LanguageDetectionResult languageResult, String fileName) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== COMPREHENSIVE ADVANCED TEXT ANALYSIS ===\n");
        sb.append("File: ").append(fileName).append("\n");
        sb.append("Analysis Date: ").append(LocalDateTime.now().toString()).append("\n\n");

        sb.append("EXECUTIVE SUMMARY:\n");
        sb.append("  Document Type: ").append(languageResult.getLanguage()).append("\n");
        sb.append("  Complexity Level: ").append(advancedStats.getComplexityLevel()).append("\n");
        sb.append("  Reading Level: ").append(readability.getReadingLevel()).append("\n");
        sb.append("  Vocabulary Richness: ").append(String.format("%.1f%%", advancedStats.getLexicalRichness())).append("\n");
        sb.append("  Has Structure: ").append(advancedStats.hasStructuralElements() ? "Yes" : "No").append("\n");
        sb.append("  Contact Info Present: ").append(advancedStats.hasContactInformation() ? "Yes" : "No").append("\n\n");

        sb.append(formatFileStatistics(fileStats, fileName));
        sb.append(formatCharacterStatistics(charStats));
        sb.append(formatWordStatistics(wordStats));
        sb.append(formatReadabilityMetrics(readability));
        sb.append(formatAdvancedStatistics(advancedStats));
        sb.append(formatLanguageDetection(languageResult));

        return sb.toString();
    }
}
