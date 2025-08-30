import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedTextAnalyzer {
    
    private Pattern emailPattern;
    private Pattern urlPattern;
    private Pattern phonePattern;
    private Pattern numberPattern;
    private Set<String> commonWords;

    public AdvancedTextAnalyzer() {
        initializePatterns();
        initializeCommonWords();
    }

    private void initializePatterns() {
        emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");
        urlPattern = Pattern.compile("https?://[\\w.-]+(?:\\.[\\w.-]+)+[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*");
        phonePattern = Pattern.compile("(\\+?\\d{1,4}[\\s.-]?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}");
        numberPattern = Pattern.compile("-?\\d+(?:\\.\\d+)?");
    }

    private void initializeCommonWords() {
        commonWords = new HashSet<>();
        String[] words = {"the", "be", "to", "of", "and", "a", "in", "that", "have", "i", "it", "for", 
                         "not", "on", "with", "he", "as", "you", "do", "at", "this", "but", "his", "by", 
                         "from", "they", "she", "or", "an", "will", "my", "one", "all", "would", "there", 
                         "their", "we", "him", "been", "has", "had", "which", "more", "when", "who", "oil", 
                         "its", "now", "find", "he", "up", "may", "what", "said", "each", "so", "can", "way", 
                         "about", "out", "many", "then", "them", "these", "could", "like", "her", "into", 
                         "time", "very", "when", "much", "new", "write", "go", "see", "number", "no", "come", 
                         "his", "your", "now", "people", "over", "think", "just", "where", "work", "life", 
                         "only", "without", "again", "day", "same", "another", "know", "while", "last", 
                         "might", "us", "great", "old", "year", "off", "come", "since", "against", "go", 
                         "came", "right", "used", "take", "three"};

        for (String word : words) {
            commonWords.add(word);
        }
    }

    public AdvancedTextStatistics analyzeAdvancedFeatures(String text) {
        AdvancedTextStatistics stats = new AdvancedTextStatistics();

        if (text == null || text.trim().isEmpty()) {
            return stats;
        }

        analyzeStructuralElements(text, stats);
        analyzeSpecialPatterns(text, stats);
        analyzeLexicalComplexity(text, stats);
        analyzeLanguageFeatures(text, stats);

        return stats;
    }

    private void analyzeAdvancedFeatures(String text, AdvancedTextStatistics stats) {
        String[] lines = text.split("\\r?\\n");
        int codeBlocks = 0;
        int quotes = 0;
        int lists = 0;
        int headers = 0;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.startsWith("```") || trimmed.startsWith("    ") && trimmed.length() > 4) {
                codeBlocks++;
            }

            if (trimmed.startsWith(">") || trimmed.startsWith("\"")) {
                quotes++;
            }

            if (trimmed.matches("^[\\*\\-\\+]\\s+.*") || trimmed.matches("^\\d+\\.\\s+.*")) {
                lists++;
            }

            if (trimmed.startsWith("#") || trimmed.matches("^[A-Z][A-Z\\s]+:?$")) {
                headers++;
            }
        }

        stats.setCodeBlocks(codeBlocks);
        stats.setQuotes(quotes);
        stats.setLists(lists);
        stats.setHeaders(headers);
    }

    private void analyzeSpecialPatterns(String text, AdvancedTextStatistics stats) {
        List<String> emails = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Double> numbers = new ArrayList<>();

        Matcher emailMatcher = emailPattern.matcher(text);
        while (emailMatcher.find()) {
            emails.add(emailMatcher.group());
        }

        Matcher urlMatcher = urlPattern.matcher(text);
        while (urlMatcher.find()) {
            urls.add(urlMatcher.group());
        }

        Matcher phoneMatcher = phonePattern.matcher(text);
        while (phoneMatcher.find()) {
            phoneNumbers.add(phoneMatcher.group());
        }

        Matcher numberMatcher = numberPattern.matcher(text);
        while (numberMatcher.find()) {
            try {
                numbers.add(Double.parseDouble(numberMatcher.group()));
            } catch (NumberFormatException e) {
                System.out.println("Number Format Exception -- Line 121 'AdvancedTextAnalyzer.java' " + e.getMessage());
                e.printStackTrace();
            }
        }

        stats.setEmails(emails);
        stats.setUrls(urls);
        stats.setPhoneNumbers(phoneNumbers);
        stats.setNumbers(numbers);
    }

    private void analyzeLexicalComplexity(String text, AdvancedTextStatistics stats) {
        String[] words = text.toLowerCase().split("\\W+");
        Set<String> uniqueWords = new HashSet<>();
        Set<String> contentWords = new HashSet<>();
        Map<String, Integer> wordFrequency = new HashMap<>();

        int totalWords = 0;
        int longWords = 0;
        int complexWords = 0;

        for (String word : words) {
            if (!word.isEmpty()) {
                totalWords++;
                uniqueWords.add(word);
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);

                if (!commonWords.contains(word)) {
                    contentWords.add(word);
                }

                if (word.length() >= 7) {
                    longWords++;
                }

                if (estimateSyllables(word) >= 3) {
                    complexWords++;
                }
            }
        }

        double lexicalDiversity = totalWords > 0 ? (double) uniqueWords.size() / totalWords : 0;
        double contentWordRatio = totalWords > 0 ? (double) contentWords.size() / totalWords : 0;

        stats.setLexicalDiversity(lexicalDiversity);
        stats.setContentWordRatio(contentWordRatio);
        stats.setLongWords(longWords);
        stats.setComplexWords(complexWords);

        calculateWordFrequencyDistribution(wordFrequency, stats);
    }

    private void analyzeLanguageFeatures(String text, AdvancedTextStatistics stats) {
        int questions = countOccurrences(text, "\\?");
        int exclamations = countOccurrences(text, "!");
        int allCapsWords = countPattern(text, "\\\\b[A-Z]{2,}\\\\b");
        int contractions = countPattern(text, "\\\\b\\\\w+'\\\\w+\\\\b");
        int abbreviations = countPattern(text, "\\\\b[A-Z]{2,}\\\\.?\\\\b");

        stats.setQuestions(questions);
        stats.setExclamations(exclamations);
        stats.setAllCapsWords(allCapsWords);
        stats.setContractions(contractions);
        stats.setAbbreviations(abbreviations);

        analyzeSentenceComplexity(text, stats);
    }

    private void analyzeSentenceComplexity(String text, AdvancedTextStatistics stats) {
        String[] sentences = text.split("[.!?]+");
        int totalSentences = sentences.length;
        int complexSentences = 0;
        int simpleSentences = 0;
        double totalWordsInSentences = 0;

        for (String sentence : sentences) {
            if (sentence.trim().isEmpty()) continue;

            String[] words = sentence.trim().split("\\s+");
            int wordCount = words.length;
            totalWordsInSentences += wordCount;

            if (wordCount > 20 || sentence.contains(",") && sentence.contains(";")) {
                complexSentences++;
            } else if (wordCount <= 10) {
                simpleSentences++;
            }
        }

        double avgWordsPerSentence = totalSentences > 0 ? totalWordsInSentences / totalSentences : 0;

        stats.setComplexSentences(complexSentences);
        stats.setSimpleSentences(simpleSentences);
        stats.setAverageWordsPerSentece(avgWordsPerSentence);
    }

    private void calculateWordFrequencyDistribution(Map<String, Integer> wordFrequency, AdvancedTextStatistics stats) {
        List<Integer> frequencies = new ArrayList<>(wordFrequency.values());
        Collections.sort(frequencies, Collections.reverseOrder());

        Map<Integer, Integer> frequencyDistribution = new HashMap<>();
        for (int freq : frequencies) {
            frequencyDistribution.put(freq, frequencyDistribution.getOrDefault(freq, 0) + 1);
        }

        stats.setWordFrequencyDistribution(frequencyDistribution);

        int hapaxLegomena = 0;
        int disLegomena = 0;

        for (int freq : frequencies) {
            if (freq == 1) hapaxLegomena++;
            else if (freq == 2) disLegomena++;
        }

        stats.setHapaxLegomena(hapaxLegomena);
        stats.setDisLegomena(disLegomena);
    }

    private int countOccurrences(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    private int countPattern(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    private int estimateSyllables(String word) {
        if (word == null || word.length() == 0) return 0;

        word = word.toLowerCase().replaceAll("[^a-z]", "");
        if (word.length() <= 3) return 1;

        int syllables = 0;
        boolean previousWasVowel = false;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            boolean isVowel = isVowel(c);

            if (isVowel && !previousWasVowel) {
                syllables++;
            }

            previousWasVowel = isVowel;
        }

        if (word.endsWith("e")) {
            syllables--;
        }

        return Math.max(1, syllables);
    }

    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y';
    }

    public LanguageDetectionResult detectLanguage(String text) {
        LanguageDetectionResult result = new LanguageDetectionResult();

        if (text == null || text.trim().isEmpty()) {
            result.setLanguage("Unknown");
            result.setConfidence(0.0);
            return result;
        }

        Map<String, Double> languageScores = new HashMap<>();

        analyzeEnglishFeatures(text, languageScores);
        analyzeCodeFeatures(text, languageScores);
        analyzeMarkdownFeatures(text, languageScores);

        String detectedLanguage = "Unknown";
        double maxScore = 0.0;

        for (Map.Entry<String, Double> entry : languageScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                detectedLanguage = entry.getValue();
            }
        }

        result.setLanguage(detectedLanguage);
        result.setConfidence(Math.min(maxScore, 1.0));
        result.setLanguageScores(languageScores);

        return result;
    }

    private void analyzeEnglishFeatures(String text, Map<String, Double> scores) {
        String lowerText = text.toLowerCase();
        double score = 0.0;

        for (String commonWord : commonWords) {
            if (lowerText.contains(" " + commonWord + " ") || 
                lowerText.startsWith(commonWord + " ") || 
                lowerText.endsWith(" " + commonWord)) {
                score += 0.1;
            }
        }

        if (text.matches(".*\\\\b(the|and|or|but|if|when|where|how|what|who|why)\\\\b.*")) {
            score += 2.0;
        }

        scores.put("English", score);
    }

    private void analyzeCodeFeatures(String text, Map<String, Double> scores) {
        double score = 0.0;

        String[] codeKeywords = {"public", "private", "class", "function", "def", "var", "int", 
                                "String", "boolean", "if", "else", "for", "while", "return", 
                                "import", "include", "#include", "package", "namespace"};

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                score += 1.0;
            }
        }

        if (text.contains("{") && text.contains("}")) score += 2.0;
        if (text.contains("(") && text.contains(")")) score += 1.0;
        if (text.contains(";")) score += 1.0;
        if (text.contains("//") || text.contains("/*")) score += 1.0;

        scores.put("Code", score);
    }

    private void analyzeMarkdownFeatures(String text, Map<String, Double> scores) {
        double score = 0.0;

        if (text.contains("#")) score += 1.0;
        if (text.contains("**") || text.contains("*")) score += 1.0;
        if (text.contains("```")) score += 2.0;
        if (text.contains("[") && text.contains("]") && text.contains("(") && text.contains(")")) score += 1.5;
        if (text.contains("|")) score += 1.0;

        scores.put("Markdown", score);
    }
}
