public class CheckingAccount extends BankAccount {

    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.5;
    private static final double MINIMUM_BALANCE = 0.0;

    public CheckingAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
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