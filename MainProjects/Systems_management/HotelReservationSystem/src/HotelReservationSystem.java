import java.time.LocalDate;
import java.util.*;

public class HotelReservationSystem {
    private final Hotel hotel;
    private final RoomManager roomManager;
    private final ReservationManager reservationManager;
    private final GuestManager guestManager;
    private final Scanner scanner;
    
    public HotelReservationSystem() {
        this.roomManager = new RoomManager();
        this.reservationManager = new ReservationManager(roomManager);
        this.guestManager = new GuestManager();
        this.scanner = new Scanner(System.in);
        this.hotel = initializeHotel();
        initializeSampleData();
    }
    
    private Hotel initializeHotel() {
        Hotel h = new Hotel("Grand Paradise Hotel", "123 Beach Boulevard", "Miami", "USA", 
                              5, "+1-305-555-0123", "info@grandparadise.com");
        h.setDescription("Luxury beachfront hotel with world-class amenities");
        h.setHasPool(true);
        h.setHasGym(true);
        h.setHasSpa(true);
        h.setHasRestaurant(true);
        h.setHasBar(true);
        h.setHasWifi(true);
        h.setHasParking(true);
        h.setTotalFloors(15);
        h.setTotalRooms(200);
        
        h.addAmenity("24/7 Room Service");
        h.addAmenity("Concierge Service");
        h.addAmenity("Valet Parking");
        h.addAmenity("Beach Access");
        h.addAmenity("Business Center");
        
        h.addService("Airport Shuttle");
        h.addService("Laundry Service");
        h.addService("Spa Treatments");
        h.addService("Tour Booking");
        
        return h;
    }
    
    private void initializeSampleData() {
        createSampleRooms();
        createSampleGuests();
        createSampleReservations();
    }
    
    private void createSampleRooms() {
        RoomType[] types = {RoomType.SINGLE, RoomType.DOUBLE, RoomType.TWIN, RoomType.DELUXE, RoomType.SUITE};
        Random random = new Random();
        
        for (int floor = 1; floor <= 15; floor++) {
            for (int roomOnFloor = 1; roomOnFloor <= 15; roomOnFloor++) {
                int roomNumber = floor * 100 + roomOnFloor;
                RoomType type = types[random.nextInt(types.length)];
                double basePrice = type.getBasePrice() + (random.nextDouble() * 50);
                
                Room room = new Room(roomNumber, type, basePrice);
                room.setFloor(floor);
                
                if (random.nextDouble() < 0.3) room.setHasSeaView(true);
                if (random.nextDouble() < 0.4) room.setHasBalcony(true);
                if (random.nextDouble() < 0.2) room.setHasMountainView(true);
                if (random.nextDouble() < 0.1) room.setAccessible(true);
                
                roomManager.addRoom(room);
            }
        }
        
        for (int i = 0; i < 3; i++) {
            int roomNumber = 1500 + i + 1;
            Room presidentialSuite = new Room(roomNumber, RoomType.PRESIDENTIAL, 800.0);
            presidentialSuite.setFloor(15);
            presidentialSuite.setHasSeaView(true);
            presidentialSuite.setHasBalcony(true);
            presidentialSuite.addAmenity("Private Butler");
            presidentialSuite.addAmenity("Jacuzzi");
            presidentialSuite.addAmenity("Private Dining Room");
            roomManager.addRoom(presidentialSuite);
        }
    }
    
    private void createSampleGuests() {
        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily", "James", "Anna"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Wilson", "Miller", "Moore", "Taylor", "Anderson", "Thomas"};
        String[] countries = {"USA", "UK", "Canada", "Germany", "France", "Australia", "Japan", "Italy", "Spain", "Brazil"};
        
        Random random = new Random();
        
