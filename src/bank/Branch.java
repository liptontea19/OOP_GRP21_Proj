package bank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Branch class will be used to store and access branch information e.g. location, code, opening and closing hours.
 * The class has methods which work hand-in-hand with the Bank it will be attached to, to display information and perform financial transactions.
 */

public class Branch {

    /** int branchCode is used as a unique identifier to represent the branches */
    private int branchCode;

    /** String branchName stores the branch name from the Branch.csv */
    private String branchName;

    /** float branchReserve stores the amount of money a bank has
     * this attribute will be called for financial transactions when a user:
     * deposits or withdraws from a bank*/
    private float branchReserve;

    /** LocalTime openingHours represents the opening hours in 24-hour time format for each branch */
    private LocalTime openingHours;

    /** LocalTime closingHours represents the closing hours in 24-hour time format for each branch */
    private LocalTime closingHours;

    /** Array branchDetails stores the attributes that belongs to a branch for viewing
     * it stores branch name, branch code, branch reserve, opening hours and closing hours */
    private ArrayList<ArrayList<String>> branchDetails;


    /**
     * Branch constructor takes in a specific id of the branch code and obtains necessary data from Branch.csv
     * It displays the values of each attribute that a branch has and segregates them by individual branch code
     * Buffered Reader is scans through the Branch.csv for the following variables:
     * - branch code
     * - branch name
     * - branch reserve
     * - opening hours
     * - closing hours
     * for loop is used for assigning the attributes of the branch class from the array
     */
    public Branch(int branchCode) {
        this.branchCode = branchCode;
        branchDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("OOP_GRP21_Proj-main/data/Branch.csv"));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split by comma since it's CSV
                ArrayList<String> branch = new ArrayList<>();
                for (String part : parts) {
                    branch.add(part.trim());
                }
                branchDetails.add(branch);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ArrayList<String> branch : branchDetails) {
            if (Double.parseDouble(branch.get(0)) == branchCode){
                setBranchName(branch.get(1));
                setBranchReserve(Float.parseFloat(branch.get(2)));
                setOpeningHours(LocalTime.parse(branch.get(3)));
                setClosingHours(LocalTime.parse(branch.get(4)));
            }
        }
    }

    /**
     * Main class is used for testing of the branch class
     * Can be used by initiating a new branch object
     * then call out its specific functions to see if they work
     */
    public static void main(String[] args) {
        Branch branch = new Branch(530);
        branch.viewBranchDetails();
        branch.viewAllBranches();
        branch.checkAvailability();
        branch.depositReserve(500);
        branch.depositReserve(60);
        branch.withdrawReserve(20);
    }

    /**
     * Function to view the list of branches by looping through the array that has been instantised
     * It will display every branch detail according to the branch code
     */
    public void viewAllBranches(){
        //Print all data
        for (ArrayList<String> branch : branchDetails) {
            System.out.println("Branch Code: " + branch.get(0));
            System.out.println("Branch Name: " + branch.get(1));
            System.out.println("Branch Reserve: " + branch.get(2));
            System.out.println("Opening Hours: " + branch.get(3));
            System.out.println("Closing Hours: " + branch.get(4));
            System.out.println(); // Add a newline for better readability
        }
    }

    /**
     * checkAvailability retrieves the opening and closing hours of the branch and assign it to the variables, open and close
     * LocalTime currentTiming calls for the current time by LocalTime function
     * If statement is used checking for whether the branch is open or closed based on current timing
     * If the branch is closed, it will display a message to come again tomorrow
     * If the branch is open then it will tell the user that the branch is currently open
     */
    public void checkAvailability(){
        LocalTime openTiming = getOpeninghours();
        LocalTime closeTiming = getClosingHours();
        LocalTime currentTiming = LocalTime.now();

        if( (currentTiming.isAfter(closeTiming)) || currentTiming.isBefore(openTiming) ){
            System.out.println("The branch is currently closed! Please come again tomorrow!");
        }
        else {
            System.out.println("The branch is currently open!");
        }


    }

    /**
     * Getter and Setter methods for the attributes of the branch class
     * getBranchCode returns the branch code of a specific branch
     */
    public int getBranchCode(){
        return branchCode;
    }

    /**
     * getBranchReserve returns the branch's reserve of a specific bank
     */
    public double getBranchReserve(){
        return branchReserve;
    }

    /**
     * getBranchName returns the branch's name
     */
    public String getBranchName(){
        return branchName;
    }

    /**
     * getOpeningHours returns the branch's opening hours in LocalTime format
     */
    public LocalTime getOpeninghours(){
        return openingHours;
    }

    /**
     * getClosingHours returns the branch's closing hours in LocalTime format
     */
    public LocalTime getClosingHours(){
        return closingHours;
    }

    /**
     * setBranchName can change the name of the branch based on user
     * @param branchName value to change the branch name
     */
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    /**
     * setBranchReserve allows a user to manually set how much reserve a bank has
     * @param branchReserve value to change the reserve amount of a specific branch
     */
    public void setBranchReserve(float branchReserve){
        this.branchReserve = branchReserve;
    }


    /**
     * setOpeningHours can change the opening hours of a branch
     * @param openingHours value to change the opening hours of a branch
     */
    public void setOpeningHours(LocalTime openingHours){
        this.openingHours = openingHours;
    }

    /**
     *  setClosingHours can change the closing hours of a branch
     * @param closingHours value to change the closing hours of a branch
     */
    public void setClosingHours(LocalTime closingHours){
        this.closingHours = closingHours;
    }

    /**
     * Method withdrawReserve is called when the user withdraws money from a specific branch
     * The amount withdrawn from branch will deduct its reserve
     * @param amount is used when an amount is entered for the user to withdraw from their account
     * It checks if the withdrawn amount is more than the branch's reserve
     */
    public void withdrawReserve(double amount){
        if(amount > 0 && branchReserve >= amount){
            branchReserve -= amount;
        }
        else if (amount > branchReserve){
            System.out.println("This branch has insufficient funds for your withdrawal, please try another branch.");
        }
        else{
            System.out.println("This is an invalid amount!");
        }
    }

    /**
     * Method depositReserve is called when the user deposits money through their bank account
     * the money deposited would go into a branch's reserves depending on the user's choice of branch
     * @param amount is used when an amount is entered for the user to deposit into their account
     * In this method, it checks for the amount sent by the user
     */
    public void depositReserve(double amount){
        if(amount > 0){
            branchReserve += amount;
        }
        else{
            System.out.println("This is an invalid amount!");
        }
    }

    /**
     * Method viewBranchDetails is used to print the specific set of information:
     * branch code, branch name, branch reserve and their opening/closing Hours
     */
    public void viewBranchDetails(){
        System.out.println("Branch Code is: " + branchCode +
                "\nBranch Name is: " + branchName +
                "\nBranch Reserve is: " + branchReserve +
                "\nOpening Hour: " + openingHours +
                "\nClosing Hour: " + closingHours);
    }

}
