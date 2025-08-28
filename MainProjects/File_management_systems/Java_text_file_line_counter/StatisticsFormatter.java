import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsFormatter {

    private DecimalFormat decimalFormat;

    public StatisticsFormatter() {
        this.decimalFormat = new DecimalFormat("#.##");
    }

    public String formatFileStatistics(FileStatistics stats, String fileName) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== File Statistics For: ").append(fileName).append(" ===\n\n");

        sb.append("Line Statistics:\n");
        sb.append("  Total lines: ").append(stats.getTotalLines()).append("\n");
        sb.append("  Empty lines: ").append(stats.getEmptyLines()).append("\n");
        sb.append("  Non-empty lines: ").append(stats.getNonEmptyLines()).append("\n");
        sb.append("  Longest line length: ").append(stats.getLongestLineLength()).append(" characters\n");
        sb.append("  Shortest line length: ").append(stats.getShortestLineLength()).append(" characters\n");

        if (!stats.getLongestLine().isEmpty()) {
            String longestPreview = stats.getLongestLine().length() > 50 ?
                stats.getLongestLine().substring(0, 50) + "..." : stats.getLongestLine();
            sb.append("  Longest line preview: \"").append(longestPreview).append("\"\n");
        }

        if (!stats.getShortestLine().isEmpty()) {
            String shortestPreview = stats.getShortestLine().length() > 50 ?
                stats.getShortestLine().substring(0, 50) + "..." : stats.getShortestLine();
            sb.append("  Shortest line preview: \"").append(shortestPreview).append("\"\n");
        }

        sb.append("\nText Statistics:\n");
        sb.append("  Total words: ").append(stats.getTotalWords()).append("\n");
        sb.append("  Total characters: ").append(stats.getTotalCharacters()).append("\n");
        sb.append("  Total characters, no spaces: ").append(stats.getTotalCharactersNoSpaces()).append("\n");
        sb.append("  Total paragraphs: ").append(stats.getTotalParagraphs()).append("\n");
        sb.append("  Total sentences: ").append(stats.getTotalSentences()).append("\n");

        sb.append("\nAverages:\n");
        sb.append("  Average words per line: ").append(decimalFormat.format(stats.getAverageWordsPerLine())).append("\n");
        sb.append("  Average character per line: ").append(decimalFormat.format(stats.getAverageCharactersPerLine())).append("\n");
        sb.append("  Average words per sentence: ").append(decimalFormat.format(stats.getAverageWordsPerSentence())).append("\n");

        return sb.toString();
    }

    public String formatCharacterStatistics(CharacterStatistics stats) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== Character Statistics ===\n\n");

        sb.append("Character type counts:\n");
        sb.append("  Alphabetical characters: ").append(stats.getAlphabeticalCharacters()).append("\n");
        sb.append("  Numeric characters ").append(stats.getNumericCharacters()).append("\n");
        sb.append("  Special characters: ").append(stats.getSpecialCharacters()).append("\n");
        sb.append("  Whitespace characters: ").append(stats.getWhitespaceCharacters()).append("\n");
        sb.append("  Uppercase letters: ").append(stats.getUppercaseLetters()).append("\n");
        sb.append("  Lowercase letters: ").append(stats.getLowercaseLetters()).append("\n");
        sb.append("  Vowels: ").append(stats.getVowels()).append("\n");
        sb.append("  Consonants: ").append(stats.getConsonants()).append("\n");
        sb.append("  Punctuation marks: ").append(stats.getPunctuationMarks()).append("\n");

        sb.append("\nCharacter Frequency Analysis:\n");
        sb.append("  Unique characters: ").append(stats.getUniqueCharacterCount()).append("\n");

        char mostFrequent = stats.getMostFrequentCharacter();
        if (mostFrequent != '\0') {
            String charDisplay = Character.isWhitespace(mostFrequent) ? "SPACE" : String.valueOf(mostFrequent);
            sb.append("  Most frequent character: '").append(charDisplay).append("' (").append(stats.getCharacterFrequency(mostFrequent)).append(" times)\n");
        }

        char leastFrequent = stats.getLeastFrequentCharacter();
        if (leastFrequent != '\0') {
            String charDisplay = Character.isWhitespace(leastFrequent) ? "SPACE" : String.valueOf(leastFrequent);
            sb.append("  Least frequent character: '").append(charDisplay).append("' (").append(stats.getCharacterFrequency(leastFrequent)).append(" times)\n");
        }

        sb.append("\nTop 10 Most Frequent Characters:\n");
        Map<Character, Integer> charFreq = stats.getCharacterFrequency();
        List<Map.Entry<Character, Integer>> sortedChars = new ArrayList<>(charFreq.entrySet());
        sortedChars.sort(Map.Entry.<Character, Integer>comparingByValue().reversed());

        int count = 0;
        for (Map.Entry<Character, Integer> entry : sortedChars) {
            if (count >= 10) break;
            char ch = entry.getKey();
            String charDisplay = Character.isWhitespace(ch) ? "SPACE" : String.valueOf(ch);
            sb.append(" ").append(count + 1).append(". '").append(charDisplay).append("': ").append(entry.getValue()).append(" times\n");
            count++;
        }

        return sb.toString();
    }

    public String formatWordStatistics(WordStatistics stats) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== Word Statistics ===\n\n");

        sb.append("Word counts:\n");
        sb.append("  Total words: ").append(stats.getTotalWords()).append("\n");
        sb.append("  Unique words: ").append(stats.getUniqueWords()).append("\n");

        if (stats.getTotalWords() > 0) {
            double uniquePercentage = (double) stats.getUniqueWords() / stats.getTotalWords() * 100;
            sb.append("  Vocabulary richness: ").append(decimalFormat.format(uniquePercentage)).append("%\n");
        }

        sb.append("\nWord Length Analysis:\n");
        sb.append("  Average word length: ").append(decimalFormat.format(stats.getAverageWordLength())).append(" characters\n");
        sb.append("  Longest word: \"").append(stats.getLongestWord()).append("\" (").append(stats.getLongestWordLength()).append(" characters)\n");
        sb.append("  Shortest word: \"").append(stats.getShortestWord()).append("\" (").append(stats.getShortestWordLength()).append(" characters)\n");
        sb.append("  Most common word length: ").append(stats.getMostCommonWordLength()).append(" characters\n");

        sb.append("\nFrequency analysis:\n");
        sb.append("  Most frequent word: \"").append(stats.getMostFrequentWord()).append("\" (").append(stats.getWordFrequency(stats.getMostFrequentWord())).append(" times)\n");
        sb.append("  Least frequent word: \"").append(stats.getLeastFrequentWord()).append("\" (").append(stats.getWordFrequency(stats.getLeastFrequentWord())).append(" times)\n");

        sb.append("\nTop 15 Most Frequent Words:\n");
        Map<String, Integer> wordFreq = stats.getWordFrequency();
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordFreq.entrySet());
        sortedWords.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedWords) {
            if (count >= 15) break;
            sb.append(" ").append(count + 1).append(". \"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(" times\n");
            count++;
        }

        sb.append("\nWord Length Distribution:\n");
        Map<Integer, Integer> lengthFreq = stats.getWordLengthFrequency();
        List<Map.Entry<Integer, Integer>> sortedLengths = new ArrayList<>(lengthFreq.entrySet());
        sortedLengths.sort(Map.Entry.comparingByKey());

        for (Map.Entry<Integer, Integer> entry : sortedLengths) {
            double percentage = (double) entry.getValue() / stats.getTotalWords() * 100;
            sb.append(" ").append(entry.getKey()).append(" characters: ").append(entry.getValue()).append(" words(").append(decimalFormat.format(percentage)).append("%)\n");
        }

        return sb.toString();
    }

    public String formatReadabilityMetrics(ReadabilityMetrics metrics) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=== Readability Analysis ===\n\n");

        sb.append("Readability Scores:\n");
        sb.append("  Flesch reading ease: ").append(decimalFormat.format(metrics.getFleschReadingEase())).append(" (").append(metrics.getReadingLevel()).append(")\n");
        sb.append("  Flesch-Kincaid grade level: ").append(decimalFormat.format(metrics.getFleschKincaidGradeLevel())).append("\n");

        if (metrics.getAutomatedReadabilityIndex() > 0) {
            sb.append("  Automated readability index: ").append(decimalFormat.format(metrics.getAutomatedReadabilityIndex())).append("\n");
        }

        if (metrics.getColemanLiauIndex() > 0) {
            sb.append("  Coleman-Liau index: ").append(decimalFormat.format(metrics.getColemanLiauIndex())).append("\n");
        }

        if (metrics.getGunningFogIndex() > 0) {
            sb.append("  Gunning Fog index: ").append(decimalFormat.format(metrics.getGunningFogIndex())).append("\n");
        }

        if (metrics.getSmogIndex() > 0) {
            sb.append("  SMOG Index: ").append(decimalFormat.format(metrics.getSmogIndex())).append("\n");
        }

        double avgGrade = metrics.getAverageGradeLevel();
        if (avgGrade > 0) {
            sb.append("  Average grade level: ").append(decimalFormat.format(avgGrade)).append("\n");
        }

        sb.append("\nReadability Interpretation:\n");
        sb.append("  The text has a reading difficulty level of: ").append(metrics.getReadingLevel()).append("\n");

        if (metrics.getFleschReadingEase() >= 60) {
            sb.append("  This text should be easily understood by most readers.\n");
        } else if (metrics.getFleschReadingEase() >= 30) {
            sb.append("  This text may require a higher education level to understand comfortably.\n");
        } else {
            sb.append("  This text is quite difficult and may require specialized knowledge or graduate-level education.\n");
        }

        return sb.toString();
    }

    public String formatCompleteSummary(FileStatistics fileStats, CharacterStatistics charStats,
                                        WordStatistics wordStats, ReadabilityMetrics readability, String fileName) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== COMPLETE TEXT ANALYSIS SUMMARY ===\n");
        sb.append("File: ").append(fileName).append("\n");
        sb.append("Analysis Date: ").append(LocalDateTime.now().toString()).append("\n\n");

        sb.append("Quick Overview:\n");
        sb.append("  |  Lines: ").append(fileStats.getTotalLines());
        sb.append("  |  Characters: ").append(fileStats.getTotalCharacters());
        sb.append("  |  Paragraphs: ").append(fileStats.getTotalParagraphs()).append("\n");
        sb.append("  Reading Level: ").append(readability.getReadingLevel()).append("\n");
        sb.append("  Vocabulary Richness: ");
        if (wordStats.getTotalWords() > 0) {
            double richness = (double) wordStats.getUniqueWords() / wordStats.getTotalWords() * 100;
            sb.append(decimalFormat.format(richness)).append("%");
        } else {
            sb.append("N/A");
        }
        sb.append("\n\n");

        sb.append(formatFileStatistics(fileStats, fileName));
        sb.append(formatCharacterStatistics(charStats));
        sb.append(formatWordStatistics(wordStats));
        sb.append(formatReadabilityMetrics(readability));

        return sb.toString();
    }
}