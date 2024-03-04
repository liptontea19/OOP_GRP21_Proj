package CreditCard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CreditCard class is used to store information of a customer's credit card based on their card number.
 * The class has methods which work together with the Account it will be attached to, to perform transactions and other functions of a credit card.
 * */
public class CreditCard {
    private double balance = 0, creditLimit;
    private String customerName;
    private int accountID;
    private long cardNumber;
    private LocalDate expiryDate;
    private List<String> transactionHistory;
    private static List<CreditCard> allCreditCards = new ArrayList<>();

    /**Generic class constructor for class private attributes
     * @param customerName Name of Customer
     * @param accountID The unique identifier for each account
     * @param cardNumber The card number assigned to the credit card
     * @param creditLimit The limit the credit card can charge. This does not change.
     */

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

    /**
     * Special constructor which fetches the information from CreditCard.csv and places them into the creditCard arraylist.
     * It reads the data from the CreditCard.csv containing CardNumber, CustomerName, CreditLimit, ExpiryDate, AccountID and Balance.
     * Use to perform functionalities of a credit card class based on the credit card number.
     * For loop is used for assigning data from csv into the local variable, to retrieve the data in printCreditCardDetails method.
     * */
    public CreditCard(long cardNumber) {
        this.cardNumber = cardNumber;
        this.transactionHistory = new ArrayList<>();
        ArrayList<ArrayList<String>> creditDetails = new ArrayList<>();
        allCreditCards.add(this);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("CreditCard/CreditCard.csv")); // data/Branch.csv
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
                setCreditLimitQuiet(Double.parseDouble(creditCard.get(2)));
                setCardExpiry(LocalDate.parse(creditCard.get(3)));
                setAccountID(Integer.parseInt(creditCard.get(4)));
                setBalance(Double.parseDouble(creditCard.get(5)));
            }
        }
    }

    /** Main class is used for testing purposes
    *   to show the functionalities of the CreditCard Class
    * */
    public static void main(String[] args){
        CreditCard creditCard = new CreditCard(3108398698038530L);
        creditCard.chargeCredit(2000);
        creditCard.chargeCredit(5000);
        creditCard.payBill(1900);
        System.out.println(creditCard.availableCredit());
        creditCard.printTransactionHistory();
        creditCard.printAllCreditCards(1);
    }

    /**
     * chargeCredit checks for the amount that will be charged into the credit card.
     * It checks for 3 things prior to successful charging of Credit Card:
     * if the credit card is expired; if the given amount is a valid number; if it has exceeded the credit limit.
     * If the amount for chargeCredit passes these checks, it can successfully charge the credit card.
     * */
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

    /**
     * payBill checks if it is a valid amount
     * before proceeding to pay off the credit card's balance.
     * This method is used in contrast to chargeCredit:
     * when charging the credit card, one has to pay off his or her balance as well.
     * */
    public void payBill(double amount){
        if (amount <=0 || amount > balance){
            System.out.println("Invalid payment amount.");
        } else {
            balance -= amount;
            System.out.println("Credit card bill of " + amount + " made successfully!");
            transactionHistory.add("[" + LocalDate.now() + "] Payment of " + amount + " deducted from balance");
        }
    }

    /**
     * printAllCreditCards retrieves the information
     * of all the credit cards of a certain account ID.
     * */
    public static void printAllCreditCards(int accountID){
        System.out.println("\n List of all Credit Cards for accountID " + accountID + ": \n");
        for (CreditCard card : allCreditCards){
            if (card.getAccountID() == accountID){
                card.printCreditCardDetails();
                System.out.println();
            }
        }
    }

    /**
     * Method to retrieve current available credit
     * */
    public double availableCredit(){
        return creditLimit - balance;
    }

    /**
     * printTransactionHistory shows the transaction history when the
     * credit card is charged or when the balance is paid.
     * */
    public void printTransactionHistory(){
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory){
            System.out.println(transaction);
        }
    }

    /**
     * Returns the balance attribute as a double value
     * @return double value of credit card balance to be paid.
     * */
    public double getCreditBalance(){
        return balance;
    }

    /**
     * Returns the expiryDate attribute as a LocalDate value
     * @return LocalDate value of credit card's expiry date.
     * */
    public LocalDate getCardExpiry(){
        return expiryDate;
    }

    /**
     * Returns the customer name attribute as a String value
     * @return String value of credit card's expiry date.
     * */
    public String getCustomerName(){
        return customerName;
    }

    /**
     * Returns the creditLimit attribute as a double value
     * @return double value of credit card's limit.
     * */
    public double getCreditLimit(){
        return creditLimit;
    }

    /**
     * Returns the cardNumber attribute as a long value
     * @return long value of the credit card number.
     * */
    public long getCardNumber(){
        return cardNumber;
    }

    /**
     * Returns the accountID attribute as an integer
     * @return integer value of the account ID for credit card.
     * */
    public Integer getAccountID(){
        return accountID;
    }

    /**
     * This method is to set the creditLimit of a credit card.
     * The limit can only be set between 1 and 100000.
     * If the limit is set below 1 or above 100000, it will be invalid.
     * */
    public void setCreditLimit(double creditLimit){
        if (creditLimit > 0 && creditLimit <= 100000){
            this.creditLimit = creditLimit;
            System.out.println("Credit limit has been changed to $" + Double.toString(creditLimit));
        }else {
            System.out.println("The credit limit of $" + Double.toString(creditLimit) + " you have entered is invalid.");
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

    public void setCreditLimitQuiet(double creditLimit){
        this.creditLimit = creditLimit;
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
