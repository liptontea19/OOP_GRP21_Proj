import java.util.ArrayList;
import java.util.List;

public class Bank {

    private static List<Account> accounts;
    private static String bankName;
    private static Insurance insurancePolicies;
    private static Branch branches;

    public void addAccount(Account account){
        accounts.add(account);
    }

    public String getName(){
        return bankName;
    }

    public static List<Account> getAccounts() {
        return accounts;
    }

    public void printBankDetails(){
        System.out.println("Bank Name: " + bankName + "\nInsurance Policies available: " + insurancePolicies + "\nBranches available: " + branches + "\nAccounts: " + accounts);
    }

    public void ProcessTransactions(){
        // Process Transactions here.
    }

    public static void main(String[] args){
        accounts = new ArrayList<>();
        // Do the rest of the functions here.
        Branch TPYbranch = new Branch(297, "Toa Payoh");
        Branch AMKbranch = new Branch(560, "Ang Mo Kio");
        Branch HOGbranch = new Branch(530, "Hougang");
        Branch TMPbranch = new Branch(478, "Tampines");


    }



}
