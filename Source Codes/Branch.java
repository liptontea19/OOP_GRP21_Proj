public class Branch {
    private int branchCode;
    private String branchName;
    private double branchReserve;

    public void checkAvailability(){

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

    public void printBranchDetails(){
        System.out.println("Branch Code is: " + branchCode +
                            "\nBranch Name is: " + branchName +
                            "\nBranch Reserve is: " + branchReserve);
    }
}