import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

public class HotelUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9\\s\\-()]+$"
    );
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 10 && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return checkIn.isBefore(checkOut) && !checkIn.isBefore(LocalDate.now());
    }
    
    public static String formatCurrency(double amount) {
        return String.format("$%.2f", amount);
    }
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "Not set";
    }
    
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static String generateConfirmationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 3; i++) {
            code.append((char) ('A' + random.nextInt(26)));
        }
        
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'&]", "");
    }
    
    public static boolean isWeekend(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7;
    }
    
    public static double calculateWeekendSurcharge(double basePrice, LocalDate checkIn, LocalDate checkOut) {
        double totalSurcharge = 0.0;
        LocalDate current = checkIn;
        
        while (current.isBefore(checkOut)) {
            if (isWeekend(current)) {
                totalSurcharge += basePrice * 0.2;
            }
            current = current.plusDays(1);
        }
        
        return totalSurcharge;
    }
    
    public static double calculateSeasonalAdjustment(double basePrice, LocalDate date) {
        int month = date.getMonthValue();
        
        if (month >= 6 && month <= 8) {
            return basePrice * 1.3;
        } else if (month == 12 || month <= 2) {
            return basePrice * 1.2;
        } else if (month >= 3 && month <= 5) {
            return basePrice * 1.1;
        } else {
            return basePrice;
        }
    }
    
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return email;
        }
        
        String maskedUsername = username.charAt(0) + "*".repeat(username.length() - 2) + username.charAt(username.length() - 1);
        return maskedUsername + "@" + domain;
    }
    
    public static String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        
        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 4) {
            return phone;
        }
        
        String masked = "*".repeat(cleaned.length() - 4) + cleaned.substring(cleaned.length() - 4);
        return masked;
    }
    
    public static List<LocalDate> getDateRange(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = start;
        
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        
        return dates;
    }
    
    public static Map<String, Object> calculateStayStatistics(LocalDate checkIn, LocalDate checkOut, double totalAmount) {
        Map<String, Object> stats = new HashMap<>();
        
        long nights = checkIn.until(checkOut).getDays();
        double averageNightlyRate = nights > 0 ? totalAmount / nights : totalAmount;
        
        stats.put("totalNights", nights);
        stats.put("averageNightlyRate", averageNightlyRate);
        stats.put("totalAmount", totalAmount);
        stats.put("isWeekendStay", hasWeekend(checkIn, checkOut));
        stats.put("seasonalPeriod", getSeasonalPeriod(checkIn));
        
        return stats;
    }
    
    private static boolean hasWeekend(LocalDate checkIn, LocalDate checkOut) {
        LocalDate current = checkIn;
        while (current.isBefore(checkOut)) {
            if (isWeekend(current)) {
                return true;
            }
            current = current.plusDays(1);
        }
        return false;
    }
    
    private static String getSeasonalPeriod(LocalDate date) {
        int month = date.getMonthValue();
        
        if (month >= 6 && month <= 8) {
            return "Peak Season";
        } else if (month == 12 || month <= 2) {
            return "High Season";
        } else if (month >= 3 && month <= 5) {
            return "Shoulder Season";
        } else {
            return "Low Season";
        }
    }
    
    public static String formatDuration(long minutes) {
        if (minutes < 60) {
            return minutes + " minutes";
        } else if (minutes < 1440) {
            long hours = minutes / 60;
            long remainingMinutes = minutes % 60;
            return hours + " hours" + (remainingMinutes > 0 ? " " + remainingMinutes + " minutes" : "");
        } else {
            long days = minutes / 1440;
            long remainingHours = (minutes % 1440) / 60;
            return days + " days" + (remainingHours > 0 ? " " + remainingHours + " hours" : "");
        }
    }
    
    public static double calculateTaxes(double amount, double taxRate) {
        return amount * (taxRate / 100);
    }
    
    public static double applyDiscount(double amount, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            return amount;
        }
        return amount * (1 - discountPercentage / 100);
    }
    
    public static String generateGuestId(String firstName, String lastName) {
        String initials = (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();
        Random random = new Random();
        int number = random.nextInt(9999) + 1;
        return initials + String.format("%04d", number);
    }
    
    public static boolean isValidRoomNumber(int roomNumber) {
        return roomNumber >= 100 && roomNumber <= 9999;
    }
    
    public static int getFloorFromRoomNumber(int roomNumber) {
        return roomNumber / 100;
    }
    
    public static String getRoomDescription(Room room) {
        if (room == null) return "";
        
        StringBuilder description = new StringBuilder();
        description.append(room.getRoomType().getDisplayName());
        description.append(" on floor ").append(room.getFloor());
        
        List<String> features = new ArrayList<>();
        if (room.hasSeaView()) features.add("sea view");
        if (room.hasMountainView()) features.add("mountain view");
        if (room.hasBalcony()) features.add("balcony");
        if (room.isAccessible()) features.add("wheelchair accessible");
        if (room.hasKitchenette()) features.add("kitchenette");
        
        if (!features.isEmpty()) {
            description.append(" with ").append(String.join(", ", features));
        }
        
        return description.toString();
    }
    
    public static List<String> validateReservationData(Reservation reservation, Guest guest, Room room) {
        List<String> errors = new ArrayList<>();
        
        if (reservation == null) {
            errors.add("Reservation data is missing");
            return errors;
        }
        
        if (guest == null) {
            errors.add("Guest information is missing");
        } else {
            if (!isValidEmail(guest.getEmail())) {
                errors.add("Invalid email address");
            }
            if (!isValidPhone(guest.getPhoneNumber())) {
                errors.add("Invalid phone number");
            }
        }
        
        if (room == null) {
            errors.add("Room information is missing");
        } else {
            if (reservation.getNumberOfGuests() > room.getMaxOccupancy()) {
                errors.add("Number of guests exceeds room capacity");
            }
        }
        
        if (!isValidDateRange(reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            errors.add("Invalid check-in/check-out dates");
        }
        
        if (reservation.getTotalAmount() < 0) {
            errors.add("Invalid total amount");
        }
        
        return errors;
    }
    
    public static String getReservationStatusColor(ReservationStatus status) {
        switch (status) {
            case CONFIRMED:
                return "GREEN";
            case PENDING:
                return "YELLOW";
            case CANCELLED:
            case NO_SHOW:
                return "RED";
            case CHECKED_IN:
                return "BLUE";
            case CHECKED_OUT:
                return "GRAY";
            default:
                return "BLACK";
        }
    }
    
    public static String getRoomStatusColor(RoomStatus status) {
        switch (status) {
            case AVAILABLE:
                return "GREEN";
            case RESERVED:
                return "YELLOW";
            case OCCUPIED:
                return "BLUE";
            case DIRTY:
                return "ORANGE";
            case CLEANING:
                return "PURPLE";
            case MAINTENANCE:
            case OUT_OF_ORDER:
                return "RED";
            default:
                return "BLACK";
        }
    }
    
    public static double calculateCancellationFee(Reservation reservation) {
        if (reservation == null || reservation.getCheckInDate() == null) {
            return 0.0;
        }
        
        long daysUntilCheckIn = LocalDate.now().until(reservation.getCheckInDate()).getDays();
        double totalAmount = reservation.getTotalAmount();
        
        if (daysUntilCheckIn < 1) {
            return totalAmount;
        } else if (daysUntilCheckIn < 3) {
            return totalAmount * 0.8;
        } else if (daysUntilCheckIn < 7) {
            return totalAmount * 0.5;
        } else if (daysUntilCheckIn < 14) {
            return totalAmount * 0.25;
        } else {
            return 0.0;
        }
    }
    
    public static List<String> generateReservationReport(Reservation reservation, Guest guest, Room room) {
        List<String> report = new ArrayList<>();
        
        report.add("=== RESERVATION REPORT ===");
        report.add("Confirmation: " + reservation.getConfirmationCode());
        report.add("Guest: " + (guest != null ? guest.getFullName() : "Unknown"));
        report.add("Room: " + reservation.getRoomNumber() + 
                  (room != null ? " (" + room.getRoomType().getDisplayName() + ")" : ""));
        report.add("Dates: " + formatDate(reservation.getCheckInDate()) + 
                  " to " + formatDate(reservation.getCheckOutDate()));
        report.add("Nights: " + reservation.getNumberOfNights());
        report.add("Guests: " + reservation.getNumberOfGuests());
        report.add("Status: " + reservation.getStatus().getDisplayName());
        report.add("Total: " + formatCurrency(reservation.getTotalAmount()));
        report.add("Paid: " + formatCurrency(reservation.getPaidAmount()));
        report.add("Balance: " + formatCurrency(reservation.getRemainingBalance()));
        
        if (reservation.isBreakfastIncluded()) {
            report.add("Includes: Breakfast");
        }
        if (reservation.isParkingIncluded()) {
            report.add("Includes: Parking");
        }
        if (!reservation.getAdditionalServices().isEmpty()) {
            report.add("Services: " + String.join(", ", reservation.getAdditionalServices()));
        }
        
        return report;
    }
    
    public static Map<String, Integer> analyzeReservationPatterns(List<Reservation> reservations) {
        Map<String, Integer> patterns = new HashMap<>();
        
        int weekdayReservations = 0;
        int weekendReservations = 0;
        int shortStays = 0; // 1-2 nights
        int mediumStays = 0; // 3-5 nights
        int longStays = 0; // 6+ nights
        
        for (Reservation reservation : reservations) {
            long nights = reservation.getNumberOfNights();
            
            if (hasWeekend(reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                weekendReservations++;
            } else {
                weekdayReservations++;
            }
            
            if (nights <= 2) {
                shortStays++;
            } else if (nights <= 5) {
                mediumStays++;
            } else {
                longStays++;
            }
        }
        
        patterns.put("weekdayReservations", weekdayReservations);
        patterns.put("weekendReservations", weekendReservations);
        patterns.put("shortStays", shortStays);
        patterns.put("mediumStays", mediumStays);
        patterns.put("longStays", longStays);
        
        return patterns;
    }
    
    public static String getRecommendedRoomType(int numberOfGuests, boolean isLuxuryPreference, double budget) {
        if (numberOfGuests == 1) {
            return budget >= 150 ? "DELUXE" : "SINGLE";
        } else if (numberOfGuests == 2) {
            if (isLuxuryPreference && budget >= 250) {
                return "SUITE";
            } else if (budget >= 180) {
                return "DELUXE";
            } else {
                return "DOUBLE";
            }
        } else if (numberOfGuests <= 4) {
            return budget >= 250 ? "SUITE" : "DELUXE";
        } else {
            return "PRESIDENTIAL";
        }
    }
    
    public static boolean isEarlyCheckIn(LocalDate checkInDate) {
        return checkInDate.equals(LocalDate.now());
    }
    
    public static boolean isLateCheckOut(LocalDate checkOutDate) {
        return checkOutDate.equals(LocalDate.now());
    }
    
    public static double calculateLoyaltyDiscount(Guest guest) {
        if (guest == null) return 0.0;
        
        double totalSpent = guest.getTotalSpent();
        
        if (guest.isVip()) {
            return 15.0; // 15% discount for VIP
        } else if (totalSpent >= 2000) {
            return 10.0; // 10% discount for loyal customers
        } else if (totalSpent >= 1000) {
            return 5.0; // 5% discount for regular customers
        }
        
        return 0.0;
    }
    
    public static String formatPhoneNumber(String phone) {
        if (phone == null) return "";
        
        String cleaned = phone.replaceAll("[^0-9+]", "");
        
        if (cleaned.startsWith("+1") && cleaned.length() == 12) {
            return String.format("+1 (%s) %s-%s", 
                cleaned.substring(2, 5), 
                cleaned.substring(5, 8), 
                cleaned.substring(8, 12));
        } else if (cleaned.length() == 10) {
            return String.format("(%s) %s-%s", 
                cleaned.substring(0, 3), 
                cleaned.substring(3, 6), 
                cleaned.substring(6, 10));
        }
        
        return phone;
    }
    
    public static List<String> getAvailableAmenities() {
        return Arrays.asList(
            "WiFi", "Air Conditioning", "Mini Bar", "Room Service", 
            "Balcony", "Sea View", "Mountain View", "Kitchenette",
            "Safe", "Hair Dryer", "Iron", "Coffee Machine",
            "Bathrobe", "Slippers", "Toiletries", "TV"
        );
    }
    
    public static List<String> getAvailableServices() {
        return Arrays.asList(
            "Breakfast", "Parking", "Airport Transfer", "Laundry",
            "Spa", "Gym", "Pool", "Business Center",
            "Concierge", "Room Service", "Valet Parking",
            "Tour Booking", "Car Rental", "Babysitting"
        );
    }
    
    public static String calculateCheckInTime(LocalDate checkInDate) {
        if (checkInDate.equals(LocalDate.now())) {
            return "Available now";
        } else if (checkInDate.equals(LocalDate.now().plusDays(1))) {
            return "Tomorrow after 3:00 PM";
        } else {
            return formatDate(checkInDate) + " after 3:00 PM";
        }
    }
    
    public static String calculateCheckOutTime(LocalDate checkOutDate) {
        if (checkOutDate.equals(LocalDate.now())) {
            return "Before 11:00 AM today";
        } else if (checkOutDate.equals(LocalDate.now().plusDays(1))) {
            return "Tomorrow before 11:00 AM";
        } else {
            return formatDate(checkOutDate) + " before 11:00 AM";
        }
    }
    
    public static boolean isBusinessTravelPeriod(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5; // Monday to Friday
    }
    
    public static String getBookingAdvice(LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
        StringBuilder advice = new StringBuilder();
        
        if (isWeekend(checkIn)) {
            advice.append("Weekend booking - consider booking early for better rates. ");
        }
        
        String season = getSeasonalPeriod(checkIn);
        if ("Peak Season".equals(season)) {
            advice.append("Peak season booking - limited availability and premium rates apply. ");
        }
        
        if (roomType != null && roomType.isLuxury()) {
            advice.append("Luxury accommodation - includes premium amenities and services. ");
        }
        
        long nights = checkIn.until(checkOut).getDays();
        if (nights >= 7) {
            advice.append("Extended stay - you may be eligible for weekly discounts. ");
        }
        
        if (advice.length() == 0) {
            advice.append("Standard booking conditions apply. ");
        }
        
        return advice.toString().trim();
    }
    
    public static Map<String, Double> calculateRoomRevenue(List<Reservation> reservations, Room room) {
        Map<String, Double> revenue = new HashMap<>();
        
        double totalRevenue = 0.0;
        double averageRevenue = 0.0;
        int completedStays = 0;
        
        for (Reservation reservation : reservations) {
            if (reservation.getRoomNumber() == room.getRoomNumber() && 
                reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
                totalRevenue += reservation.getTotalAmount();
                completedStays++;
            }
        }
        
        if (completedStays > 0) {
            averageRevenue = totalRevenue / completedStays;
        }
        
        revenue.put("totalRevenue", totalRevenue);
        revenue.put("averageRevenue", averageRevenue);
        revenue.put("completedStays", (double) completedStays);
        
        return revenue;
    }
    
    public static boolean requiresSpecialAttention(Guest guest, Reservation reservation) {
        if (guest == null || reservation == null) return false;
        
        return guest.isVip() || 
               reservation.getTotalAmount() > 1000 ||
               reservation.getNumberOfNights() > 7 ||
               (reservation.getSpecialRequests() != null && !reservation.getSpecialRequests().trim().isEmpty());
    }
    
    public static String generateWelcomeMessage(Guest guest, Reservation reservation) {
        StringBuilder message = new StringBuilder();
        message.append("Welcome to our hotel, ");
        
        if (guest.isVip()) {
            message.append("VIP Guest ");
        }
        
        message.append(guest.getFirstName()).append("!\n\n");
        message.append("We are delighted to have you stay with us.\n");
        message.append("Confirmation: ").append(reservation.getConfirmationCode()).append("\n");
        message.append("Room: ").append(reservation.getRoomNumber()).append("\n");
        message.append("Check-out: ").append(formatDate(reservation.getCheckOutDate())).append("\n\n");
        
        if (guest.isVip()) {
            message.append("As our VIP guest, you have access to exclusive amenities and priority service.\n");
        }
        
        message.append("Enjoy your stay!");
        
        return message.toString();
    }
}