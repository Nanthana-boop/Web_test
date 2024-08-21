import java.util.ArrayList;
import java.util.Scanner;

class Person {
    String idNumber;
    String name;
    String gender;

    public Person(String idNumber, String name, String gender) {
        this.idNumber = idNumber;
        this.name = name;
        this.gender = gender;
    }
}

class Account extends Person {
    private String accountId;
    private String accountPassword;
    private double balance;

    public Account(String idNumber, String name, String gender, String accountId, String accountPassword,
            double balance) {
        super(idNumber, name, gender);

        this.accountId = accountId;

        if (!accountPassword.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        this.accountPassword = accountPassword;

        if (balance > 1000000) {
            throw new IllegalArgumentException("Balance cannot exceed 1,000,000.");
        }
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public double getBalance() {
        return balance;
    }

    public double getBalanceBTC(double rate) {
        return (balance / rate);
    }

    public void setBalanceBTC(double balanceBTC, double rate) {
        if (balance > 1000000) {
            throw new IllegalArgumentException("Balance cannot exceed 1,000,000.");
        }
        this.balance = (balanceBTC * rate);
    }

    public void setBalance(double balance) {
        if (balance > 1000000) {
            throw new IllegalArgumentException("Balance cannot exceed 1,000,000.");
        }
        this.balance = balance;
    }
}

class Manager extends Person {
    private String managerId;
    private String managerPassword;

    public Manager(String idNumber, String name, String gender, String managerId, String managerPassword) {
        super(idNumber, name, gender);
        this.managerId = managerId;
        this.managerPassword = managerPassword;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getManagerPassword() {
        return managerPassword;
    }
}

interface ATMAction {
    void checkBalanceInBTC();

    void withdraw(double amount);

    void deposit(double amount);

    void transfer(Account toAccount, double amount);
}

interface ATMActionExtended extends ATMAction {
    void additionalFunction();
}

class ATM implements ATMActionExtended {
    private ArrayList<Account> bankAccounts;
    private Account currentAccount;
    private double btcRate;

    public ATM(ArrayList<Account> bankAccounts) {
        this.bankAccounts = bankAccounts;
        this.currentAccount = null;
        this.btcRate = 0;
    }

    public void setBtcRate(double btcRate) {
        this.btcRate = btcRate;
    }

    public double getBtcRate() {
        return btcRate;
    }

    public boolean login(String accountId, String accountPassword) {
        for (Account account : bankAccounts) {
            if (account.getAccountId().equals(accountId) && account.getAccountPassword().equals(accountPassword)) {
                this.currentAccount = account;
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkBalanceInBTC() {
        double balanceInBTC = currentAccount.getBalanceBTC(btcRate);
        System.out.println("Balance in BTC: " + balanceInBTC);
    }

    public void checkBalanceInTHB() {
        System.out.println("Balance in THB: " + currentAccount.getBalance());
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= currentAccount.getBalance()) {
            currentAccount.setBalance(currentAccount.getBalance() - amount);
            System.out.println("Withdrawal successful. New balance in BTC: " + currentAccount.getBalance());
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance.");
        }
    }

    public void withdrawBTC(double amountBTC) {
        if (amountBTC > 0 && amountBTC <= currentAccount.getBalanceBTC(btcRate)) {
            currentAccount.setBalanceBTC(currentAccount.getBalanceBTC(btcRate) - amountBTC, btcRate);
            System.out.println("Withdrawal successful. New balance in BTC: " + currentAccount.getBalance() / btcRate);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient balance.");
        }
    }

    @Override
    public void deposit(double amount) {
        currentAccount.setBalance(currentAccount.getBalance() + amount);
        System.out.println("Deposit successful. New balance: " + currentAccount.getBalance());
    }

    @Override
    public void transfer(Account toAccount, double amount) {
        if (amount > 0 && amount <= currentAccount.getBalance()) {
            currentAccount.setBalance(currentAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            System.out.println("Transfer successful. New balance: " + currentAccount.getBalance());
        } else {
            System.out.println("Invalid transfer amount or insufficient balance.");
        }
    }

    @Override
    public void additionalFunction() {
        // Additional functionality for ATM
        System.out.println("Additional ATM functionality.");
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }
}

public class Bank_account_thanyaburi {
    private static ArrayList<Account> bankAccounts;
    private static Manager manager;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize bank accounts and manager
        bankAccounts = new ArrayList<>();
        manager = null;

        // Check if a manager is already set up
        boolean managerSetUp = false;

        while (true) {
            if (!managerSetUp) {
                System.out.println("No manager set up. Please create a manager account.");

                // Step 1: Enter manager details
                System.out.println("\nManager Account Set Up");
                System.out.print("Manager ID : ");
                String managerId = scanner.next();

                // Consume the newline character
                scanner.nextLine();

                System.out.print("Manager Name (up to 50 characters): ");
                String managerName = scanner.nextLine();

                System.out.print("Enter Manager Gender: ");
                String managerGender = scanner.next();

                System.out.print("Enter Manager Password (4 digits): ");
                String managerPassword = scanner.next();

                try {
                    manager = new Manager(managerId, managerName, managerGender, managerId, managerPassword);
                    managerSetUp = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage() + " Please try again.");
                }
            }

            // Manager login
            System.out.print("\nManager Login ");
            System.out.print("\nManager ID: ");
            String managerIdAttempt = scanner.next();
            System.out.print("Manager Password: ");
            String managerPasswordAttempt = scanner.next();

            if (manager.getManagerId().equals(managerIdAttempt)
                    && manager.getManagerPassword().equals(managerPasswordAttempt)) {
                // Manager logged in, proceed to ATM functions
                break;
            } else {
                System.out.println("Invalid Manager ID or Password. Please try again.");
            }
        }

        // Step 2: Enter bank account details
        System.out.print("Step 2. Enter amount of all accounts: ");
        int numAccounts = readInt(scanner);

        for (int i = 0; i < numAccounts; i++) {
            System.out.println("\nAccount " + (i + 1));
            System.out.print("Account ID (exactly 13 digits): ");
            String accountId = scanner.next();

            // Consume the newline character
            scanner.nextLine();

            System.out.print("Account Name (up to 50 characters): ");
            String accountName = scanner.nextLine();

            System.out.print("Enter User Gender: ");
            String accountGender = scanner.next();

            System.out.print("Enter PIN (4 digits): ");
            String password = scanner.next();

            // Separate prompts for PIN and balance
            System.out.print("Balance (not exceeding 1,000,000): ");
            double balance = readDouble(scanner);

            try {
                bankAccounts.add(new Account(accountId, accountName, accountGender, accountId, password, balance));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
                i--; // Retry for the same account
            }
        }

        // Step 3: Initialize ATM
        ATM atm = new ATM(bankAccounts);

        // Step 4: Set up BTC rate
        System.out.print("Step 4. Enter BTC exchange rate: ");
        double btcRate = readDouble(scanner);
        atm.setBtcRate(btcRate);

        // Step 5: Manager Menu
        while (true) {
            System.out.println("\nManager Menu");
            System.out.println("1. Check BTC Exchange Rate");
            System.out.println("2. Logout");
            System.out.print("Choose: ");
            int managerChoice = readInt(scanner);

            if (managerChoice == 1) {
                System.out.println("BTC Exchange Rate: " + atm.getBtcRate());
            } else if (managerChoice == 2) {
                System.out.println("Manager Logout.");
                break;
            } else {
                System.out.println("Invalid choice. Please choose again.");
            }
        }

        // Step 6: ATM Login
        while (true) {
            System.out.print("\nStep 6 (ATM Login). Enter Account ID: ");
            String accountId = scanner.next();
            System.out.print("Enter PIN: ");
            String password = scanner.next();

            if (atm.login(accountId, password)) {
                // Step 7: Display ATM menu
                while (true) {
                    System.out.println("\nATM ComputerThanyaburi Bank");
                    System.out.println("Account ID: " + atm.getCurrentAccount().getAccountId());
                    System.out.println("Menu Service");
                    System.out.println("1. Check Balance in THB");
                    System.out.println("2. Check Balance in BTC");
                    System.out.println("3. Withdrawal in THB");
                    System.out.println("4. Withdrawal in BTC");
                    System.out.println("5. Deposit in THB");
                    System.out.println("6. Transfer");
                    System.out.println("7. Exit");

                    System.out.print("Choose: ");
                    int atmChoice = readInt(scanner);

                    if (atmChoice == 1) {
                        atm.checkBalanceInTHB();
                    } else if (atmChoice == 2) { // Assuming option 6 is for checking balance in THB
                        atm.checkBalanceInBTC();
                    } else if (atmChoice == 3) {
                        System.out.print("Enter withdrawal amount in THB: ");
                        double amount = readDouble(scanner);
                        atm.withdraw(amount);
                    } else if (atmChoice == 4) {
                        System.out.print("Enter withdrawal amount in BTC: ");
                        double amountBTC = readDouble(scanner);
                        atm.withdrawBTC(amountBTC);
                    } else if (atmChoice == 5) {
                        System.out.print("Enter deposit amount in THB: ");
                        double amount = readDouble(scanner);
                        atm.deposit(amount);
                    } else if (atmChoice == 6) {
                        System.out.print("Enter transfer amount in BTC: ");
                        double amount = readDouble(scanner);
                        System.out.print("Enter recipient's account ID: ");
                        String recipientID = scanner.next();
                        Account recipient = findAccount(bankAccounts, recipientID);
                        if (recipient != null) {
                            atm.transfer(recipient, amount);
                        } else {
                            System.out.println("Recipient account not found.");
                        }
                    } else if (atmChoice == 7) {
                        break; // Exit the ATM loop
                    } else {
                        System.out.println("Invalid choice. Please choose again.");
                    }
                }
            } else {
                System.out.println("Login failed. Invalid Account ID or PIN.");
            }
        }

    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextInt();
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextDouble();
    }

    private static Account findAccount(ArrayList<Account> accounts, String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    //private static void displayAccountDetails(Account account) {
    //    System.out.println("\nAccount Details");
    //    System.out.println("Account ID: " + account.getAccountId());
    //    System.out.println("Name: " + account.name);
    //    System.out.println("Gender: " + account.gender);
    //    System.out.println("Balance: " + account.getBalance());
    //}
}