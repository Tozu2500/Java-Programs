
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationManager {

    private final Map<Integer, Reservation> reservations;
    private final Map<Integer, List<Reservation>> guestReservations;
    private final Map<String, Reservation> confirmationCodeMap;
    private int nextReservationId;
    private final RoomManager roomManager;

    public ReservationManager(RoomManager roomManager) {
        this.reservations = new HashMap<>();
        this.guestReservations = new HashMap<>();
        this.confirmationCodeMap = new HashMap<>();
        this.nextReservationId = 1000;
        this.roomManager = roomManager;
    }

    public Reservation createReservation(int guestId, int roomNumber, LocalDate checkIn,
                                        LocalDate checkOut, int numberOfGuests) {
        if (!roomManager.isRoomAvailable(roomNumber, checkIn, checkOut)) {
            return null;
        }

        Room room = roomManager.getRoom(roomNumber);
        if (room == null || numberOfGuests > room.getMaxOccupancy()) {
            return null;
        }

        Reservation reservation = new Reservation(nextReservationId++, guestId, roomNumber,
                                                checkIn, checkOut, numberOfGuests);
                                    
        double basePrice = room.getActualPrice();
        long nights = reservation.getNumberOfNights();
        reservation.setTotalAmount(basePrice * nights);

        reservations.put(reservation.getReservationId(), reservation);

        guestReservations.computeIfAbsent(guestId, _ -> new ArrayList<>()).add(reservation);

        confirmationCodeMap.put(reservation.getConfirmationCode(), reservation);

        roomManager.addReservationToRoom(roomNumber, reservation);

        return reservation;
    }

    public boolean cancelReservation(int reservationId, String reason) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || !reservation.canCancel()) {
            return false;
        }

        reservation.cancel(reason);
        roomManager.removeReservationFromRoom(reservation.getRoomNumber(), reservation);
        return true;
    }

    public boolean confirmReservation(int reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null && reservation.getStatus() == ReservationStatus.PENDING) {
            reservation.confirm();
            return true;
        }
        return false;
    }

    public boolean checkInGuest(int reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || !reservation.getStatus().canCheckIn()) {
            return false;
        }

        if (!LocalDate.now().equals(reservation.getCheckInDate()) &&
            !LocalDate.now().isAfter(reservation.getCheckInDate())) {
            return false;
        }

        reservation.checkIn();
        roomManager.checkInGuest(reservation.getRoomNumber());
        return true;
    }

    public boolean checkOutGuest(int reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || !reservation.getStatus().canCheckOut()) {
            return false;
        }

        reservation.checkOut();
        roomManager.checkOutGuest(reservation.getRoomNumber());
        return true;
    }

    public boolean modifyReservation(int reservationId, LocalDate newCheckIn,
                                    LocalDate newCheckOut, int newNumberOfGuests) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || !reservation.canModify()) {
            return false;
        }

        int roomNumber = reservation.getRoomNumber();
        roomManager.removeReservationFromRoom(roomNumber, reservation);

        if (!roomManager.isRoomAvailable(roomNumber, newCheckIn, newCheckOut)) {
            roomManager.addReservationToRoom(roomNumber, reservation);
            return false;
        }

        Room room = roomManager.getRoom(roomNumber);
        if (room == null) {
            roomManager.addReservationToRoom(roomNumber, reservation);
            return false;
        }

        if (newNumberOfGuests > room.getMaxOccupancy()) {
            roomManager.addReservationToRoom(roomNumber, reservation);
            return false;
        }

        reservation.setCheckInDate(newCheckIn);
        reservation.setCheckOutDate(newCheckOut);
        reservation.setNumberOfGuests(newNumberOfGuests);

        double basePrice = room.getActualPrice();
        long nights = reservation.getNumberOfNights();
        reservation.setTotalAmount(basePrice * nights);

        roomManager.addReservationToRoom(roomNumber, reservation);
        return true;
    }

    public Reservation getReservation(int reservationId) {
        return reservations.get(reservationId);
    }

    public Reservation getReservationByConfirmationCode(String confirmationCode) {
        return confirmationCodeMap.get(confirmationCode);
    }

    public List<Reservation> getGuestReservations(int guestId) {
        List<Reservation> guestRes = guestReservations.get(guestId);
        return guestRes != null ? new ArrayList<>(guestRes) : new ArrayList<>();
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> getActiveReservations() {
        return reservations.values().stream()
                .filter(r -> r.getStatus().isActive())
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservations.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsForDate(LocalDate date) {
        return reservations.values().stream()
                .filter(r -> (r.getCheckInDate().isEqual(date) || r.getCheckOutDate().isEqual(date)) ||
                        (r.getCheckInDate().isBefore(date) && r.getCheckOutDate().isAfter(date)))
                .collect(Collectors.toList());
    }

    public List<Reservation> getArrivalsForDate(LocalDate date) {
        return reservations.values().stream()
                .filter(r -> r.getCheckInDate().equals(date) && r.getStatus().isActive())
                .collect(Collectors.toList());
    }

    public List<Reservation> getDeparturesForDate(LocalDate date) {
        return reservations.values().stream()
                .filter(r -> r.getCheckOutDate().equals(date) &&
                        (r.getStatus() == ReservationStatus.CHECKED_IN || r.getStatus() == ReservationStatus.CHECKED_OUT))
                .collect(Collectors.toList());
    }

    public List<Reservation> getOverdueReservations() {
        return reservations.values().stream()
                .filter(Reservation::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Reservation> getPendingReservations() {
        return getReservationsByStatus(ReservationStatus.PENDING);
    }

    public List<Reservation> getConfirmedReservations() {
        return getReservationsByStatus(ReservationStatus.CONFIRMED);
    }

    public List<Reservation> getCancelledReservations() {
        return reservations.values().stream()
                .filter(r -> r.getStatus().isCancelled())
                .collect(Collectors.toList());
    }

    public boolean processPayment(int reservationId, double amount, PaymentMethod paymentMethod) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || amount <= 0) {
            return false;
        }

        reservation.makePayment(amount);
        if (reservation.getPaymentMethod() == null) {
            reservation.setPaymentMethod(paymentMethod);
        }

        if (reservation.isFullyPaid() && reservation.getStatus() == ReservationStatus.PENDING) {
            reservation.confirm();
        }

        return true;
    }

    public double getTotalRevenue() {
        return reservations.values().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CHECKED_OUT)
                .mapToDouble(Reservation::getTotalAmount)
                .sum();
    }

    public double getTotalRevenueForPeriod(LocalDate startDate, LocalDate endDate) {
        return reservations.values().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CHECKED_OUT)
                .filter(r -> !r.getCheckOutDate().isBefore(startDate) && !r.getCheckOutDate().isAfter(endDate))
                .mapToDouble(Reservation::getTotalAmount)
                .sum();
    }

    public double getPendingPayments() {
        return reservations.values().stream()
                .filter(r -> r.getStatus().isActive())
                .mapToDouble(Reservation::getRemainingBalance)
                .sum();
    }

    public int getTotalReservationCount() {
        return reservations.size();
    }

    public Map<ReservationStatus, Integer> getReservationStatusDistribution() {
        Map<ReservationStatus, Integer> distribution = new HashMap<>();
        for (ReservationStatus status : ReservationStatus.values()) {
            distribution.put(status, 0);
        }

        for (Reservation reservation : reservations.values()) {
            ReservationStatus status = reservation.getStatus();
            distribution.put(status, distribution.get(status) + 1);
        }

        return distribution;
    }

    public double getAverageReservationValue() {
        if (reservations.isEmpty()) return 0.0;

        return reservations.values().stream()
                .mapToDouble(Reservation::getTotalAmount)
                .average()
                .orElse(0.0);
    }

    public double getCancellationRate() {
        if (reservations.isEmpty()) return 0.0;

        long cancelledCount = reservations.values().stream()
                .filter(r -> r.getStatus().isCancelled())
                .count();

        return (double) cancelledCount / reservations.size() * 100;
    }

    public List<Reservation> getReservationsRequiringAttention() {
        List<Reservation> requiresAttention = new ArrayList<>();

        requiresAttention.addAll(getOverdueReservations());
        requiresAttention.addAll(getPendingReservations().stream()
                .filter(r -> !r.isFullyPaid())
                .collect(Collectors.toList()));
        
        return requiresAttention;
    }

    public void processNoShows() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Reservation> confirmedReservations = getReservationsByStatus(ReservationStatus.CONFIRMED);

        for (Reservation reservation : confirmedReservations) {
            if (reservation.getCheckInDate().isBefore(yesterday)) {
                reservation.noShow();
                roomManager.removeReservationFromRoom(reservation.getRoomNumber(), reservation);
            }
        }
    }

    public List<Reservation> searchReservations(String guestName, String confirmationCode,
                                                LocalDate date, ReservationStatus status) {
        return reservations.values().stream()
                .filter(r -> guestName == null ||
                        (r.getBookedBy() != null && r.getBookedBy().toLowerCase().contains(guestName.toLowerCase())))
                .filter(r -> confirmationCode == null ||
                        (r.getConfirmationCode() != null && r.getConfirmationCode().equalsIgnoreCase(confirmationCode)))
                .filter(r -> date == null || r.getCheckInDate().equals(date) || r.getCheckOutDate().equals(date))
                .filter(r -> status == null || r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean extendStay(int reservationId, LocalDate newCheckOutDate) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null || reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            return false;
        }

        if (!roomManager.isRoomAvailable(reservation.getRoomNumber(),
                                        reservation.getCheckOutDate(), newCheckOutDate)) {
            return false;
        }

        Room room = roomManager.getRoom(reservation.getRoomNumber());
        if (room == null) {
            return false;
        }

        long additionalNights = reservation.getCheckOutDate().until(newCheckOutDate).getDays();
        double additionalAmount = room.getActualPrice() * additionalNights;

        reservation.setCheckOutDate(newCheckOutDate);
        reservation.setTotalAmount(reservation.getTotalAmount() + additionalAmount);

        return true;
    }

    public void printReservationReport() {
        System.out.println("--- Reservation Management Report ---");
        System.out.println("Total Reservations: " + getTotalReservationCount());
        System.out.println("Total Revenue: $" + String.format("%.2f", getTotalRevenue()));
        System.out.println("Pending Payments: $" + String.format("%.2f", getPendingPayments()));
        System.out.println("Average Reservation Value: $" + String.format("%.2f", getAverageReservationValue()));
        System.out.println("Cancellation Rate: " + String.format("%.2f%%", getCancellationRate()));
        System.out.println();

        System.out.println("Reservation Status Distribution:");
        for (Map.Entry<ReservationStatus, Integer> entry : getReservationStatusDistribution().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        System.out.println("Today's Arrivals: " + getArrivalsForDate(LocalDate.now()).size());
        System.out.println("Today's Departures: " + getDeparturesForDate(LocalDate.now()).size());
        System.out.println("Overdue Reservations: " + getOverdueReservations().size());
    }

}