public class BusinessAccount extends BankAccount {

    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 2.0;
    private static final double MINIMUM_BALANCE = 500.0;

    public BusinessAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Business Account";
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}