
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GuestManager {
    private Map<Integer, Guest> guests;
    private Map<String, Integer> emailToGuestId;
    private Map<String, Integer> phoneToGuestId;
    private Set<Integer> vipGuests;
    private int nextGuestId;
    
    public GuestManager() {
        this.guests = new HashMap<>();
        this.emailToGuestId = new HashMap<>();
        this.phoneToGuestId = new HashMap<>();
        this.vipGuests = new HashSet<>();
        this.nextGuestId = 1;
    }
    
    public Guest createGuest(String firstName, String lastName, String email, String phoneNumber) {
        if (emailToGuestId.containsKey(email.toLowerCase())) {
            return null;
        }
        
        Guest guest = new Guest(nextGuestId++, firstName, lastName, email, phoneNumber);
        return addGuest(guest) ? guest : null;
    }
    
    public Guest createGuest(String firstName, String lastName, String email, String phoneNumber,
                            String address, LocalDate dateOfBirth, String nationality, String idNumber) {
        if (emailToGuestId.containsKey(email.toLowerCase())) {
            return null;
        }
        
        Guest guest = new Guest(nextGuestId++, firstName, lastName, email, phoneNumber,
                               address, dateOfBirth, nationality, idNumber);
        return addGuest(guest) ? guest : null;
    }
    
    public boolean addGuest(Guest guest) {
        if (guest == null || guests.containsKey(guest.getGuestId())) {
            return false;
        }
        
        String email = guest.getEmail().toLowerCase();
        String phone = guest.getPhoneNumber();
        
        if (emailToGuestId.containsKey(email) || phoneToGuestId.containsKey(phone)) {
            return false;
        }
        
        guests.put(guest.getGuestId(), guest);
        emailToGuestId.put(email, guest.getGuestId());
        phoneToGuestId.put(phone, guest.getGuestId());
        
        if (guest.isVip()) {
            vipGuests.add(guest.getGuestId());
        }
        
        return true;
    }

    public boolean removeGuest(int guestId) {
        Guest guest = guests.remove(guestId);
        if (guest != null) {
            emailToGuestId.remove(guest.getEmail().toLowerCase());
            phoneToGuestId.remove(guest.getPhoneNumber());
            vipGuests.remove(guestId);
            return true;
        }
        return false;
    }

    public Guest getGuest(int guestId) {
        return guests.get(guestId);
    }

    public Guest getGuestByEmail(String email) {
        Integer guestId = emailToGuestId.get(email.toLowerCase());
        return guestId != null ? guests.get(guestId) : null;
    }

    public Guest getGuestByPhone(String phoneNumber) {
        Integer guestId = phoneToGuestId.get(phoneNumber);
        return guestId != null ? guests.get(guestId) : null;
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests.values());
    }

    public List<Guest> getVipGuests() {
        return vipGuests.stream()
                .map(guests::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Guest> searchGuests(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllGuests();
        }

        String term = searchTerm.toLowerCase().trim();
        return guests.values().stream()
                .filter(guest ->
                    guest.getFirstName().toLowerCase().contains(term) ||
                    guest.getLastName().toLowerCase().contains(term) ||
                    guest.getFullName().toLowerCase().contains(term) ||
                    (guest.getEmail() != null && guest.getEmail().toLowerCase().contains(term)) ||
                    (guest.getPhoneNumber() != null && guest.getPhoneNumber().contains(term)) ||
                    (guest.getIdNumber() != null && guest.getIdNumber().toLowerCase().contains(term))
                )
                .collect(Collectors.toList());
    }

    public List<Guest> getGuestsByNationality(String nationality) {
        return guests.values().stream()
                .filter(guest -> nationality.equalsIgnoreCase(guest.getNationality()))
                .collect(Collectors.toList());
    }

    public List<Guest> getGuestsByAgeRange(int minAge, int maxAge) {
        return guests.values().stream()
                .filter(guest -> {
                    int age = guest.getAge();
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());
    }

    public List<Guest> getGuestsRegisteredAfter(LocalDate date) {
        return guests.values().stream()
                .filter(guest -> guest.getRegistrationDate().isAfter(date))
                .collect(Collectors.toList());
    }

    public List<Guest> getGuestsWithTotalSpentAbove(double amount) {
        return guests.values().stream()
                .filter(guest -> guest.getTotalSpent() >= amount)
                .collect(Collectors.toList());
    }

    public boolean updateGuestInfo(int guestId, String firstName, String lastName,
                                String email, String phoneNumber, String address) {
        Guest guest = guests.get(guestId);
        if (guest == null) {
            return false;
        }

        String oldEmail = guest.getEmail().toLowerCase();
        String oldPhone = guest.getPhoneNumber();

        if (!email.equalsIgnoreCase(guest.getEmail()) && emailToGuestId.containsKey(email.toLowerCase())) {
            return false;
        }

        if (!phoneNumber.equals(guest.getPhoneNumber()) && phoneToGuestId.containsKey(phoneNumber)) {
            return false;
        }

        emailToGuestId.remove(oldEmail);
        phoneToGuestId.remove(oldPhone);

        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPhoneNumber(phoneNumber);
        guest.setAddress(address);

        emailToGuestId.put(email.toLowerCase(), guestId);
        phoneToGuestId.put(phoneNumber, guestId);

        return true;
    }

    public boolean updateGuestPersonalInfo(int guestId, LocalDate dateOfBirth,
                                        String nationality, String idNumber) {
        Guest guest = guests.get(guestId);
        if (guest == null) {
            return false;
        }

        guest.setDateOfBirth(dateOfBirth);
        guest.setNationality(nationality);
        guest.setIdNumber(idNumber);

        return true;
    }

    public boolean promoteToVip(int guestId) {
        Guest guest = guests.get(guestId);
        if (guest != null && !guest.isVip()) {
            guest.promoteToVip();
            vipGuests.add(guestId);
            return true;
        }
        return false;
    }

    public boolean demoteFromVip(int guestId) {
        Guest guest = guests.get(guestId);
        if (guest != null && guest.isVip()) {
            guest.demoteFromVip();
            vipGuests.remove(guestId);
            return true;
        }
        return false;
    }

    public void addSpending(int guestId, double amount) {
        Guest guest = guests.get(guestId);
        if (guest != null && amount > 0) {
            guest.addToTotalSpent(amount);
            if (guest.isVip() && !vipGuests.contains(guestId)) {
                vipGuests.add(guestId);
            }
        }
    }

    public boolean validateGuestData(Guest guest) {
        if (guest == null) return false;

        if (guest.getFirstName() == null || guest.getFirstName().trim().isEmpty()) {
            return false;
        }

        if (guest.getLastName() == null || guest.getLastName().trim().isEmpty()) {
            return false;
        }

        if (!guest.isValidEmail()) {
            return false;
        }

        if (!guest.isValidPhone()) {
            return false;
        }

        return true;
    }

    public List<Guest> getGuestsWithIncompleteInfo() {
        return guests.values().stream()
                .filter(guest ->
                    guest.getAddress() == null || guest.getAddress().trim().isEmpty() ||
                    guest.getDateOfBirth() == null ||
                    guest.getNationality() == null || guest.getNationality().trim().isEmpty() ||
                    guest.getIdNumber() == null || guest.getIdNumber().trim().isEmpty()
                )
                .collect(Collectors.toList());
    }

    public int getTotalGuestCount() {
        return guests.size();
    }

    public int getVipGuestCount() {
        return vipGuests.size();
    }

    public double getTotalGuestSpending() {
        return guests.values().stream()
                .mapToDouble(Guest::getTotalSpent)
                .sum();
    }

    public double getAverageGuestSpending() {
        if (guests.isEmpty()) return 0.0;

        return getTotalGuestSpending() / guests.size();
    }

    public Guest getTopSpendingGuest() {
        return guests.values().stream()
                .max(Comparator.comparingDouble(Guest::getTotalSpent))
                .orElse(null);
    }

    public List<Guest> getTopSpendingGuests(int count) {
        return guests.values().stream()
                .sorted((g1, g2) -> Double.compare(g2.getTotalSpent(), g1.getTotalSpent()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getNationalityDistribution() {
        Map<String, Integer> distribution = new HashMap<>();

        for (Guest guest : guests.values()) {
            String nationality = guest.getNationality();
            if (nationality != null && !nationality.trim().isEmpty()) {
                distribution.put(nationality, distribution.getOrDefault(nationality, 0) + 1);
            }
        }
        
        return distribution;
    }

    public Map<Integer, Integer> getAgeDistribution() {
        Map<Integer, Integer> distribution = new HashMap<>();

        for (Guest guest : guests.values()) {
            int ageGroup = (guest.getAge() / 10) * 10;
            distribution.put(ageGroup, distribution.getOrDefault(ageGroup, 0) + 1);
        }

        return distribution;
    }

    public List<Guest> getRecentGuests(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return guests.values().stream()
                .filter(guest -> guest.getRegistrationDate().isAfter(cutoffDate))
                .collect(Collectors.toList());
    }

    public List<Guest> getInactiveGuests(int monthsThreshold) {
        LocalDate cutoffDate = LocalDate.now().minusMonths(monthsThreshold);
        return guests.values().stream()
                .filter(guest -> guest.getRegistrationDate().isBefore(cutoffDate) && guest.getTotalSpent() == 0.0)
                .collect(Collectors.toList());
    }

    public boolean mergeGuests(int primaryGuestId, int secondaryGuestId) {
        Guest primaryGuest = guests.get(primaryGuestId);
        Guest secondaryGuest = guests.get(secondaryGuestId);

        if (primaryGuest == null || secondaryGuest == null || primaryGuestId == secondaryGuestId) {
            return false;
        }

        primaryGuest.addToTotalSpent(secondaryGuest.getTotalSpent());

        if (secondaryGuest.isVip() && !primaryGuest.isVip()) {
            primaryGuest.promoteToVip();
            vipGuests.add(primaryGuestId);
        }

        if (primaryGuest.getAddress() == null && secondaryGuest.getAddress() != null) {
            primaryGuest.setAddress(secondaryGuest.getAddress());
        }

        if (primaryGuest.getDateOfBirth() == null && secondaryGuest.getDateOfBirth() != null) {
            primaryGuest.setDateOfBirth(secondaryGuest.getDateOfBirth());
        }

        if (primaryGuest.getNationality() == null && secondaryGuest.getNationality() != null) {
            primaryGuest.setNationality(secondaryGuest.getNationality());
        }

        if (primaryGuest.getIdNumber() == null && secondaryGuest.getIdNumber() != null) {
            primaryGuest.setIdNumber(secondaryGuest.getIdNumber());
        }

        removeGuest(secondaryGuestId);
        return true;
    }

    public List<Guest> findPotentialDuplicates() {
        List<Guest> potentialDuplicates = new ArrayList<>();
        List<Guest> allGuests = getAllGuests();

        for (int i = 0; i < allGuests.size(); i++) {
            for (int j = i + 1; j < allGuests.size(); j++) {
                Guest guest1 = allGuests.get(i);
                Guest guest2 = allGuests.get(j);

                if (isPotentialDuplicate(guest1, guest2)) {
                    if (!potentialDuplicates.contains(guest1)) {
                        potentialDuplicates.add(guest1);
                    }
                    if (!potentialDuplicates.contains(guest2)) {
                        potentialDuplicates.add(guest2);
                    }
                }
            }
        }

        return potentialDuplicates;
    }

    private boolean isPotentialDuplicate(Guest guest1, Guest guest2) {
        if (guest1.getFirstName().equalsIgnoreCase(guest2.getFirstName()) &&
            guest1.getLastName().equalsIgnoreCase(guest2.getLastName())) {
            return true;
        }

        if (guest1.getEmail().equalsIgnoreCase(guest2.getEmail())) {
            return true;
        }

        if (guest1.getPhoneNumber().equals(guest2.getPhoneNumber())) {
            return true;
        }

        if (guest1.getIdNumber() != null && guest2.getIdNumber() != null &&
            guest1.getIdNumber().equalsIgnoreCase(guest2.getIdNumber())) {
            return true;
        }

        return false;
    }

    public void cleanupInactiveGuests(int monthsThreshold) {
        List<Guest> inactiveGuests = getInactiveGuests(monthsThreshold);
        for (Guest guest : inactiveGuests) {
            removeGuest(guest.getGuestId());
        }
    }

    public double getVipSpendingPercentage() {
        if (guests.isEmpty()) return 0.0;

        double totalSpending = getTotalGuestSpending();
        if (totalSpending == 0.0) return 0.0;

        double vipSpending = vipGuests.stream()
                .mapToDouble(id -> {
                    Guest guest = guests.get(id);
                    return guest != null ? guest.getTotalSpent() : 0.0;
                })
                .sum();

        return (vipSpending / totalSpending) * 100;
    }

    public List<Guest> getGuestsRequiringAttention() {
        List<Guest> requiresAttention = new ArrayList<>();

        requiresAttention.addAll(getGuestsWithIncompleteInfo());

        requiresAttention.addAll(guests.values().stream()
                .filter(guest -> !guest.isValidEmail() || !guest.isValidPhone())
                .collect(Collectors.toList()));

        return requiresAttention.stream().distinct().collect(Collectors.toList());
    }

    public boolean exportGuestData(int guestId) {
        Guest guest = guests.get(guestId);
        if (guest == null) {
            return false;
        }

        System.out.println("--- Guest Data Export---");
        System.out.println("Guest ID: " + guest.getGuestId());
        System.out.println("Name: " + guest.getFullName());
        System.out.println("Email: " + guest.getEmail());
        System.out.println("Phone: " + guest.getPhoneNumber());
        System.out.println("Address: " + (guest.getAddress() != null ? guest.getAddress() : "Not provided"));
        System.out.println("Date of Birth: " + (guest.getDateOfBirth() != null ? guest.getDateOfBirth() : "Not provided"));
        System.out.println("Nationality: " + (guest.getNationality() != null ? guest.getNationality() : "Not provided"));
        System.out.println("ID Number: " + (guest.getIdNumber() != null ? guest.getIdNumber() : "Not provided"));
        System.out.println("Registration Date: " + guest.getRegistrationDate());
        System.out.println("VIP Status: " + (guest.isVip() ? "Yes" : "No"));
        System.out.println("Total Spent: $" + String.format("%.2f", guest.getTotalSpent()));
        System.out.println("Age: " + guest.getAge());

        return true;
    }

    public void printGuestReport() {
        System.out.println("--- Guest Management Report ---");
        System.out.println("Total Guests: " + getTotalGuestCount());
        System.out.println("VIP Guests: " + getVipGuestCount() + " (" + String.format("%.1f%%", (double)getVipGuestCount()/getTotalGuestCount()*100) + ")");
        System.out.println("Total Guest Spending: $" + String.format("%.2f", getTotalGuestSpending()));
        System.out.println("Average Guest Spending: $" + String.format("%.2f", getAverageGuestSpending()));
        System.out.println("VIP Spending Percentage: " + String.format("%.1f%%", getVipSpendingPercentage()));
        System.out.println();

        Guest topSpender = getTopSpendingGuest();
        if (topSpender != null) {
            System.out.println("Top Spending Guest: " + topSpender.getFullName() + " ($" + String.format("%.2f", topSpender.getTotalSpent()) + ")");
        }

        System.out.println("Recent Guests (30 days): " + getRecentGuests(30).size());
        System.out.println("Guests with Incomplete Info: " + getGuestsWithIncompleteInfo().size());
        System.out.println("Potential Duplicate Guests: " + findPotentialDuplicates().size());

        System.out.println("\nNationality Distribution:");
        Map<String, Integer> nationalityDist = getNationalityDistribution();
        nationalityDist.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

}
