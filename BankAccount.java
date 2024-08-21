class BankAccount {
    private String accountNumber;
    private String accountName;
    private double balance;
    private String password;

    public BankAccount(String accountNumber, String accountName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = initialBalance;
        this.password = null;
    }

    public void setPassword(String password) {
        if (password.length() == 4) {
            this.password = password;
            System.out.println("Password set successfully.");
        } else {
            System.out.println("Password must be 4 digits.");
        }
    
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited " + amount + " Baht. New balance: " + this.balance + " Baht.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            System.out.println("Withdrew " + amount + " Baht. New balance: " + this.balance + " Baht.");
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getPassword() {
        return this.password;
    }
    public String  getName(){
        return this.accountName;
    }
}






