package account;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * The Loan class represents a Loan.
 */

public class Loan {
    private UUID loanID;
    private double principalAmount;
    private float interestRate;
    private int termInMonths;
    private double monthlyPayment;
    private double outstandingAmount;
    private double totalPayment;
    private LocalDate startDate;
    private LocalDate endDate;
    private int accountID;
    private String status;
    private ArrayList<LocalDate> paymentDates;
    private String loantypes;
    private int creditScore;
    private DecimalFormat moneyDecimalFormat = new DecimalFormat("#,###.00");

    /**
     * Constructs a new Loan object with the specified details. 
     * @param principalAmount the principal amount of the Loan
     * @param interestRate the annual interest rate of the Loan
     * @param termInMonths the Loan term in months
     * @param accountID the customerID associated with the Loan
     */
    public Loan(double principalAmount, float interestRate, int termInMonths, int accountID) {
        this.loanID = UUID.randomUUID();
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.termInMonths = termInMonths;
        this.monthlyPayment = calculateAmortizationPayment();
        this.outstandingAmount = principalAmount;
        this.totalPayment = monthlyPayment * termInMonths;
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusMonths(termInMonths);
        this.accountID = accountID;
        this.status = "Pending";
        this.paymentDates = new ArrayList<LocalDate>();
        this.loantypes = "Car, Student, Mortage";
        //this.creditScore = creditScore;
    }
    
    /**
     * Gets the unique identifier (UUID) associated with the Loan.
     *
     * @return The UUID representing the Loan.
     */
    public UUID getLoanID() {
        return loanID;
    }
    /**
     * Sets the Unique Identifier (UUID) for the Loan.
     *
     * @param loanID The UUID to set for the Loan.
     */
    public void setLoanID(UUID loanID) {
        this.loanID = loanID;
    }
    
