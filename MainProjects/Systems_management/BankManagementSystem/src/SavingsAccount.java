public class SavingsAccount extends BankAccount {

    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 3.5;
    private static final double MINIMUM_BALANCE = 100.0;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
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
