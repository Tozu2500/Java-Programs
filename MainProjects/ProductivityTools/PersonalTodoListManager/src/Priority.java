public enum Priority {

    LOW(1, "Low"),
    MEDIUM(2, "Medium"),
    HIGH(3, "High"),
    URGENT(4, "Urgent");

    private final int level;
    private final String displayName;

    Priority(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Priority fromString(String value) {
        if (value == null) return MEDIUM;

        try {
            return Priority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            switch (value.toLowerCase()) {
                case "1":
                case "low":
                    return LOW;
                case "2":
                case "medium":
                    return MEDIUM;
                case "3":
                case "high":
                    return HIGH;
                case "4":
                case "urgent":
                    return URGENT;
                default:
                    return MEDIUM;
            }
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
