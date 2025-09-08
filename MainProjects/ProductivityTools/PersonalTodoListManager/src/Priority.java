import java.awt.Color;

public enum Priority {
    URGENT(5, "Urgent", new Color(220, 53, 69)),
    HIGH(4, "High", new Color(255, 193, 7)),
    MEDIUM(3, "Medium", new Color(40, 167, 69)),
    LOW(2, "Low", new Color(108, 117, 125)),
    MINIMAL(1, "Minimal", new Color(173, 181, 189));
    
    private final int weight;
    private final String displayName;
    private final Color color;
    
    Priority(int weight, String displayName, Color color) {
        this.weight = weight;
        this.displayName = displayName;
        this.color = color;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static Priority fromString(String priority) {
        for (Priority p : Priority.values()) {
            if (p.name().equalsIgnoreCase(priority) || p.displayName.equalsIgnoreCase(priority)) {
                return p;
            }
        }
        return MEDIUM;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}