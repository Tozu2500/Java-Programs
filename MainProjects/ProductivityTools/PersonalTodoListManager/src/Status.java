import java.awt.Color;

public enum Status {
    PENDING("Pending", new Color(255, 193, 7)),
    IN_PROGRESS("In Progress", new Color(13, 202, 240)),
    COMPLETED("Completed", new Color(25, 135, 84)),
    CANCELLED("Cancelled", new Color(108, 117, 125)),
    ON_HOLD("On Hold", new Color(253, 126, 20));
    
    private final String displayName;
    private final Color color;
    
    Status(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getColor() {
        return color;
    }
    
    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.name().equalsIgnoreCase(status) || s.displayName.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return PENDING;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}