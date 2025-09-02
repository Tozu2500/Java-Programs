public enum Status {

    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");
    
    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Status fromString(String value) {
        if (value == null) return PENDING;

        try {
            return Status.valueOf(value.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            switch (value.toLowerCase()) {
                case "pending":
                case "p":
                    return PENDING;
                case "in progress":
                case "progress":
                case "ip":
                    return IN_PROGRESS;
                case "completed":
                case "done":
                case "c":
                    return COMPLETED;
                case "cancelled":
                case "canceled":
                case "x":
                    return CANCELLED;
                default:
                    return PENDING;
            }
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
