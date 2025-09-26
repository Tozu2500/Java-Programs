public enum ReservationStatus {

    PENDING("Pending", "Reservation is pending confirmation"),
    CONFIRMED("Confirmed", "Reservation has been confirmed and is active"),
    CHECKED_IN("Checked In", "The guest has checked in"),
    CHECKED_OUT("Checked Out", "The guest has checked out"),
    CANCELLED("Cancelled", "The reservation has been cancelled"),
    NO_SHOW("No Show", "The guest did not show up"),
    EXPIRED("Expired", "The reservation has expired");

    private final String displayName;
    private final String description;

    ReservationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == CHECKED_IN;
    }

    public boolean isCompleted() {
        return this == CHECKED_OUT;
    }

    public boolean isCancelled() {
        return this == CANCELLED || this == NO_SHOW || this == EXPIRED;
    }

    public boolean canCheckIn() {
        return this == CONFIRMED;
    }

    public boolean canCheckOut() {
        return this == CHECKED_IN;
    }

    public boolean canModify() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean canCancel() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean requiresPayment() {
        return this == PENDING || this == CONFIRMED;
    }

    public static ReservationStatus fromString(String status) {
        for (ReservationStatus reservationStatus : values()) {
            if (reservationStatus.name().equalsIgnoreCase(status) ||
                reservationStatus.displayName.equalsIgnoreCase(status)) {
                return reservationStatus;
            }
        }
        return PENDING;
    }

    public static ReservationStatus[] getActiveStatuses() {
        return new ReservationStatus[]{PENDING, CONFIRMED, CHECKED_IN};
    }

    public static ReservationStatus[] getInactiveStatuses() {
        return new ReservationStatus[]{CHECKED_OUT, CANCELLED, NO_SHOW, EXPIRED};
    }

    public ReservationStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return CONFIRMED;
            case CONFIRMED:
                return CHECKED_IN;
            case CHECKED_IN:
                return CHECKED_OUT;
            default:
                return this;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }

}
