public enum RoomType {

    SINGLE("Single Room", 1, 80.0),
    DOUBLE("Double Room", 2, 120.0),
    TWIN("Twin Room", 2, 130.0),
    DELUXE("Deluxe Room", 3, 180.0),
    SUITE("Suite", 4, 250.0),
    PRESIDENTIAL("Presidential Suite", 6, 500.0);

    private final String displayName;
    private final int standardOccupancy;
    private final double basePrice;

    RoomType(String displayName, int standardOccupancy, double basePrice) {
        this.displayName = displayName;
        this.standardOccupancy = standardOccupancy;
        this.basePrice = basePrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getStandardOccupancy() {
        return standardOccupancy;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getDescription() {
        switch (this) {
            case SINGLE:
                return "Comfortable single room with all the essential amenities";
            case DOUBLE:
                return "Spacious double room, perfect for couples";
            case TWIN:
                return "Twin bed room, ideal for friends and/or colleagues";
            case DELUXE:
                return "Upgraded room with premium level amenities and some extra space";
            case SUITE:
                return "Luxury suite with separate living area and kitchenette";
            case PRESIDENTIAL:
                return "Ultimate luxury with multiple rooms, premium service and everything else";
            default:
                return "Standard hotel room";
        }
    }

    public boolean isLuxury() {
        return this == SUITE || this == PRESIDENTIAL;
    }

    public boolean isPremium() {
        return this == DELUXE || isLuxury();
    }

    public static RoomType fromString(String type) {
        for (RoomType roomType : values()) {
            if (roomType.name().equalsIgnoreCase(type) ||
                roomType.displayName.equalsIgnoreCase(type)) {
                return roomType;
            }
        }
        return SINGLE;
    }

    public static RoomType[] getLuxuryTypes() {
        return new RoomType[]{SUITE, PRESIDENTIAL};
    }

    public static RoomType[] getStandardTypes() {
        return new RoomType[]{SINGLE, DOUBLE, TWIN};
    }

    public static RoomType[] getPremiumTypes() {
        return new RoomType[]{DELUXE, SUITE, PRESIDENTIAL};
    }

    @Override
    public String toString() {
        return displayName;
    }

}