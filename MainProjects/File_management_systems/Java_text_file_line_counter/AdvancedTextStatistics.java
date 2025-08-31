import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedTextStatistics {
    
    private int codeBlocks;
    private int quotes;
    private int lists;
    private int headers;
    private List<String> emails;
    private List<String> urls;
    private List<String> phoneNumbers;
    private List<Double> numbers;
    private double lexicalDiversity;
    private double contentWordRatio;
    private int longWords;
    private int complexWords;
    private int questions;
    private int exclamations;
    private int allCapsWords;
    private int contractions;
    private int abbreviations;
    private int complexSentences;
    private int simpleSentences;
    private double averageWordsPerSentence;
    private Map<Integer, Integer> wordFrequencyDistribution;
    private int hapaxLegomena;
    private int disLegomena;

    public AdvancedTextStatistics() {
        this.codeBlocks = 0;
        this.quotes = 0;
        this.lists = 0;
        this.headers = 0;
        this.emails = new ArrayList<>();
        this.urls = new ArrayList<>();
        this.phoneNumbers = new ArrayList<>();
        this.numbers = new ArrayList<>();
        this.lexicalDiversity = 0.0;
        this.contentWordRatio = 0.0;
        this.longWords = 0;
        this.complexWords = 0;
        this.questions = 0;
        this.exclamations = 0;
        this.allCapsWords = 0;
        this.contractions = 0;
        this.abbreviations = 0;
        this.complexSentences = 0;
        this.simpleSentences = 0;
        this.averageWordsPerSentence = 0.0;
        this.wordFrequencyDistribution = new HashMap<>();
        this.hapaxLegomena = 0;
        this.disLegomena = 0;
    }

    public void setCodeBlocks(int codeBlocks) {
        this.codeBlocks = codeBlocks;
    }

    public int getCodeBlocks() {
        return codeBlocks;
    }

    public void setQuotes(int quotes) {
        this.quotes = quotes;
    }

    public int getQuotes() {
        return quotes;
    }

    public void setLists(int lists) {
        this.lists = lists;
    }

    public int getLists() {
        return lists;
    }

    public void setHeaders(int headers) {
        this.headers = headers;
    }

    public int getHeaders() {
        return headers;
    }

    public void setEmails(List<String> emails) {
        this.emails = new ArrayList<>(emails);
    }

    public List<String> getEmails() {
        return new ArrayList<>(emails);
    }

    public void setUrls(List<String> urls) {
        this.urls = new ArrayList<>(urls);
    }

    public List<String> getUrls() {
        return new ArrayList<>(urls);
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = new ArrayList<>(phoneNumbers);
    }

    public List<String> getPhoneNumbers() {
        return new ArrayList<>(phoneNumbers);
    }

    public void setNumbers(List<Double> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }
    
    public List<Double> getNumbers() {
        return new ArrayList<>(numbers);
    }

    public void setLexicalDiversity(double lexicalDiversity) {
        this.lexicalDiversity = lexicalDiversity;
    }

    public double getLexicalDiversity() {
        return lexicalDiversity;
    }

    public void setContentWordRatio(double contentWordRatio) {
        this.contentWordRatio = contentWordRatio;
    }

    public double getContentWordRatio() {
        return contentWordRatio;
    }

    public void setLongWords(int longWords) {
        this.longWords = longWords;
    }

    public int getLongWords() {
        return longWords;
    }

    public void setComplexWords(int complexWords) {
        this.complexWords = complexWords;
    }

    public int getComplexWords() {
        return complexWords;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getQuestions() {
        return questions;
    }

    public void setExclamations(int exclamations) {
        this.exclamations = exclamations;
    }

    public int getExclamations() {
        return exclamations;
    }

    public void setAllCapsWord(int allCapsWord) {
        this.allCapsWords = allCapsWord;
    }

    public int getAllCapsWords() {
        return allCapsWords;
    }

    public void setContractions(int contractions) {
        this.contractions = contractions;
    }

    public int getContractions() {
        return contractions;
    }

    public void setAbbreviations(int abbreviations) {
        this.abbreviations = abbreviations;
    }

    public int getAbbreviations() {
        return abbreviations;
    }

    public void setComplexSentences(int complexSentences) {
        this.complexSentences = complexSentences;
    }

    public int getComplexSentences() {
        return complexSentences;
    }

    public void setSimpleSentences(int simpleSentences) {
        this.simpleSentences = simpleSentences;
    }

    public int getSimpleSentences() {
        return simpleSentences;
    }

    public void setAverageWordsPerSentence(double averageWordsPerSentence) {
        this.averageWordsPerSentence = averageWordsPerSentence;
    }

    public double getAverageWordsPerSentence() {
        return averageWordsPerSentence;
    }

    public void setWordFrequencyDistribution(Map<Integer, Integer> wordFrequencyDistribution) {
        this.wordFrequencyDistribution = new HashMap<>(wordFrequencyDistribution);
    }

    public Map<Integer, Integer> getWordFrequencyDistribution() {
        return new HashMap<>(wordFrequencyDistribution);
    }

    public void setHapaxLegomena(int hapaxLegomena) {
        this.hapaxLegomena = hapaxLegomena;
    }

    public int getHapaxLegomena() {
        return hapaxLegomena;
    }

    public void setDisLegomena(int disLegomena) {
        this.disLegomena = disLegomena;
    }

    public int getDisLegomena() {
        return disLegomena;
    }

    public double getLexicalRichness() {
        return lexicalDiversity * 100;
    }

    public double getComplexityScore() {
        double score = 0.0;

        if (averageWordsPerSentence > 20) score += 2.0;
        else if (averageWordsPerSentence > 15) score += 1.0;

        if (lexicalDiversity > 0.7) score += 2.0;
        else if (lexicalDiversity > 0.5) score += 1.0;

        if (contentWordRatio > 0.6) score += 1.0;

        return Math.min(score, 5.0);
    }

    public String getComplexityLevel() {
        double score = getComplexityScore();

        if (score >= 4.0) return "Very Complex";
        if (score >= 3.0) return "Complex";
        if (score >= 2.0) return "Moderate";
        if (score >= 1.0) return "Simple";
        else return "Very simple";
    }

    public boolean hasStructuralElements() {
        return codeBlocks > 0 || quotes > 0 || lists > 0 || headers > 0;
    }

    public boolean hasContactInformation() {
        return !emails.isEmpty() || !phoneNumbers.isEmpty();
    }

    public boolean hasUrls() {
        return !urls.isEmpty();
    }

    public boolean hasNumericData() {
        return !numbers.isEmpty();
    }

    public int getTotalStructuralElements() {
        return codeBlocks + quotes + lists + headers;
    }

    public int getTotalContactElements() {
        return emails.size() + phoneNumbers.size();
    }

    public double getNumberStatisticsSum() {
        return numbers.stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getNumberStatisticsAverage() {
        if (numbers.isEmpty()) return 0.0;
        return getNumberStatisticsSum() / numbers.size();
    }

    public double getNumberStatisticsMax() {
        if (numbers.isEmpty()) return 0.0;
        return numbers.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    public double getNumberStatisticsMin() {
        if (numbers.isEmpty()) return 0.0;
        return numbers.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }
}