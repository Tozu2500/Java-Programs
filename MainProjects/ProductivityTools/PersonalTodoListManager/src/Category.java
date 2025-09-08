import java.awt.Color;

public enum Category {
    WORK("Work", new Color(52, 58, 64)),
    PERSONAL("Personal", new Color(13, 110, 253)),
    HEALTH("Health", new Color(25, 135, 84)),
    EDUCATION("Education", new Color(111, 66, 193)),
    FINANCE("Finance", new Color(220, 53, 69)),
    SHOPPING("Shopping", new Color(255, 193, 7)),
    HOUSEHOLD("Household", new Color(13, 202, 240)),
    TRAVEL("Travel", new Color(102, 16, 242)),
    ENTERTAINMENT("Entertainment", new Color(214, 51, 132)),
    GENERAL("General", new Color(108, 117, 125));
    
    private final String displayName;
    private final Color color;
    
    Category(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static Category fromString(String category) {
        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(category) || c.displayName.equalsIgnoreCase(category)) {
                return c;
            }
        }
        return GENERAL;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}