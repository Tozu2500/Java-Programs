import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

public class Reservation {
    
    private int reservationId;
    private int guestId;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime reservationDateTime;
    private ReservationStatus status;
    private int numberOfGuests;
    private double totalAmount;
    private double paidAmount;
    private double discountAmount;
    private String specialRequests;
    private PaymentMethod paymentMethod;
    private String confirmationCode;
    private boolean isBreakfastIncluded;
    private boolean isParkingIncluded;
    private boolean isWifiIncluded;
    private String notes;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    private List<String> additionalServices;
    private String cancellationReason;
    private LocalDateTime cancellationDateTime;
    private double cancellationFee;
    private boolean isRefundable;
    private String bookedBy;
    private String contactEmail;
    private String contactPhone;
    
    public Reservation() {
        this.reservationDateTime = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
        this.additionalServices = new ArrayList<>();
        this.paidAmount = 0.0;
        this.discountAmount = 0.0;
        this.cancellationFee = 0.0;
        this.isRefundable = true;
        this.isWifiIncluded = true;
    }
    
    public Reservation(int reservationId, int guestId, int roomNumber, 
                      LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        this();
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.confirmationCode = generateConfirmationCode();
    }
    
    public Reservation(int reservationId, int guestId, int roomNumber, 
                      LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests,
                      double totalAmount, PaymentMethod paymentMethod) {
        this(reservationId, guestId, roomNumber, checkInDate, checkOutDate, numberOfGuests);
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }
    
    private String generateConfirmationCode() {
        return "HTL" + System.currentTimeMillis() % 1000000;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getGuestId() {
        return guestId;
    }
    
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }
    
    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public double getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getConfirmationCode() {
        return confirmationCode;
    }
    
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
    
    public boolean isBreakfastIncluded() {
        return isBreakfastIncluded;
    }
    
    public void setBreakfastIncluded(boolean breakfastIncluded) {
        isBreakfastIncluded = breakfastIncluded;
    }
    
    public boolean isParkingIncluded() {
        return isParkingIncluded;
    }
    
    public void setParkingIncluded(boolean parkingIncluded) {
        isParkingIncluded = parkingIncluded;
    }
    
    public boolean isWifiIncluded() {
        return isWifiIncluded;
    }
    
    public void setWifiIncluded(boolean wifiIncluded) {
        isWifiIncluded = wifiIncluded;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCheckInDateTime() {
        return checkInDateTime;
    }
    
    public void setCheckInDateTime(LocalDateTime checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }
    
    public LocalDateTime getCheckOutDateTime() {
        return checkOutDateTime;
    }
    
    public void setCheckOutDateTime(LocalDateTime checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }
    
    public List<String> getAdditionalServices() {
        return new ArrayList<>(additionalServices);
    }
    
    public void setAdditionalServices(List<String> additionalServices) {
        this.additionalServices = new ArrayList<>(additionalServices);
    }
    
    public void addAdditionalService(String service) {
        if (!additionalServices.contains(service)) {
            additionalServices.add(service);
        }
    }
    
    public void removeAdditionalService(String service) {
        additionalServices.remove(service);
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }
    
    public void setCancellationDateTime(LocalDateTime cancellationDateTime) {
        this.cancellationDateTime = cancellationDateTime;
    }
    
    public double getCancellationFee() {
        return cancellationFee;
    }
    
    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
    
    public boolean isRefundable() {
        return isRefundable;
    }
    
    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }
    
    public String getBookedBy() {
        return bookedBy;
    }
    
    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    public double getRemainingBalance() {
        return totalAmount - paidAmount;
    }
    
    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }
    
    public boolean isOverdue() {
        return LocalDate.now().isAfter(checkInDate) && status == ReservationStatus.PENDING;
    }
    
    public boolean canCancel() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.PENDING;
    }
    
    public boolean canModify() {
        return status == ReservationStatus.CONFIRMED && 
               LocalDate.now().isBefore(checkInDate.minusDays(1));
    }
    
    public void confirm() {
        if (status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.CONFIRMED;
        }
    }
    
    public void cancel(String reason) {
        if (canCancel()) {
            this.status = ReservationStatus.CANCELLED;
            this.cancellationReason = reason;
            this.cancellationDateTime = LocalDateTime.now();
            calculateCancellationFee();
        }
    }
    
    private void calculateCancellationFee() {
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
        if (daysUntilCheckIn < 1) {
            cancellationFee = totalAmount;
            isRefundable = false;
        } else if (daysUntilCheckIn < 7) {
            cancellationFee = totalAmount * 0.5;
        } else if (daysUntilCheckIn < 14) {
            cancellationFee = totalAmount * 0.25;
        } else {
            cancellationFee = 0.0;
        }
    }
    
    public void checkIn() {
        if (status == ReservationStatus.CONFIRMED) {
            this.status = ReservationStatus.CHECKED_IN;
            this.checkInDateTime = LocalDateTime.now();
        }
    }
    
    public void checkOut() {
        if (status == ReservationStatus.CHECKED_IN) {
            this.status = ReservationStatus.CHECKED_OUT;
            this.checkOutDateTime = LocalDateTime.now();
        }
    }
    
    public void noShow() {
        if (status == ReservationStatus.CONFIRMED && LocalDate.now().isAfter(checkInDate)) {
            this.status = ReservationStatus.NO_SHOW;
            this.cancellationFee = totalAmount * 0.8;
        }
    }
    
    public double calculateFinalAmount() {
        double finalAmount = totalAmount - discountAmount;
        
        if (isBreakfastIncluded) finalAmount += 25 * getNumberOfNights();
        if (isParkingIncluded) finalAmount += 15 * getNumberOfNights();
        
        for (String service : additionalServices) {
            finalAmount += getServicePrice(service);
        }
        
        return finalAmount;
    }
    
    private double getServicePrice(String service) {
        switch (service.toLowerCase()) {
            case "spa": return 80.0;
            case "laundry": return 30.0;
            case "room service": return 25.0;
            case "airport transfer": return 50.0;
            case "late checkout": return 40.0;
            case "early checkin": return 30.0;
            default: return 0.0;
        }
    }
    
    public void applyDiscount(double percentage) {
        if (percentage > 0 && percentage <= 100) {
            this.discountAmount = totalAmount * (percentage / 100);
        }
    }
    
    public void makePayment(double amount) {
        if (amount > 0) {
            this.paidAmount += amount;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return reservationId == that.reservationId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", guestId=" + guestId +
                ", roomNumber=" + roomNumber +
                ", checkIn=" + checkInDate +
                ", checkOut=" + checkOutDate +
                ", status=" + status +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}