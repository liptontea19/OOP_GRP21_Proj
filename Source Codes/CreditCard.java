package CreditCard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard {
    private double balance = 0, creditLimit;
    private String cardHolderName, cardNumber;
    private Integer accountID;
    private LocalDate expiryDate;
    private List<String> transactionHistory;
    private ArrayList<ArrayList<String>> creditDetails;

    public CreditCard(String cardHolderName, Integer accountID, String cardNumber, double creditLimit){
        this.cardHolderName = cardHolderName;
        this.accountID = accountID;
        this.cardNumber = cardNumber;
        LocalDate currentDate = LocalDate.now(); // LocalDate is to retrieve year-month-day
        this.expiryDate = currentDate.plusYears(5);  // Expiry set to current date + 5 years
        this.creditLimit = creditLimit;
        this.transactionHistory = new ArrayList<>();
    }


    public CreditCard(String filePath) {
        creditDetails = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }
                String[] parts = line.split(","); // Split by comma since it's CSV
                ArrayList<String> creditCard = new ArrayList<>();
                for (String part : parts) {
                    creditCard.add(part.trim());
                }
                creditDetails.add(creditCard);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Testing Purposes
    public static void main(String[] args){

    }

     */


    public void chargeCredit(double amount){
        if (LocalDate.now().isAfter(expiryDate)){
            System.out.println("Transaction declined. The credit card is expired.");
        }else if (amount <=0){
            System.out.println("Invalid purchase amount.");
        }else if (amount > availableCredit()){
            System.out.println("Purchase exceeds credit limit.");
        }else {
            balance += amount;
            System.out.println("Purchase of " + amount + " made successfully!");
            transactionHistory.add("[" + LocalDate.now() + "] Charge of " + amount + " added to balance");
        }
    }

    public void payBill(double amount){
        if (amount <=0 || amount > balance){
            System.out.println("Invalid payment amount.");
        } else {
            balance -= amount;
            System.out.println("Credit card bill of " + amount + " made successfully");
            transactionHistory.add("[" + LocalDate.now() + "] Payment of " + amount + " deducted from balance");
        }
    }
    public double availableCredit(){
        return creditLimit - balance;
    }

    public void printTransactionHistory(){
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory){
            System.out.println(transaction);
        }
    }

    public double getCreditBalance(){
        return balance;
    }

    public LocalDate getCardExpiry(){
        return expiryDate;
    }

    public String getCardHolderName(){
        return cardHolderName;
    }

    public double getCreditLimit(){
        return creditLimit;
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public Integer getAccountNumber(){
        return accountID;
    }

    public void setCardExpiry(LocalDate date){
        this.expiryDate = date;
    }

    public void setCardHolderName(String name){
        this.cardHolderName = name;
    }

    public void setCreditLimit(double limit){
        this.creditLimit = limit;
    }

    public void setCardNumber(String number){
        this.cardNumber = number;
    }

    public void setAccountNumber(Integer accountID){
        this.accountID = accountID;
    }

    public void printCreditCardDetails(){
        System.out.println("Balance: " + balance
                + "\nCard Holder Name: " + cardHolderName
                + "\nCard Number: " + cardNumber
                + "\nCredit Limit: " + creditLimit
                + "\nExpiry Date: " + expiryDate
                + "\nAccount ID: " + accountID);
    }


}
