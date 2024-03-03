import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Branch {
    private int branchCode;
    private String branchName;
    private double branchReserve;

    private LocalTime openingHours;
    private LocalTime closingHours;
    private ArrayList<ArrayList<String>> branchDetails;

    public Branch(int branchCode, String branchName, double branchReserve,
                  LocalTime openingHours, LocalTime closingHours){
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchReserve = branchReserve;
        this.openingHours = openingHours;
        this.closingHours = closingHours;

    }

    public Branch(int branchCode) {
        this.branchCode = branchCode;
        branchDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Project/Branch.csv"));
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

    public static void main(String[] args) {
        Branch branch = new Branch(530);
        branch.printBranchDetails();
        branch.viewBranches();
        branch.checkAvailability();
    }

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

    public int getBranchCode(){
        return branchCode;
    }

    public double getBranchReserve(){
        return branchReserve;
    }

    public String getBranchName(){
        return branchName;
    }

    public void setBranchCode(int branchCode){
        this.branchCode = branchCode;
    }

    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    public void setBranchReserve(double branchReserve){
        this.branchReserve = branchReserve;
    }

    public LocalTime getOpeninghours(){
        return openingHours;
    }

    public LocalTime getClosingHours(){
        return closingHours;
    }

    public void setOpeningHours(LocalTime openingHours){
        this.openingHours = openingHours;
    }

    public void setClosingHours(LocalTime closingHours){
        this.closingHours = closingHours;
    }

    public void printBranchDetails(){
        System.out.println("Branch Code is: " + branchCode +
                            "\nBranch Name is: " + branchName +
                            "\nBranch Reserve is: " + branchReserve +
                            "\nOpening Hour: " + openingHours +
                            "\nClosing Hour: " + closingHours);
    }
}
