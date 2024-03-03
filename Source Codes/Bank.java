import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


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
        System.out.println("Project.Bank Name: " + bankName + "\nInsurance Policies available: " + insurancePolicies + "\nBranches available: " + branches + "\nAccounts: " + accounts);
    }

    public void ProcessTransactions(Account account, int choice, int[] acclist){
        // Process Transactions here.
        Scanner scanner = new Scanner(System.in);
        if(choice == 1){
            System.out.print("Enter the amount you want to deposit: ");
            double amt = scanner.nextDouble();
            account.deposit(amt);
        }
        else if(choice == 2){
            System.out.println("""
                    Choose the amount of your withdrawal:
                    (1): $30
                    (2): $50
                    (3): $100
                    (4): $200
                    (5): $250
                    """);
            int amtChoice = scanner.nextInt();
            int amt = 0;
            if(amtChoice == 1){
                amt = 30;
            }
            else if(amtChoice ==2){
                amt = 50;
            }
            else if(amtChoice == 3){
                amt = 100;
            }
            else if(amtChoice == 4){
                amt = 200;
            }
            else if(amtChoice == 5){
                amt = 250;
            }
            account.withdraw(amt);

        }
        else if(choice == 3){
            for (int transferAccs : acclist){
                System.out.println("Account: " + transferAccs);
            }
            System.out.println("Enter the account to transfer to: ");
            int accChoice = scanner.nextInt();

            boolean transferExist = false;
            for(int num : acclist){
                if(num == accChoice){
                    transferExist = true;
                }
            }
            if(transferExist)
            {
                Account transferAcc = new Account(accChoice);
                System.out.print("Enter the amount to transfer: ");
                double transferAmt = scanner.nextDouble();
                account.transfer(transferAcc,transferAmt);
            }
            else {
                System.out.println("You have entered an account not from this bank.");
                System.out.println("Would you like to transfer to a third party bank?");
                String thirdParty = scanner.nextLine();
                System.out.println("Processing....Thank you for waiting!");
            }
        }

        scanner.close();
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        accounts = new ArrayList<>();
        // Do the rest of the functions here.
        Bank myBank = new Bank();

        String csvFile = "src/Project/Bank.csv"; // Path to your CSV file
        List<String> branchCodes = new ArrayList<>();
        List<String> branchNames = new ArrayList<>();
        List<String> accNumbers = new ArrayList<>();
        List<String> insuranceNums = new ArrayList<>();

        try (Scanner idScanner = new Scanner(new File(csvFile))) {
            // Assuming comma-separated values
            while (idScanner.hasNextLine()) {
                String line = idScanner.nextLine();
                String[] values = line.split(","); // Split the line based on comma

                if (values.length >= 4) {
                    // Add the first value to the list
                    branchCodes.add(values[1].trim());
                    branchNames.add(values[2].trim());
                    accNumbers.add(values[3].trim());
                    insuranceNums.add(values[4].trim());

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String branchCArray = branchCodes.get(1);
        String branchNArray = branchNames.get(1);
        String accArray = accNumbers.get(1);
        String insuranceArray = insuranceNums.get(1);

        String[] splitBranchC = branchCArray.split("\\.");
        String[] splitBranchN = branchNArray.split("\\.");
        String[] splitAccount = accArray.split("\\.");
        String[] splitInsurance = insuranceArray.split("\\.");

        String[] BranchClist = new String[splitBranchC.length];
        String[] BranchNlist = new String[splitBranchN.length];
        String[] Accountlist = new String[splitAccount.length];
        String[] Insurancelist = new String[splitInsurance.length];

        for (int i = 0; i < splitBranchC.length; i++) {
            BranchClist[i] = splitBranchC[i];
        }
        for (int i = 0; i < splitBranchN.length; i++) {
            BranchNlist[i] = splitBranchN[i];
        }
        for (int i = 0; i < splitAccount.length; i++) {
            Accountlist[i] = splitAccount[i];
        }
        for (int i = 0; i < splitInsurance.length; i++) {
            Insurancelist[i] = splitInsurance[i];
        }

        int[] acclist = new int[Accountlist.length];
        for (int i = 0; i < Accountlist.length; i++) {
            acclist[i] = Integer.parseInt(Accountlist[i]);
        }

        int[] branchcodelist = new int[BranchClist.length];
        for (int i = 0; i < BranchClist.length; i++) {
            branchcodelist[i] = Integer.parseInt(BranchClist[i]);
        }

       /* System.out.println(Arrays.toString(newBranchC));
        System.out.println(Arrays.toString(newBranchN));

        System.out.println(Arrays.toString(newInsurance));*/

        System.out.print("""
                Welcome to UOB Bank!
                Enter your input:
                (1): View all Accounts
                (2): View all Branches
                (3): View all Insurances
                """);
        int firstChoice = scanner.nextInt();

        if(firstChoice == 1)
        {
            //Do all the account transaction
            System.out.println("List of accounts");
            System.out.println(Arrays.toString(Accountlist));
            System.out.print("Enter the account to login: ");
            int userAcc = scanner.nextInt();
            boolean userExist = false;

            for(int i = 0; i < acclist.length; i++){
                if(userAcc == acclist[i]){
                    userExist = true;
                }
            }

            if(userExist)
            {
                Account myAccount = new Account(userAcc);
                System.out.println("Welcome!");
                myAccount.printAccountDetails();
                System.out.println("""
                        Enter your input:
                        (1): Deposit
                        (2): Withdraw
                        (3): Transfer""");
                int userChoice = scanner.nextInt();
                myBank.ProcessTransactions(myAccount, userChoice,acclist);

            }
            else{
                System.out.println("Sorry, this account does not exist!");
            }



        }
        else if(firstChoice == 2)
        {
            Branch branch = new Branch(530);
            branch.viewBranches();
        }
        else if(firstChoice == 3)
        {
            Insurance insurance = new Insurance("MP01");
            insurance.viewInsuranceMenu();
        }


        scanner.close();
    }

}
