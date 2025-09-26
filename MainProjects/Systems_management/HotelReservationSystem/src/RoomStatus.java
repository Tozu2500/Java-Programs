public enum RoomStatus {

    AVAILABLE("Available", "Room is ready for new guests to arrive"),
    RESERVED("Reserved", "Room is reserved for future check-in"),
    OCCUPIED("Occupied", "Room is currently occupied by guests"),
    DIRTY("Dirty", "Room needs some cleaning after checkout"),
    CLEANING("Cleaning", "Room is currently being cleaned"),
    MAINTENANCE("Maintenance", "Room is under maintenance"),
    OUT_OF_ORDER("Out of Order", "Room is temporarily unavailabe");

    private final String displayName;
    private final String description;

    RoomStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBookable() {
        return this == AVAILABLE;
    }

    public boolean isOccupied() {
        return this == OCCUPIED || this == RESERVED;
    }

    public boolean needsAttention() {
        return this == DIRTY || this == CLEANING || this == MAINTENANCE || this == OUT_OF_ORDER;
    }

    public boolean canCheckIn() {
        return this == AVAILABLE || this == RESERVED;
    }

    public boolean canClean() {
        return this == DIRTY;
    }

    public boolean isOperational() {
        return this != OUT_OF_ORDER && this != MAINTENANCE;
    }

    public static RoomStatus fromString(String status) {
        for (RoomStatus roomStatus : values()) {
            if (roomStatus.name().equalsIgnoreCase(status) ||
                roomStatus.displayName.equalsIgnoreCase(status)) {
                return roomStatus;
            }
        }
        return AVAILABLE;
    }

    public static RoomStatus[] getAvailablStatuses() {
        return new RoomStatus[]{AVAILABLE};
    }

    public static RoomStatus[] getUnavailableStatuses() {
        return new RoomStatus[]{RESERVED, OCCUPIED, DIRTY, CLEANING, MAINTENANCE, OUT_OF_ORDER};
    }

    public static RoomStatus[] getMaintenanceStatuses() {
        return new RoomStatus[]{MAINTENANCE, OUT_OF_ORDER};
    }

    @Override
    public String toString() {
        return displayName;
    }

}