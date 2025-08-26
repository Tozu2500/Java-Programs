import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordStatistics {
    
    private Map<String, Integer> wordFrequency;
    private Map<Integer, Integer> wordLengthFrequency;
    private long totalWords;
    private long uniqueWords;
    private double averageWordLength;
    private String longestWord;
    private String shortestWord;
    private int longestWordLength;
    private int shortestWordLength;

    public WordStatistics() {
        this.wordFrequency = new HashMap<>();
        this.wordLengthFrequency = new HashMap<>();
        this.totalWords = 0;
        this.uniqueWords = 0;
        this.averageWordLength = 0.0;
        this.longestWord = "";
        this.shortestWord = "";
        this.longestWordLength = 0;
        this.shortestWordLength = Integer.MAX_VALUE;
    }

    public void analyzeWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            return;
        }

        String cleanWord = word.trim().toLowerCase();
        wordFrequency.put(cleanWord, wordFrequency.getOrDefault(cleanWord, 0) + 1);

        int length = cleanWord.length();
        wordLengthFrequency.put(length, wordLengthFrequency.getOrDefault(length, 0) + 1);

        totalWords++;

        if (length > longestWordLength) {
            longestWordLength = length;
            longestWord = cleanWord;
        }

        if (length < shortestWordLength) {
            shortestWordLength = length;
            shortestWord = cleanWord;
        }
    }

    public void calculateStatistics() {
        uniqueWords = wordFrequency.size();

        if (totalWords > 0) {
            long totalCharactersInWords = 0;
            for (String word : wordFrequency.keySet()) {
                totalCharactersInWords += word.length() * wordFrequency.get(word);
            }
            averageWordLength = (double) totalCharactersInWords / totalWords;
        }
    }

    public Map<String, Integer> getWordFrequency() {
        return new HashMap<>(wordFrequency);
    }

    public Map<Integer, Integer> getWordLengthFrequency() {
        return new HashMap<>(wordLengthFrequency);
    }

    public long getTotalWords() {
        return totalWords;
    }

    public long getUniqueWords() {
        return uniqueWords;
    }

    public double getAverageWordLength() {
        return averageWordLength;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public String getShortestWord() {
        return shortestWord;
    }

    public int getLongestWordLength() {
        return longestWordLength;
    }

    public int getShortestWordLength() {
        return shortestWordLength == Integer.MAX_VALUE ? 0 : shortestWordLength;
    }

    public String getMostFrequentWord() {
        if (wordFrequency.isEmpty()) {
            return "";
        }

        String mostFrequent = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        return mostFrequent;
    }

    public String getLeastFrequentWord() {
        if (wordFrequency.isEmpty()) {
            return "";
        }

        String leastFrequent = "";
        int minCount = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                leastFrequent = entry.getKey();
            }
        }

        return leastFrequent;
    }

    public int getWordFrequency(String word) {
        return wordFrequency.getOrDefault(word.toLowerCase(), 0);
    }

    public List<String> getWordsOfLength(int length) {
        List<String> words = new ArrayList<>();
        for (String word : wordFrequency.keySet()) {
            if (word.length() == length) {
                words.add(word);
            }
        }

        return words;
    }

    public int getMostCommonWordLength() {
        if (wordLengthFrequency.isEmpty()) {
            return 0;
        }

        int mostCommonLength = 0;
        int maxCount = 0;

        for (Map.Entry<Integer, Integer> entry : wordLengthFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonLength = entry.getKey();
            }
        }

        return mostCommonLength;
    }
}
