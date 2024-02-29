public class Insurance {
    private double insuranceBalance = 0;
    private String insuranceType, insurancePlan, policyName;
    private int policyNumber;

    // Ask what does add insurance means:
    public void addInsurance(double amount){
        insuranceBalance += amount;
    }

    public double getInsuranceBalance(){
        return insuranceBalance;
    }

    public String getInsurancePlan(){
        return insurancePlan;
    }

    public String getInsuranceType(){
        return insuranceType;
    }

    public String getPolicyName(){
        return policyName;
    }

    public int getPolicyNumber(){
        return policyNumber;
    }

    public void setInsuranceBalance(double balance){
        this.insuranceBalance = balance;
    }

    public void setInsurancePlan(String plan){
        this.insurancePlan = plan;
    }

    public void setInsuranceType(String type){
        this.insuranceType = type;
    }

    public void setPolicyName(String name){
        this.policyName = name;
    }

    public void setPolicyNo(int number){
        this.policyNumber = number;
    }

    public void printInsuranceDetail(){
        System.out.println("Insurance Balance: " + insuranceBalance
                        + "\nInsurance Type: " + insuranceType
                        + "\nInsurance Plan: " + insurancePlan
                        + "\nPolicy Name: " + policyName
                        + "\nPolicy Number: " + policyNumber);
    }
}