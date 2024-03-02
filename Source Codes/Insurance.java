import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.crypto.Data;

public class Insurance {
    private double premiumBalance = 0; //To pay monthly for insurance
    private double coverageBalance = 0; //To claim when i got into an accident :<
    private Date startDate;
    private Date endDate;
    private static ArrayList<ArrayList<Data>> insuranceDetails = new ArrayList<>();
    private String insuranceType, policyName, policyNumber;

    // Ask what does add insurance means:
    public Insurance(String insuranceType, String policyName, String policyNumber,
                     double premiumBalance, double coverageBalance, Date startDate,
                     Date endDate){
        this.insuranceType = insuranceType;
        this.policyName = policyName;
        this.policyNumber = policyNumber;
        this.premiumBalance = premiumBalance;
        this.coverageBalance = coverageBalance;
        this.startDate = startDate;
        this.endDate = endDate;

        //add insurance type
        ArrayList<ArrayList<Data>> nestedList = new ArrayList<>();

        // Creating inner ArrayLists
        ArrayList<Data> insuranceTypeList = new ArrayList<>();
        insuranceTypeList.add(new Data("Medical", "360 Health", "Policy100"));
        insuranceTypeList.add(new Data("Medical", "MedicalHealth", "Policy101"));
        
        ArrayList<Data> policyNameList = new ArrayList<>();
        policyNameList.add(new Data("Property", "Fire", "Policy200"));
        policyNameList.add(new Data("Property", "Damage", "Policy201"));
        
        ArrayList<Data> policyNumberList = new ArrayList<>();
        data.add(new Data("Travel", "Accident", "Policy300"));
        data.add(new Data("Travel", "Lost", "Policy301"));


        // Adding inner ArrayLists to the nested ArrayList
        nestedList.add(insuranceTypeList);
        nestedList.add(policyNameList);
        nestedList.add(policyNumberList);

    }
    class Data {
        private String insuranceType;
        private String policyName;
        private String policyNumber;
    
        public Data(String insuranceType, String policyName, String policyNumber) {
            this.insuranceType = insuranceType;
            this.policyName = policyName;
            this.policyNumber = policyNumber ;
        }
    
        @Override
        public String toString() {
            return "Data{" +"insuranceType=" + insuranceType +", policyName='" + policyName + '\'' +", policyNumber=" + policyNumber +'}';
        }
    }
    public static void main(String[] args){
        for (int i=0; i< insuranceDetails.size(); i++){
            System.out.println(insuranceDetails[i]);
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
                        + "\nInsurance Type: " + insuranceType
                        + "\nPolicy Name: " + policyName
                        + "\nPolicy Number: " + policyNumber);
    }
}
