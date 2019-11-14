public class BankAccount {
        
    private int pin;
    private long accountNo;
    private double balance;
    private User accountHolder;
    
    ////////////////////////////////////////////////////////////////////////////
    //                                                                        //
    // Refer to the Simple ATM tutorial to fill in the details of this class. //
    //                                                                        //
    ////////////////////////////////////////////////////////////////////////////
    public BankAccount(int pin, long accountNo, double balance, User accountHolder){
        this.pin = pin; 
        this.accountNo = accountNo;
        this.balance = balance;
        this.accountHolder = accountHolder; 
    }

    public int getPin(){
        return pin;
    }

    public long getAccountNo(){
        return accountNo;
    }

    public double getBalance(){
        return balance;
    }

    public User getAccountHolder(){
        return accountHolder;
    }

    public void deposit(double amount){
        balance = balance + amount;
    }

    public void withdraw(double amount){
        balance = balance - amount;
    }
    /*
     * Formats the account balance in preparation to be written to the data file.
     * 
     * @return a fixed-width string in line with the data file specifications.
     */
    
    private String formatBalance() {
        return String.format("%1$15s", balance);
    }
    
    /*
     * Converts this BankAccount object to a string of text in preparation to
     * be written to the data file.
     * 
     * @return a string of text formatted for the data file
     */
    
    @Override
    public String toString() {
        return String.valueOf(accountNo) +
            String.valueOf(pin) +
            accountHolder.serialize() +
            formatBalance();
    }
}