    /**
     * Gets the Principal Amount of the Loan.
     *
     * @return The Principal Amount of the Loan.
     */
    public double getPrincipalAmount() {
        return principalAmount;
    }
    /**
     * Sets the Principal Amount of the Loan.
     *
     * @param principalAmount The Principal Amount to set for the Loan.
     */
    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    /**
     * Gets the Interest Rate of the Loan.
     *
     * @return The Interest Rate of the Loan.
     */
    public float getInterestRate() {
        return interestRate;
    }
    /**
     * Sets the Interest Rate of the Loan.
     *
     * @param interestRate The Interest Rate to set for the Loan.
     */
    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Gets the Loan term in months of the Loan.
     *
     * @return The Loan term in months.
     */
    public int getTermInMonths() {
        return termInMonths;
    }
    /**
     * Sets the Loan term in months.
     *
     * @param termInMonths The term in months to set for the Loan.
     */
    public void setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
    }

    /**
     * Gets the outstanding amount of the Loan.
     *
     * @return The outstanding amount.
     */
    public double getOutstandingAmount() {
        return outstandingAmount;
    }
    /**
     * Sets the outstanding amount.
     *
     * @param outstandingAmount The outstanding amount to set for the Loan.
     */
    public void setOutstandingAmount(double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * Gets the monthly payment amount of the Loan.
     *
     * @return The monthly payment amount.
     */
    public double getMonthlyPayment() {
        return monthlyPayment;
    }
    /**
     * Sets the monthly payment amount of the Loan.
     *
     * @param monthlyPayment The monthly payment to set for the Loan.
     */
    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    /**
     * Gets the total payment required of the entire Loan.
     *
     * @return The total payment.
     */
    public double getTotalPayment() {
        return totalPayment;
    }
    /**
     * Sets the total payment required of the Loan.
     *
     * @param totalPayment The total payment to set for the Loan.
     */
    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    /**
     * Gets the start date of the Loan.
     *
     * @return The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }
    /**
     * Sets the start date of the Loan.
     *
     * @param startDate The start date to set for the Loan.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the Loan.
     *
     * @return The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    /**
     * Sets the end date of the Loan.
     *
     * @param endDate The end date to set for the Loan.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /** 
     * Gets the accountID associated with the Loan
     * 
     * @return the accountID
     */
    public int getAccountID(){
        return accountID;
    }
    /**
     * Sets the accountID associated with the Loan.
     * 
     * @param accountID the accountID to set for the Loan.
     */
    public void setAccountID(int accountID){
        this.accountID = accountID;
    }

    /**
     * Gets the status of the Loan.
     *
     * @return The status of the Loan.
     */
    public String getStatus() {
        return status;
    }
    /**
     * Sets the status of the Loan.
     *
     * @param status The status to set for the Loan.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the list of payment dates for the Loan.
     *
     * @return The list of payment dates.
     */
    public ArrayList<LocalDate> getPaymentDates() {
        return paymentDates;
    }

    /**
     * Sets the list of payment dates of the Loan.
     *
     * @param paymentDates The list of payment dates to set for the Loan.
     */
    public void setPaymentDates(ArrayList<LocalDate> paymentDates) {
        this.paymentDates = paymentDates;
    }

    /**
     * Gets the loan types of the Loan.
     *
     * @return The loan types of the Loan.
     */
    public String getLoanTypes(){
        return loantypes;
    }
    /**
     * Sets the loan types of the Loan.
     *
     * @param loantypes The loan types to set for the Loan.
     */
    public void setLoanTypes(String loantypes){
        this.loantypes = loantypes;
    }
    /**
     * Gets the credit score of the Loan.
     *
     * @return The credit score of the Loan.
     */
    public int getCreditScore(){
        return creditScore;
    }
    /**
     * Sets the credit score of the Loan.
     *
     * @param creditScore The credit score to set for the Loan.
     */
    public void setCreditScore(int creditScore){
        this.creditScore = creditScore;
    }
    /**
     * Check Eligiblity of customer to apply for Loan.
     * If customer's credit score is higher than or equal to 600, customer is eligible for a Loan.
     * Otherwise, customer is not eligble for a Loan
     *
     * @param creditScore The credit score of the customer applying for the Loan.
     * @return True if  customer is eligible for a Loan, otherwise return false.
     */
    public static boolean checkEligiblity(int creditScore) {
        if (creditScore >= 600) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Applies for new Loan.
     *
     * @param principalAmount The principal amount of the Loan.
     * @param interestRate The annual interest rate for the Loan.
     * @param termInMonths The Loan term in months.
     * @param accountID The accountID of customer applying for the Loan.
     * @param creditScore The credit score of customer applying for the Loan.
     * @return Loan object for successful application, otherwise return null.
     */
    public static Loan applyForLoan(double principalAmount, float interestRate, int termInMonths,int accountID, int creditScore) {
        if (principalAmount <= 0 || interestRate <= 0 || termInMonths <= 0) {
            System.out.println("Error. Please provide non-negative values for principal amount, interest rate, and term in months.");
            return null;
        }
        if (accountID <= 0){
            System.out.println("Error. Account ID cannot be a negative number.");
            return null;
        }
        if (creditScore < 0) {
            System.out.println("Error. Please provide non-negative value for credit score.");
            return null;
        }
        if(Loan.checkEligiblity(creditScore) == true) {
            Loan loan = new Loan(principalAmount, interestRate, termInMonths, accountID);
            System.out.println("Applying for Loan.");
            return loan;
        }
        else {
            System.out.println("Failure to apply for loan as credit score is too low.");
            return null;
        }
    }

    /**
     * Approves the Loan and updates the status of the Loan from "Pending" to "Approved".
     */
    public void approveLoan() {
        this.setStatus("Approved");
        System.out.println("Loan is approved.");
    }

    /**
     * Rejects the Loan and updates the status of the Loan from "Pending" to "Rejected".
     */
    public void rejectLoan() {
        this.setStatus("Rejected");
        System.out.print("Loan is rejected.");
    }

    /**
     * Displays information about the Loan.
     */
    public void displayInfo() {
        System.out.println("\nLoan Information: ");
        System.out.println("Loan ID: " + loanID);
        System.out.println("Principal Amount: " + moneyDecimalFormat.format(principalAmount));
        System.out.println("Outstanding Amount: " + moneyDecimalFormat.format(outstandingAmount));
        System.out.println("Monthly Payment: " + moneyDecimalFormat.format(monthlyPayment));
        System.out.println("Interest Rate in Month: " + interestRate + "%");
        System.out.println("Loan Period(Months): " + termInMonths);
        System.out.println("Total Payment: " + moneyDecimalFormat.format(totalPayment));
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Account ID: " + accountID);
        System.out.println("Payment Dates: " + paymentDates);
        System.out.println("Status: " + status);
        System.out.println("Loan Types: " + loantypes);
        System.out.println("Credit Score: " + creditScore);
        for (int i = 0; i < paymentDates.size(); i++) {
            System.out.println("Payment Date " + (i+1) + ": " + paymentDates.get(i));
        }
    }

    /**
     * Prints amortization schedule of the Loan.
     */
    public void printAmortizationSchedule() {
        System.out.println("Month\tMonthly Payment\tPrincipal Amount\tInterest\tRemaining Balance of Principal");

        double remainingBalance = principalAmount;

        for (int month = 1; month <= termInMonths; month++) {
            double interestPayment = remainingBalance * (interestRate / 12 / 100);
            double principalPayment = monthlyPayment - interestPayment;
            remainingBalance -= principalPayment;
            
            String outputString = String.format("%d\t$%.2f\t\t$%.2f\t\t\t$%.2f\t\t$%.2f", month, monthlyPayment, principalPayment, interestPayment, Math.max(0,remainingBalance));
            System.out.println(outputString);
        }
    }

    /**
     * Adds payment date to the list of payment dates for the Loan.
     *
     * @param paymentDate The payment date to be added to the list of payment dates.
     */
    public void addPaymentDate(LocalDate paymentDate) {
        paymentDates.add(paymentDate);
    }

    /**
     * Repays the monthly payment of the Loan, updating payment and Account information.
     *
     * @param balance The balance of the account to deduct balance for repayment of the Loan.
     */
    public double repay(double balance) {
        if(this.getStatus().equalsIgnoreCase("Approved")) {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            boolean isPaid = false;
            if(!paymentDates.isEmpty()) {
                LocalDate date = paymentDates.get(paymentDates.size()-1);
                int month = date.getMonthValue();
                int year = date.getYear();
                if(month == currentMonth && year == currentYear) {
                    isPaid = true;
                }
            }
            if(isPaid == true) {
                System.out.println("Already paid this month.");
            }
            else {
                    addPaymentDate(currentDate);
                    double interestPayment = outstandingAmount * (interestRate / 12/ 100);
                    double principalPayment = monthlyPayment - interestPayment;
                    outstandingAmount -= principalPayment;
                    System.out.println("Account ID: " + String.valueOf(accountID) + ". Original Account Balance: " + moneyDecimalFormat.format(balance));
                    System.out.println("Repaid: " + moneyDecimalFormat.format(monthlyPayment) + ". Outstanding Amount to pay: " + moneyDecimalFormat.format(outstandingAmount));
                    balance -= monthlyPayment;
                    System.out.println("Account balance: " + moneyDecimalFormat.format(balance));

                    if(outstandingAmount == 0.0) {
                        System.out.println("Loan Cleared.");
                    }
            }
        }
        else {
            System.out.println("Error. Loan is not approved. ");
        }
        return balance; //return the updated balance to Account Class
    }
    
    /**
     * Calculates the monthly amortization payment of the Loan.
     *
     * @return The calculated monthly amortization payment.
     */
    public double calculateAmortizationPayment() {
        double monthlyInterestRate = interestRate / 12.0 / 100.0;
        double monthlyPayment = (principalAmount * monthlyInterestRate * Math.pow((1+monthlyInterestRate),termInMonths)) / (Math.pow((1+monthlyInterestRate),termInMonths)-1);
        return monthlyPayment;
    }
    
    /**
     * Reads list of Loans from CSV file and returns a list of Loan objects.
     * that match the specified customer and have the "Approved" status.
     *
     * 
     * @param accountID The accountID of the specified customer to retrieve Loans associated with the specified acount.
     * @return A list of Loans with "Approved" status.
     */
    public static Loan readLoansFromCSV(int accountID) {

        try (BufferedReader br = new BufferedReader(new FileReader("OOP_GRP21_Proj-main/data/Loan.csv"))) {
            // Skip the header row
            String header = br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int fileAccountID = Integer.parseInt(data[9]); // parse accountID from CSV
                if(fileAccountID == accountID && data[10].equalsIgnoreCase("Approved")) {
                    UUID loanID = UUID.fromString(data[0]);
                    double principalAmount = Double.parseDouble(data[1]);
                    float interestRate = Float.parseFloat(data[2]);
                    int termInMonths = Integer.parseInt(data[3]);
                    double monthlyPayment = Double.parseDouble(data[4]);
                    double outstandingAmount = Double.parseDouble(data[5]);
                    double totalPayment = Double.parseDouble(data[6]);
                    LocalDate startDate = LocalDate.parse(data[7]);
                    LocalDate endDate = LocalDate.parse(data[8]);
                    String status = data[10];
                    String loantypes = data[11];
                    int creditScore = Integer.parseInt(data[12]);
                    ArrayList<LocalDate> paymentDates = new ArrayList<>();
                    for (int i = 13; i < data.length; i++) {
                        paymentDates.add(LocalDate.parse(data[i]));
                    }

                    Loan loan = new Loan(principalAmount, interestRate, termInMonths, fileAccountID);
                    loan.setLoanID(loanID);
                    loan.setMonthlyPayment(monthlyPayment);
                    loan.setOutstandingAmount(outstandingAmount);
                    loan.setTotalPayment(totalPayment);
                    loan.setStartDate(startDate);
                    loan.setEndDate(endDate);
                    loan.setStatus("Approved");
                    loan.setPaymentDates(paymentDates);
                    loan.setLoanTypes("Car, Student, Mortage");
                    loan.setCreditScore(creditScore);
                    return loan;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
