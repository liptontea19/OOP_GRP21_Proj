import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Branch {
    private int branchCode;     /**it will be used as an ID to represent the branch objects */
    private String branchName;  /** Name to represent the branch name */
    private double branchReserve;   /** branchReserve represents the amount of money a bank has */

    private LocalTime openingHours; /** LocalTime to specifically represent the opening hours in 24-hour time format for each branch */
    private LocalTime closingHours; /** LocalTime to specifically represent the closing hours in 24-hour time format for each branch */
    private ArrayList<ArrayList<String>> branchDetails; /** Array to store the Branch objects */


    /*public Branch(int branchCode, String branchName, double branchReserve,
                  LocalTime openingHours, LocalTime closingHours){
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchReserve = branchReserve;
        this.openingHours = openingHours;
        this.closingHours = closingHours;

    }*/

    /**
     * Object constructor that takes in a specific id which is the branch code and read each specific data in the file
     * Helps in displaying the values of each attribute that a branch has and segregate them by their branch code
     * useful for viewBranches() only
     * It reads Branch.csv as the data contains branch code, branch name, branch reserve, opening and closing hours
     * for loop is used for assigning the attributes into an array and then displaying them as a full list of branches
     */
    public Branch(int branchCode) {
        this.branchCode = branchCode;
        branchDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Project/data/Branch.csv"));
            String line;
            boolean firstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }
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
                setBranchReserve(Double.parseDouble(branch.get(2)));
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
        branch.printBranchDetails();
        branch.viewBranches();
        branch.checkAvailability();
    }

    /**
     * Function to view the list of branches by looping through the array that has been instantised
     * It will display every branch detail according to the branch code
     */
    public void viewBranches(){
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
     * it calls for the current time with the use of LocalTime an if statement is created to check if the current time is after closing time or before opening time
     * if they are then, it will display a message to come again tomorrow
     * if not then it will tell the user that the branch is currently open
     */
    public void checkAvailability(){
        LocalTime open = getOpeninghours();
        LocalTime close = getClosingHours();
        LocalTime current = LocalTime.now();

        if( (current.isAfter(close)) || current.isBefore(open) ){
            System.out.println("The branch is currently closed! Please come again tomorrow!");
        }
        else {
            System.out.println("The branch is currently open!");
        }


    }

    /**
     * Getter and Setter methods for the attributes of the branch class
     * getBranchCode returns the branch code
     */
    public int getBranchCode(){
        return branchCode;
    }

    /**
     * getBranchReserve returns the branch's reserve
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
     * setBranchCode allows the branch code to be changed accordingly
     * @param branchCode value to change the branch code
     */
    public void setBranchCode(int branchCode){
        this.branchCode = branchCode;
    }

    /**
     * setBranchName can change the name of the branch based on user
     * @param branchName value to change the branch name
     */
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    /**
     * setBranchReserve allows a user to manually set how much a bank has
     * @param branchReserve value to change the reserve of a branch
     */
    public void setBranchReserve(double branchReserve){
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
     * Method withdrawReserve is when the user withdraws money from their bank account from a specific branch, and the money from the branch's reserve would be deducted
     * @param amount is used when an amount is entered for the user to withdraw from their acc
     * With this amount it reduces the specific branch's reserve
     */
    public void withdrawReserve(double amount){
        if(amount > 0){
            branchReserve -= amount;
        }
        else{
            System.out.println("This is an invalid amount!");
        }

    }

    /**
     * Method depositReserve is when the user deposits money through their bank account, the money would go to the branch's reserves
     * @param amount is used when an amount is entered for the user to deposit into their acc
     * With this amount, it increases the branch's reserve as well
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
     * Method printBranchDetails is used to print the specific set of information of branch code, branch name, branch reserve and their opening/closing Hours
     */
    public void printBranchDetails(){
        System.out.println("Branch Code is: " + branchCode +
                            "\nBranch Name is: " + branchName +
                            "\nBranch Reserve is: " + branchReserve +
                            "\nOpening Hour: " + openingHours +
                            "\nClosing Hour: " + closingHours);
    }
}
