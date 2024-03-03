package CreditCard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditCard {
    private double balance = 0, creditLimit;
    private String customerName;
    private int accountID;
    private long cardNumber;
    private LocalDate expiryDate;
    private List<String> transactionHistory;
    private ArrayList<ArrayList<String>> creditDetails;
    private static List<CreditCard> allCreditCards = new ArrayList<>();


    public CreditCard(String customerName, int accountID, long cardNumber, double creditLimit){
        this.customerName = customerName;
        this.accountID = accountID;
        this.cardNumber = cardNumber;
        LocalDate currentDate = LocalDate.now(); // LocalDate is to retrieve year-month-day
        this.expiryDate = currentDate.plusYears(5);  // Expiry set to current date + 5 years
        this.creditLimit = creditLimit;
        this.transactionHistory = new ArrayList<>();
        allCreditCards.add(this);
    }


    public CreditCard(long cardNumber) {
        this.cardNumber = cardNumber;
        this.transactionHistory = new ArrayList<>();
        creditDetails = new ArrayList<>();
        allCreditCards.add(this);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("CreditCard/CreditCard.csv"));
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiryDate = LocalDate.parse(creditCard.get(3), formatter);

                creditDetails.add(creditCard);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ArrayList<String> creditCard : creditDetails){
            if (Long.parseLong(creditCard.get(0)) == cardNumber){
                setCardNumber(Long.parseLong(creditCard.get(0)));
                setCustomerName(creditCard.get(1));
                setCreditLimit(Double.parseDouble(creditCard.get(2)));
                setCardExpiry(LocalDate.parse(creditCard.get(3)));
                setAccountID(Integer.parseInt(creditCard.get(4)));
                setBalance(Double.parseDouble(creditCard.get(5)));
            }
        }
    }

    //Testing Purposes
    public static void main(String[] args){
        CreditCard creditCard = new CreditCard(3108398698038530L);
        CreditCard creditCard2 = new CreditCard(8108372529219270L);
        creditCard.chargeCredit(2000);
        creditCard.chargeCredit(5000);
        creditCard.payBill(1900);
        System.out.println(creditCard.availableCredit());
        creditCard.printTransactionHistory();
        creditCard.printAllCreditCards(3);
    }


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

    public static void printAllCreditCards(int accountID){
        System.out.println("\n List of all Credit Cards for accountID " + accountID + ": \n");
        for (CreditCard card : allCreditCards){
            if (card.getAccountID() == accountID){
                card.printCreditCardDetails();
                System.out.println();
            }    
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

    public String getCustomerName(){
        return customerName;
    }

    public double getCreditLimit(){
        return creditLimit;
    }

    public double getCardNumber(){
        return cardNumber;
    }

    public Integer getAccountID(){
        return accountID;
    }

    public void setCreditLimit(double creditLimit){
        if (creditLimit > 0 && creditLimit <= 10000){
            this.creditLimit = creditLimit;
            System.out.println("Credit limit has been changed to $" + Double.toString(creditLimit));
        }else {
            System.out.println("The transfer limit of $" + Double.toString(creditLimit) + " you have entered is invalid.");
        }
    }

    public void setCardExpiry(LocalDate date){
        this.expiryDate = date;
    }


    public void setCustomerName(String name){
        this.customerName = name;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public void setCreditLimit(double limit){
        this.creditLimit = limit;
    }

    public void setCardNumber(long number){
        this.cardNumber = number;
    }

    public void setAccountID(Integer accountID){
        this.accountID = accountID;
    }

    public void printCreditCardDetails(){
        System.out.println("Card Number: " + cardNumber
                + "\nCustomer Name: " + customerName
                + "\nCredit Limit: " + creditLimit
                + "\nExpiry Date: " + expiryDate
                + "\nAccount ID: " + accountID
                + "\nBalance " + balance);
    }


}
