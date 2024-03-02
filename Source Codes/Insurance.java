package Project;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Insurance {
    private double premiumBalance = 0; //To pay monthly for insurance
    private double coverageBalance = 0; //To claim when i got into an accident :<
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

    public Insurance(String policyNumber){
        this.policyNumber = policyNumber;
        insuranceDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Project/Insurance.csv"));
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
        for (ArrayList<String> insurance : insuranceDetails) {
            if (insurance.get(0).equals(policyNumber)){
                setPolicyName(insurance.get(1));
                setInsuranceType(insurance.get(2));
                setPremiumBalance(Double.parseDouble(insurance.get(3)));
                setStartDate(LocalDate.parse(insurance.get(4)));
                setEndDate(LocalDate.parse(insurance.get(5)));
            }
        }
    }
    public static void main(String[] args) {
        //debugging purposes
        Insurance insurance = new Insurance("MP01"); 
        insurance.printInsuranceDetail();
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
        }
    }

    public void payOffPremium(double amt){
        premiumBalance += amt;
        System.out.println("This amount of " + amt + " has been paid!");
        System.out.println("This is the current premium balance: " + premiumBalance);
    }

    public double getPremiumBalance(){
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

    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
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

    public void setStartDate(LocalDate date){
        this.startDate = date;
    }

    public void setEndDate(LocalDate date){
        this.endDate = date;
    }

    public void printInsuranceDetail(){
        System.out.println(String.format("\nCoverage Balance: $%.2f\n",coverageBalance));
        System.out.println(String.format("Premium Balance: $%.2f",premiumBalance));
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Policy Name: " + policyName);
        System.out.println("Insurance Type: " + insuranceType);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate +"\n");
    }
}
