package account;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Scanner;

import bank.Bank;

/**
 * Used for user's bank account information and the associated CreditCard, Customer and Insurance class objects
 * Main package interface with {@link Bank} class.
 * <p>
 * Has credit card transaction methods that interface with CreditCard object methods.
 * Available transaction methods will differ if the user does or doesn't have a credit card associated with their account.
 * </p>
 * <p>
 * Similarly, has insurance transaction methods that interface with the associated Insurance object.
 * Available transaction methods will also differ based on the user presently having an insurance policy associated with their account.
 * </p>
 */
public class Account {
    /**A unique account id number*/
    private int accountNumber;
    /**  */
    private float interestRate;
    private String accountType;
    private double balance, transferLimit;
    private int branchCode;
    private DecimalFormat moneyDecimalFormat = new DecimalFormat("#,###.00");

    /** Initialised with Account's Credit Card details */
    public CreditCard creditCard;
    /** Account Customer Information */
    public Customer customer;
    private int creditScore;

    public ForeignX foreignX;
    public Insurance insurance;
    public Loan loan;
    private boolean insureFlag = false, cardFlag = false, loanFlag = true;

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

    /**
     * Constructor for user account object. Fetches information from the data/Account.csv file.
     * <p>
     * Uses the accountNumber method argument to check Account.csv for the entry with the matching ID number value.
     * When found, initializes the private attributes in class object.
     * </p>
     * <p>
     * If the account has a credit card number in the 8th CSV column(CardNumber), will construct a creditcard object to use for credit card-related transactions.
     * If the account has an insurance policy number ini the 9th CSV column(InsurPolNum), will construct an insurance object to use for insurance-related transactions.
     * </p>
     * @param accountNumber unique account ID used as search value for
     */
    public Account(int accountNumber){
        String filePath = "data\\Account.csv";
        //Fetches account details from Account.csv using accountNumber and initialises into class attribute
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            //BufferedReader reader = new BufferedReader(new FileReader(filePath));   // Instantiates bufferedReader obj to read Account CSV file
            String line, custId = "", cardNumber = "";
            String[] accDetail;
            Boolean foundFlag = false;
            reader.readLine();
            while ((line = reader.readLine()) != null){
                accDetail = line.split(",");
                if (Integer.parseInt(accDetail[0]) == accountNumber) {
                    foundFlag = true;
                    this.accountNumber = accountNumber;
                    this.accountType = accDetail[1];
                    this.balance = Double.parseDouble(accDetail[2]);
                    this.branchCode = Integer.parseInt(accDetail[3]);
                    custId = accDetail[4];
                    this.transferLimit = Double.parseDouble(accDetail[5]);
                    this.interestRate = Float.parseFloat(accDetail[6]);
                    cardNumber = accDetail[7];
                    if (!accDetail[8].equals("-")){ // action when an insurance account is identified
                        this.insurance = new Insurance(accountNumber);
                        insureFlag = true;
                    }
                    this.foreignX = new ForeignX(accountNumber);
                    this.loan = loan.readLoansFromCSV(accountNumber);
                    this.loanFlag = true;
                    break;
                }
            }
            if (foundFlag == true){
                this.customer = new Customer(custId);// replace with Customer csv constructor
                this.creditScore = customer.getCreditScore();
                if (cardNumber != ""){
                    cardFlag = true;
                    this.creditCard = new CreditCard(Long.parseLong(cardNumber));
                }
                else {
                    System.out.println("Account has no credit card.");
                }

            }
            else {
                System.err.println("Unable to locate account ID: " + Integer.toString(accountNumber));
                System.out.println("Unable to locate ID");
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.println("Unable to locate file in path " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Account data file unexpectedly closed.");
        }
    }

    public void addLoan(double principalAmount, float interestRate, int termInMonths, int accountNumber, int creditScore){
        if (loanFlag == false){
            Loan newLoan = Loan.applyForLoan(principalAmount, interestRate, termInMonths, accountNumber, creditScore);
            if (newLoan != null){
                this.loan = newLoan;
                loanFlag = true;
            }
            else{
                System.err.println("Loan application failed.");
            }
        }
        else{
            System.err.println("User already has an existing Loan.");
        }
    }

    public void approveLoan(){
        if (this.loan !=null && "Pending".equals(this.loan.getStatus())){
            this.loan.approveLoan();
        }else {
            System.err.println("No pending loan to approve.");
        }
    }

    public void rejectLoan(){
        if (this.loan!=null && "Pending".equals(this.loan.getStatus())){
            this.loan.rejectLoan();
        }else {
            System.err.println("No pending loan to reject.");
        }
    }

    /**
     * Adds an {@link #insurance} object to the account if there isn't an existing one.
     * Input argument takes information from the {@link bank.InsuranceCatalog} class in the hashmap format 
     * <pre>
     * "code": Policy Code
     * "name": Name
     * "type": Type
     * "annualCost": Annual Premium Cost
     * "coverage": Annual Total Policy Coverage 
     * "duration": Policy Duration in ISO8061 String Format  
     * </pre>
     * Divides the annual cost into 12 to get the monthly value and converts the ISO8061 String value into a {@link Period} object
     * @param policyInfo Hashmap of policy information from {@link bank.InsuranceCatalog}
     * @see bank.InsuranceCatalog#retrievePolicyMap(int)
     * @see Period#parse(CharSequence) Method to parse charsequences or Strings into an ISO8061 format
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO8601 Format</a>
     */
    public void addInsurance(HashMap<String,String> policyInfo){
        if (insureFlag == false){
            try {
                this.insurance = new Insurance(accountNumber, policyInfo.get("code"), LocalDate.now(), 
                (Double.parseDouble(policyInfo.get("annualCost"))/12), Double.parseDouble(policyInfo.get("coverage")), 
                Period.parse(policyInfo.get("duration")));  // converts value into Period type
                this.insureFlag = true;
            } catch (NullPointerException error){
                System.err.println("Insurance policy record is incomplete.");
            }
        }
        else {
            System.err.println("User already has existing insurance policy.");
        }
    }

    public void addCard(){
        if (cardFlag == false){
            this.creditCard = new CreditCard(customer.getCustomerName(), accountNumber, 3108398698038531L,3000);
            cardFlag = true;
        }
        else {
            System.err.println("Account " + Integer.toString(accountNumber) + " already has existing card");
        }
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

    /** Increases the account's monetary balance attribute by the specified value in the amount parameter.
     * @param amount amount to deposit into the user's account
     */
    public void deposit(double amount){
        // deposit function
        balance += amount;
        System.out.println("Amount of $" + moneyDecimalFormat.format(amount) + " has been deposited into account: " + accountNumber);
        //System.out.println("Current Balance: $" + balance);

    }

    public Boolean getCardFlag(){
        return cardFlag;
    }

    public int getAccountNumber(){
        return accountNumber;
    }

    public Boolean getInsurFlag(){
        return insureFlag;
    }

    public Boolean getLoanFlag(){
        return loanFlag;
    }

    public int getBranchCreated(){
        return branchCode;
    }

    public float getInterestRate(){
        return interestRate;
    }

    public void makeLoanPayment(){
        if (loanFlag == false){     // checks if there is a Loan class instantiated in account
            System.out.println("Account does not have a Loan associated with it");
            return;
        }
        double monthlyLoanPaymentAmount = loan.getMonthlyPayment();
        if (monthlyLoanPaymentAmount > balance){        //checks if balance is sufficient to pay of monthly Loan
            System.out.println("There is insufficient balance in account to pay amount of " + monthlyLoanPaymentAmount);
            System.out.println("Current Account Balance: $" + moneyDecimalFormat.format(balance));
        }
        else {
            this.balance = loan.repay(balance); //used to update the new balance after payment
        }
    }

    /**
     * Pays for outstanding {@link Insurance} premium for the current month.
     */
    public void payInsurancePremium(){
        if (insureFlag == false){   // checks if there is an insurance class instantiated in account
            System.out.println("Account does not have insurance plan... yet!");
            return;
        }
        // Function to pay for insurance premium
        double insuranceBalance = insurance.getmonthlyPremium();
        if (balance > insuranceBalance){
            balance -= insuranceBalance;
            insurance.payMonthPremium();
        }
        else {
            System.out.println("There is insufficient balance in account.");
            System.out.println("Current Account Balance: $" + moneyDecimalFormat.format(balance));
        }
    }

    public void makeCCPayment(double amount){
        // Make payment for outstanding credit card balance.
        // creditCard.getCreditBalance() - amount;
        if (cardFlag == false){
            System.out.println("Account does not have a credit card associated with it.");
            return;
        }
        double creditBalance = creditCard.getCreditBalance();
        if (amount > creditBalance){
            System.out.println("Payment amount of $" + moneyDecimalFormat.format(amount) + " is higher than owed balance in credit card.");
            System.out.println("Current Credit Balance: $" + moneyDecimalFormat.format(creditBalance));
        }
        else if (amount > balance){
            System.out.println("There is insufficient balance in account to pay amount of " + moneyDecimalFormat.format(amount));
            System.out.println("Current Account Balance: $" + moneyDecimalFormat.format(creditBalance));
        }
        else {
            creditCard.payBill(amount);
        }
    }

    public void makeForeignExchange(float exchangeAmount, int exchangeChoice){
        if(exchangeAmount <= balance){
            balance -= exchangeAmount;
            System.out.println("Your current balance: " + moneyDecimalFormat.format(balance));
            foreignX.exchangeToForeign(exchangeAmount, exchangeChoice);
        }
        else{
            System.out.println("Insufficient balance! Please try again!");
        }
    }

    public void makeSGDExchange(float foreignAmount){
        float SGDAmount = foreignX.exchangeToSGD(foreignAmount);
        balance += SGDAmount;
        System.out.println("Your current balance: " + moneyDecimalFormat.format(balance));

    }

    public void printAccountDetails(){
        System.out.println("Account Number: " + Integer.toString(accountNumber) +
                "\nAccount Type: " + accountType + "\nBalance: $" + moneyDecimalFormat.format(balance) +
                "\nBranch: " + Integer.toString(branchCode) + "\nInterest Rate: " + Float.toString(interestRate*100) +
                "%\nTransfer Limit: $" + moneyDecimalFormat.format(transferLimit));
        if (cardFlag == true){
            System.out.println("Credit Card Number: " + creditCard.getCardNumber());
            System.out.println("| Current Balance : $" + moneyDecimalFormat.format(creditCard.getCreditBalance()));
        }
        if (insureFlag == true){
            System.out.println("Insurance: " + insurance.getPolicyName());
            System.out.println("| Start Date: " + insurance.getStartDate());
            System.out.println("| End Date: " + insurance.getEndDate());
        }
        if (loanFlag == true){
            System.out.println("Loan: " + loan.getLoanID());
        }
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
        if (transferLimit > 0 && transferLimit <= 20000){
            this.transferLimit = transferLimit;
            System.out.println("Transfer limit has been changed to $" + moneyDecimalFormat.format(transferLimit));
        }else {
            System.out.println("The transfer limit of $" + moneyDecimalFormat.format(transferLimit) + " you have entered is invalid.");
        }
    }

    public void transfer(Account transferAcc, double transferAmt){
        // Transfer to another account
        //check that transfer amount is not more than the user's balance
        if(transferAmt > balance){
            System.out.println("Account balance $" + moneyDecimalFormat.format(balance) + " is insufficient for this transfer!");
        }
        //check that the transfer amount is not higher than the transfer limit
        else if(transferAmt > transferLimit){
            System.out.println("Amount to transfer exceeds account's transfer limit of $" + transferAmt);
        }
        else {
            balance -= transferAmt;
            //transfer to receiver by adding transferAmt to transfer acc balance and setting as new balance value
            transferAcc.setBalance(transferAcc.checkBalance()+transferAmt);
            System.out.println("Amount Transferred: $" + moneyDecimalFormat.format(transferAmt));
            System.out.println("Current Balance: $" + moneyDecimalFormat.format(balance));

        }
    }

    public void withdraw(double withdrawAmt){
        // Withdraw to cash from specified branchCode
        if(balance >= withdrawAmt)
        {
            balance -= withdrawAmt;
            System.out.println("Amount Withdrawn: $" + moneyDecimalFormat.format(withdrawAmt));
            System.out.println("Current Balance: $" + moneyDecimalFormat.format(balance));
        }
        else{
            System.out.println("Bank balance of $" + moneyDecimalFormat.format(balance) + " is insufficient for this withdrawal!");
        }

    }

    public static void main(String[] args) {
        Account myAccount = new Account(1);
        System.out.println("Welcome!");
        myAccount.printAccountDetails();

        // Test Account without Insurance
        Account acc6 = new Account(6);
        acc6.printAccountDetails();
        acc6.payInsurancePremium();
    }
}
