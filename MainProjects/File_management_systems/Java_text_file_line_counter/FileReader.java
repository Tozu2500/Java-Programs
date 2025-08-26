import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    
    private String filePath;
    private List<String> lines;
    private boolean fileLoaded;

    public FileReader(String filePath) {
        this.filePath = filePath;
        this.lines = new ArrayList<>();
        this.fileLoaded = false;
    }

    public boolean loadFile() {
        try {
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                System.err.println("That file doesn't exist: " + filePath);
                return false;
            }

            if (!Files.isReadable(path)) {
                System.err.println("File is not readable: " + filePath);
                return false;
            }

            lines.clear();

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            fileLoaded = true;
            return true;

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            System.err.println("Security error accessing file: " + e.getMessage());
            return false;
        }
    }

    public List<String> getLines() {
        if (!fileLoaded) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public String getLine(int lineNumber) {
        if (!fileLoaded || lineNumber < 0 || lineNumber >= lines.size()) {
            return null;
        }
        return lines.get(lineNumber);
    }

    public int getTotalLines() {
        return fileLoaded ? lines.size() : 0;
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.fileLoaded = false;
        this.lines.clear();
    }

    public long getFileSizeInBytes() {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                return Files.size(path);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving the file size: " + e.getMessage());
        }
        return -1;
    }

    public boolean isTextFile() {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                String contentType = Files.probeContentType(path);
                return contentType != null && contentType.startsWith("text/");
            }
        } catch (IOException e) {
            System.err.println("Error determining the file type: " + e.getMessage());
        }

        String extension = getFileExtension().toLowerCase();
        return extension.equals("txt") || extension.equals("java") ||
                extension.equals("py") || extension.equals("cpp") ||
                extension.equals("c") || extension.equals("html") ||
                extension.equals("css") || extension.equals("js") ||
                extension.equals("xml") || extension.equals("json") ||
                extension.equals("csv") || extension.equals("md");
    }

    public String getFileExtension() {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }

    public String getFileName() {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();
    }

    public String getFileContent() {
        if (!fileLoaded) {
            return "";
        }

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            content.append(lines.get(i));
            if (i < lines.size() - 1) {
                content.append(System.lineSeparator());
            }
        }

        return content.toString();
    }
}
