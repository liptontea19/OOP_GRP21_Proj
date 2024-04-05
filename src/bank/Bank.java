package bank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import account.Account;
import account.Insurance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;


public class Bank {

    //private static List<Account> accounts;
    /** Name of Bank */
    private static String bankName;
    private static Insurance insurancePolicies; // will be deprecated soon after main1's removal
    private static Branch branches;
    private Security secSession;
    private DecimalFormat moneyFormat = new DecimalFormat("#,###.00");
    private Scanner input = new Scanner(System.in);

    /** Insurance Catalog Class Object that is used for insurance policy display*/
    private InsuranceCatalog insuranceCatalog;
    /** Maps all brank branches. Key: Normal Integer Index Val: Branch Object */
    private HashMap<Integer, Branch> branchMap;
    /** Maps all user accounts. Key: Account ID, Value: Account Object connected to Account ID */
    private HashMap<Integer, Account> accountMap;

    public Bank(){
        //accounts = new ArrayList<>();
        accountMap = new HashMap<>();
        branchMap = new HashMap<>();
        insuranceCatalog = new InsuranceCatalog();
        secSession = new Security(60);
        String[] accountCSVLine, branchCodeCSVLine;

        String csvFile = "data/Bank.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String bankLine;
            bankLine = reader.readLine();   // reads first line and skips lmao
            bankLine = reader.readLine();   // reads the actual data in Bank.csv
            String[] bankVals = bankLine.split(",");
            bankName = bankVals[0];
            branchCodeCSVLine = bankVals[1].split("\\.");
            accountCSVLine = bankVals[3].split("\\.");

            makeBranch(branchCodeCSVLine);  // creates branchMap 
            makeAccounts(accountCSVLine);   // creates accountMap
            insuranceCatalog = new InsuranceCatalog();  // initialises insuranceCatalog object            
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName(){
        return bankName;
    }

    public void bankWelcomeMessage(){
        System.out.println("_____________________");
        System.out.println("|Welcome to " + bankName + " bank|");
        System.out.println("---------------------");
    }

    /**
     * Displays a main menu to choose values from. Rejects values not in display.
     * @return Main Menu Choice value between 0 and 4
     */
    public int bankMainMenuSelect() {
        int menuChoice = 0;
        System.out.println("--------------------------------------------");
        System.out.print("""                
                Enter your input:
                (1): Login to an Account
                (2): View all Branches
                (3): View all Insurance Policies
                (4): Exit
                """);

        do{
            try{
                menuChoice = Integer.parseInt(input.nextLine());
                if (1 <= menuChoice && menuChoice <= 4){    // menuchoice has to be between 1-3
                    break;
                } else {
                    System.err.println("Invalid choice please try again.");
                } 
            }catch (NumberFormatException e) {
                System.err.println("Value entered was not an option.");
            }
        } while (menuChoice != 4);
        return menuChoice;   //default value
    }

    /**
     * Initialises hashmap with Branch objects based on their branch code
     * @param branchNumList String array of Branch IDs to initialise into branchMap
     */
    public void makeBranch(String[] branchNumList){
        for(int i=0;i<branchNumList.length;i++){
            branchMap.put(i+1, new Branch(Integer.parseInt(branchNumList[i])));
        }
    }

    public void makeAccounts(String[] accIdStrings){
        for(int i=0;i<accIdStrings.length;i++){
            accountMap.put(i+1, new Account(Integer.parseInt(accIdStrings[i])));
            //accounts.add(new Account(Integer.parseInt(accIdStrings[i])));
        }
    }

    /** Displays all bank customer account IDs and names */
    public void displayAccounts(){
        if (accountMap.size() < 1){
            System.err.println("No accounts to display.");
            return;
        }
        System.out.println("Acc No.   Name   ");
        Account displayAccount;
        for (int i=0;i<accountMap.size();i++){
            displayAccount = accountMap.get(i+1);
            System.out.println(displayAccount.getAccountNumber() + ":        " +
            displayAccount.customer.getCustomerName());
        }
    }

    /**
     * Overload method that displays all account except the one specified in account ID
     * @param userId Account ID of account to exclude from displayed list
     */
    public void displayAccounts(int userId){
        if (accountMap.size() < 1){
            System.err.println("No Accounts to display.");
            return;
        }
        System.out.println("Acc No.   Name   ");
        Account displayAccount;
        for (int i=0;i<accountMap.size();i++){
            displayAccount = accountMap.get(i+1);
            if(displayAccount.getAccountNumber()==userId){continue;}    // skip displaying input userId account
            System.out.println((i+1) + ":  " + displayAccount.getAccountNumber() + "    " +
            displayAccount.customer.getCustomerName());
        }
    }

    /**
     * Display all accounts and then prompts the user to enter their account credentials.
     * User has 3 attempts to succesfully log into the account.
     * Requires: accountMap and security object
     * @return Account ID of logged in account; else returns 0 on failure
     */
    public int accountLogin(){
        displayAccounts();  // account display lmao 
        int loginAccChoice = 0, attemptCount = 1;
        String password;

        while (attemptCount < 4){
            System.out.println("Enter User ID:");
            try {
                loginAccChoice = Integer.parseInt(input.nextLine());
                if(accountMap.containsKey(loginAccChoice)){
                    System.out.println("Enter password:");
                    password = input.nextLine();
                    if(secSession.validatePassword(loginAccChoice, password)){
                        break;
                    }
                } else {
                    System.out.println("ID entered was not in database.");
                }
            } catch (NumberFormatException e){
                System.out.println("Entered value was not an ID.");
            }    
            System.out.println("You have " + Integer.toString(3 - attemptCount) + " remaining attempts.");
            attemptCount++;
        }
        return loginAccChoice;
    }

    /** Handles the Account menu options
     * 1) Account login event
     * 2) Begin account action loop
     */
    public void accountMenu(){
        int accountId = 0, userChoice;    //account ID of logged in account
        boolean contAccount = true;
        
        accountId = accountLogin();   // user logs into account 
        if (accountId == 0){
            return;
        }

        while (contAccount) {
            System.out.println("----------------------------------------------");
            System.out.print(accountMap.get(accountId).customer.getCustomerName()+ "'s ");
            System.out.println("""
                            Account Actions:
                            (1): Deposit,Withdraw,Transfer
                            (2): View Credit Card Options
                            (3): View Insurance Options
                            (4): View Foreign Currency Options
                            (5): View Loan Options
                            (6): View Account Details
                            (7): Log out""");
            System.out.println("---------------------------------------------");
            userChoice = input.nextInt();
            double amt;
            int branchChoice;
            switch (userChoice) {
                case 1: // dep,with,trans method     
                    accProcess(accountId);                   
                    break;
                case 2: // Credit Card Options
                    ccProcess(accountId);
                    break;
                case 3:
                    insProcess(accountId);
                    break;
                case 4:
                    fxProcess(accountId);
                    break;
                case 5: 

                case 6:
                    accountMap.get(accountId).printAccountDetails();
                    break;
                case 7:
                    return; // exits the account process
                default:
                    System.out.println("Selected action is not in list.");
                    break;
            }
        }
    }

    public void accProcess(int accountId){
        double amt;
        int branchChoice;
        System.out.println("""
            Select your choice:
            (1): Deposit
            (2): Withdraw
            (3): Transfer""");

        switch (input.nextInt()) {
            case 1:
                System.out.print("Enter the amount you want to deposit: ");
                amt = input.nextDouble();
                System.out.println("Select branch you would like to withdraw from.");
                displayBranches();
                branchChoice = input.nextInt();
                accountMap.get(accountId).deposit(amt);
                branchMap.get(branchChoice).depositReserve(amt);
                break;

            case 2: // User wants to withdraw money from a branch
                double[] amountWithdraw = {30,50,100,200,250};
                System.out.println("""
                    Choose the amount of your withdrawal:
                    (1): $30
                    (2): $50
                    (3): $100
                    (4): $200
                    (5): $250
                    (6): Amount of choice
                    """);
                int withdrawChoice = input.nextInt();
                if (withdrawChoice > 0 && withdrawChoice < 6){
                    amt = amountWithdraw[withdrawChoice - 1];
                } else if (withdrawChoice == 6){
                    System.out.println("Enter amount to withdraw");
                    amt = input.nextDouble();
                } else { 
                    System.out.println("The choice you have entered is invalid.");
                    break;
                }

                System.out.println("Select branch you would like to withdraw from.");
                displayBranches();
                branchChoice = input.nextInt();
                accountMap.get(accountId).withdraw(amt);
                branchMap.get(branchChoice).withdrawReserve(amt);
                break;
            case 3: // User wants to transfer money to another account
                System.out.println("Select recipient's account ID.");
                displayAccounts(accountId); // displays recipient accounts
                int recepientId = input.nextInt();
                System.out.print("Enter the amount you want to transfer: ");
                amt = input.nextDouble();
                if (accountMap.containsKey(recepientId)){
                    accountMap.get(accountId).withdraw(amt);
                    accountMap.get(recepientId).deposit(amt);
                } else {
                    System.out.println("There is no such account in our bank.");
                }
                break;
            default:
                System.out.println("Not an option");
        }
    }
    
    public void ccProcess(int accountId){
        if (!accountMap.get(accountId).getCardFlag()){
            System.out.println("Your account does not have a credit card at the moment, would you like to add one?");
            System.out.println("""
                    (1) Yes
                    (2) No
                    """);
            if(input.nextInt() == 1){
                accountMap.get(accountId).addCard();
            } else {
                return;
            }
        } 
        else {
            double credBalance = accountMap.get(accountId).creditCard.getCreditBalance();
            double credLimit = accountMap.get(accountId).creditCard.getCreditLimit();
            System.out.println("Select your choice:\n(1): Pay outstanding balance: $" + 
            moneyFormat.format(credBalance) + 
            "\n(2): Increase transfer limit: $" + moneyFormat.format(credLimit));
            switch (input.nextInt()) {
                case 1:
                    if (credBalance>0){
                        System.out.println("Amount to pay: ");
                        double payAmount = input.nextDouble();
                        accountMap.get(accountId).makeCCPayment(payAmount);
                    }
                    break;
                case 2:
                    break;  
                default:
                    break;
            }
        }
    }

    public void fxProcess(int accountID){
        System.out.println("Select your choice.\n(1): Exchange SGD to JPY/USD \n(2): Exchange JPY/USD to SGD \n(3): View Foreign Balances");
        int firstChoice = input.nextInt();
        int secondChoice;
        double exchangeAmt;
        switch(firstChoice){
            case 1:
                accountMap.get(accountID).FXE.displayRates();
                System.out.println("Which Foreign currency would you like to exchange to?\n(1): JPY\n(2): USD");
                secondChoice = input.nextInt();
                System.out.println("Enter the amount you want to exchange:");
                exchangeAmt = input.nextDouble();
                accountMap.get(accountID).makeExchange(firstChoice,secondChoice,exchangeAmt);
                break;
            case 2:
                accountMap.get(accountID).printFXBalance();
                System.out.println("Which currency would you like to exchange to SGD?\n(1): JPY\n(2): USD");
                secondChoice = input.nextInt();
                System.out.println("Enter the amount you want to exchange:");
                exchangeAmt = input.nextDouble();
                accountMap.get(accountID).makeExchange(firstChoice,secondChoice,exchangeAmt);
                break;
            case 3:
                accountMap.get(accountID).printFXBalance();
                break;
            default:
                System.out.println("Invalid Entry!");
                break;

        }

    }

    public void loanProcess(int accountId){
        if(accountMap.get(accountId).getInsurFlag()){

        }
        else {
            System.out.println("Do you want to apply for Loan?");
        }

    }

    public void insProcess(int accountId){
        if(accountMap.get(accountId).getInsurFlag()) {
            System.out.println("""
            Select your choice:
            (1): Pay Monthly Bill
            (2): View Insurance Details""");

            switch (input.nextInt()) {
                case 1:
                    accountMap.get(accountId).payInsurancePremium();
                    break;
                case 2:
                    accountMap.get(accountId).insurance.displayPremiumBilling();
                    break;
                default:
                    System.out.println("Invalid Entry! Try again!");
                    break;
            }
        } else {
            System.out.println("Get insurance?");
            System.out.println("Which policy would you like to add to your account?");
            insuranceCatalog.printInsuranceCatalog(true);
            System.out.println("Policy: ");
            accountMap.get(accountId).addInsurance(insuranceCatalog.retrievePolicyMap(input.nextInt()));
            if(accountMap.get(accountId).getInsurFlag()){
                System.out.println("Succesfully added insurance policy to your account!");
            }
        }
    }
    
    public void displayBranches() {
        System.out.println("Branch Name    Branch Code    Current Reserves    Opening Hours");
        Branch selectedBranch;
        for (int i=1;i<=branchMap.size();i++){
            selectedBranch = branchMap.get(i);
            System.out.println(selectedBranch.getBranchName() + "    " + selectedBranch.getBranchCode() + 
            "    $" + moneyFormat.format(selectedBranch.getBranchReserve()) + "    " + selectedBranch.getOpeninghours() + "~" + 
            selectedBranch.getClosingHours());
        }
    }

    /**
     * 
     * @param listofAccs
     * @deprecated
     */
    public void DisplayBankUI(int[] listofAccs) {
        System.out.println("_____________________");
        System.out.println("|Welcome to " + bankName + " bank|");
        System.out.println("---------------------");
        int displayaccNum;
        String displayname = "";
        double displayamt;
        int displaybranchC;
        double displaycreditamt;

        System.out.println(" Acc No.  Name    Bank Balance   Branch Code    Credit Bal");

        for(int i = 1; i<=listofAccs.length; i++)   {
            Account account = new Account(i);
            displayaccNum = account.getAccountNumber();
            displayname = account.customer.getCustomerName();
            displayamt = account.checkBalance();
            displaybranchC = account.getBranchCreated();
            displaycreditamt = account.creditCard.getCreditBalance();
            System.out.println(i + ":   " + displayaccNum + "    " + displayname + "    " + displayamt + "        " + displaybranchC + "            " + displaycreditamt);
        }
        System.out.print("""                
                Enter your input:
                (1): Login to an Account
                (2): View all Branches
                (3): View all Insurances
                """);

    }

    /*public void ProcessTransactions(Account account, int choice, int[] acclist) {
        // Process Transactions here.
        Scanner scanner = new Scanner(System.in);
        if(choice == 1){
            System.out.println("""
                        Select your choice:
                        (1): Deposit
                        (2): Withdraw
                        (3): Transfer""");
            int nextChoice = scanner.nextInt();
            scanner.nextLine();
            if(nextChoice == 1){
                System.out.print("Enter the amount you want to deposit: ");
                double amt = scanner.nextDouble();
                account.deposit(amt);
            }
            else if(nextChoice == 2){
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
            } else if(nextChoice == 3){
                System.out.println("Enter the account to transfer to: ");
                int accChoice = scanner.nextInt();
                scanner.nextLine();

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
                    System.out.println("Transferring you to another page...");
                }
            }
        }
        else if(choice == 2){
            if(!account.getCardFlag())
            {
                System.out.println("You do not have a credit card! Would you like to apply one now?");
            }
            else{
                System.out.println("""
                        Select your choice:
                        (1): Pay outstanding balance
                        (2): Increase Transfer Limit""");
                int nextChoice = scanner.nextInt();
                scanner.nextLine();

                if(nextChoice == 1){
                    double creditbal = account.creditCard.getCreditBalance();
                    System.out.println("Outstanding Credit Balance: " + creditbal);
                    if(creditbal > 0)
                    {
                        System.out.print("Enter amount to pay: ");
                        double payCreditbal = scanner.nextDouble();
                        scanner.nextLine();
                        account.makeCCPayment(payCreditbal);
                    }
                    else{
                        System.out.println("You do not have any credit balance!");
                    }
                }
                else if(nextChoice == 2){
                    System.out.print("Enter new credit limit: ");
                    double newcreditLimit = scanner.nextDouble();
                    scanner.nextLine();
                    account.creditCard.setCreditLimit(newcreditLimit);
                }
            }
        }
        else if(choice == 3){
            if(!account.getInsurFlag())
            {
                System.out.println("You do not have an Insurance plan! Would you like to apply one now?");
            }
            else{
                System.out.println("""
                        Select your choice:
                        (1): View current plan
                        (2): Pay outstanding premium""");
                int nextChoice = scanner.nextInt();
                scanner.nextLine();

                if(nextChoice == 1){
                    account.insurance.printInsuranceDetail();
                }
                else if(nextChoice == 2){
                    double premiumBal = account.insurance.getMonthlyPremium();
                    System.out.println("Current Outstanding Balance: " + premiumBal);
                    account.payInsurancePremium();
                }
            }
        }
        else if(choice == 4){
            System.out.println("""
                        Select your choice:
                        (1): Convert SGD to Foreign
                        (2): Convert Foreign to SGD
                        (3): View all Foreign balances""");

            int foreignChoice = scanner.nextInt();
            scanner.nextLine();

            if(foreignChoice == 1){
               // account.foreignX.viewCurrencyRates();
                System.out.println("Enter an amount you want to convert");
                int SGDamount = scanner.nextInt();
                scanner.nextLine();
                double currentAmount = account.checkBalance();
                if(SGDamount <= currentAmount)
                {
                    System.out.println("""
                Which country you want to exchange to?
                (1): United States (USD)
                (2): Japan (JPY)
                (3): Malaysia (MYR)
                (4): Australia (AUD)
                (5): United Kingdom (GBP)""");
                    int exchangeChoice = scanner.nextInt();
                    //account.foreignX.exchangeToForeign(SGDamount,exchangeChoice);
                }
                else {
                    System.out.println("Insufficient balance!");
                }

            }
            else if(foreignChoice == 2){
                //account.foreignX.viewCurrencyRates();
                System.out.println("Enter an amount you want to convert");
                int foreignamount = scanner.nextInt();
                scanner.nextLine();
                //account.foreignX.exchangeToSGD(foreignamount);

            }
            else if(foreignChoice == 3){
                //account.foreignX.viewCurrencyRates();
            }
        }
        scanner.close();
    }
     */

    /*public static void main1(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //accounts = new ArrayList<>();
        Bank myBank = new Bank();

        String csvFile = "data\\Bank.csv"; // Path to your CSV file
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
                    bankName = (values[0].trim());
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

        int[] acclist = new int[Accountlist.length];    //Creating an int array for acc numbers
        for (int i = 0; i < Accountlist.length; i++) {  //for loop to iterate through the array
            acclist[i] = Integer.parseInt(Accountlist[i]); //Changing the elements in the array to int
        }

        int[] branchcodelist = new int[BranchClist.length]; //Creating an int array for branch codes
        for (int i = 0; i < BranchClist.length; i++) {      //for loop to iterate through the array
            branchcodelist[i] = Integer.parseInt(BranchClist[i]);   //Changing the elements in the array to int
        }

        boolean continueRunning = true;
        while (continueRunning) {


            myBank.DisplayBankUI(acclist);      //Calling the Display UI function for the layout
            int firstChoice = scanner.nextInt();
            scanner.nextLine();

            if (firstChoice == 1) {
                System.out.println("List of accounts");
                System.out.println(Arrays.toString(Accountlist));
                System.out.print("Enter account no. to login: ");
                int userAcc = scanner.nextInt();
                scanner.nextLine();
                boolean userExist = false;

                for (int i = 0; i < acclist.length; i++) {
                    if (userAcc == acclist[i]) {
                        userExist = true;
                    }
                }

                if (userExist) {
                    Account myAccount = new Account(userAcc);
                    System.out.println("\nWelcome " + myAccount.customer.getCustomerName() + "!");
                    myAccount.printAccountDetails();
                    System.out.println("""
                            Select your choice:
                            (1): Deposit,Withdraw,Transfer
                            (2): View Credit Card Options
                            (3): View Insurance Options
                            (4): View Foreign Currency Options""");
                    int userChoice = scanner.nextInt();
                    scanner.nextLine();
                    myBank.ProcessTransactions(myAccount, userChoice, acclist);
                } else {
                    System.out.println("Sorry, this account does not exist!");
                }
            } else if (firstChoice == 2) {
                System.out.println(Arrays.toString(BranchNlist));
                System.out.println(Arrays.toString(branchcodelist));
                System.out.print("Enter a branch code to view more details:");
                int branchSelect = scanner.nextInt();
                scanner.nextLine();
                boolean branchExist = false;
                for (int i = 0; i < branchcodelist.length; i++) {
                    if (branchSelect == branchcodelist[i]) {
                        branchExist = true;
                    }
                }

                if (branchExist) {
                    branches = new Branch(branchSelect);
                    branches.printBranchDetails();
                } else {
                    System.out.println("You have entered an invalid branch!");
                }
            } else if (firstChoice == 3) {
                System.out.println(Arrays.toString(Insurancelist));
                System.out.println("Enter a policy number to view more details:");
                String policySelect = scanner.nextLine();
                boolean insuranceExist = false;
                for (int i = 0; i < Insurancelist.length; i++) {
                    if (policySelect.equals(Insurancelist[i])) {
                        insuranceExist = true;
                    }
                }
                if (insuranceExist) {
                    insurancePolicies = new Insurance(policySelect);
                    insurancePolicies.printInsuranceDetail();
                } else {
                    System.out.println("You have entered an invalid insurance policy!");
                }
            }
            System.out.print("Do you want to continue? (yes/no): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();

            if(continueChoice.equals("no")){
                System.out.println("Exiting " + bankName + " Bank System...");
            }

            continueRunning = continueChoice.equals("yes");
        }
        scanner.close();
    } */
    

    public static void main(String[] args) {
        int menuChoice;
        boolean endSession = false;
        Bank bankSession = new Bank();

        bankSession.bankWelcomeMessage();  // Displays welcome message
        while (endSession!=true) {
            menuChoice = bankSession.bankMainMenuSelect();
            switch(menuChoice) {
                case 1: 
                    bankSession.accountMenu();
                    break;
                case 2:
                    bankSession.displayBranches();
                    break;

                case 3: // Display Insurance Policies
                    bankSession.insuranceCatalog.printInsuranceCatalog();
                    break;

                default:
                    System.out.println("Thank you for using the bank application.");
                    endSession = true;
                    break;
            }
        }
    }
}
