import java.util.ArrayList;
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

    public void ProcessTransactions(Account account, int choice){
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
            String csvFile = "src/Project/Account.csv"; // Path to your CSV file
            List<String> accNumbers = new ArrayList<>();

            try (Scanner idScanner = new Scanner(new File(csvFile))) {
                // Assuming comma-separated values
                while (idScanner.hasNextLine()) {
                    String line = idScanner.nextLine();
                    String[] values = line.split(","); // Split the line based on comma

                    if (values.length > 0) {
                        // Add the first value to the list
                        accNumbers.add(values[0].trim());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Enter the account to transfer to: ");

            int[] intArray = new int[accNumbers.size()];
            for(int i=1; i<accNumbers.size(); i++){
                System.out.println("[" + accNumbers.get(i) + "]");
                intArray[i] = Integer.parseInt(accNumbers.get(i));
            }
            int accChoice = scanner.nextInt();

            for(int num : intArray){
                if(num == accChoice){
                    Account transferAcc = new Account(accChoice);
                    System.out.print("Enter the amount to transfer: ");
                    double transferAmt = scanner.nextDouble();

                    account.transfer(transferAcc,transferAmt);
                }
                else{
                    System.out.println("You have entered an account not from this bank.");
                    System.out.println("Would you like to transfer to a third party bank?");
                    String thirdParty = scanner.nextLine();
                    System.out.println("Processing....Thank you for waiting!");
                }
            }


        }
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        accounts = new ArrayList<>();
        // Do the rest of the functions here.
        Bank myBank = new Bank();

        System.out.print("""
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
            Account myAccount = new Account(000001);
            myAccount.printAccountDetails();
            System.out.print("""
                Enter your input:
                (1): Deposit
                (2): Withdraw
                (3): Transfer 
                """);

            //int userChoice = scanner.nextInt();
            //myBank.ProcessTransactions(myAccount, userChoice);
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












    }



}