        for (int i = 0; i < 50; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@email.com";
            String phone = "+1-" + (random.nextInt(900) + 100) + "-" + (random.nextInt(900) + 100) + "-" + (random.nextInt(9000) + 1000);
            String country = countries[random.nextInt(countries.length)];
            
            LocalDate birthDate = LocalDate.now().minusYears(random.nextInt(50) + 18);
            
            Guest guest = guestManager.createGuest(firstName, lastName, email, phone, 
                                                  "123 Main St, City, State", birthDate, country, "ID" + (i + 1000));
            
            if (guest != null && random.nextDouble() < 0.15) {
                guest.addToTotalSpent(random.nextDouble() * 8000 + 2000);
            }
        }
    }
    
    private void createSampleReservations() {
        List<Guest> guests = guestManager.getAllGuests();
        List<Room> rooms = roomManager.getAllRooms();
        Random random = new Random();
        
        for (int i = 0; i < 20; i++) {
            if (guests.isEmpty() || rooms.isEmpty()) break;
            
            Guest guest = guests.get(random.nextInt(guests.size()));
            Room room = rooms.get(random.nextInt(rooms.size()));
            
            LocalDate checkIn = LocalDate.now().plusDays(random.nextInt(60));
            LocalDate checkOut = checkIn.plusDays(random.nextInt(7) + 1);
            int numberOfGuests = random.nextInt(room.getMaxOccupancy()) + 1;
            
            if (roomManager.isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                Reservation reservation = reservationManager.createReservation(
                    guest.getGuestId(), room.getRoomNumber(), checkIn, checkOut, numberOfGuests);
                
                if (reservation != null) {
                    reservation.setPaymentMethod(PaymentMethod.CREDIT_CARD);
                    reservation.setBookedBy(guest.getFullName());
                    reservation.setContactEmail(guest.getEmail());
                    reservation.setContactPhone(guest.getPhoneNumber());
                    
                    if (random.nextDouble() < 0.8) {
                        reservationManager.confirmReservation(reservation.getReservationId());
                    }
                    
                    if (random.nextDouble() < 0.3) {
                        reservation.setBreakfastIncluded(true);
                    }
                    
                    if (random.nextDouble() < 0.2) {
                        reservation.setParkingIncluded(true);
                    }
                }
            }
        }
    }
    
    public void start() {
        System.out.println("========================================");
        System.out.println("  Welcome to " + hotel.getName());
        System.out.println("========================================");
        System.out.println(hotel.getDescription());
        System.out.println("Location: " + hotel.getFullAddress());
        System.out.println("Rating: " + hotel.getStarRatingDisplay());
        System.out.println("========================================");
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1 -> roomManagementMenu();
                case 2 -> reservationManagementMenu();
                case 3 -> guestManagementMenu();
                case 4 -> reportsMenu();
                case 5 -> searchMenu();
                case 6 -> systemAdministrationMenu();
                case 0 -> {
                    System.out.println("Thank you for using " + hotel.getName() + " Reservation System!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Room Management");
        System.out.println("2. Reservation Management");
        System.out.println("3. Guest Management");
        System.out.println("4. Reports & Analytics");
        System.out.println("5. Search & Lookup");
        System.out.println("6. System Administration");
        System.out.println("0. Exit System");
        System.out.println("===============================");
    }
    
    private void roomManagementMenu() {
        while (true) {
            System.out.println("\n========== ROOM MANAGEMENT ==========");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Available Rooms");
            System.out.println("3. View Room by Number");
            System.out.println("4. Update Room Status");
            System.out.println("5. Mark Room Out of Order");
            System.out.println("6. Mark Room Operational");
            System.out.println("7. Clean Room");
            System.out.println("8. Room Maintenance");
            System.out.println("9. Room Analytics");
            System.out.println("0. Back to Main Menu");
            System.out.println("=====================================");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1 -> viewAllRooms();
                case 2 -> viewAvailableRooms();
                case 3 -> viewRoomByNumber();
                case 4 -> updateRoomStatus();
                case 5 -> markRoomOutOfOrder();
                case 6 -> markRoomOperational();
                case 7 -> cleanRoom();
                case 8 -> roomMaintenanceMenu();
                case 9 -> roomAnalytics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void reservationManagementMenu() {
        while (true) {
            System.out.println("\n========== RESERVATION MANAGEMENT ==========");
            System.out.println("1. Create New Reservation");
            System.out.println("2. View All Reservations");
            System.out.println("3. Find Reservation");
            System.out.println("4. Check In Guest");
            System.out.println("5. Check Out Guest");
            System.out.println("6. Cancel Reservation");
            System.out.println("7. Modify Reservation");
            System.out.println("8. Process Payment");
            System.out.println("9. Today's Arrivals/Departures");
            System.out.println("10. Reservation Analytics");
            System.out.println("0. Back to Main Menu");
            System.out.println("============================================");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1 -> createNewReservation();
                case 2 -> viewAllReservations();
                case 3 -> findReservation();
                case 4 -> checkInGuest();
                case 5 -> checkOutGuest();
                case 6 -> cancelReservation();
                case 7 -> modifyReservation();
                case 8 -> processPayment();
                case 9 -> todaysArrivalsAndDepartures();
                case 10 -> reservationAnalytics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void guestManagementMenu() {
        while (true) {
            System.out.println("\n========== GUEST MANAGEMENT ==========");
            System.out.println("1. Register New Guest");
            System.out.println("2. View All Guests");
            System.out.println("3. Find Guest");
            System.out.println("4. Update Guest Information");
            System.out.println("5. VIP Guest Management");
            System.out.println("6. Guest History");
            System.out.println("7. Guest Analytics");
            System.out.println("0. Back to Main Menu");
            System.out.println("======================================");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1 -> registerNewGuest();
                case 2 -> viewAllGuests();
                case 3 -> findGuest();
                case 4 -> updateGuestInformation();
                case 5 -> vipGuestManagement();
                case 6 -> guestHistory();
                case 7 -> guestAnalytics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void createNewReservation() {
        System.out.println("\n=== CREATE NEW RESERVATION ===");
        
        String email = getStringInput("Enter guest email: ");
        Guest guest = guestManager.getGuestByEmail(email);
        
        if (guest == null) {
            System.out.println("Guest not found. Would you like to register a new guest? (y/n)");
            String response = scanner.nextLine().trim();
            if (response.equalsIgnoreCase("y")) {
                guest = registerNewGuestInline();
                if (guest == null) return;
            } else {
                return;
            }
        }
        
        LocalDate checkIn = getDateInput("Check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = getDateInput("Check-out date (YYYY-MM-DD): ");
        
        if (checkIn == null || checkOut == null || checkIn.isAfter(checkOut) || checkIn.isBefore(LocalDate.now())) {
            System.out.println("Invalid dates. Please check your input.");
            return;
        }
        
        displayRoomTypes();
        String roomTypeStr = getStringInput("Preferred room type (or 'any'): ");
        RoomType roomType = roomTypeStr.equalsIgnoreCase("any") ? null : RoomType.fromString(roomTypeStr);
        
        int numberOfGuests = getIntInput("Number of guests: ");
        
        List<Room> availableRooms;
        if (roomType != null) {
            availableRooms = roomManager.getAvailableRoomsByType(roomType, checkIn, checkOut);
        } else {
            availableRooms = roomManager.getAvailableRooms(checkIn, checkOut);
        }
        
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected dates and criteria.");
            return;
        }
        
        System.out.println("\nAvailable rooms:");
        for (int i = 0; i < Math.min(10, availableRooms.size()); i++) {
            Room room = availableRooms.get(i);
            System.out.printf("%d. Room %d - %s - $%.2f/night (Max %d guests)\n",
                             i + 1, room.getRoomNumber(), room.getRoomType().getDisplayName(), 
                             room.getActualPrice(), room.getMaxOccupancy());
        }
        
        int roomChoice = getIntInput("Select room (1-" + Math.min(10, availableRooms.size()) + "): ") - 1;
        if (roomChoice < 0 || roomChoice >= availableRooms.size()) {
            System.out.println("Invalid room selection.");
            return;
        }
        
        Room selectedRoom = availableRooms.get(roomChoice);
        
        if (numberOfGuests > selectedRoom.getMaxOccupancy()) {
            System.out.println("Number of guests exceeds room capacity.");
            return;
        }
        
        Reservation reservation = reservationManager.createReservation(
            guest.getGuestId(), selectedRoom.getRoomNumber(), checkIn, checkOut, numberOfGuests);
        
        if (reservation != null) {
            reservation.setBookedBy(guest.getFullName());
            reservation.setContactEmail(guest.getEmail());
            reservation.setContactPhone(guest.getPhoneNumber());
            
            System.out.println("Would you like to add any extras? (y/n)");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                addReservationExtras(reservation);
            }
            
            displayPaymentMethods();
            String paymentStr = getStringInput("Select payment method: ");
            PaymentMethod paymentMethod = PaymentMethod.fromString(paymentStr);
            reservation.setPaymentMethod(paymentMethod);
            
            double finalAmount = reservation.calculateFinalAmount();
            reservation.setTotalAmount(finalAmount);
            
            System.out.println("\n=== RESERVATION CREATED ===");
            System.out.println("Confirmation Code: " + reservation.getConfirmationCode());
            System.out.println("Guest: " + guest.getFullName());
            System.out.println("Room: " + selectedRoom.getRoomNumber() + " (" + selectedRoom.getRoomType().getDisplayName() + ")");
            System.out.println("Check-in: " + checkIn);
            System.out.println("Check-out: " + checkOut);
            System.out.println("Nights: " + reservation.getNumberOfNights());
            System.out.println("Guests: " + numberOfGuests);
            System.out.println("Total Amount: $" + String.format("%.2f", finalAmount));
            
            reservationManager.confirmReservation(reservation.getReservationId());
            System.out.println("Reservation confirmed!");
        } else {
            System.out.println("Failed to create reservation. Please try again.");
        }
    }
    
    private void addReservationExtras(Reservation reservation) {
        System.out.println("\n=== AVAILABLE EXTRAS ===");
        System.out.println("1. Breakfast - $25/night");
        System.out.println("2. Parking - $15/night");
        System.out.println("3. Spa Package - $80");
        System.out.println("4. Airport Transfer - $50");
        System.out.println("5. Late Checkout - $40");
        System.out.println("Enter extras (comma-separated numbers, or 'none'): ");
        
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("none")) return;
        
        String[] choices = input.split(",");
        for (String choice : choices) {
            try {
                int option = Integer.parseInt(choice.trim());
                switch (option) {
                    case 1 -> reservation.setBreakfastIncluded(true);
                    case 2 -> reservation.setParkingIncluded(true);
                    case 3 -> reservation.addAdditionalService("spa");
                    case 4 -> reservation.addAdditionalService("airport transfer");
                    case 5 -> reservation.addAdditionalService("late checkout");
                    default -> System.out.println("Invalid option: " + choice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option: " + choice);
            }
        }
    }
    
    private Guest registerNewGuestInline() {
        System.out.println("\n=== REGISTER NEW GUEST ===");
        String firstName = getStringInput("First name: ");
        String lastName = getStringInput("Last name: ");
        String email = getStringInput("Email: ");
        String phone = getStringInput("Phone number: ");
        
        return guestManager.createGuest(firstName, lastName, email, phone);
    }
    
    private void registerNewGuest() {
        Guest guest = registerNewGuestInline();
        if (guest != null) {
            System.out.println("Guest registered successfully! ID: " + guest.getGuestId());
        } else {
            System.out.println("Failed to register guest.");
        }
    }
    
    private void viewAllGuests() {
        List<Guest> guests = guestManager.getAllGuests();
        System.out.println("\n=== ALL GUESTS (" + guests.size() + ") ===");
        for (Guest guest : guests) {
            System.out.printf("ID: %d | %s | %s | %s | VIP: %s\n",
                guest.getGuestId(), guest.getFullName(), guest.getEmail(),
                guest.getPhoneNumber(), guest.isVip() ? "Yes" : "No");
        }
    }
    
    private void findGuest() {
        String searchTerm = getStringInput("Enter guest name or email: ");
        List<Guest> results = guestManager.searchGuests(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No guests found.");
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Guest guest : results) {
                System.out.printf("ID: %d | %s | %s | %s\n",
                    guest.getGuestId(), guest.getFullName(), guest.getEmail(), guest.getPhoneNumber());
            }
        }
    }
    
    private void updateGuestInformation() {
        int guestId = getIntInput("Enter guest ID: ");
        Guest guest = guestManager.getGuest(guestId);
        
        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }
        
        System.out.println("Current info: " + guest.getFullName() + " | " + guest.getEmail());
        String firstName = getStringInput("New first name (or press Enter to skip): ");
        String lastName = getStringInput("New last name (or press Enter to skip): ");
        String email = getStringInput("New email (or press Enter to skip): ");
        String phone = getStringInput("New phone (or press Enter to skip): ");
        String address = getStringInput("New address (or press Enter to skip): ");
        
        if (guestManager.updateGuestInfo(guestId, 
                firstName.isEmpty() ? guest.getFirstName() : firstName,
                lastName.isEmpty() ? guest.getLastName() : lastName,
                email.isEmpty() ? guest.getEmail() : email,
                phone.isEmpty() ? guest.getPhoneNumber() : phone,
                address.isEmpty() ? guest.getAddress() : address)) {
            System.out.println("Guest information updated successfully.");
        } else {
            System.out.println("Failed to update guest information.");
        }
    }
    
    private void vipGuestManagement() {
        System.out.println("\n=== VIP GUEST MANAGEMENT ===");
        System.out.println("1. View VIP Guests");
        System.out.println("2. Promote Guest to VIP");
        System.out.println("3. Demote Guest from VIP");
        
        int choice = getIntInput("Select option: ");
        
        switch (choice) {
            case 1 -> {
                List<Guest> vips = guestManager.getVipGuests();
                System.out.println("\n=== VIP GUESTS ===");
                for (Guest guest : vips) {
                    System.out.printf("%s | Total Spent: $%.2f\n",
                        guest.getFullName(), guest.getTotalSpent());
                }
            }
            case 2 -> {
                int guestId = getIntInput("Enter guest ID to promote: ");
                if (guestManager.promoteToVip(guestId)) {
                    System.out.println("Guest promoted to VIP successfully.");
                } else {
                    System.out.println("Failed to promote guest.");
                }
            }
            case 3 -> {
                int guestId = getIntInput("Enter guest ID to demote: ");
                if (guestManager.demoteFromVip(guestId)) {
                    System.out.println("Guest demoted from VIP successfully.");
                } else {
                    System.out.println("Failed to demote guest.");
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }
    
    private void guestHistory() {
        int guestId = getIntInput("Enter guest ID: ");
        Guest guest = guestManager.getGuest(guestId);
        
        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }
        
        List<Reservation> reservations = reservationManager.getGuestReservations(guestId);
        System.out.println("\n=== GUEST HISTORY: " + guest.getFullName() + " ===");
        System.out.println("Total Reservations: " + reservations.size());
        System.out.println("Total Spent: $" + String.format("%.2f", guest.getTotalSpent()));
        System.out.println("VIP Status: " + (guest.isVip() ? "Yes" : "No"));
        
        System.out.println("\nReservations:");
        displayReservationList(reservations);
    }
    
    private void guestAnalytics() {
        System.out.println("\n=== GUEST ANALYTICS ===");
        System.out.println("Total Guests: " + guestManager.getTotalGuestCount());
        System.out.println("VIP Guests: " + guestManager.getVipGuestCount());
        System.out.println("Total Guest Spending: $" + String.format("%.2f", guestManager.getTotalGuestSpending()));
        System.out.println("Average Guest Spending: $" + String.format("%.2f", guestManager.getAverageGuestSpending()));
        
        Guest topSpender = guestManager.getTopSpendingGuest();
        if (topSpender != null) {
            System.out.printf("Top Spender: %s ($%.2f)\n", topSpender.getFullName(), topSpender.getTotalSpent());
        }
    }
    
    private void viewAllRooms() {
        List<Room> rooms = roomManager.getAllRooms();
        System.out.println("\n=== ALL ROOMS ===");
        System.out.printf("%-8s %-15s %-12s %-10s %-10s\n", "Room", "Type", "Status", "Price", "Floor");
        System.out.println("--------------------------------------------------------");
        
        for (Room room : rooms) {
            System.out.printf("%-8d %-15s %-12s $%-9.2f %-10d\n",
                room.getRoomNumber(),
                room.getRoomType().getDisplayName(),
                room.getStatus().getDisplayName(),
                room.getActualPrice(),
                room.getFloor());
        }
    }
    
    private void viewAvailableRooms() {
        LocalDate checkIn = getDateInput("Check-in date (YYYY-MM-DD, or press enter for today): ");
        if (checkIn == null) checkIn = LocalDate.now();
        
        LocalDate checkOut = getDateInput("Check-out date (YYYY-MM-DD, or press enter for tomorrow): ");
        if (checkOut == null) checkOut = LocalDate.now().plusDays(1);
        
        List<Room> availableRooms = roomManager.getAvailableRooms(checkIn, checkOut);
        
        System.out.println("\n=== AVAILABLE ROOMS ===");
        System.out.printf("%-8s %-15s %-10s %-12s %-8s\n", "Room", "Type", "Price", "Features", "Max Occ");
        System.out.println("-----------------------------------------------------------");
        
        for (Room room : availableRooms) {
            StringBuilder features = new StringBuilder();
            if (room.hasSeaView()) features.append("Sea ");
            if (room.hasBalcony()) features.append("Balcony ");
            if (room.isAccessible()) features.append("Access ");
            
            System.out.printf("%-8d %-15s $%-9.2f %-12s %-8d\n",
                room.getRoomNumber(),
                room.getRoomType().getDisplayName(),
                room.getActualPrice(),
                features.toString().trim(),
                room.getMaxOccupancy());
        }
        
        System.out.println("\nTotal available rooms: " + availableRooms.size());
    }
    
    private void viewRoomByNumber() {
        int roomNumber = getIntInput("Enter room number: ");
        Room room = roomManager.getRoom(roomNumber);
        
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        System.out.println("\n=== ROOM DETAILS ===");
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Type: " + room.getRoomType().getDisplayName());
        System.out.println("Status: " + room.getStatus().getDisplayName());
        System.out.println("Floor: " + room.getFloor());
        System.out.println("Base Price: $" + String.format("%.2f", room.getBasePrice()));
        System.out.println("Actual Price: $" + String.format("%.2f", room.getActualPrice()));
        System.out.println("Max Occupancy: " + room.getMaxOccupancy());
        System.out.println("Sea View: " + (room.hasSeaView() ? "Yes" : "No"));
        System.out.println("Balcony: " + (room.hasBalcony() ? "Yes" : "No"));
    }
    
    private void updateRoomStatus() {
        int roomNumber = getIntInput("Enter room number: ");
        Room room = roomManager.getRoom(roomNumber);
        
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        System.out.println("Current status: " + room.getStatus().getDisplayName());
        System.out.println("Select new status:");
        System.out.println("1. AVAILABLE");
        System.out.println("2. OCCUPIED");
        System.out.println("3. DIRTY");
        System.out.println("4. CLEANING");
        System.out.println("5. MAINTENANCE");
        
        int choice = getIntInput("Enter choice: ");
        RoomStatus newStatus = switch (choice) {
            case 1 -> RoomStatus.AVAILABLE;
            case 2 -> RoomStatus.OCCUPIED;
            case 3 -> RoomStatus.DIRTY;
            case 4 -> RoomStatus.CLEANING;
            case 5 -> RoomStatus.MAINTENANCE;
            default -> null;
        };
        
        if (newStatus != null && roomManager.updateRoomStatus(roomNumber, newStatus)) {
            System.out.println("Room status updated successfully.");
        } else {
            System.out.println("Failed to update room status.");
        }
    }
    
    private void markRoomOutOfOrder() {
        int roomNumber = getIntInput("Enter room number: ");
        String reason = getStringInput("Enter reason: ");
        roomManager.markRoomOutOfOrder(roomNumber, reason);
        System.out.println("Room marked as out of order.");
    }
    
    private void markRoomOperational() {
        int roomNumber = getIntInput("Enter room number: ");
        roomManager.markRoomOperational(roomNumber);
        System.out.println("Room marked as operational.");
    }
    
    private void cleanRoom() {
        int roomNumber = getIntInput("Enter room number: ");
        String cleanedBy = getStringInput("Cleaned by: ");
        if (roomManager.cleanRoom(roomNumber, cleanedBy)) {
            System.out.println("Room cleaned successfully.");
        } else {
            System.out.println("Failed to clean room.");
        }
    }
    
    private void roomMaintenanceMenu() {
        System.out.println("\n=== ROOM MAINTENANCE ===");
        System.out.println("Rooms needing maintenance: " + roomManager.getRoomsDueForMaintenance().size());
        System.out.println("Rooms needing cleaning: " + roomManager.getRoomsNeedingCleaning().size());
    }
    
    private void roomAnalytics() {
        System.out.println("\n=== ROOM ANALYTICS ===");
        System.out.println("Total Rooms: " + roomManager.getTotalRoomCount());
        System.out.println("Available Rooms: " + roomManager.getAvailableRoomCount());
        System.out.println("Occupied Rooms: " + roomManager.getOccupiedRoomCount());
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", roomManager.getOccupancyRate()));
        System.out.println("Average Room Price: $" + String.format("%.2f", roomManager.getAverageRoomPrice()));
    }
    
    private void viewAllReservations() {
        List<Reservation> reservations = reservationManager.getAllReservations();
        System.out.println("\n=== ALL RESERVATIONS ===");
        displayReservationList(reservations);
    }
    
    private void findReservation() {
        System.out.println("\n=== FIND RESERVATION ===");
        System.out.println("1. By Confirmation Code");
        System.out.println("2. By Reservation ID");
        
        int choice = getIntInput("Select search method: ");
        Reservation reservation = null;
        
        switch (choice) {
            case 1 -> {
                String code = getStringInput("Enter confirmation code: ");
                reservation = reservationManager.getReservationByConfirmationCode(code);
            }
            case 2 -> {
                int id = getIntInput("Enter reservation ID: ");
                reservation = reservationManager.getReservation(id);
            }
            default -> {
                System.out.println("Invalid option.");
                return;
            }
        }
        
        if (reservation != null) {
            displayReservationDetails(reservation);
        } else {
            System.out.println("Reservation not found.");
        }
    }
    
    private void checkInGuest() {
        int reservationId = getIntInput("Enter reservation ID: ");
        if (reservationManager.checkInGuest(reservationId)) {
            System.out.println("Guest checked in successfully.");
        } else {
            System.out.println("Failed to check in guest.");
        }
    }
    
    private void checkOutGuest() {
        int reservationId = getIntInput("Enter reservation ID: ");
        if (reservationManager.checkOutGuest(reservationId)) {
            System.out.println("Guest checked out successfully.");
        } else {
            System.out.println("Failed to check out guest.");
        }
    }
    
    private void cancelReservation() {
        int reservationId = getIntInput("Enter reservation ID: ");
        String reason = getStringInput("Cancellation reason: ");
        if (reservationManager.cancelReservation(reservationId, reason)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Failed to cancel reservation.");
        }
    }
    
    private void modifyReservation() {
        int reservationId = getIntInput("Enter reservation ID: ");
        Reservation reservation = reservationManager.getReservation(reservationId);
        
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }
        
        System.out.println("Current check-in: " + reservation.getCheckInDate());
        System.out.println("Current check-out: " + reservation.getCheckOutDate());
        
        LocalDate newCheckIn = getDateInput("New check-in date (or press Enter to keep): ");
        LocalDate newCheckOut = getDateInput("New check-out date (or press Enter to keep): ");
        int newGuests = getIntInput("New number of guests (or 0 to keep): ");
        
        if (newCheckIn == null) newCheckIn = reservation.getCheckInDate();
        if (newCheckOut == null) newCheckOut = reservation.getCheckOutDate();
        if (newGuests == 0) newGuests = reservation.getNumberOfGuests();
        
        if (reservationManager.modifyReservation(reservationId, newCheckIn, newCheckOut, newGuests)) {
            System.out.println("Reservation modified successfully.");
        } else {
            System.out.println("Failed to modify reservation.");
        }
    }
    
    private void processPayment() {
        int reservationId = getIntInput("Enter reservation ID: ");
        double amount = getDoubleInput("Enter amount: ");
        
        displayPaymentMethods();
        String methodStr = getStringInput("Select payment method: ");
        PaymentMethod method = PaymentMethod.fromString(methodStr);
        
        if (reservationManager.processPayment(reservationId, amount, method)) {
            System.out.println("Payment processed successfully.");
        } else {
            System.out.println("Failed to process payment.");
        }
    }
    
    private void todaysArrivalsAndDepartures() {
        LocalDate today = LocalDate.now();
        
        System.out.println("\n=== TODAY'S ARRIVALS ===");
        List<Reservation> arrivals = reservationManager.getArrivalsForDate(today);
        displayReservationList(arrivals);
        
        System.out.println("\n=== TODAY'S DEPARTURES ===");
        List<Reservation> departures = reservationManager.getDeparturesForDate(today);
        displayReservationList(departures);
    }
    
    private void reservationAnalytics() {
        System.out.println("\n=== RESERVATION ANALYTICS ===");
        System.out.println("Total Reservations: " + reservationManager.getTotalReservationCount());
        System.out.println("Active Reservations: " + reservationManager.getActiveReservations().size());
        System.out.println("Total Revenue: $" + String.format("%.2f", reservationManager.getTotalRevenue()));
        System.out.println("Pending Payments: $" + String.format("%.2f", reservationManager.getPendingPayments()));
        System.out.println("Average Reservation Value: $" + String.format("%.2f", reservationManager.getAverageReservationValue()));
        System.out.println("Cancellation Rate: " + String.format("%.2f%%", reservationManager.getCancellationRate()));
    }
    
    private void reportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Room Analytics");
        System.out.println("2. Reservation Analytics");
        System.out.println("3. Guest Analytics");
        
        int choice = getIntInput("Select report: ");
        
        switch (choice) {
            case 1 -> roomAnalytics();
            case 2 -> reservationAnalytics();
            case 3 -> guestAnalytics();
            default -> System.out.println("Invalid option.");
        }
    }
    
    private void searchMenu() {
        System.out.println("\n=== SEARCH & LOOKUP ===");
        System.out.println("1. Find Guest");
        System.out.println("2. Find Reservation");
        System.out.println("3. Find Room");
        
        int choice = getIntInput("Select search type: ");
        
        switch (choice) {
            case 1 -> findGuest();
            case 2 -> findReservation();
            case 3 -> viewRoomByNumber();
            default -> System.out.println("Invalid option.");
        }
    }
    
    private void systemAdministrationMenu() {
        System.out.println("\n=== SYSTEM ADMINISTRATION ===");
        System.out.println("1. Process No-Shows");
        System.out.println("2. View System Statistics");
        
        int choice = getIntInput("Select option: ");
        
        switch (choice) {
            case 1 -> {
                reservationManager.processNoShows();
                System.out.println("No-shows processed.");
            }
            case 2 -> {
                System.out.println("Total Rooms: " + roomManager.getTotalRoomCount());
                System.out.println("Total Guests: " + guestManager.getTotalGuestCount());
                System.out.println("Total Reservations: " + reservationManager.getTotalReservationCount());
            }
            default -> System.out.println("Invalid option.");
        }
    }
    
    private void displayReservationDetails(Reservation reservation) {
        Guest guest = guestManager.getGuest(reservation.getGuestId());
        Room room = roomManager.getRoom(reservation.getRoomNumber());
        
        System.out.println("\n=== RESERVATION DETAILS ===");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Confirmation Code: " + reservation.getConfirmationCode());
        System.out.println("Status: " + reservation.getStatus().getDisplayName());
        System.out.println("Guest: " + (guest != null ? guest.getFullName() : "Unknown"));
        System.out.println("Room: " + reservation.getRoomNumber() + 
                         (room != null ? " (" + room.getRoomType().getDisplayName() + ")" : ""));
        System.out.println("Check-in: " + reservation.getCheckInDate());
        System.out.println("Check-out: " + reservation.getCheckOutDate());
        System.out.println("Nights: " + reservation.getNumberOfNights());
        System.out.println("Guests: " + reservation.getNumberOfGuests());
        System.out.println("Total Amount: $" + String.format("%.2f", reservation.getTotalAmount()));
        System.out.println("Paid Amount: $" + String.format("%.2f", reservation.getPaidAmount()));
        System.out.println("Balance: $" + String.format("%.2f", reservation.getRemainingBalance()));
    }
    
    private void displayReservationList(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        
        System.out.println("\nReservations:");
        for (Reservation res : reservations) {
            System.out.printf("ID: %d | Code: %s | Room %d | %s to %s | %s | $%.2f\n",
                res.getReservationId(), res.getConfirmationCode(), res.getRoomNumber(),
                res.getCheckInDate(), res.getCheckOutDate(), 
                res.getStatus().getDisplayName(), res.getTotalAmount());
        }
    }
    
    private void displayRoomTypes() {
        System.out.println("\n=== AVAILABLE ROOM TYPES ===");
        for (RoomType type : RoomType.values()) {
            System.out.printf("%s - %s (Max %d guests) - Starting from $%.2f/night\n",
                type.name(), type.getDisplayName(), type.getStandardOccupancy(), type.getBasePrice());
        }
    }
    
    private void displayPaymentMethods() {
        System.out.println("\n=== PAYMENT METHODS ===");
        for (PaymentMethod method : PaymentMethod.values()) {
            System.out.printf("%s - %s\n", method.name(), method.getDisplayName());
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return 0;
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return 0.0;
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return null;
                return LocalDate.parse(input);
            } catch (Exception e) {
                System.out.println("Please enter date in YYYY-MM-DD format.");
            }
        }
    }
    
    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.start();
    }
}
