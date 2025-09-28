
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {

    private String name;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private int starRating;
    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private List<String> amenities;
    private List<String> services;
    private Map<String, String> policies;
    private boolean hasPool;
    private boolean hasGym;
    private boolean hasSpa;
    private boolean hasRestaurant;
    private boolean hasBar;
    private boolean hasBusinessCenter;
    private boolean hasMeetingRooms;
    private boolean hasAirportShuttle;
    private boolean hasPetPolicy;
    private boolean hasWifi;
    private boolean hasParking;
    private int totalFloors;
    private int totalRooms;
    private String managerId;
    private double latitude;
    private double longitude;

    public Hotel() {
        this.amenities = new ArrayList<>();
        this.services = new ArrayList<>();
        this.policies = new HashMap<>();
        this.checkInTime = LocalTime.of(15, 0);
        this.checkOutTime = LocalTime.of(11, 0);
        this.hasWifi = true;
        initializeDefaultPolicies();
    }

    public Hotel(String name, String address, String city, String country) {
        this();
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    public Hotel(String name, String address, String city, String country,
                int starRating, String phoneNumber, String email) {
        this(name, address, city, country);
        this.starRating = starRating;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    private void initializeDefaultPolicies() {
        policies.put("Cancellation", "Free cancellation up to 24 hours before check-in");
        policies.put("Children", "Children of all ages are welcome");
        policies.put("Pets", "Pets are not allowed");
        policies.put("Smoking", "Non-smoking property");
        policies.put("Payment", "All major credit cards accepted");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        if (starRating >= 1 && starRating <= 5) {
            this.starRating = starRating;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
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

    public List<String> getServices() {
        return new ArrayList<>(services);
    }

    public void setServices(List<String> services) {
        this.services = new ArrayList<>(services);
    }

    public void addService(String service) {
        if (!services.contains(service)) {
            services.add(service);
        }
    }

    public void removeService(String service) {
        services.remove(service);
    }

    public Map<String, String> getPolicies() {
        return new HashMap<>(policies);
    }

    public void setPolicies(Map<String, String> policies) {
        this.policies = new HashMap<>(policies);
    }

    public void addPolicy(String policyType, String policyText) {
        policies.put(policyType, policyText);
    }

    public void removePolicy(String policyType) {
        policies.remove(policyType);
    }

    public String getPolicy(String policyType) {
        return policies.get(policyType);
    }

    public boolean hasPool() {
        return hasPool;
    }

    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }

    public boolean hasGym() {
        return hasGym;
    }

    public void setHasGym(boolean hasGym) {
        this.hasGym = hasGym;
    }

    public boolean hasSpa() {
        return hasSpa;
    }

    public void setHasSpa(boolean hasSpa) {
        this.hasSpa = hasSpa;
    }

    public boolean hasRestaurant() {
        return hasRestaurant;
    }

    public void setHasRestaurant(boolean hasRestaurant) {
        this.hasRestaurant = hasRestaurant;
    }

    public boolean hasBar() {
        return hasBar;
    }

    public void setHasBar(boolean hasBar) {
        this.hasBar = hasBar;
    }

    public boolean hasBusinessCenter() {
        return hasBusinessCenter;
    }

    public void setHasBusinessCenter(boolean hasBusinessCenter) {
        this.hasBusinessCenter = hasBusinessCenter;
    }

    public boolean hasMeetingRooms() {
        return hasMeetingRooms;
    }

    public void setHasMeetingRooms(boolean hasMeetingRooms) {
        this.hasMeetingRooms = hasMeetingRooms;
    }

    public boolean hasAirportShuttle() {
        return hasAirportShuttle;
    }

    public void setHasAirportShuttle(boolean hasAirportShuttle) {
        this.hasAirportShuttle = hasAirportShuttle;
    }

    public boolean hasPetPolicy() {
        return hasPetPolicy;
    }

    public void setHasPetPolicy(boolean hasPetPolicy) {
        this.hasPetPolicy = hasPetPolicy;
    }

    public boolean hasWifi() {
        return hasWifi;
    }

    public void setHasWifi(boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public boolean hasParking() {
        return hasParking;
    }

    public void setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null) sb.append(address);
        if (city != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }

        if (postalCode != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(postalCode);
        }

        if (country != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(country);
        }
        return sb.toString();
    }

    public String getStarRatingDisplay() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < starRating; i++) {
            stars.append("★");
        }

        for (int i = starRating; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }

    public boolean isLuxury() {
        return starRating >= 4;
    }

    public boolean isBudget() {
        return starRating <= 2;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", starRating=" + starRating +
                ", totalRooms=" + totalRooms +
                '}';
    }

}