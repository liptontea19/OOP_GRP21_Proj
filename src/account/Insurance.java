package account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;


/**
 * Insurance class stores and accesses insurance policy information found in the user's account.
 * The class has methods which work hand-in-hand with the {@link Account} object it will be attached to, 
 * allowing the account to carry out finance related insurance processes.
 * <pre>
 * General usage flow:
 * - Use {@link #Insurance(int)} for initialising insurance objects with existing records in InsuranceAccounts.csv
 * - OR {@link #Insurance(int, String, LocalDate, double, double, Period)} for adding a new insurance object 
 * - Use {@link #payMonthPremium()} for paying of monthly premiums 
 * - Use {@link #displayPremiumBilling()} to view next due premium dates
 * </pre>
 * File Dependency: <code>InsuranceAccounts.csv</code> (Account-specific insurance policy information)
 * @see bank.InsuranceCatalog Bank insurance catalog class for displaying all policies
 */
public class Insurance {
    /** The monthly premium due for the insurance policy. */
    private double monthlyPremium = 0; //To pay monthly for insurance
    /** Tracks whether premium has been paid for the current month */
    private boolean monthlyPremiumPaid = false;
    /** Balance that insurance coverage policyholder is able to claim */
    private double claimBalance = 0; //To claim when i got into an accident :<
    /** Start date of insurance policy coverage */
    private LocalDate startDate;
    /** End date of insurance policy coverage */
    private LocalDate endDate;
    /** Type of insurance coverage: Medical/Property/Travel */
    private String insuranceType;
    /** Name of insurance policy */
    private String policyName;
    /** Unique identifier for insurance policy, format XP0Y
     * X: Insurance type (M/P/T), Y: Insurance type specific policy number
     */
    private String policyId;
    private DecimalFormat moneyFormat = new DecimalFormat("#,###.00");
    private DateTimeFormatter dateDisplay = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 

