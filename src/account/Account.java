package account;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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

    protected String SGDcurrency = "SGD";
    protected String JPYcurrency = "JPY";
    protected String USDcurrency = "USD";

    private int branchCode;
    private DecimalFormat moneyDecimalFormat = new DecimalFormat("#,###.00");

    /** Initialised with Account's Credit Card details */
    public CreditCard creditCard;
    /** Account Customer Information */
    public Customer customer;
    private int creditScore;

    public g11_FXE FXE;
    public Insurance insurance;
    private boolean insureFlag = false, cardFlag = false, loanFlag = false;

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

    public Account(int accId, int branchCode, Scanner input){
        this.accountNumber = accId;
        this.accountType = "Savings";
        this.branchCode = branchCode;
        this.customer = new Customer(input);
        System.out.println("Initial Deposit ($):");
        try{
            this.balance = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e){
            this.balance = 20000;
        }
        this.transferLimit = 2000;
        System.out.println("Set your transfer limit ($):");
        try {
            this.transferLimit = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e){
            this.transferLimit = 2000;
        }
        addCard();
        this.FXE = new g11_FXE();
    }

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
        String filePath = "data/Account.csv";
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
                    this.FXE = new g11_FXE();
                    break;
                }
            }
            if (foundFlag == true){
                this.customer = new Customer(custId);// replace with Customer csv constructor
                this.creditScore = customer.getCreditScore();
                if (!this.customer.getLoans().isEmpty()) {
                    loanFlag = true;
                }
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

    public void repayLoan(Scanner input) {
        List<Loan> loans = this.customer.getLoans();  // Assuming we can access the customer's loans

        if (loans.isEmpty()) {
            System.out.println("No loans found for this customer.");
            return;
        }

        System.out.println("Please select a loan to repay:");
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            System.out.println(i + 1 + ": Loan ID " + loan.getLoanID() + " with monthly payment of " + loan.getMonthlyPayment());
        }

        System.out.print("Enter the number of the loan to repay: ");
        int choice = input.nextInt();

        if (choice < 1 || choice > loans.size()) {
            System.out.println("Invalid loan selection.");
            return;
        }

        Loan selectedLoan = loans.get(choice - 1);
        BigDecimal monthlyPayment = selectedLoan.getMonthlyPayment();
        BigDecimal accountBalance = new BigDecimal(this.balance);

        if (accountBalance.compareTo(monthlyPayment) >= 0) {
            selectedLoan.repay(String.valueOf(this.accountNumber), accountBalance);
            LoanUtil.saveLoanToCSV(selectedLoan);

            this.balance = accountBalance.subtract(monthlyPayment).doubleValue();
            System.out.println("Loan repayment for loan ID " + selectedLoan.getLoanID() + " made successfully.");
        } else {
            System.out.println("Insufficient account balance for the monthly loan repayment.");
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
                this.insurance.setInsuranceType(policyInfo.get("type"));
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
            Random rand = new Random();
            long cardNumber = rand.nextLong(9999999999999999L);
            this.creditCard = new CreditCard(customer.getCustomerName(), accountNumber, cardNumber,3000);
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

    public Customer getCustomer(){
        return this.customer;
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


    /**
     * Pays for outstanding {@link Insurance} premium for the current month.
     */
    public void payInsurancePremium(){
        if (insureFlag == false){   // checks if there is an insurance class instantiated in account
            System.out.println("Account does not have insurance plan... yet!");
            return;
        }
        // Function to pay for insurance premium
        double insuranceBalance = insurance.getMonthlyPremium();
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

    public void makeExchange(int firstChoice, int secondChoice, double exchangeAmt){
        if(firstChoice == 1 && secondChoice == 1){
            if(exchangeAmt > balance){
                System.out.println("Insufficient balance!");
            }
            else {
                balance -= exchangeAmt;
                System.out.println("Current Account Balance: " + balance);
            }
        }
        else if(firstChoice == 1 && secondChoice == 2){
            if(exchangeAmt > balance){
                System.out.println("Insufficient balance!");
            }
            else {
                balance -= exchangeAmt;
                System.out.println("Current Account Balance: " + balance);
            }
        }
        else if(firstChoice == 2 && secondChoice == 1){
            double targetAmt = FXE.convert(JPYcurrency,SGDcurrency,exchangeAmt);
            balance += targetAmt;
            System.out.println("Current Account Balance: " + balance);
        }
        else if(firstChoice == 2 && secondChoice == 2){
            double targetAmt = FXE.convert(USDcurrency,SGDcurrency,exchangeAmt);
            balance += targetAmt;
            System.out.println("Current Account Balance: " + balance);
        }
        else {
            System.out.println("Invalid Entry! Please try again!");
        }
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
        HashMap<String,String> pol1 = new HashMap<>();
        pol1.put("code", "MP03");
        pol1.put("name", "Medical Policy 03");
        pol1.put("type", "Medical");
        pol1.put("annualCost","2000");
        pol1.put("coverage", "20000");
        pol1.put("duration", "P5Y3M");
        Account insurAccount = new Account(3);
        insurAccount.addInsurance(pol1);
        insurAccount.printAccountDetails();

        /*Customer cust1 = myAccount.getCustomer();  // Ensure this is not null
        if (cust1 != null) {
            cust1.printCustomerDetails();

            /* Apply for a loan and review it
            Loan newLoan = cust1.applyForLoan(7000, 5.0, 12);
            if (newLoan != null) {
                cust1.reviewAndProcessLoan(newLoan);  // This method should internally update the loan status and add it to the customer's loan list
            }

            // Now print all loans of the customer to confirm the loan has been added
            cust1.printAllLoans();

            // Repay a loan if any exists
            myAccount.repayLoan();
            myAccount.repayLoan();
        } else {
            System.out.println("Customer details not loaded correctly.");
        }*/
    }
}