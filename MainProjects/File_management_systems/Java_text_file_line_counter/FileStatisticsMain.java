
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileStatisticsMain {
    private static Scanner scanner = new Scanner(System.in);
    private static TextAnalyzer analyzer = new TextAnalyzer();
    private static StatisticsFormatter formatter = new StatisticsFormatter();
    
    public static void main(String[] args) {
        System.out.println("=== TEXT FILE STATISTICS ANALYZER ===");
        System.out.println("Welcome to the comprehensive text analysis tool!");
        System.out.println();
        
        boolean continueAnalysis = true;
        
        while (continueAnalysis) {
            displayMenu();
            int choice = getMenuChoice();
            
            switch (choice) {
                case 1:
                    analyzeFileFromPath();
                    break;
                case 2:
                    analyzeTextInput();
                    break;
                case 3:
                    compareFiles();
                    break;
                case 4:
                    batchAnalyzeFiles();
                    break;
                case 5:
                    displayHelp();
                    break;
                case 6:
                    continueAnalysis = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (continueAnalysis) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        System.out.println("Thank you for using the Text File Statistics Analyzer!");
    }
    
    private static void displayMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Analyze a file");
        System.out.println("2. Analyze text input");
        System.out.println("3. Compare two files");
        System.out.println("4. Batch analyze multiple files");
        System.out.println("5. Help");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }
    
    private static int getMenuChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void analyzeFileFromPath() {
        System.out.print("Enter the path to the text file: ");
        String filePath = scanner.nextLine().trim();
        
        if (filePath.isEmpty()) {
            System.out.println("No file path provided.");
            return;
        }
        
        FileReader fileReader = new FileReader(filePath);
        
        if (!fileReader.loadFile()) {
            System.out.println("Failed to load the file. Please check the path and try again.");
            return;
        }
        
        if (!fileReader.isTextFile()) {
            System.out.println("Warning: This may not be a text file. Analysis may produce unexpected results.");
            System.out.print("Continue anyway? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("y") && !response.equals("yes")) {
                return;
            }
        }
        
        System.out.println("Analyzing file: " + fileReader.getFileName());
        System.out.println("File size: " + formatFileSize(fileReader.getFileSizeInBytes()));
        
        analyzer.analyzeFile(fileReader);
        
        FileStatistics fileStats = analyzer.getFileStatistics();
        CharacterStatistics charStats = analyzer.getCharacterStatistics();
        WordStatistics wordStats = analyzer.getWordStatistics();
        ReadabilityMetrics readability = analyzer.calculateReadability(fileReader.getFileContent());
        
        displayAnalysisMenu(fileStats, charStats, wordStats, readability, fileReader.getFileName());
    }

    private static void analyzeTextInput() {
        System.out.println("Enter your text (type 'END' on a new line to finish):");
        StringBuilder textInput = new StringBuilder();
        String line;

        while(!(line = scanner.nextLine()).equals("END")) {
            if (textInput.length() > 0) {
                textInput.append("\n");
            }
            textInput.append(line);
        }

        if (textInput.length() == 0) {
            System.out.println("No text provided for analysis.");
            return;
        }

        String text = textInput.toString();
        System.out.println("Analyzing provided text...");

        analyzer.analyzeText(text);

        FileStatistics fileStats = analyzer.getFileStatistics();
        CharacterStatistics charStats = analyzer.getCharacterStatistics();
        WordStatistics wordStats = analyzer.getWordStatistics();
        ReadabilityMetrics readability = analyzer.calculateReadability(text);

        displayAnalysisMenu(fileStats, charStats, wordStats, readability, "User Input");
    }

    private static void compareFiles() {
        System.out.println("Enter the path to the first file: ");
        String filePath1 = scanner.nextLine().trim();

        System.out.println("Enter the path to the second file ");
        String filePath2 = scanner.nextLine().trim();

        if (filePath1.isEmpty() || filePath2.isEmpty()) {
            System.out.println("Both file paths are required for the file comparison!");
            return;
        }

        FileReader fileReader1 = new FileReader(filePath1);
        FileReader fileReader2 = new FileReader(filePath2);

        if (!fileReader1.loadFile()) {
            System.out.println("Failed to load the first file: " + filePath1);
            return;
        }

        if (!fileReader2.loadFile()) {
            System.out.println("Failed to load the second file: " + filePath2);
            return;
        }

        System.out.println("Analyzing both files for comparison...");

        analyzer.analyzeFile(fileReader1);
        FileStatistics stats1 = new FileStatistics();
        copyFileStatistics(analyzer.getFileStatistics(), stats1);
        CharacterStatistics charStats1 = analyzer.getCharacterStatistics();
        WordStatistics wordStats1 = analyzer.getWordStatistics();

        analyzer.analyzeFile(fileReader2);
        FileStatistics stats2 = analyzer.getFileStatistics();
        CharacterStatistics charStats2 = analyzer.getCharacterStatistics();
        WordStatistics wordStats2 = analyzer.getWordStatistics();

        displayFileComparison(stats1, stats2, charStats1, charStats2, wordStats1, wordStats2,
                            fileReader1.getFileName(), fileReader2.getFileName());
    }

    private static void batchAnalyzeFiles() {
        System.out.println("Enter the directory path containing the text files: ");
        String directoryPath = scanner.nextLine().trim();

        if (directoryPath.isEmpty()) {
            System.out.println("No directory path provided!");
            return;
        }

        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path or the directory doesn't exist!");
            return;
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the directory!");
            return;
        }

        System.out.println("Found " + files.length + " files. Analyzing the text files...");
        System.out.println();

        int analyzedCount = 0;

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".txt") || fileName.endsWith(".java") ||
                    fileName.endsWith(".py") || fileName.endsWith(".cpp") ||
                    fileName.endsWith(".c") || fileName.endsWith(".html") ||
                    fileName.endsWith(".css") || fileName.endsWith(".js")) {

                    FileReader fileReader = new FileReader(file.getAbsolutePath());
                    if (fileReader.loadFile()) {
                        analyzer.analyzeFile(fileReader);
                        FileStatistics stats = analyzer.getFileStatistics();

                        System.out.println("--- " + file.getName() + " ---");
                        System.out.println("Lines: " + stats.getTotalLines() +
                                        "  | Words: " + stats.getTotalWords() +
                                        "  | Characters: " + stats.getTotalCharacters());
                        System.out.println("Size: " + formatFileSize(fileReader.getFileSizeInBytes()));
                        System.out.println();

                        analyzedCount++;
                    }
                }
            }
        }

        System.out.println("Batch analysis complete. Analyzed a total of " + analyzedCount + " files.");
    }

    private static void displayAnalysisMenu(FileStatistics fileStats, CharacterStatistics charStats,
                                            WordStatistics wordStats, ReadabilityMetrics readability, String fileName) {
        boolean showMenu = true;

        while (showMenu) {
            System.out.println("\n=== Analysis Results Menu ===");
            System.out.println("1. Show complete summary");
            System.out.println("2. Show file statistics only");
            System.out.println("3. Show character statistics only");
            System.out.println("4. Show word statistics only");
            System.out.println("5. Show readability analysis only");
            System.out.println("6. Export results to a file");
            System.out.println("7. Return to the main menu");
            System.out.println("Enter your choice: (1-7): ");

            int choice = getMenuChoice();

            switch (choice) {
                // Test the print cases with the toString() method, if it doesn't work, try to remove it
                case 1:
                    System.out.println(formatter.formatCompleteSummary(fileStats, charStats, wordStats, readability, fileName).toString());
                    break;
                case 2:
                    System.out.println(formatter.formatFileStatistics(fileStats, fileName).toString());
                    break;
                case 3:
                    System.out.println(formatter.formatCharacterStatistics(charStats));
                    break;
                case 4:
                    System.out.println(formatter.formatWordStatistics(wordStats));
                    break;
                case 5:
                    System.out.println(formatter.formatReadabilityMetrics(readability));
                    break;
                case 6:
                    exportResults(fileStats, charStats, wordStats, readability, fileName);
                    break;
                case 7:
                    showMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again!");
            }
        }
    }

    private static void displayFileComparison(FileStatistics stats1, FileStatistics stats2,
                                            CharacterStatistics charStats1, CharacterStatistics charStats2,
                                            WordStatistics wordStats1, WordStatistics wordStats2,
                                            String fileName1, String fileName2) {
        System.out.println("\n=== File Comparison Results ===");
        System.out.println("File 1: " + fileName1);
        System.out.println("File 2: " + fileName2);
        System.out.println();

        System.out.println("Basic Statistics Comparison:");
        System.out.printf("%-25s %15s %15s %15s%n", "Metric", fileName1, fileName2, "Difference");
        System.out.println("-".repeat(70));

        printComparisonRow("Total Lines", stats1.getTotalLines(), stats2.getTotalLines());
        printComparisonRow("Total Words", stats1.getTotalWords(), stats2.getTotalWords());
        printComparisonRow("Total Characters", stats1.getTotalCharacters(), stats2.getTotalCharacters());
        printComparisonRow("Empty Lines", stats1.getEmptyLines(), stats2.getEmptyLines());
        printComparisonRow("Paragraphs", stats1.getTotalParagraphs(), stats2.getTotalParagraphs());
        printComparisonRow("Sentences", stats1.getTotalSentences(), stats2.getTotalSentences());
        printComparisonRow("Unique Words", wordStats1.getUniqueWords(), wordStats2.getUniqueWords());

        System.out.println("\nCharacter Type Comparison");
        printComparisonRow("Alphabetical", charStats1.getAlphabeticalCharacters(), charStats2.getAlphabeticalCharacters());
        printComparisonRow("Numeric", charStats1.getNumericCharacters(), charStats2.getNumericCharacters());
        printComparisonRow("Uppercase", charStats1.getUppercaseLetters(), charStats2.getUppercaseLetters());
        printComparisonRow("Lowercase", charStats1.getLowercaseLetters(), charStats2.getLowercaseLetters());
        printComparisonRow("Vowels", charStats1.getVowels(), charStats2.getVowels());
        printComparisonRow("Consonants", charStats1.getConsonants(), charStats2.getConsonants());

        System.out.println("\nWord Length Comparison");
        System.out.printf("%-25s %15.2f %15.2f %15.2f%n", "Average Word Length",
                        wordStats1.getAverageWordLength(), wordStats2.getAverageWordLength(),
                        wordStats2.getAverageWordLength() - wordStats1.getAverageWordLength());
        System.out.printf("%-25s %15d %15d %15d%n", "Longest Word Length",
                        wordStats1.getLongestWordLength(), wordStats2.getLongestWordLength(),
                        wordStats2.getLongestWordLength() - wordStats1.getLongestWordLength());
        System.out.printf("%-25s %15d %15d %15d%n", "Shortest Word Length",
                        wordStats1.getShortestWordLength(), wordStats2.getShortestWordLength(),
                        wordStats2.getShortestWordLength() - wordStats1.getShortestWordLength());
    }

    private static void printComparisonRow(String metric, long value1, long value2) {
        long difference = value2 - value1;
        String diffStr = (difference >= 0 ? "+" : "") + difference;
        System.out.printf("%-25s %15d %15d %15s%n", metric, value1, value2, diffStr);
    }

    private static void copyFileStatistics(FileStatistics source, FileStatistics dest) {
        dest.setTotalLines(source.getTotalLines());
        dest.setEmptyLines(source.getEmptyLines());
        dest.setNonEmptyLines(source.getNonEmptyLines());
        dest.setTotalWords(source.getTotalWords());
        dest.setTotalCharacters(source.getTotalCharacters());
        dest.setTotalCharactersNoSpaces(source.getTotalCharactersNoSpaces());
        dest.setTotalParagraphs(source.getTotalParagraphs());
        dest.setTotalSentences(source.getTotalSentences());
        dest.setAverageWordsPerLine(source.getAverageWordsPerLine());
        dest.setAverageCharactersPerLine(source.getAverageCharactersPerLine());
        dest.setAverageWordsPerSentence(source.getAverageWordsPerSentence());
        dest.setLongestLineLength(source.getLongestLineLength());
        dest.setShortestLineLength(source.getShortestLineLength());
        dest.setLongestLine(source.getLongestLine());
        dest.setShortestLine(source.getShortestLine());
    }

    private static void exportResults(FileStatistics fileStats, CharacterStatistics charStats,
                                    WordStatistics wordStats, ReadabilityMetrics readability, String fileName) {
        System.out.println("Enter the output file name (without the extension): ");
        String outputFileName = scanner.nextLine().trim();

        if (outputFileName.isEmpty()) {
            outputFileName = "analysis_results";
        }

        outputFileName += "_" + System.currentTimeMillis() + ".txt";

        try {
            FileWriter writer = new FileWriter(outputFileName);
            String results = formatter.formatCompleteSummary(fileStats, charStats, wordStats, readability, fileName);
            writer.write(results);
            writer.close();

            System.out.println("Results successfully exported to: " + outputFileName);
        } catch (IOException e) {
            System.out.println("Error exporting results: " + e.getMessage());
        }
    }

    private static String formatFileSize(long bytes) {
        if (bytes < 0) {
            return "Unknown";
        }

        if (bytes < 1024) {
            return bytes + " bytes";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes /1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private static void displayHelp() {
        System.out.println("\n=== TEXT FILE STATISTICS ANALYZER HELP ===");
        System.out.println();
        System.out.println("This tool provides comprehensive analysis of text files including:");
        System.out.println();
        System.out.println("FILE STATISTICS:");
        System.out.println("  - Line counts (total, empty, non-empty)");
        System.out.println("  - Word and character counts");
        System.out.println("  - Paragraph and sentence counts");
        System.out.println("  - Line length analysis");
        System.out.println("  - Average metrics");
        System.out.println();
        System.out.println("CHARACTER ANALYSIS:");
        System.out.println("  - Character frequency distribution");
        System.out.println("  - Character type classification");
        System.out.println("  - Uppercase/lowercase analysis");
        System.out.println("  - Vowel/consonant analysis");
        System.out.println("  - Special character detection");
        System.out.println();
        System.out.println("WORD ANALYSIS:");
        System.out.println("  - Word frequency analysis");
        System.out.println("  - Vocabulary richness calculation");
        System.out.println("  - Word length distribution");
        System.out.println("  - Most/least common words");
        System.out.println();
        System.out.println("READABILITY METRICS:");
        System.out.println("  - Flesch Reading Ease score");
        System.out.println("  - Flesch-Kincaid Grade Level");
        System.out.println("  - Reading difficulty assessment");
        System.out.println();
        System.out.println("FEATURES:");
        System.out.println("  - Single file analysis");
        System.out.println("  - Direct text input analysis");
        System.out.println("  - File comparison");
        System.out.println("  - Batch directory analysis");
        System.out.println("  - Results export to file");
        System.out.println();
        System.out.println("SUPPORTED FILE TYPES:");
        System.out.println("  - Plain text (.txt)");
        System.out.println("  - Source code (.java, .py, .cpp, .c, .js)");
        System.out.println("  - Web files (.html, .css)");
        System.out.println("  - Other text-based formats");
        System.out.println();
        System.out.println("USAGE TIPS:");
        System.out.println("  - Use full file paths for best results");
        System.out.println("  - Large files may take longer to analyze");
        System.out.println("  - Comparison feature helps identify differences");
        System.out.println("  - Batch analysis is great for project directories");
        System.out.println("  - Export feature saves detailed reports");
        System.out.println();
        System.out.println("For file input analysis, type 'END' on a new line to finish input.");
    }
}