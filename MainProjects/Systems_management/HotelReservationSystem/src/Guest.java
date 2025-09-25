
import java.time.LocalDate;
import java.util.Objects;

public class Guest {

    private int guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String nationality;
    private String idNumber;
    private LocalDate registrationDate;
    private boolean isVip;
    private double totalSpent;

    public Guest() {
        this.registrationDate = LocalDate.now();
        this.isVip = false;
        this.totalSpent = 0.0;
    }

    public Guest(int guestId, String firstName, String lastName, String email, String phoneNumber) {
        this();
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Guest(int guestId, String firstName, String lastName, String email, String phoneNumber,
                String address, LocalDate dateOfBirth, String nationality, String idNumber) {
        this(guestId, firstName, lastName, email, phoneNumber);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.idNumber = idNumber;
    }

    public int guestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void addToTotalSpent(double amount) {
        this.totalSpent += amount;
        if (this.totalSpent >= 5000) {
            this.isVip = true;
        }
    }

    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean isValidPhone() {
        return phoneNumber != null && phoneNumber.matches("\\+?[0-9\\s\\-()]+");
    }

    public void promoteToVip() {
        this.isVip = true;
    }

    public void demoteFromVip() {
        this.isVip = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Guest guest = (Guest) obj;
        return guestId == guest.guestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", isVip=" + isVip +
                ", totalSpent=" + totalSpent +
                '}';
    }
}