package account;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.IIOException;

/**
 * The Customer class is used to store and access a customer's information
 * This class works together with the account class.
 */

public class Customer {

    private int Age, ContactNo, CreditScore;
    private String Address, ID, CustomerName, MaritalStatus, Country, EmailAddress, Occupation, Employer, DateOfBirth;
    private List<Loan> loans; //a single Customer can have a List of loans


    /**
     * Constructor reads the customer's data from "OOP_GRP21_Proj-main/data/Customer.csv" file
     */

    public Customer(String customerID){
        String path = "data/Customer.csv"; // Use this if you're not on windows it might work: "src/Project/data/Customer.csv"

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = "";
            String delimiter = ",";
            Boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine == true){
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(delimiter);
                if (data[0].equals(customerID)){
                    this.ID = data[0];
                    this.CustomerName = data[1];
                    this.Age = Integer.parseInt(data[2]);
                    this.ContactNo = Integer.parseInt(data[3]);
                    this.Address = data[4];
                    this.MaritalStatus = data[5];
                    this.Country = data[6];
                    this.EmailAddress = data[7];
                    this.Occupation = data[8];
                    this.Employer = data[9];
                    this.DateOfBirth = data[10];
                    this.CreditScore = Integer.parseInt(data[11]);
                    break;
                }

            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.println("Unable to locate Customer CSV Data File.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to locate Customer ID: " + customerID);
        }

        this.loans = new ArrayList<>();