    /**Generic class constructor for class private attributes.
     * @param insuranceType Type of insurance coverage: Medical/Property/Travel
     * @param policyName Name of Insurance Policy format: [Type]Policy[Number], e.g. PropertyPolicy02
     * @param policyId Unique identifier for insurance policy, e.g. PP02
     * @param monthlyPremium Outstanding monthly insurance premium balance
     * @param claimBalance Balance that insurance coverage policyholder is able to claim
     * @param startDate Start date of insurance policy coverage
     * @param endDate End date of insurance policy coverage 
    */
    public Insurance(String insuranceType, String policyName, String policyId,
                     double monthlyPremium, double claimBalance, LocalDate startDate,
                     LocalDate endDate){
        this.insuranceType = insuranceType;
        this.policyName = policyName;
        this.policyId = policyId;
        this.monthlyPremium = monthlyPremium;
        this.claimBalance = claimBalance;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructor called when adding a new insurance object from {@link Account} 
     * @param accountId Id of account
     * @param policyId Policy ID, must correspond with existing insurance policy found in InsuranceCatalog
     * @param startDate LocalDate value of policy starting
     * @param monthlyPremium premium due each month 
     * @param claimBalance annual claimable amount
     * @param polPeriod duration of insurance policy, uses Period object
     */
    public Insurance(int accountId,String policyId, LocalDate startDate, double monthlyPremium, double claimBalance, Period polPeriod){

        if (policyId.charAt(0) == 'M'){
            this.insuranceType = "Medical";
        } else if (policyId.charAt(0) == 'P'){
            this.insuranceType = "Property";
        } else if (policyId.charAt(0) == 'T'){
            this.insuranceType = "Travel";
        }

        this.policyId = policyId;
        this.policyName = insuranceType + " Policy " + policyId.substring(2,4);
        this.startDate = startDate;
        this.endDate = startDate.plus(polPeriod);
        this.monthlyPremium = monthlyPremium;
        this.claimBalance = claimBalance;
        this.monthlyPremiumPaid = false;
    }
    
    /**
     * Constructor that gets information for object attributes from the Insurance.csv file.
     * This constructor's object is normally bound to the user account class to carry out insurance policy related actions.
     * Initialises this object's data attributes with those found in the CSV that match the policyId record. 
     * Raises an IO Exception if Insurance.csv data file cannot be located and NoSuchElementException when specified record cannot be found.
     * @param policyId The unique policy identifier code that matches the Policy ID field in Insurance.csv.
     */
    public Insurance(int accountID){ 
        String path = "data\\InsuranceAccounts.csv";
  
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = "";
            String delimiter = ",";
            boolean foundFlag = false;
            br.readLine();  // skips first line of file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                if (data[0].equals(Integer.toString(accountID))){
                    this.policyId = data[1];
                    this.insuranceType = data[2];
                    this.policyName = insuranceType + " Policy " + policyId.substring(2,4);
                    this.monthlyPremium = Double.parseDouble(data[3]);
                    if (data[4].equals("Yes")){
                        this.monthlyPremiumPaid = true;
                    } else {
                        this.monthlyPremiumPaid = false;
                    }
                    this.claimBalance = Double.parseDouble(data[5]);
                    this.startDate = LocalDate.parse(data[6]);
                    this.endDate = LocalDate.parse(data[7]);
                    foundFlag = true;
                    break;
                }   
            }
            if (!foundFlag){
                System.out.println("Failed to find account insurance policy.");
                throw new NoSuchElementException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to locate Insurance.csv");
        }
    }

    /** Test bench for Insurance class methods */
    public static void main(String[] args) {
        //debugging purposes
        Insurance insur1 = new Insurance(1); 
        insur1.printInsuranceDetail();
        Insurance insur2 = new Insurance(1,"PP02", LocalDate.now().minusMonths(4), 240, 12000, Period.parse("P2Y"));
        insur2.printInsuranceDetail();
        insur2.displayPremiumBilling();
        insur2.payMonthPremium();
        insur2.displayPremiumBilling();
    }

    /**
     * Displays a list of monthly premiums and their payment dates.
     * Checks for payment in the current billable month period and displays the annual billing start date and current date.
     */
    public void displayPremiumBilling(){
        int yearBetween = Period.between(startDate, LocalDate.now()).getYears(), rowIter, thisMonth;
        int monthsLeft = 12 - (Period.between(startDate.minusYears(yearBetween), LocalDate.now()).getMonths());    // get number of months left in year billing cycle
        LocalDate billStart = startDate.plusYears(yearBetween); // date of billing cycle start for year
        if (!monthlyPremiumPaid){
            rowIter = 0;
            thisMonth = 1;
        } else {
            rowIter = 1;
            thisMonth = 0;
        }
        System.out.println(policyName + " Billing Service");
        System.out.println("Billing Start Date: " + billStart.format(dateDisplay) + " | Today Date: " + LocalDate.now().format(dateDisplay));
        System.out.println("Billing months left: " + Integer.toString(monthsLeft + thisMonth) + " months");
        System.out.println("Date           Amount Due");
        while(rowIter<= monthsLeft){
            System.out.println(billStart.plusMonths(12 - monthsLeft + rowIter).format(dateDisplay) + "     $" + moneyFormat.format(monthlyPremium));
            rowIter++;
        }
        System.out.println("Total: $" + moneyFormat.format((monthsLeft+thisMonth)*monthlyPremium));
    }

    /**
     * Pays off the input value of outstanding monthly premium balance. 
     * Checks if value in amt argument is within monthlyPremium value and
     * reduces monthlyPremium attribute by amt and informs user on completion.
     * @param amt amount to deduct from the monthly premium attribute, monthlyPremium
     * @deprecated Replaced by {@link #payMonthPremium()}
     * @see #payMonthPremium()
     */
    public void payOffPremium(double amt){
        if (monthlyPremium >= amt){
            monthlyPremium -= amt;
        System.out.println("This amount of " + amt + " has been paid!");
        System.out.println("This is the current premium balance: " + monthlyPremium);
        } else {
            System.out.println("Unable to pay premium balance.");
        }
    }

    /**
     * Pays the policy premium due for the current month.
     * Checks if premium has already been paid for the month before paying.
     * Informs user on next billable month.
     */
    public void payMonthPremium(){
        if (monthlyPremiumPaid == false) {
            monthlyPremiumPaid = true;
            System.out.println("Premium has been paid for " + LocalDate.now().getMonth() + ", next premium due in " + 
            LocalDate.now().plusMonths(1).getMonth());
        } else {
            System.out.println("Premium has been paid for current month.");
        }
    }

    /** Returns the monthlyPremium attribute as a double value 
     * @return insurance premium outstanding balance for the month
    */
    public double getmonthlyPremium(){
        return monthlyPremium;
    }

    /** Returns the claimBalance attribute as a double value 
     * @return insurance plan coverage balance
    */
    public double getCoverageBalance(){
        return claimBalance;
    }

    /** Returns the insuranceType as a string 
     * Will return Medical, Property or Travel as a string
     * @return type of insurance policy 
    */
    public String getInsuranceType(){
        return insuranceType;
    }

    /** Returns the policyName attribute as a string 
     * @return the name of the insurance policy
    */
    public String getPolicyName(){
        return policyName;
    }

    /** Returns the policyId attribute as a string 
     * @return string value of policy number
    */
    public String getPolicyNumber(){
        return policyId;
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
     * Sets the monthlyPremium private class attribute to the input argument
     * @param balance value to change monthlyPremium to 
     */
    public void setmonthlyPremium(double balance){
        this.monthlyPremium = balance;
    }

    /**
     * Sets the claimBalance private class attribute to the input argument
     * @param balance value to change claimBalance to
     */
    public void setCoverageBalance(double balance){
        this.claimBalance = balance;
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
        this.policyId = number;
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
        System.out.println("Coverage Balance is: $" + moneyFormat.format(claimBalance));
        System.out.println("Premium Balance: $" + moneyFormat.format(monthlyPremium));
        System.out.println("Policy ID: " + policyId);
        System.out.println("Policy Name: " + policyName);
        System.out.println("Insurance Type: " + insuranceType);
        System.out.println("Start Date: " + startDate.format(dateDisplay));
        System.out.println("End Date: " + endDate.format(dateDisplay) +"\n");
    }
}
