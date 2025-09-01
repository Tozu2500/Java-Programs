public enum Category {

    GENERAL("General"),
    WORK("Work"),
    PERSONAL("Personal"),
    STUDY("Study"),
    HEALTH("Health"),
    SHOPPING("Shopping"),
    FINANCE("Finance"),
    FAMILY("Family"),
    TRAVEL("Travel"),
    HOBBY("Hobby");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromString(String value) {
        if (value == null) return GENERAL;

        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (Category category : Category.values()) {
                if (category.displayName.equalsIgnoreCase(value)) {
                    return category;
                }
            }
            return GENERAL;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
