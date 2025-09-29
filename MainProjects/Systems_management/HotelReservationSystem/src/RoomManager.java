
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomManager {

    private Map<Integer, Room> rooms;
    private Map<Integer, List<Reservation>> roomReservations;
    private Set<Integer> outOfOrderRooms;
    private Map<RoomType, Integer> roomTypeCount;

    public RoomManager() {
        this.rooms = new HashMap<>();
        this.roomReservations = new HashMap<>();
        this.outOfOrderRooms = new HashSet<>();
        this.roomTypeCount = new HashMap<>();
        initializeRoomTypeCounts();
    }

    private void initializeRoomTypeCounts() {
        for (RoomType type : RoomType.values()) {
            roomTypeCount.put(type, 0);
        }
    }

    public boolean addRoom(Room room) {
        if (room == null || rooms.containsKey(room.getRoomNumber())) {
            return false;
        }
        rooms.put(room.getRoomNumber(), room);
        roomReservations.put(room.getRoomNumber(), new ArrayList<>());
        roomTypeCount.put(room.getRoomType(), roomTypeCount.get(room.getRoomType()) + 1);
        return true;
    }

    public boolean removeRoom(int roomNumber) {
        Room room = rooms.remove(roomNumber);
        if (room != null) {
            roomReservations.remove(roomNumber);
            outOfOrderRooms.remove(roomNumber);
            roomTypeCount.put(room.getRoomType(), roomTypeCount.get(room.getRoomType()) - 1);
            return true;
        }
        return false;
    }

    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Room> getAvailableRoomsByType(RoomType roomType) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getRoomType() == roomType && room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Room> getAvailableRoomsByType(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getRoomType() == roomType && isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        Room room = rooms.get(roomNumber);
        if (room == null || !room.isAvailable() || outOfOrderRooms.contains(roomNumber)) {
            return false;
        }
        
        List<Reservation> reservations = roomReservations.get(roomNumber);
        if (reservations == null) {
            return true;
        }
        
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().isActive()) {
                if (datesOverlap(checkIn, checkOut, reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public void addReservationToRoom(int roomNumber, Reservation reservation) {
        List<Reservation> reservations = roomReservations.get(roomNumber);
        if (reservations != null) {
            reservations.add(reservation);
            Room room = rooms.get(roomNumber);
            if (room != null) {
                room.reserve();
            }
        }
    }

    public void removeReservationFromRoom(int roomNumber, Reservation reservation) {
        List<Reservation> reservations = roomReservations.get(roomNumber);
        if (reservations != null) {
            reservations.remove(reservation);
        }
    }

    public List<Reservation> getRoomReservations(int roomNumber) {
        List<Reservation> reservations = roomReservations.get(roomNumber);
        return reservations != null ? new ArrayList<>(reservations) : new ArrayList<>();
    }

    public boolean checkInGuest(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.RESERVED) {
            room.checkIn();
            return true;
        }
        return false;
    }

    public boolean checkOutGuest(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
            room.checkOut();
            return true;
        }
        return false;
    }

    public boolean cleanRoom(int roomNumber, String cleanedBy) {
        Room room = rooms.get(roomNumber);
        if (room != null && room.getStatus() == RoomStatus.DIRTY) {
            room.clean();
            room.setLastCleanedBy(cleanedBy);
            return true;
        }
        return false;
    }

    public void markRoomOutOfOrder(int roomNumber, String reason) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            room.makeUnavailable();
            room.setNeedsMaintenance(true);
            outOfOrderRooms.add(roomNumber);
        }
    }

    public void markRoomOperational(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room != null && outOfOrderRooms.contains(roomNumber)) {
            room.setNeedsMaintenance(false);
            room.makeAvailable();
            outOfOrderRooms.remove(roomNumber);
        }
    }

    public List<Room> getOutOfOrderRooms() {
        List<Room> outOfOrder = new ArrayList<>();
        for (int roomNumber : outOfOrderRooms) {
            Room room = rooms.get(roomNumber);
            if (room != null) {
                outOfOrder.add(room);
            }
        }
        return outOfOrder;
    }

    public List<Room> getRoomsByStatus(RoomStatus status) {
        List<Room> roomsByStatus = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getStatus() == status) {
                roomsByStatus.add(room);
            }
        }
        return roomsByStatus;
    }

    public List<Room> getRoomsByFloor(int floor) {
        List<Room> roomsByFloor = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getFloor() == floor) {
                roomsByFloor.add(room);
            }
        }
        return roomsByFloor;
    }

    public List<Room> getRoomsWithAmenity(String amenity) {
        List<Room> roomsWithAmenity = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getAmenities().contains(amenity)) {
                roomsWithAmenity.add(room);
            }
        }
        return roomsWithAmenity;
    }

    public List<Room> searchRooms(RoomType roomType, boolean hasSeaView, boolean hasBalcony,
                                boolean isAccessible, int minFloor, int maxFloor) {
        List<Room> matchingRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if ((roomType == null || room.getRoomType() == roomType) &&
                (!hasSeaView || room.hasSeaView()) &&
                (!hasBalcony || room.hasBalcony()) &&
                (!isAccessible || room.isAccessible()) &&
                room.getFloor() >= minFloor &&
                room.getFloor() <= maxFloor) {
                matchingRooms.add(room);
            }
        }
        return matchingRooms;
    }

    public double getAverageRoomPrice() {
        if (rooms.isEmpty()) return 0.0;

        double total = 0.0;
        for (Room room : rooms.values()) {
            total += room.getActualPrice();
        }
        return total / rooms.size();
    }

    public double getAverageRoomPriceByType(RoomType roomType) {
        List<Room> roomsOfType = getRoomsByType(roomType);
        if (roomsOfType.isEmpty()) return 0.0;

        double total = 0.0;
        for (Room room : roomsOfType) {
            total += room.getActualPrice();
        }
        return total / roomsOfType.size();
    }

    public List<Room> getRoomsByType(RoomType roomType) {
        List<Room> roomsOfType = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getRoomType() == roomType) {
                roomsOfType.add(room);
            }
        }
        return roomsOfType;
    }

    public int getTotalRoomCount() {
        return rooms.size();
    }

    public int getAvailableRoomCount() {
        return getAvailableRooms().size();
    }

    public int getOccupiedRoomCount() {
        return getRoomsByStatus(RoomStatus.OCCUPIED).size();
    }

    public int getRoomCountByType(RoomType roomType) {
        return roomTypeCount.getOrDefault(roomType, 0);
    }

    public double getOccupancyRate() {
        if (rooms.isEmpty()) return 0.0;
        return (double) getOccupiedRoomCount() / rooms.size() * 100;
    }

    public Map<RoomStatus, Integer> getRoomStatusDistribution() {
        Map<RoomStatus, Integer> distribution = new HashMap<>();
        for (RoomStatus status : RoomStatus.values()) {
            distribution.put(status, 0);
        }

        for (Room room : rooms.values()) {
            RoomStatus status = room.getStatus();
            distribution.put(status, distribution.get(status) + 1);
        }

        return distribution;
    }

    public Map<RoomType, Integer> getRoomTypeDistribution() {
        return new HashMap<>(roomTypeCount);
    }

    public List<Room> getRoomsDueForMaintenance() {
        List<Room> maintenanceRooms = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.needsMaintenance()) {
                maintenanceRooms.add(room);
            }
        }
        return maintenanceRooms;
    }

    public boolean updateRoomPrice(int roomNumber, double newPrice) {
        Room room = rooms.get(roomNumber);
        if (room != null && newPrice > 0) {
            room.setBasePrice(newPrice);
            return true;
        }
        return false;
    }

    public boolean updateRoomStatus(int roomNumber, RoomStatus newStatus) {
        Room room = rooms.get(roomNumber);
        if (room != null) {
            room.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public List<Room> getRoomsNeedingCleaning() {
        return getRoomsByStatus(RoomStatus.DIRTY);
    }

    public List<Room> getRoomsCurrentlyOccupied() {
        return getRoomsByStatus(RoomStatus.OCCUPIED);
    }

    public Room findBestAvailableRoom(RoomType preferredType, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = getAvailableRoomsByType(preferredType, checkIn, checkOut);

        if (availableRooms.isEmpty()) {
            availableRooms = getAvailableRooms(checkIn, checkOut);
        }

        if (availableRooms.isEmpty()) {
            return null;
        }

        return availableRooms.stream()
                .max(Comparator.comparing(Room::hasSeaView)
                        .thenComparing(Room::hasBalcony)
                        .thenComparing(Room::getFloor)
                        .thenComparing(room -> room.getAmenities().size()))
                .orElse(availableRooms.get(0));
    }

    public boolean hasRoomsOfType(RoomType roomType) {
        return roomTypeCount.getOrDefault(roomType, 0) > 0;
    }

    public void printRoomReport() {
        System.out.println("--- Room Management Report ---");
        System.out.println("Total Rooms: " + getAvailableRoomCount());
        System.out.println("Available Rooms: " + getAvailableRoomCount());
        System.out.println("Occupied Rooms: " + getOccupiedRoomCount());
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", getOccupancyRate()));
        System.out.println();

        System.out.println("Room Type Distribution:");
        for (Map.Entry<RoomType, Integer> entry : getRoomTypeDistribution().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        System.out.println("Room Status Distribution:");
        for (Map.Entry<RoomStatus, Integer> entry : getRoomStatusDistribution().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}