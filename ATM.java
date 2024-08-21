import java.util.Scanner;
class ATM {
    public boolean authenticate(BankAccount account) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String accountNumber = s.nextLine();
        System.out.print("Enter password: ");
        String password = s.nextLine();

        if (accountNumber.equals(account.getAccountNumber()) && password.equals(account.getPassword())) {
            System.out.println("Authentication successful.");
            return true;
        } else {
            System.out.println("Authentication failed. Please check your account number and password.");
            return false;
        }
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter amount of all account : ");
        int numberOfAccounts = s.nextInt();
        s.nextLine();  // Consume the newline character

        // Validate the number of accounts
        if (numberOfAccounts < 1 || numberOfAccounts > 5) {
            System.out.println("Invalid number of accounts. Exiting program.");
            
        }

        // Create an array to store BankAccount objects
        BankAccount[] accounts = new BankAccount[numberOfAccounts];

        // Populate the array with bank account information
        for (int i = 0; i < numberOfAccounts; i++) {
            System.out.println("\nAccount " + (i + 1));
            System.out.print("Enter account number: ");
            String accountNumber = s.nextLine();

            System.out.print("Enter account name: ");
            String accountName = s.nextLine();

            System.out.print("Enter initial balance: ");
            double initialBalance = s.nextDouble();
            s.nextLine();  // Consume the newline character

            // Create a bank account object and set password
            BankAccount account = new BankAccount(accountNumber, accountName, initialBalance);
            System.out.print("Set account password (4 digits): ");
            String password = s.nextLine();
            account.setPassword(password);

            // Add the account to the array
            accounts[i] = account;
        }

        // Create ATM
        ATM atm = new ATM();
        System.out.println("\n--- ATM Interface ---");
        System.out.print("Enter account number: ");
        String userInputAccountNumber = s.nextLine();

        System.out.print("Enter account password: ");
        String userInputPassword = s.nextLine();

        boolean authenticated = false;
        for (BankAccount account : accounts) {
            if (userInputAccountNumber.equals(account.getAccountNumber()) && userInputPassword.equals(account.getPassword())) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            System.out.println("\nAuthentication successful. Access granted.");
            // Continue with transaction or further operations
        } else {
            System.out.println("\nAccess denied. Unable to log in.");
        }
        System.out.println("\n--- ATM Interface ---");

        boolean exitATM = false;
        while (!exitATM) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Log Out");

            System.out.print("Enter choice (1-3): ");
            int choice = s.nextInt();
            s.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    // Check Balance
                    if (atm.authenticate(accounts[0])) {  // Assuming we check balance for the first account
                        System.out.println("Current balance: " + accounts[0].getBalance() + " Baht.");
                    }
                    break;
                case 2:
                    // Withdraw Money
                    if (atm.authenticate(accounts[0])) {  // Assuming we withdraw money from the first account
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawalAmount = s.nextDouble();
                        s.nextLine();  // Consume the newline character

                        accounts[0].withdraw(withdrawalAmount);
                        System.out.println("Current balance: " + accounts[0].getBalance() + " Baht.");
                    }
                    break;
                case 3:
                    // Log Out
                    exitATM = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        } s.close();
    }
    }
        
    
