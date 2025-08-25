public class FileStatistics {

    private long totalLines;
    private long emptyLines;
    private long nonEmptyLines;
    private long totalWords;
    private long totalCharacters;
    private long totalCharactersNoSpaces;
    private long totalParagraphs;
    private long totalSentences;
    private double averageWordsPerLine;
    private double averageCharactersPerLine;
    private double averageWordsPerSentence;
    private long longestLineLength;
    private long shortestLineLength;
    private String longestLine;
    private String shortestLine;

    public FileStatistics() {
        this.totalLines = 0;
        this.emptyLines = 0;
        this.nonEmptyLines = 0;
        this.totalWords = 0;
        this.totalCharacters = 0;
        this.totalCharactersNoSpaces = 0;
        this.totalParagraphs = 0;
        this.totalSentences = 0;
        this.averageWordsPerLine = 0;
        this.averageCharactersPerLine = 0;
        this.averageWordsPerSentence = 0;
        this.longestLineLength = 0;
        this.shortestLineLength = Long.MAX_VALUE;
        this.longestLine = "";
        this.shortestLine = "";
    }

    public void setTotalLines(long totalLines) {
        this.totalLines = totalLines;
    }

    public long getTotalLines() {
        return totalLines;
    }

    public void setEmptyLines(long emptyLines) {
        this.emptyLines = emptyLines;
    }

    public long getEmptyLines() {
        return emptyLines;
    }

    public void setNonEmptyLines(long nonEmptyLines) {
        this.nonEmptyLines = nonEmptyLines;
    }

    public long getNonEmptyLines() {
        return nonEmptyLines;
    }

    public void setTotalWords(long totalWords) {
        this.totalWords = totalWords;
    }

    public long getTotalWords() {
        return totalWords;
    }

    public void setTotalCharacters(long totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public long getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharactersNoSpaces(long totalCharactersNoSpaces) {
        this.totalCharactersNoSpaces = totalCharactersNoSpaces;
    }
    
    public long getTotalCharactersNoSpaces() {
        return totalCharactersNoSpaces;
    }

    public void setTotalParagraphs(long totalParagraphs) {
        this.totalParagraphs = totalParagraphs;
    }

    public long getTotalParagraphs() {
        return totalParagraphs;
    }

    public void setTotalSentences(long totalSentences) {
        this.totalSentences = totalSentences;
    }

    public long getTotalSentences() {
        return totalSentences;
    }

    public void setAverageWordsPerLine(double averageWordsPerLine) {
        this.averageWordsPerLine = averageWordsPerLine;
    }

    public double getAverageWordsPerLine() {
        return averageWordsPerLine;
    }

    public void setAverageCharactersPerLine(double averageCharactersPerLine) {
        this.averageCharactersPerLine = averageCharactersPerLine;
    }

    public double getAverageCharactersPerLine() {
        return averageCharactersPerLine;
    }

    public void setAverageWordsPerSentence(double averageWordsPerSentence) {
        this.averageWordsPerSentence = averageWordsPerSentence;
    }

    public double getAverageWordsPerSentence() {
        return averageWordsPerSentence;
    }

    public void setLongestLineLength(long longestLineLength) {
        this.longestLineLength = longestLineLength;
    }

    public long getLongestLineLength() {
        return longestLineLength;
    }

    public void setShortestLineLength(long shortestLineLength) {
        this.shortestLineLength = shortestLineLength;
    }

    public long getShortestLineLength() {
        return shortestLineLength;
    }

    public void setLongestLine(String longestLine) {
        this.longestLine = longestLine;
    }

    public String getLongestLine() {
        return longestLine;
    }

    public void setShortestLine(String shortestLine) {
        this.shortestLine = shortestLine;
    }

    public String getShortestLine() {
        return shortestLine;
    }

}