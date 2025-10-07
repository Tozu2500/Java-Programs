import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room {

    private int roomNumber;
    private RoomType roomType;
    private RoomStatus status;
    private double basePrice;
    private int floor;
    private boolean hasBalcony;
    private boolean hasSeaView;
    private boolean hasMountainView;
    private boolean isAccessible;
    private boolean isSmokingAllowed;
    private int maxOccupancy;
    private List<String> amenities;
    private String description;
    private double area;
    private int numberOfBeds;
    private String bedType;
    private boolean hasKitchenette;
    private boolean hasMinibar;
    private String lastCleanedBy;
    private boolean needsMaintenance;

    public Room() {
        this.amenities = new ArrayList<>();
        this.status = RoomStatus.AVAILABLE;
        this.needsMaintenance = false;
    }

    public Room(int roomNumber, RoomType roomType, double basePrice) {
        this();
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.floor = roomNumber / 100;
        setDefaultsBasedOnType();
    }

    public Room(int roomNumber, RoomType roomType, double basePrice, int floor,
                boolean hasBalcony, boolean hasSeaView, boolean hasAccessible) {
        this(roomNumber, roomType, basePrice);
        this.floor = floor;
        this.hasBalcony = hasBalcony;
        this.hasSeaView = hasSeaView;
        this.isAccessible = hasAccessible;
    }

    private void setDefaultsBasedOnType() {
        switch (roomType) {
            case SINGLE:
                this.maxOccupancy = 1;
                this.numberOfBeds = 1;
                this.bedType = "Single";
                this.area = 15.0;
                break;
            case DOUBLE:
                this.maxOccupancy = 2;
                this.numberOfBeds = 1;
                this.bedType = "Double";
                this.area = 20.0;
                break;
            case TWIN:
                this.maxOccupancy = 2;
                this.numberOfBeds = 2;
                this.bedType = "Twin";
                this.area = 22.0;
                break;
            case SUITE:
                this.maxOccupancy = 4;
                this.numberOfBeds = 2;
                this.bedType = "King + Sofa Bed";
                this.area = 45.0;
                this.hasKitchenette = true;
                break;
            case DELUXE:
                this.maxOccupancy = 3;
                this.numberOfBeds = 1;
                this.bedType = "King";
                this.area = 35.0;
                break;
            case PRESIDENTIAL:
                this.maxOccupancy = 6;
                this.numberOfBeds = 3;
                this.bedType = "Multiple";
                this.area = 80.0;
                this.hasKitchenette = true;
                this.hasBalcony = true;
                break;
        }
        this.hasMinibar = roomType != RoomType.SINGLE;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumer(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
        setDefaultsBasedOnType();
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getActualPrice() {
        double price = basePrice;
        if (hasSeaView) price *= 1.3;
        if (hasMountainView) price *= 1.2;
        if (hasBalcony) price *= 1.15;
        if (floor >= 10) price *= 1.1;
        return price;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean hasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public boolean hasSeaView() {
        return hasSeaView;
    }

    public void setHasSeaView(boolean hasSeaView) {
        this.hasSeaView = hasSeaView;
    }

    public boolean hasMountainView() {
        return hasMountainView;
    }

    public void setHasMountainView(boolean hasMountainView) {
        this.hasMountainView = hasMountainView;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public boolean isSmokingAllowed() {
        return isSmokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        isSmokingAllowed = smokingAllowed;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public List<String> getAmenities() {
        return new ArrayList<>(amenities);
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = new ArrayList<>(amenities);
    }

    public void addAmenity(String amenity) {
        if (!amenities.contains(amenity)) {
            amenities.add(amenity);
        }
    }

    public void removeAmenity(String amenity) {
        amenities.remove(amenity);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public boolean hasKitchenette() {
        return hasKitchenette;
    }

    public void setHasKitchenette(boolean hasKitchenette) {
        this.hasKitchenette = hasKitchenette;
    }

    public boolean hasMinibar() {
        return hasMinibar;
    }

    public void setHasMinibar(boolean hasMinibar) {
        this.hasMinibar = hasMinibar;
    }

    public String getLastCleanedBy() {
        return lastCleanedBy;
    }

    public void setLastCleanedBy(String lastCleanedBy) {
        this.lastCleanedBy = lastCleanedBy;
    }

    public boolean needsMaintenance() {
        return needsMaintenance;
    }

    public void setNeedsMaintenance(boolean needsMaintenance) {
        this.needsMaintenance = needsMaintenance;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public void checkIn() {
        this.status = RoomStatus.OCCUPIED;
    }

    public void checkOut() {
        this.status = RoomStatus.DIRTY;
    }

    public void clean() {
        if (status == RoomStatus.DIRTY) {
            this.status = RoomStatus.AVAILABLE;
        }
    }

    public void reserve() {
        if (status == RoomStatus.AVAILABLE) {
            this.status = RoomStatus.RESERVED;
        }
    }

    public void makeUnavailable() {
        this.status = RoomStatus.OUT_OF_ORDER;
    }

    public void makeAvailable() {
        if (status == RoomStatus.OUT_OF_ORDER && !needsMaintenance) {
            this.status = RoomStatus.AVAILABLE;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", status=" + status +
                ", price=" + getActualPrice() +
                ", floor=" + floor +
                ", maxOccupancy=" + maxOccupancy +
                '}';
    }

}