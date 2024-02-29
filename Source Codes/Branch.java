import java.time.LocalDate;
import java.time.LocalTime;
public class Branch {
    private int branchCode;
    private String branchName;
    private double branchReserve;

    public Branch(int branchCode, String branchName){
        this.branchCode = branchCode;
        this.branchName = branchName;
    }

    public void checkAvailability(){
        LocalTime open = LocalTime.of(9,0);
        LocalTime close = LocalTime.of(18,0);
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

    public void printBranchDetails(){
        System.out.println("Branch Code is: " + branchCode +
                            "\nBranch Name is: " + branchName +
                            "\nBranch Reserve is: " + branchReserve);
    }
}