        String loansPath = "liptontea19/OOP_GRP21_Proj/data/Loans.csv"; // Path to the loans CSV file
        try {
            List<String> lines = Files.readAllLines(Paths.get(loansPath));
            for (String line : lines) {
                String[] data = line.split(",");
                if (data[1].equals(this.ID)) {
                    BigDecimal principal = new BigDecimal(data[2]);
                    BigDecimal interestRate = new BigDecimal(data[3]);
                    int termInMonths = Integer.parseInt(data[4]);

                    // Create a dummy Credit object with customer's ID and CreditScore
                    // Assuming the existence of a Credit class constructor that takes these parameters
                    Credit credit = new Credit(this.ID, this.CreditScore);

                    // Use the applyForLoan method to create and add the loan
                    Loan newLoan = Loan.applyForLoan(this.loans, principal, interestRate, termInMonths, credit);
                    this.reviewAndProcessLoan(newLoan);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error reading loans file.");
        }
    }

    public Loan applyForLoan(double principalAmount, double interestRate, int termInMonths) {
        Credit credit = new Credit(this.ID, this.CreditScore);  // Create Credit object using customer's ID and credit score
    
        if (credit.checkEligiblity()) {
            BigDecimal principal = BigDecimal.valueOf(principalAmount);
            BigDecimal interest = BigDecimal.valueOf(interestRate);
            
            Loan newLoan = Loan.applyForLoan(loans, principal, interest, termInMonths, credit);  // Use the credit object
    
            if (newLoan != null) {
                //loan already added to the list in the loan.applyForLoan method, so inside customer not required
                System.out.println("Loan applied successfully.");
                LoanUtil.saveLoanToCSV(newLoan);
            } else {
                System.out.println("Loan application failed.");
            }
            return newLoan;
        } else {
            System.out.println("Customer is not eligible for a loan.");
            return null;
        }
    }
    

    public void reviewAndProcessLoan(Loan loan) {
        try {
            if (loan != null) {
                loan.approveLoan();  // This is where the exception is thrown
                //there is an underlying error in the Loan.approveLoan method "ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result."
                //the loan.approve method sets status to approved at the start of the method before error is encounted, hence approval still works after catching the error
                System.out.println("Loan is approved.");
            }
        } catch (ArithmeticException e) {
            System.err.println("Failed to process loan due to an arithmetic error: " + e.getMessage());
            // Implement alternative logic or recovery here if possible
        }
    }
       
    
    /**
     * Prints the details of all loans associated with the customer.
     */
    public void printAllLoans() {
        if (loans.isEmpty()) {
            System.out.println("No loans found for this customer.");
            return;
        }

        System.out.println("Loans for Customer ID: " + this.ID);
        for (Loan loan : loans) {
            loan.displayInfo();
        }
    }

    public List<Loan> getLoans() {
    return loans;
}


    /**
     * Returns customer's credit score
     * @return integer of customer's credit score
     * */
    public int getCreditScore(){
        return CreditScore;
    }
    /**
     * Returns customer's contact number
     * @return integer of customer's contact number
     * */
    public int getContactNo(){
        return ContactNo;
    }
    /**
     * Returns customer's marital status
     * @return string of customer's marital status
     * */
    public String getMaritalStatus(){
        return MaritalStatus;
    }
    /**
     * Returns customer's country
     * @return string of customer's country
     * */
    public String getCountry(){
        return Country;
    }
    /**
     * Returns customer's email address
     * @return string of customer's email address
     * */
    public String getEmailAddress(){
        return EmailAddress;
    }
    /**
     * Returns customer's occupation
     * @return string of customer's occupation
     * */
    public String getOccupation(){
        return Occupation;
    }
    /**
     * Returns customer's employer company
     * @return string of customer's employer company
     * */
    public String getEmployer(){
        return Employer;
    }
    /**
     * Returns customer's name
     * @return string of customer's name
     * */
    public String getCustomerName() {
        return CustomerName;
    }
    /**
     * Returns customer's age
     * @return integer of customer's age
     * */
    public int getCustomerAge(){
        return Age;
    }
    /**
     * Returns customer's address
     * @return string of customer's address
     * */
    public String getCustomerAddress(){
        return Address;
    }
    /**
     * Returns customer's date of birth
     * @return string of customer's date of birth
     * */
    public String getCustomerDOB(){
        return DateOfBirth;
    }
    /**
     * Returns customer's NRIC
     * @return string of customer's NRIC
     * */
    public String getCustomerNRIC(){
        return ID;
    }
    /**
     * Sets customer's credit score
     * @param CreditScore retrives customer's credit score
     * */
    public void setCreditScore(int CreditScore){
        this.CreditScore = CreditScore;
    }
    /**
     * Sets customer's contact number
     * @param ContactNo retrives customer's contact number
     * */
    public void setContactNo(int ContactNo){
        this.ContactNo = ContactNo;
    }
    /**
     * Sets customer's marital status
     * @param MaritalStatus retrives customer's marital status "Single, Married, Divorced"
     * */
    public void setMaritalStatus(String maritalStatus){
        this.MaritalStatus = maritalStatus;
    }
    /**
     * Sets customer's country
     * @param Country retrives customer's bank country
     * */
    public void setCountry(String Country){
        this.Country = Country;
    }
    /**
     * Sets customer's email address
     * @param EmailAddress retrives customer's email address
     * */
    public void setEmailAddress(String emailAddress){
        this.EmailAddress = emailAddress;
    }
    /**
     * Sets customer's occupation
     * @param Occupation retrives customer's job occupation
     * */
    public void setOccupation(String occupation){
        this.Occupation = occupation;
    }
    /**
     * Sets customer's employer company
     * @param Employer retrives customer's employer company
     * */
    public void setEmployer(String employer){
        this.Employer = employer;
    }
    /**
     * Sets customer's age
     * @param CustomerName retrives customer's age
     * */
    public void setCustomerAge(int age){
        this.Age = age;
    }
    /**
     * Sets customer's address
     * @param Address retrives customer's residential address
     * */
    public void setCustomerAddress(String address){
        this.Address = address;
    }
    /**
     * Sets customer's date of birth
     * @param DateOfBirth retrives customer's date of birth
     * */
    public void setCustomerDOB(String dob){
        this.DateOfBirth = dob;
    }
    /**
     * Sets customer's name
     * @param CustomerName retrives customer's full name
     * */
    public void setCustomerName(String name){
        this.CustomerName = name;
    }
    /**
     * Sets customer's NRIC
     * @param ID retrives customer's NRIC
     * */
    public void setCustomerNRIC(String id){
        this.ID = id;
    }
    /**
     * This method prints out customer's details
     * */
    public void printCustomerDetails(){
        System.out.println("Name: " + CustomerName
                + "\nAge: " + Age
                + "\nAddress: " + Address
                + "\nDate of Birth: " + DateOfBirth
                + "\nID: " + ID
                + "\nContact Number: " + ContactNo
                + "\nMarital Status: " + MaritalStatus
                + "\nCountry: " + Country
                + "\nEmail Address: " + EmailAddress
                + "\nOccupation: " + Occupation
                + "\nEmployer: " + Employer);
    }
    public static void main(String[] args){
        Customer cust1 = new Customer("S1234A");
        Customer cust2 = new Customer("S1235B");
        cust1.printCustomerDetails();
        //Loan newLoan = cust1.applyForLoan(7000, 5.0, 12);
        //cust1.reviewAndProcessLoan(newLoan);
        Loan newLoan = cust2.applyForLoan(7000, 5.0, 12);
        cust2.reviewAndProcessLoan(newLoan);
        cust1.printAllLoans();
        cust2.printAllLoans();
        

    }
}