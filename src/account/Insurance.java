package account;
//package Project;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Insurance class will be used to store and access insurance policy information both specific to the user and generically as a catalogue.
 * The class has methods which work hand-in-hand with the Account it will be attached to, to carry out finance related insurance processes.
 */
public class Insurance {
    /** The monthly premium due for the insurance policy. */
    private double premiumBalance = 0; //To pay monthly for insurance
    /** Balance that insurance coverage policyholder is able to claim */
    private double coverageBalance = 0; //To claim when i got into an accident :<
    /** Start date of insurance policy coverage */
    private LocalDate startDate;
    /** End date of insurance policy coverage */
    private LocalDate endDate;
    /** Arraylist that stores all available insurance policy details */
    private ArrayList<ArrayList<String>> insuranceDetails;
    /** Type of insurance coverage: Medical/Property/Travel */
    private String insuranceType;
    /** Name of insurance policy */
    private String policyName;
    /** Unique identifier for insurance policy, format XP0Y
     * X: Insurance type (M/P/T), Y: Insurance type specific policy number
     */
    private String policyNumber;

    /**Generic class constructor for class private attributes.
     * @param insuranceType Type of insurance coverage: Medical/Property/Travel
     * @param policyName Unique name for insurance policy, format: Capital letters of insurance type followed by the number, e.g. PropertyPolicy02
     * @param policyNumber Unique identifier for insurance policy, e.g. PP02
     * @param premiumBalance Outstanding monthly insurance premium balance
     * @param coverageBalance Balance that insurance coverage policyholder is able to claim
     * @param startDate Start date of insurance policy coverage
     * @param endDate End date of insurance policy coverage 
    */
    public Insurance(String insuranceType, String policyName, String policyNumber,
                     double premiumBalance, double coverageBalance, LocalDate startDate,
                     LocalDate endDate){
        this.insuranceType = insuranceType;
        this.policyName = policyName;
        this.policyNumber = policyNumber;
        this.premiumBalance = premiumBalance;
        this.coverageBalance = coverageBalance;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Special constructor that fetches all insurance policy records and places them into the insuranceDetails arraylist.
     * Used for storing and displaying all available policies found in the Insurance.csv file in the Bank main method.
     * Does not have a specific insurance policy's attributes and cannot be used with most methods in the class.
     * Use only with: viewInsuranceMenu() method
     */
    public Insurance(){  // constructor for all insurance obj
        //this.policyNumber = policyNumber;
        insuranceDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\Insurance.csv"));
            String line;
            boolean firstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }
                String[] parts = line.split(","); // Split by comma since it's CSV
                ArrayList<String> insurance = new ArrayList<>();
                for (String part : parts) {
                    insurance.add(part.trim());
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                startDate = LocalDate.parse(insurance.get(4), formatter);
                endDate = LocalDate.parse(insurance.get(5), formatter);
                insuranceDetails.add(insurance);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Object constructor to be used for initialising a single insurance policy.
     * This constructor's object is normally bound to the user account class to carry out insurance policy related actions.
     * Initialises this object's data attributes with those found in the CSV that match the policyNumber record. 
     * Able to use all normal getter and setter methods, payOffPremium(double) and printInsuranceDetail().
     * DO NOT USE WITH viewInsuranceMenu()!!!
     * @param policyNumber The unique policy number that matches the PolicyNumber field in Insurance.csv.
     */
    public Insurance(String policyNumber){   //constructor for one insurance object
        String Path = "data\\Insurance.csv";
  
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            String line = "";
            String delimiter = ",";
            Boolean firstLine = true;
              while ((line = br.readLine()) != null) {
                if (firstLine == true){
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(delimiter);
                if (data[0].equals(policyNumber)){
                    this.policyNumber = data[0];
                    this.policyName = data[1];
                    this.insuranceType = data[2];
                    this.premiumBalance = Double.parseDouble(data[3]);
                    this.startDate = LocalDate.parse(data[4]);
                    this.endDate = LocalDate.parse(data[5]);
                    break;
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to locate Customer.csv");
        }
    }

    /** Test bench for Insurance class methods */
    public static void main(String[] args) {
        //debugging purposes
        Insurance insur1 = new Insurance("MP01"); 
        insur1.printInsuranceDetail();
        Insurance insurMenu = new Insurance();
        insurMenu.viewInsuranceMenu();
    }

    /**
     * Displays all data stored in the insuranceDetails arrayList in sequential order in the system output terminal.
     * Format: Insurance Type, Policy Name, Policy Number, Start Date, End Date
     * Requires special Insurance constructor, new Insurance() object to use method with. 
     */
    public void viewInsuranceMenu(){    // for insurance menu object
        //Print all data
        for (ArrayList<String> insurance : insuranceDetails) {
            System.out.println("Insurance Type: " + insurance.get(0));
            System.out.println("Policy Name: " + insurance.get(1));
            System.out.println("Policy Number: " + insurance.get(2));
            System.out.println("Start Date: " + insurance.get(3));
            System.out.println("End Date: " + insurance.get(4));
        }
    }

    /**
     * Pays off a set value, amt, of outstanding monthly premium balance. 
     * Reduces premiumBalance attribute by amt and informs user on completion.
     * @param amt the amount that Account user will deduct from their balance to pay premium
     */
    public void payOffPremium(double amt){
        premiumBalance -= amt;
        System.out.println("This amount of " + amt + " has been paid!");
        System.out.println("This is the current premium balance: " + premiumBalance);
    }

    /** Returns the premiumBalance attribute as a double value 
     * @return double value of insurance premium outstanding balance
    */
    public double getPremiumBalance(){
        return premiumBalance;
    }

    /** Returns the coverageBalance attribute as a double value 
     * @return double value of coverage balance
    */
    public double getCoverageBalance(){
        return coverageBalance;
    }

    /** Returns the insuranceType attribute as a string 
     * @return string value of insurance type 
    */
    public String getInsuranceType(){
        return insuranceType;
    }

    /** Returns the policyName attribute as a string 
     * @return string value of policy name
    */
    public String getPolicyName(){
        return policyName;
    }

    /** Returns the policyNumber attribute as a string 
     * @return string value of policy number
    */
    public String getPolicyNumber(){
        return policyNumber;
    }

    /** Returns the starting date of the insurance policy as a LocalDate variable 
     * @return LocalDate object
    */
    public LocalDate getStartDate(){
        return startDate;
    }

    /** Returns the ending date of the insurance policy as a LocalDate variable 
     * @return LocalDate object
    */
    public LocalDate getEndDate(){
        return endDate;
    }

    /**
     * Sets the premiumBalance private class attribute to the input argument
     * @param balance value to change premiumBalance to 
     */
    public void setPremiumBalance(double balance){
        this.premiumBalance = balance;
    }

    /**
     * Sets the coverageBalance private class attribute to the input argument
     * @param balance value to change coverageBalance to
     */
    public void setCoverageBalance(double balance){
        this.coverageBalance = balance;
    }

    /** 
     * Sets the insurance type to the value specified in type input argument
     *  @param type Should only accept Medical/Property/Health values
     */ 
    public void setInsuranceType(String type){
        this.insuranceType = type;
    }
    
    /**
     * Sets the insurance policy's name to the value specified in the input argument, 
     * should correspond with the object's policy type and number
     * @param name input argument to set object policy name to
     */
    public void setPolicyName(String name){
        this.policyName = name;
    }

    /**
     * Sets the unique identifier for insurance policy, should correspond with policy type and name
     * @param number Value should be in format XP0Y. 
     * X: Insurance type (M/P/T), Y: Insurance type specific policy number (1/2/3)
     */
    public void setPolicyNo(String number){
        this.policyNumber = number;
    }

    /**
     * Sets the starting date of the policy 
     * @param date LocalDate format
     */
    public void setStartDate(LocalDate date){
        this.startDate = date;
    }

    /**
     * Sets the ending date of the policy 
     * @param date LocalDate format
     */
    public void setEndDate(LocalDate date){
        this.endDate = date;
    }

    /**
     * Prints the insurance object's policy attributes on System Terminal for viewing.
     */
    public void printInsuranceDetail(){ // for normal insurance object
        System.out.println(String.format("\nCoverage Balance: $%.2f\n",coverageBalance));
        System.out.println(String.format("Premium Balance: $%.2f",premiumBalance));
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Policy Name: " + policyName);
        System.out.println("Insurance Type: " + insuranceType);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate +"\n");
    }
}
