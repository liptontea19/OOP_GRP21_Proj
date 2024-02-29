public class Account {
    private int accountNumber, interestRate;
    private String accountType;
    private double balance, transferLimit;
    private Branch branch;
    private Credit_Card creditCard;
    private Customer customer;
    private Insurance insurance;

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

    public void deposit(){
        // deposit function
    }

    public int getAccountNumber(){
        return accountNumber;
    }

    public Branch getBranchCreated(){
        return branch;
    }

    public int getInterestRate(){
        return  interestRate;
    }

    public void insuranceDeposit(){
        // Function to deposit value into insurance;
    }

    public void makePayment(double amount){
        // Make payment for credit balance.
        // creditCard.getCreditBalance() - amount;
    }

    public void printAccountDetails(){
        System.out.println("Account Number: " + accountNumber + "\nAccount Type: " + accountType + "\nBalance: " + balance + "\nBranch: " + branch + "\nCredit Card: " + creditCard + "\nCustomer: " + customer + "\nInsurance: " + insurance + "\nInterest Rate: " + interestRate + "\nTransfer Limit: " + transferLimit);
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

    public void transfer(){
        // Transfer to another account
    }

    public void withdraw(){
        // Withdraw to another account
    }
}
