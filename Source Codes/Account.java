import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Account {
    private int accountNumber; 
    private float interestRate;
    private String accountType;
    private double balance, transferLimit;
    private int branchCode;
    private CreditCard creditCard;
    private Customer customer;
    private Insurance insurance;
    private boolean insureFlag = false;

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
            String line, custId = "", cardNumber = "";
            String[] accDetail;
            Boolean foundFlag = false;
            while ((line = reader.readLine()) != null){
                accDetail = line.split(",");
                if (accDetail[0] == Integer.toString(accountNumber)) {
                    foundFlag = true;
                    this.accountType = accDetail[1];
                    this.balance = Double.parseDouble(accDetail[2]);
                    this.branchCode = Integer.parseInt(accDetail[3]);
                    custId = accDetail[4];
                    this.transferLimit = Double.parseDouble(accDetail[5]);
                    this.interestRate = Float.parseFloat(accDetail[6]);
                    cardNumber = accDetail[7];
                    try {
                        this.insurance = new Insurance(accDetail[8]);
                        insureFlag = true;
                    }
                    catch(ArrayIndexOutOfBoundsException error){
                        error.printStackTrace();
                    }
                }
            }
            reader.close();
            if (foundFlag == true){
                this.customer = new Customer(custId);// replace with Customer csv constructor
                if (cardNumber != ""){
                    this.creditCard = new CreditCard(customer.getCustomerName(),Integer.toString(accountNumber), cardNumber, 5000);          
                }
                else {
                    System.out.println("Account has no credit card.");
                }

            }
            else {
                System.out.println("Unable to locate account ID: " + Integer.toString(accountNumber));
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }

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

    public int getBranchCreated(){
        return branchCode;
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
        System.out.println("Account Number: " + Integer.toString(accountNumber) +
                "\nAccount Type: " + accountType + "\nBalance: $" + balance +
                "\nBranch: " + Integer.toString(branchCode) + "\nCredit Card Number: " + creditCard.getCardNumber() +
                "\nCustomer: " + customer.getCustomerName() + "\nInsurance: " + insurance.getPolicyName() +
                "\nInterest Rate: " + Float.toString(interestRate*100) + "%\nTransfer Limit: $" + Double.toString(transferLimit));
    }

    public void setAccountNumber(int number){
        this.accountNumber = number;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public void setBranchCode(int branch){
        this.branchCode = branch;
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
