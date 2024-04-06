package bank;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.FailedLoginException;

import account.Account;
import account.FXAccount;
import account.Insurance;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class Bank {

    //private static List<Account> accounts;
    /** Name of Bank */
    private static String bankName;
    private BankSecurity secSession;
    private DecimalFormat moneyFormat = new DecimalFormat("#,###.00");
    private Scanner input = new Scanner(System.in);

    /** Insurance Catalog Class Object that is used for insurance policy display*/
    private InsuranceCatalog insuranceCatalog;
    /** Maps all brank branches. Key: Normal Integer Index Val: Branch Object */
    private HashMap<Integer, Branch> branchMap;
    /** Maps all user accounts. Key: Account ID, Value: Account Object connected to Account ID */
    private HashMap<Integer, Account> accountMap;

    /** Maps all user Foreign Exchange accounts. Key: Account ID, Value: FXAccount Object connected to Account ID */
    private HashMap<Integer, FXAccount> fxMap;

    public Bank(){
        //accounts = new ArrayList<>();
        accountMap = new HashMap<>();
        branchMap = new HashMap<>();
        fxMap = new HashMap<>();
        insuranceCatalog = new InsuranceCatalog();
        secSession = new BankSecurity("data\\UserPass.csv");
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
            makeFXAccounts(accountCSVLine); // creates fxMap
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

    public void makeFXAccounts(String[] accIdStrings){
        for(int i=0;i<accIdStrings.length;i++){
            fxMap.put(i+1, new FXAccount(Integer.parseInt(accIdStrings[i])));
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
     * @deprecated PASS AWAY LIAO USE THE NEW ONE I MADE {@link BankSecurity#accountLogin(Scanner)}
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
                    //if(secSession.validatePassword(loginAccChoice, password)){
                    //    break;
                    //}
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
        
        displayAccounts();
        try {
            secSession.accountLogin(input);
        } catch (FailedLoginException e){
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
                    loanProcess(accountId);
                    break;
                case 6:
                    accountMap.get(accountId).printAccountDetails();
                    break;
                case 7:
                    return; // exits the account process and returns user to log in area
                default:
                    System.out.println("Selected action is not in list.");
                    break;
            }
        }
    }

    public void accProcess(int accountId){
        int branchID;
        double totalBalance;
        double totalReserves;
        double amt;
        int branchChoice;
        System.out.println("""
            Select your choice:
            (1): Deposit
            (2): Withdraw
            (3): Transfer
            (4): Return""");

        switch (input.nextInt()) {
            case 1:
                System.out.print("Enter the amount you want to deposit: ");
                amt = input.nextDouble();
                System.out.println("Select branch you would like to deposit to.");
                displayBranches();
                branchChoice = input.nextInt();
                accountMap.get(accountId).deposit(amt);
                branchMap.get(branchChoice).depositReserve(amt);

                branchID = branchMap.get(branchChoice).getBranchCode();

                totalBalance = accountMap.get(accountId).checkBalance();
                totalReserves = branchMap.get(branchChoice).getBranchReserve();

                editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                editCSV("data/Branch.csv",branchID,"BranchReserve",totalReserves);
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

                branchID = branchMap.get(branchChoice).getBranchCode();

                totalBalance = accountMap.get(accountId).checkBalance();
                totalReserves = branchMap.get(branchChoice).getBranchReserve();

                editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                editCSV("data/Branch.csv",branchID,"BranchReserve",totalReserves);
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

                    totalBalance = accountMap.get(accountId).checkBalance();
                    double recepientBalance = accountMap.get(recepientId).checkBalance();

                    editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                    editCSV("data/Account.csv",recepientId,"Balance",recepientBalance);
                } else {
                    System.out.println("There is no such account in our bank.");
                }
                break;
            case 4:
                return;
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
            "\n(2): Change transfer limit: $" + moneyFormat.format(credLimit) + "\n(3): Return");
            switch (input.nextInt()) {
                case 1:
                    if (credBalance>0){
                        System.out.println("Amount to pay: ");
                        double payAmount = input.nextDouble();
                        accountMap.get(accountId).makeCCPayment(payAmount);
                    }
                    break;
                case 2:
                    System.out.println("New Transfer Limit: ");
                    double newLimit = input.nextDouble();
                    accountMap.get(accountId).setTransferLimit(newLimit);
                    double limitBalance = accountMap.get(accountId).checkTransferLimit();
                    long CreditId = accountMap.get(accountId).creditCard.getCardNumber();
                    editCSV("data/CreditCard.csv",CreditId,"CreditLimit",limitBalance);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Entry!");
                    break;
            }
        }
    }

    public void fxProcess(int accountId){
        System.out.println("Select your choice.\n(1): Exchange SGD to JPY/USD \n(2): Exchange JPY/USD to SGD \n(3): View Foreign Balances\n(4): Return");
        int firstChoice = input.nextInt();
        int secondChoice;
        double totalBalance, totalJPYBalance, totalUSDBalance;
        double exchangeAmt;
        switch(firstChoice){
            case 1:
                fxMap.get(accountId).FXE.displayRates();
                System.out.println("Which Foreign currency would you like to exchange to?\n(1): JPY\n(2): USD");
                secondChoice = input.nextInt();
                System.out.println("Enter the amount you want to exchange:");
                exchangeAmt = input.nextDouble();

                fxMap.get(accountId).makeExchange(firstChoice,secondChoice,exchangeAmt);
                accountMap.get(accountId).makeExchange(firstChoice,secondChoice,exchangeAmt);
                totalBalance = accountMap.get(accountId).checkBalance();
                totalJPYBalance = fxMap.get(accountId).getJPYBalance();
                totalUSDBalance = fxMap.get(accountId).getUSDBalance();


                editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                editCSV("data/FXacc.csv",accountId,"JPYBal",totalJPYBalance);
                editCSV("data/FXacc.csv",accountId,"USDBal",totalUSDBalance);

                break;
            case 2:
                fxMap.get(accountId).printFXBalance();
                System.out.println("Which currency would you like to exchange to SGD?\n(1): JPY\n(2): USD");
                secondChoice = input.nextInt();
                System.out.println("Enter the amount you want to exchange:");
                exchangeAmt = input.nextDouble();

                fxMap.get(accountId).makeExchange(firstChoice,secondChoice,exchangeAmt);
                accountMap.get(accountId).makeExchange(firstChoice,secondChoice,exchangeAmt);

                totalBalance = accountMap.get(accountId).checkBalance();
                totalJPYBalance = fxMap.get(accountId).getJPYBalance();
                totalUSDBalance = fxMap.get(accountId).getUSDBalance();

                editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                editCSV("data/FXacc.csv",accountId,"JPYBal",totalJPYBalance);
                editCSV("data/FXacc.csv",accountId,"USDBal",totalUSDBalance);

                break;
            case 3:
                fxMap.get(accountId).printFXBalance();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid Entry!");
                break;

        }
    }

    public void loanProcess(int accountId){
        if(accountMap.get(accountId).getLoanFlag()){
            System.out.println("""
                    (1): Apply Loan
                    (2): Repay Loan
                    (3): View Loan Details
                    (4): Return
                    """);
            switch(input.nextInt()){
                case 1:
                    accountMap.get(accountId).customer.applyForLoan(7000,5.0,12);

                    break;
                case 2:
                    accountMap.get(accountId).repayLoan(input);
                    break;
                case 3:
                    accountMap.get(accountId).customer.printAllLoans();
                    break;
                case 4:
                    return;
                default:
                    break;
            }

        }
        else {
            System.out.println("Do you want to apply for Loan?\n(1) Yes\n(2) No");
            switch(input.nextInt()){
                case 1:
                    accountMap.get(accountId).customer.applyForLoan(7000,5.0,12);
                    accountMap.get(accountId).customer.printAllLoans();
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invalid Entry! Try again!");
                    break;
            }

        }
    }

    public void insProcess(int accountId){
        double totalBalance;
        if(accountMap.get(accountId).getInsurFlag()) {
            System.out.println("""
            Select your choice:
            (1): Pay Monthly Bill
            (2): View Insurance Details
            (3): Return""");

            switch (input.nextInt()) {
                case 1:
                    accountMap.get(accountId).payInsurancePremium();
                    totalBalance = accountMap.get(accountId).checkBalance();
                    editCSV("data/Account.csv",accountId,"Balance",totalBalance);
                    editCSV("data/InsuranceAccounts.csv",accountId,"Premium Paid","Yes");
                    break;
                case 2:
                    accountMap.get(accountId).insurance.displayPremiumBilling();
                    break;
                case 3:
                    return;
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
                System.out.println("Successfully added insurance policy to your account!");
            }
        }
    }

    public static void editCSV(String filename, int ID, String columnHeader, double newValue) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("data/temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            String headers = reader.readLine();
            writer.write(headers);
            writer.newLine();

            String[] headerArray = headers.split(",");
            int columnToEdit = -1;
            for (int i = 0; i < headerArray.length; i++) {
                if (headerArray[i].equals(columnHeader)) {
                    columnToEdit = i;
                    break;
                }
            }
            if (columnToEdit == -1) {
                System.err.println("Column header not found: " + columnHeader);
                reader.close();
                writer.close();
                return;
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > columnToEdit && Integer.parseInt(parts[0]) == ID) {
                    // Edit the specific column if the account ID matches
                    parts[columnToEdit] = Double.toString(newValue);
                    line = String.join(",", parts);
                }
                writer.write(line + "\n");
            }
            // Rename temp file to original file name
            tempFile.renameTo(inputFile);
            writer.close();
            reader.close();
            if (!inputFile.delete()) {
                System.err.println("Failed to delete original file");
            }
            // Rename the temp file to replace the original CSV file
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp file to original file");
            }
        } catch (IOException e) {
            System.err.println("Error editing CSV: " + e.getMessage());
        }
    }

    public static void editCSV(String filename, long ID, String columnHeader, double newValue) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("data/temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            String headers = reader.readLine();
            writer.write(headers);
            writer.newLine();

            String[] headerArray = headers.split(",");
            int columnToEdit = -1;
            for (int i = 0; i < headerArray.length; i++) {
                if (headerArray[i].equals(columnHeader)) {
                    columnToEdit = i;
                    break;
                }
            }
            if (columnToEdit == -1) {
                System.err.println("Column header not found: " + columnHeader);
                reader.close();
                writer.close();
                return;
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > columnToEdit && Long.parseLong(parts[0]) == ID) {
                    // Edit the specific column if the account ID matches
                    parts[columnToEdit] = Double.toString(newValue);
                    line = String.join(",", parts);
                }
                writer.write(line + "\n");
            }
            // Rename temp file to original file name
            tempFile.renameTo(inputFile);
            writer.close();
            reader.close();
            if (!inputFile.delete()) {
                System.err.println("Failed to delete original file");
            }
            // Rename the temp file to replace the original CSV file
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp file to original file");
            }
        } catch (IOException e) {
            System.err.println("Error editing CSV: " + e.getMessage());
        }
    }

    public static void editCSV(String filename, int ID, String columnHeader, String newValue) {
        try {
            File inputFile = new File(filename);
            File tempFile = new File("data/temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            String headers = reader.readLine();
            writer.write(headers);
            writer.newLine();

            String[] headerArray = headers.split(",");
            int columnToEdit = -1;
            for (int i = 0; i < headerArray.length; i++) {
                if (headerArray[i].equals(columnHeader)) {
                    columnToEdit = i;
                    break;
                }
            }
            if (columnToEdit == -1) {
                System.err.println("Column header not found: " + columnHeader);
                reader.close();
                writer.close();
                return;
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > columnToEdit && Integer.parseInt(parts[0]) == ID) {
                    // Edit the specific column if the account ID matches
                    parts[columnToEdit] = newValue;
                    line = String.join(",", parts);
                }
                writer.write(line + "\n");
            }
            // Rename temp file to original file name
            tempFile.renameTo(inputFile);
            writer.close();
            reader.close();
            if (!inputFile.delete()) {
                System.err.println("Failed to delete original file");
            }
            // Rename the temp file to replace the original CSV file
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp file to original file");
            }
        } catch (IOException e) {
            System.err.println("Error editing CSV: " + e.getMessage());
        }
    }
    
    public void displayBranches() {
        System.out.println("Branch Name    Branch Code    Current Reserves    Opening Hours");
        Branch selectedBranch;
        for (int i=1;i<=branchMap.size();i++){
            selectedBranch = branchMap.get(i);
            System.out.println(i + ": " + selectedBranch.getBranchName() + "    " + selectedBranch.getBranchCode() +
            "           $" + moneyFormat.format(selectedBranch.getBranchReserve()) + "          " + selectedBranch.getOpeninghours() + "~" +
            selectedBranch.getClosingHours());
        }
    } 

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
