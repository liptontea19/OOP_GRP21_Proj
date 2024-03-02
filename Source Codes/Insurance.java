import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Insurance {
    private double premiumBalance = 0; //To pay monthly for insurance
    private double coverageBalance = 0; //To claim for coverage
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<ArrayList<String>> insuranceDetails;
    private String insuranceType, policyName, policyNumber;

    // Constructors
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

    public Insurance(String filePath) {
        insuranceDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
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
                startDate = LocalDate.parse(insurance.get(3), formatter);
                endDate = LocalDate.parse(insurance.get(4), formatter);
                insuranceDetails.add(insurance);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Insurance insurance = new Insurance("src/Project/Insurance.csv");
        insurance.printInsuranceDetail();
        insurance.viewInsuranceMenu();
    }

    // Functions
    public void viewInsuranceMenu(){
        //Print all data
        for (ArrayList<String> insurance : insuranceDetails) {
            System.out.println("Insurance Type: " + insurance.get(0));
            System.out.println("Policy Name: " + insurance.get(1));
            System.out.println("Policy Number: " + insurance.get(2));
            System.out.println("Start Date: " + insurance.get(3));
            System.out.println("End Date: " + insurance.get(4));
            System.out.println(); // Add a newline for better readability
        }
    }

    public void payOffPremium(double amt){
        premiumBalance += amt;
        System.out.println("This amount of " + amt + " has been paid!");
        System.out.println("This is the current premium balance: " + premiumBalance);
    }

    public double getInsuranceBalance(){
        return premiumBalance;
    }

    public double getCoverageBalance(){
        return coverageBalance;
    }

    public String getInsuranceType(){
        return insuranceType;
    }

    public String getPolicyName(){
        return policyName;
    }

    public String getPolicyNumber(){
        return policyNumber;
    }

    public void setPremiumBalance(double balance){
        this.premiumBalance = balance;
    }

    public void setCoverageBalance(double balance){
        this.coverageBalance = balance;
    }

    public void setInsuranceType(String type){
        this.insuranceType = type;
    }

    public void setPolicyName(String name){
        this.policyName = name;
    }

    public void setPolicyNo(String number){
        this.policyNumber = number;
    }

    public void printInsuranceDetail(){
        System.out.println("Insurance Balance: " + premiumBalance
                        + "\nCoverage Balance: " + coverageBalance
                        + "\nInsurance Type: " + insuranceType
                        + "\nPolicy Name: " + policyName
                        + "\nPolicy Number: " + policyNumber);
    }
}
