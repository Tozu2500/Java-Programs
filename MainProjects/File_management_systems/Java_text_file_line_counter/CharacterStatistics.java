import java.util.HashMap;
import java.util.Map;

public class CharacterStatistics {
    
    private Map<Character, Integer> characterFrequency;
    private long alphabeticalCharacters;
    private long numericCharacters;
    private long specialCharacters;
    private long whitespaceCharacters;
    private long uppercaseLetters;
    private long lowercaseLetters;
    private long vowels;
    private long consonants;
    private long punctuationMarks;
    
    public CharacterStatistics() {
        this.characterFrequency = new HashMap<>();
        this.alphabeticalCharacters = 0;
        this.numericCharacters = 0;
        this.specialCharacters = 0;
        this.whitespaceCharacters = 0;
        this.uppercaseLetters = 0;
        this.lowercaseLetters = 0;
        this.vowels = 0;
        this.consonants = 0;
        this.punctuationMarks = 0;
    }

    public void analyzeCharacter(char ch) {
        characterFrequency.put(ch, characterFrequency.getOrDefault(ch, 0) + 1);

        if (Character.isLetter(ch)) {
            alphabeticalCharacters++;
            if (Character.isUpperCase(ch)) {
                uppercaseLetters++;
            } else {
                lowercaseLetters++;
            }

            if (isVowel(ch)) {
                vowels++;
            } else {
                consonants++;
            }
        } else if (Character.isDigit(ch)) {
            numericCharacters++;
        } else if (Character.isWhitespace(ch)) {
            whitespaceCharacters++;
        } else if (isPunctuation(ch)) {
            punctuationMarks++;
        } else {
            specialCharacters++;
        }
    }

    private boolean isVowel(char ch) {
        char lowerCh = Character.toLowerCase(ch);
        return lowerCh == 'a' || lowerCh == 'e' || lowerCh == 'i' ||
                lowerCh == 'o' || lowerCh == 'u';
    }

    private boolean isPunctuation(char ch) {
        return ch == '.' || ch == ',' || ch == '!' || ch == '?' ||
                ch == ';' || ch == ':' || ch == '"' || ch == '\'' ||
                ch == '(' || ch == ')' || ch == '[' || ch == ']' ||
                ch == '{' || ch == '}' || ch == '-' || ch == '_';
    }

    public Map<Character, Integer> getCharacterFrequency() {
        return new HashMap<>(characterFrequency);
    }

    public long getAlphabeticalCharacters() {
        return alphabeticalCharacters;
    }

    public long getNumericCharacters() {
        return numericCharacters;
    }

    public long getSpecialCharacters() {
        return specialCharacters;
    }

    public long getWhitespaceCharacters() {
        return whitespaceCharacters;
    }

    public long getUppercaseLetters() {
        return uppercaseLetters;
    }

    public long getLowercaseLetters() {
        return lowercaseLetters;
    }

    public long getVowels() {
        return vowels;
    }

    public long getConsonants() {
        return consonants;
    }

    public long getPunctuationMarks() {
        return punctuationMarks;
    }

    public char getMostFrequentCharacter() {
        if (characterFrequency.isEmpty()) {
            return '\0';
        }

        char mostFrequent = '\0';
        int maxCount = 0;

        for (Map.Entry<Character, Integer> entry : characterFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        return mostFrequent;
    }

    public char getLeastFrequentCharacter() {
        if (characterFrequency.isEmpty()) {
            return '\0';
        }

        char leastFrequent = '\0';
        int minCount = Integer.MAX_VALUE;

        for (Map.Entry<Character, Integer> entry : characterFrequency.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                leastFrequent = entry.getKey();
            }
        }

        return leastFrequent;
    }

    public int getCharacterFrequency(char ch) {
        return characterFrequency.getOrDefault(ch, 0);
    }

    public int getUniqueCharacterCount() {
        return characterFrequency.size();
    }
}
