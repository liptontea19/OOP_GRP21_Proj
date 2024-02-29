public class Account {
    private int accountNumber, interestRate;
    private String accountType;
    private double balance, transferLimit;
    private Branch branch;
    private Credit_Card creditCard;
    private Customer customer;
    private Insurance insurance;

    public Account(Branch branch, int accountNumber, String accountType,
                   Insurance insurance, Customer customer, double balance,
                   double transferLimit, Credit_Card creditCard){
        this.branch = branch;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.insurance = insurance;
        this.customer = customer;
        this.balance = balance;
        this.transferLimit = transferLimit;
        this.creditCard = creditCard;

    }

    public void addInsurance(Insurance insurance){
        //this.insurance += insurance;
    }

    public int calculateInterest(){
        // Calculate Interest Function
        return interestRate;
    }

    public double checkBalance(){
        return balance;
    }

    public double checkTransferLimit(){
        return transferLimit;
    }

    public void deposit(double amount){
        // deposit function
        balance += amount;
        System.out.println("Amount of " + amount + " has been deposited into " + accountNumber);
        System.out.println("Current Balance: " + balance);

    }

    public int getAccountNumber(){
        return accountNumber;
    }

    public Branch getBranchCreated(){
        return branch;
    }

    public int getInterestRate(){
        return interestRate;
    }

    public void insuranceDeposit(){
        // Function to deposit value into insurance;
    }

    public void makePayment(double amount){
        // Make payment for credit balance.
        // creditCard.getCreditBalance() - amount;
        double creditBalance = creditCard.getCreditBalance();
        if(amount >= creditBalance){
            balance -= amount;
            creditBalance -= amount;
            creditCard.setCreditBalance(creditBalance);
            System.out.println("Payment Successful!");
            System.out.println("Current Credit Balance: " + creditBalance);
            System.out.println("Current Balance: " + balance);
        }

    }

    public void printAccountDetails(){
        System.out.println("Account Number: " + accountNumber +
                "\nAccount Type: " + accountType + "\nBalance: " + balance +
                "\nBranch: " + branch + "\nCredit Card: " + creditCard +
                "\nCustomer: " + customer + "\nInsurance: " + insurance +
                "\nInterest Rate: " + interestRate + "\nTransfer Limit: " + transferLimit);
    }

    public void setAccountNumber(int number){
        this.accountNumber = number;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public void setBranch(Branch branch){
        this.branch = branch;
    }

    public void setInterestRate(int interestRate){
        this.interestRate = interestRate;
    }

    public void setTransferLimit(double transferLimit){
        this.transferLimit = transferLimit;
    }

    public void transfer(Account transferAcc, double transferAmt){
        // Transfer to another account
        if(transferAmt > balance){
            System.out.println("Your bank balance is insufficient for this transfer!");
        }
        else if(transferAmt > transferLimit){
            System.out.println("The amount you are transferring has hit its limit!");
        }
        else {
            balance -= transferAmt;
            double transferAccBal = transferAcc.balance;
            transferAccBal += transferAmt;
            transferAcc.setBalance(transferAccBal);
            System.out.println("Amount Transferred: " + transferAmt);
            System.out.println("Current Balance: " + balance);

        }
    }

    public void withdraw(int withdrawAmt){
        // Withdraw to cash
        if(balance >= withdrawAmt)
        {
            balance -= withdrawAmt;
            System.out.println("Amount Withdrawn: " + withdrawAmt);
            System.out.println("Current Balance: " + balance);
        }
        else{
            System.out.println("Your bank balance is insufficient for this withdrawal!");
        }

    }
}
