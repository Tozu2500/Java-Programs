public enum PaymentMethod {

    CASH("Cash", "Payment by cash", true),
    CREDIT_CARD("Credit Card", "Payment with credit card", true),
    DEBIT_CARD("Debit Card", "Payment with debit card", true),
    BANK_TRANSFER("Bank Transfer", "Payment with a bank transfer", false),
    PAYPAL("PayPal", "Payment with PayPal", true),
    DIGITAL_WALLET("Digital Wallet", "Payment via a digital wallet", true),
    CRYPTOCURRENCY("Cryptocurrency", "Payment with cryptocurrency", false),
    CHECK("Check", "Payment by check", false),
    COMPANY_ACCOUNT("Company Account", "Payment charged to a company account", false);

    private final String displayName;
    private final String description;
    private final boolean isInstant;

    PaymentMethod(String displayName, String description, boolean isInstant) {
        this.displayName = displayName;
        this.description = description;
        this.isInstant = isInstant;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInstant() {
        return isInstant;
    }

    public boolean requiresVerification() {
        return this == CREDIT_CARD || this == DEBIT_CARD || this == BANK_TRANSFER;
    }

    public boolean supportsRefunds() {
        return this != CASH && this != CHECK;
    }

    public boolean isDigital() {
        return this == PAYPAL || this == DIGITAL_WALLET || this == CRYPTOCURRENCY;
    }

    public boolean isCard() {
        return this == CREDIT_CARD || this == DEBIT_CARD;
    }

    public double getProcessingFee(double amount) {
        switch (this) {
            case CREDIT_CARD:
                return amount * 0.029 + 0.30;
            case DEBIT_CARD:
                return amount * 0.015 + 0.20;
            case PAYPAL:
                return amount * 0.034 + 0.30;
            case DIGITAL_WALLET:
                return amount * 0.025;
            case CRYPTOCURRENCY:
                return amount * 0.01;
            case BANK_TRANSFER:
                return 2.50;
            default:
                return 0.0;
        }
    }

    public int getProcessingTimeInMinutes() {
        switch (this) {
            case CASH:
                return 0;
            case CREDIT_CARD:
            case DEBIT_CARD:
            case PAYPAL:
            case DIGITAL_WALLET:
                return 5;
            case CRYPTOCURRENCY:
                return 30;
            case BANK_TRANSFER:
                return 1440;
            case CHECK:
                return 4320;
            case COMPANY_ACCOUNT:
                return 720;
            default:
                return 0;
        }
    }

    public static PaymentMethod fromString(String method) {
        for (PaymentMethod paymentMethod : values()) {
            if (paymentMethod.name().equalsIgnoreCase(method) ||
                paymentMethod.displayName.equalsIgnoreCase(method)) {
                return paymentMethod;
            }
        }
        return CASH;
    }

    public static PaymentMethod[] getInstantMethods() {
        return new PaymentMethod[]{CASH, CREDIT_CARD, DEBIT_CARD, PAYPAL, DIGITAL_WALLET};
    }

    public static PaymentMethod[] getDigitalMethods() {
        return new PaymentMethod[]{CREDIT_CARD, DEBIT_CARD, PAYPAL, DIGITAL_WALLET, CRYPTOCURRENCY, BANK_TRANSFER};
    }

    public static PaymentMethod[] getCardMethods() {
        return new PaymentMethod[]{CREDIT_CARD, DEBIT_CARD};
    }

    @Override
    public String toString() {
        return displayName;
    }

}
