import java.io.IOException;
import java.util.Scanner;

public class ATM {
    
    private final Scanner in;
    private BankAccount activeAccount;
    private Bank bank;

    ////////////////////////////////////////////////////////////////////////////
    // //
    // Refer to the Simple ATM tutorial to fill in the details of this class. //
    // You'll need to implement the new features yourself. //
    // //
    ////////////////////////////////////////////////////////////////////////////
    public static final int VIEW = 1;
    public static final int DEPOSIT = 2;
    public static final int WITHDRAW = 3;
    public static final int TRANSFER = 4;
    public static final int LOGOUT = 5;
    public static final int FIRST_NAME_WIDTH = 20;
    public static final int LAST_NAME_WIDTH = 30;
    
    public static final int INVALID = 0;
    public static final int INSUFFICIENT = 1;
    public static final int SUCCESS = 2;
    public static final int OVERFILL = 3;
    

    /**
     * Constructs a new instance of the ATM class.
     */
    public ATM() {
        in = new Scanner(System.in);
        activeAccount = new BankAccount(1234, 123456789, 0, new User("Ryan", "Wilson"));
        try {
            this.bank = new Bank();
        } catch (IOException e) {
            // cleanup any resources (i.e., the Scanner) and exit
        }
    }

    public void startup() {
        long accountNo;
        int pin;
        
        System.out.println("Welcome to the AIT ATM!");

        while (true) {
            System.out.print("Account no.: ");
            String accountNoStr = in.nextLine();
            
            if(accountNoStr.isEmpty()){
                accountNo = 0;
                pin = getPin();
                login(accountNo, pin);
            }else if(accountNoStr.equals("+")) {
                accountNo = 0;
                createAccount();
                continue;
            }else if(accountNoStr.matches("[0-9]+")) {
                accountNo = Long.parseLong(accountNoStr);
                pin = getPin();
                login(accountNo, pin);
            }else if(accountNoStr.equals("-1")){
                accountNo = -1;
                pin = getPin();
                login(accountNo, pin);
            }else{
                accountNo = 0;
                pin = getPin();
                login(accountNo, pin);
            }          
    }
}
    /*
     * Application execution begins here.
     */
    public void login(long accountNo, int pin){
        if (isValidLogin(accountNo, pin)) {
            activeAccount = bank.login(accountNo, pin);
            System.out.println("\nHello, again " + activeAccount.getAccountHolder().getFirstName() + "!\n");

            boolean validLogin = true;
            while (validLogin) {
                switch (getSelection()) {
                case VIEW: showBalance(); break;
                case DEPOSIT: deposit(); break;
                case WITHDRAW: withdraw(); break;
                // case TRANSFER:; break (need to make a transfer method)
                case LOGOUT: validLogin = false; in.nextLine(); break;
                default:
                    System.out.println("\nInvalid selection.\n");
                    break;
                }

            }
        } else {
            if (accountNo == -1 && pin == -1) {
                shutdown();
            } else {
                System.out.println("\nInvalid account number and/or PIN.\n");
            }
        }
    }
    

    public int getPin(){
        int pin = 0;
        System.out.print("PIN       : ");
        String pinStr = in.nextLine();
        if(pinStr.isEmpty()){
            pin = 0;
        }else if(pinStr.equals("\\d")){
            pin = Integer.valueOf(pinStr);
        }else if(pinStr.equals("-1")){
            pin = -1;
        }
        return pin;
    }

    public boolean isValidLogin(final long accountNo, final int pin) {
        return accountNo == activeAccount.getAccountNo() && pin == activeAccount.getPin();
    }

    public int getSelection() {
        System.out.println("[1] View balance");
        System.out.println("[2] Deposit money");
        System.out.println("[3] Withdraw money");
        System.out.println("[4] Transfer");
        System.out.println("[5] Logout");

        if(in.hasNextInt()){
            return in.nextInt();
        }else{
            in.nextLine();
            return 6;
        }
        
    }

    public void showBalance() {
        System.out.println("\nCurrent balance: " + activeAccount.getBalance());
    }

    public void deposit() {
        double amount = 0;
        boolean validAmt = true;
        System.out.print("\nEnter amount: ");
        try {
            amount = in.nextDouble();
        } catch (Exception e) {
            in.nextLine();
        }

        if (validAmt) {
            int status = activeAccount.deposit(amount);
            if(status == ATM.INVALID){
                System.out.println("\nInvalid Deposit. Please enter a new amount.\n");
            }else if(status == ATM.OVERFILL){
                System.out.println("Invalid Deposit. Please enter a new amount.\n ");
            }else if(status == ATM.SUCCESS){
                System.out.println("\nAmount deposited.\n");
                bank.update(activeAccount);
                bank.save();
            }else{
                System.out.println("Invalid Deposit. Please enter a new amount.\n ");
            }
        }
    }

    public void withdraw() {
        double amount = 0;
        boolean validAmt = true;
        System.out.print("\nEnter amount: ");
        
        try{
            amount = in.nextDouble();
        }catch(Exception e){
            validAmt = false;
            in.nextLine();
        }
        if(validAmt){
            int status = activeAccount.withdraw(amount);
            if(status == ATM.INVALID){
                System.out.print("\nInvalid withdraw. PLease enter a new amount.\n");
            }else if(status == ATM.INSUFFICIENT){
                System.out.print("\nInvalid withdraw. PLease enter a new amount.\n");
            }else if(status == ATM.SUCCESS){
                System.out.println("Amount withdrawed.");
                bank.update(activeAccount);
                bank.save();
            }else{
                System.out.println("\nInvalid Withdraw. Please enter a new amount.\n");
            }
        }
    }

    public void shutdown() {
        if (in != null) {
            in.close();
        }

        System.out.println("\nGoodbye");
        System.exit(0);
    }

    public void createAccount() {
        System.out.print("\nFirst Name: ");
        String firstName = in.next();
        while (firstName.length() < 1 || firstName.length() > FIRST_NAME_WIDTH) {
            System.out.print("Invalid name, try again: ");
            firstName = in.next();
        }

        System.out.print("\nLast Name: ");
        String lastName = in.next();
        while (lastName.length() < 1 || lastName.length() > LAST_NAME_WIDTH) {
            System.out.print("Invalid name, try again:.");
            lastName = in.next();
        }

        System.out.print("PIN: ");
        int pin = in.nextInt();
        while (pin < 1000 || pin > 9999) {
            System.out.print("Invalid PIN, try again: ");
            pin = in.nextInt();
        }

        BankAccount newAccount = bank.createAccount(pin, new User(firstName, lastName));
        bank.update(newAccount);
        bank.save();

        System.out.print("\nThank you. Your account number is " + activeAccount.getAccountNo() + ".");
        System.out.print("Please login to acces your newly created account. \n");

    }

 

    public static void main(final String[] args) {
        ATM atm = new ATM();
        atm.startup();

    }
}
