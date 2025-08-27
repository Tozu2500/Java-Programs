import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TextAnalyzer {
    
    private FileStatistics fileStats;
    private CharacterStatistics charStats;
    private WordStatistics wordStats;
    private Pattern wordPattern;
    private Pattern sentencePattern;

    public TextAnalyzer() {
        this.fileStats = new FileStatistics();
        this.charStats = new CharacterStatistics();
        this.wordStats = new WordStatistics();
        this.wordPattern = Pattern.compile("\\b\\w+\\b");
        this.sentencePattern = Pattern.compile("[.!?]+");
    }

    public void analyzeFile(FileReader fileReader) {
        if (!fileReader.isFileLoaded()) {
            System.err.println("File isn't loaded. Can't perform analysis.");
            return;
        }

        resetStatistics();

        List<String> lines = fileReader.getLines();
        analyzeLines(lines);
        calculateDerivedStatistics();
    }

    private void resetStatistics() {
        fileStats = new FileStatistics();
        charStats = new CharacterStatistics();
        wordStats = new WordStatistics();
    }

    private void analyzeLines(List<String> lines) {
        long totalLines = lines.size();
        long emptyLines = 0;
        long nonEmptyLines = 0;
        long totalCharacters = 0;
        long totalCharactersNoSpaces = 0;
        long totalWords = 0;
        long totalSentences = 0;
        long totalParagraphs = 0;
        long longestLineLength = 0;
        long shortestLineLength = Long.MAX_VALUE;
        String longestLine = "";
        String shortestLine = "";

        boolean inParagraph = false;

        for (String line : lines) {
            int lineLength = line.length();

            if (line.trim().isEmpty()) {
                emptyLines++;
                inParagraph = false;
            } else {
                nonEmptyLines++;

                if (!inParagraph) {
                    totalParagraphs++;
                    inParagraph = true;
                }

                if (lineLength > longestLineLength) {
                    longestLineLength = lineLength;
                    longestLine = line;
                }

                if (lineLength < shortestLineLength) {
                    shortestLineLength = lineLength;
                    shortestLine = line;
                }
            }

            totalCharacters += lineLength;

            for (char ch : line.toCharArray()) {
                charStats.analyzeCharacter(ch);
                if (!Character.isWhitespace(ch)) {
                    totalCharactersNoSpaces++;
                }
            }

            String[] words = line.split("\\s+");
            for (String word : words) {
                word = word.replaceAll("[^\\w]", "").trim();
                if (!word.isEmpty()) {
                    totalWords++;
                    wordStats.analyzeWord(word);
                }
            }

            String[] sentences = sentencePattern.split(line);
            totalSentences += Math.max(0, sentences.length - 1);
            if (line.matches(".*[.!?].*")) {
                totalSentences += 1;
            }
        }

        if (shortestLineLength == Long.MAX_VALUE) {
            shortestLineLength = 0;
        }

        fileStats.setTotalLines(totalLines);
        fileStats.setEmptyLines(emptyLines);
        fileStats.setNonEmptyLines(nonEmptyLines);
        fileStats.setTotalWords(totalWords);
        fileStats.setTotalCharacters(totalCharacters);
        fileStats.setTotalCharactersNoSpaces(totalCharactersNoSpaces);
        fileStats.setTotalParagraphs(totalParagraphs);
        fileStats.setTotalSentences(totalSentences);
        fileStats.setLongestLineLength(longestLineLength);
        fileStats.setShortestLineLength(shortestLineLength);
        fileStats.setLongestLine(longestLine);
        fileStats.setShortestLine(shortestLine);
    }

    private void calculateDerivedStatistics() {
        wordStats.calculateStatistics();

        long totalLines = fileStats.getTotalLines();
        long totalWords = fileStats.getTotalWords();
        long totalSentences = fileStats.getTotalSentences();
        long totalCharacters = fileStats.getTotalCharacters();

        if (totalLines > 0) {
            double averageWordsPerLine = (double) totalWords / totalLines;
            double averageCharactersPerLine = (double) totalCharacters / totalLines;
            fileStats.setAverageWordsPerLine(averageWordsPerLine);
            fileStats.setAverageCharactersPerLine(averageCharactersPerLine);
        }

        if (totalSentences > 0) {
            double averageWordsPerSentence = (double) totalWords / totalSentences;
            fileStats.setAverageWordsPerSentence(averageWordsPerSentence);
        }
    }

    public FileStatistics getFileStatistics() {
        return fileStats;
    }

    public CharacterStatistics getCharacterStatistics() {
        return charStats;
    }

    public WordStatistics getWordStatistics() {
        return wordStats;
    }

    public void analyzeText(String text) {
        if (text == null) {
            text = "";
        }

        resetStatistics();

        String[] lines = text.split("\\r?\\n");
        List<String> lineList = new ArrayList<>();
        for (String line : lines) {
            lineList.add(line);
        }

        analyzeLines(lineList);
        calculateDerivedStatistics();
    }

    public ReadabilityMetrics calculateReadability(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ReadabilityMetrics();
        }

        analyzeText(text);

        long totalWords = fileStats.getTotalWords();
        long totalSentences = fileStats.getTotalSentences();
        long totalSyllables = estimateSyllables(text);

        ReadabilityMetrics metrics = new ReadabilityMetrics();

        if (totalSentences > 0 && totalWords > 0) {
            double averageWordsPerSentence = (double) totalWords / totalSentences;
            double averageSyllablesPerWord = (double) totalSyllables / totalWords;

            double fleschScore = 206.835 - (1.015 * averageWordsPerSentence) - (84.6 * averageSyllablesPerWord);
            metrics.setFleschReadingEase(fleschScore);

            double fleschKincaidGrade = (0.39 * averageWordsPerSentence) + (11.8 * averageSyllablesPerWord) - 15.59;
            metrics.setFleschKincaidGradeLevel(fleschKincaidGrade);
        }

        return metrics;
    }

    private long estimateSyllables(String text) {
        long syllables = 0;
        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            if (word.length() > 0) {
                syllables += countSyllablesInWord(word);
            }
        }

        return syllables;
    }

    private int countSyllablesInWord(String word) {
        if (word == null || word.length() == 0) {
            return 0;
        }

        word = word.toLowerCase().replaceAll("[^a-z]", "");

        if (word.length() <= 3) {
            return 1;
        }

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
}
