import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Insurance {
    private double premiumBalance = 0; //To pay monthly for insurance
    private double coverageBalance = 0; //To claim when i got into an accident :<
    private Date startDate;

    private Date endDate;
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
