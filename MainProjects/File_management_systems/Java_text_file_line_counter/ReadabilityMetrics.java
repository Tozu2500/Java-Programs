public class ReadabilityMetrics {
    
    private double fleschReadingEase;
    private double fleschKincaidGradeLevel;
    private double automatedReadabilityIndex;
    private double colemanLiauIndex;
    private double gunningFogIndex;
    private double smogIndex;
    private String readingLevel;

    public ReadabilityMetrics() {
        this.fleschReadingEase = 0.0;
        this.fleschKincaidGradeLevel = 0.0;
        this.automatedReadabilityIndex = 0.0;
        this.colemanLiauIndex = 0.0;
        this.gunningFogIndex = 0.0;
        this.smogIndex = 0.0;
        this.readingLevel = "Unknown";
    }

    public void setFleschReadingEase(double fleschReadingEase) {
        this.fleschReadingEase = fleschReadingEase;
        updateReadingLevel();
    }

    public double getFleschReadingEase() {
        return fleschReadingEase;
    }

    public void setFleschKincaidGradeLevel(double fleschKincaidGradeLevel) {
        this.fleschKincaidGradeLevel = fleschKincaidGradeLevel;
    }

    public double getFleschKincaidGradeLevel() {
        return fleschKincaidGradeLevel;
    }

    public void setAutomatedReadabilityIndex(double automatedReadabilityIndex) {
        this.automatedReadabilityIndex = automatedReadabilityIndex;
    }

    public double getAutomatedReadabilityIndex() {
        return automatedReadabilityIndex;
    }

    public void setColemanLiauIndex(double colemanLiauIndex) {
        this.colemanLiauIndex = colemanLiauIndex;
    }

    public double getColemanLiauIndex() {
        return colemanLiauIndex;
    }

    public void setGunningFogIndex(double gunningFogIndex) {
        this.gunningFogIndex = gunningFogIndex;
    }

    public double getGunningFogIndex() {
        return gunningFogIndex;
    }

    public void setSmogIndex(double smogIndex) {
        this.smogIndex = smogIndex;
    }

    public double getSmogIndex() {
        return smogIndex;
    }

    public String getReadingLevel() {
        return readingLevel;
    }

    private void updateReadingLevel() {
        if (fleschReadingEase >= 90) {
            readingLevel = "Very easy (5th grade)";
        } else if (fleschReadingEase >= 80) {
            readingLevel = "Easy (6th grade)";
        } else if (fleschReadingEase >= 70) {
            readingLevel = "Fairly easy (7th grade)";
        } else if (fleschReadingEase >= 60) {
            readingLevel = "Standard (8th-9th grade)";
        } else if (fleschReadingEase >= 50) {
            readingLevel = "Fairly difficult (10th-12th grade)";
        } else if (fleschReadingEase >= 30) {
            readingLevel = "Difficult (college level)";
        } else {
            readingLevel = "Very difficult (Graduate level)";
        }
    }

    public double getAverageGradeLevel() {
        int count = 0;
        double sum = 0.0;

        if (fleschKincaidGradeLevel > 0) {
            sum += fleschKincaidGradeLevel;
            count++;
        }

        if (automatedReadabilityIndex > 0) {
            sum += automatedReadabilityIndex;
            count++;
        }

        if (colemanLiauIndex > 0) {
            sum += colemanLiauIndex;
            count++;
        }

        if (smogIndex > 0) {
            sum += smogIndex;
            count++;
        }

        return count > 0 ? sum / count : 0.0;
    }
}
