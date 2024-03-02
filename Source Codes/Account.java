import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Account {
    private int accountNumber; 
    private float interestRate;
    private String accountType;
    private double balance, transferLimit;
    private Branch branch;
    private Credit_Card creditCard;
    private Customer customer;
    private Insurance insurance;

    /*public Account(int accountNumber, String accountType, int branchCode, 
                    double balance, Customer customer, double transferLimit){
        //this.branch = branch;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.customer = customer;
        this.balance = balance;
        this.transferLimit = transferLimit;

        //Branch this.branch = new Branch(branchCode);
    }*/

    public Account(int accountNumber){
        String filePath = "Source_Codes/Account.csv";
        //Fetches account details from Account.csv using accountNumber and initialises into class attribute
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));   // Instantiates bufferedReader obj to read Account CSV file
            String line;
            String[] accDetail;
            while ((line = reader.readLine()) != null){
                accDetail = line.split(",");
                if (accDetail[0] == Integer.toString(accountNumber)) {
                    this.accountType = accDetail[1];
                    this.balance = Double.parseDouble(accDetail[2]);
                    //this.branch = new Branch(accDetail[3]);
                    //this.Customer = new Customer(accDetail[4])// instantiates a Customer obj using the specified customer ID
                    this.transferLimit = Double.parseDouble(accDetail[5]);
                }
            }
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Unable to locate account ID: " + Integer.toString(accountNumber));
        }
    }

    /*public Account(Insurance insurance, Credit_Card creditCard){
        this.insurance = insurance;
        this.creditCard = creditCard;
    }*/

    public void addInsurance(String insuranceType, String policyName, String policyNumber,
                             double premiumBalance, double coverageBalance, Date startDate,
                             Date endDate){
        //Insurance newInsurance = new Insurance(insuranceType, policyName, policyNumber,
                //premiumBalance, coverageBalance, startDate, endDate);
    }

    public double calculateInterest(){
        // Calculate Interest per annum 
        double interest = balance * interestRate;

        return interest;
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
        System.out.println("Amount of $" + amount + " has been deposited into " + accountNumber);
        System.out.println("Current Balance: $" + balance);

    }

    public int getAccountNumber(){
        return accountNumber;
    }

    public Branch getBranchCreated(){
        return branch;
    }

    public float getInterestRate(){
        return interestRate;
    }

    public void insuranceDeposit(Insurance insurance){
        // Function to pay for insurance premium
        double insuranceBalance = insurance.getInsuranceBalance();
        System.out.println("Account " + accountNumber + " has an insurance premium balance of $" + insuranceBalance + " outstanding for " + insurance.getPolicyName() + " policy.");
        if (balance > insuranceBalance){
            balance -= insuranceBalance;
            insurance.payOffPremium(insuranceBalance);
        }
        else {
            System.out.println("There is insufficient balance in account.");
            System.out.println("Current Account Balance: $" + balance);
        }
    }

    public void makePayment(double amount){
        // Make payment for outstanding credit card balance.
        // creditCard.getCreditBalance() - amount;
        double creditBalance = creditCard.getCreditBalance();
        if (amount > creditBalance){
            System.out.println("Payment amount of $" + amount + " is higher than owed balance in credit card.");
            System.out.println("Current Credit Balance: $" + creditBalance);
        }
        else if (amount > balance){
            System.out.println("There is insufficient balance in account to pay amount of " + amount);
            System.out.println("Current Account Balance: $" + balance);
        }
        else {
            creditCard.payBill(amount);
        }

    }

    public void printAccountDetails(){
        System.out.println("Account Number: " + accountNumber +
                "\nAccount Type: " + accountType + "\nBalance: $" + balance +
                "\nBranch: " + branch.getBranchName() + "\nCredit Card Number: " + creditCard.getCardNumber() +
                "\nCustomer: " + customer.getCustomerName() + "\nInsurance: " + insurance.getPolicyName() +
                "\nInterest Rate: " + interestRate + "%\nTransfer Limit: $" + transferLimit);
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
        //check that transfer amount is not more than the user's balance
        if(transferAmt > balance){
            System.out.println("Account balance $" + balance + " is insufficient for this transfer!");
        }
        //check that the transfer amount is not higher than the transfer limit
        else if(transferAmt > transferLimit){
            System.out.println("Amount to transfer exceeds account's transfer limit of $" + transferAmt);
        }
        else {
            balance -= transferAmt;
            //transfer to receiver by adding transferAmt to transfer acc balance and setting as new balance value
            transferAcc.setBalance(transferAcc.checkBalance()+transferAmt);
            System.out.println("Amount Transferred: $" + transferAmt);
            System.out.println("Current Balance: $" + balance);

        }
    }

    public void withdraw(int withdrawAmt){
        // Withdraw to cash from specified branchCode
        if(balance >= withdrawAmt)
        {
            balance -= withdrawAmt;
            System.out.println("Amount Withdrawn: $" + withdrawAmt);
            System.out.println("Current Balance: $" + balance);
        }
        else{
            System.out.println("Bank balance of $" + balance + " is insufficient for this withdrawal!");
        }

    }
}
