package account;
//package CreditCard;

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
 */
public class CreditCard {
    private double balance = 0, creditLimit;
    private String customerName;
    private int accountID;
    private long cardNumber;
    private LocalDate expiryDate;
    private List<String> transactionHistory;
    private static List<CreditCard> allCreditCards = new ArrayList<>();

    /**
     * Generic class constructor for class private attributes
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
     * Used to perform functionalities of a credit card class based on the credit card number.
     * For loop is used for assigning data from csv into the local variable, to retrieve the data in printCreditCardDetails method.
     * @param cardNumber specifies the specific creditCard to retrieve
     */
    public CreditCard(long cardNumber) {
        this.cardNumber = cardNumber;
        this.transactionHistory = new ArrayList<>();
        ArrayList<ArrayList<String>> creditDetails = new ArrayList<>();
        allCreditCards.add(this);
        try (BufferedReader reader = new BufferedReader(new FileReader("data\\CreditCard.csv"))){
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
            //reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ArrayList<String> creditCard : creditDetails){
            if (Long.parseLong(creditCard.get(0)) == cardNumber){
                setCardNumber(Long.parseLong(creditCard.get(0)));
                setCustomerName(creditCard.get(1));
                setCreditLimit(Double.parseDouble(creditCard.get(2)), true);
                setCardExpiry(LocalDate.parse(creditCard.get(3)));
                setAccountID(Integer.parseInt(creditCard.get(4)));
                setBalance(Double.parseDouble(creditCard.get(5)));
            }
        }
    }

    /** 
     * Main class is used for testing purposes
     *   to show the functionalities of the CreditCard Class
     */
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
     * @param amount The amount to charge to the credit card
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

    /**
     * payBill checks if it is a valid amount
     * before proceeding to pay off the credit card's balance.
     * This method is used in contrast to chargeCredit:
     * when charging the credit card, one has to pay off his or her balance as well.
     * @param amount The amount of the credit card balance to pay off
     */
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
     * @param accountID The accountID of Credit Cards to print
     */
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
     * Method to calculate and retrieve the current available credit of a credit card.
     * @return The available credit, computed as the difference between creditLimit and current balance.
     */
    public double availableCredit(){
        return creditLimit - balance;
    }

    /**
     * printTransactionHistory shows the transaction history of the
     * credit card when it was charged or when the balance was paid off.
     */
    public void printTransactionHistory(){
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory){
            System.out.println(transaction);
        }
    }

    /**
     * Returns the balance attribute as a double value
     * @return balance The amount owed that needs to be paid off.
     */
    public double getCreditBalance(){
        return balance;
    }

    /**
     * Returns the expiryDate attribute as a LocalDate value
     * @return LocalDate value of credit card's expiry date.
     */
    public LocalDate getCardExpiry(){
        return expiryDate;
    }

    /**
     * Returns the customer name attribute as a String value
     * @return String value of credit card's Customer name.
     */
    public String getCustomerName(){
        return customerName;
    }

    /**
     * Returns the creditLimit attribute as a double value
     * @return double value of credit card's limit.
     */
    public double getCreditLimit(){
        return creditLimit;
    }

    /**
     * Returns the cardNumber attribute as a long value
     * @return long value of the credit card number.
     */
    public long getCardNumber(){
        return cardNumber;
    }

    /**
     * Returns the accountID attribute as an integer
     * @return integer value of the account ID linked to the credit card.
     */
    public Integer getAccountID(){
        return accountID;
    }

    /**
     * This method sets the creditLimit of a credit card.
     * The limit can only be set between 1 and 100000.
     * If the limit is set below 1 or above 100000, it will be invalid.
     * Message will be printed to prompt user
     * @param creditLimit the new credit limit to set to
     */
    public void setCreditLimit(double creditLimit) {
        if (creditLimit > 0 && creditLimit <= 100000) {
            this.creditLimit = creditLimit;
            System.out.println("Credit limit has been changed to $" + creditLimit);
        } else {
            System.out.println("The credit limit of $" + creditLimit + " you have entered is invalid.");
        }
    }
    /**
     * Overloaded method sets the credit limit attribute without printing message 
     * It is called if it has an additional parameter quietFlag
     * @param creditLimit the new credit limit to set to
     * @param quietFlag if entered, credit limit will be set quietly
     */
    public void setCreditLimit(double creditLimit, boolean quietFlag) {
        this.creditLimit = creditLimit;  // Set the credit limit without validation or feedback
    }

    /**
     * Sets credit card's new expiry date based on the input argument
     * @param date retrieves the current date and adds 5 years which will be the new exipiry date to set to.
     */
    public void setCardExpiry(LocalDate date){
        this.expiryDate = date;
    }
    /**
     * Sets the customer name to the value specified in the input argument
     * @param name the new customer name to set to
     */
    public void setCustomerName(String name){
        this.customerName = name;
    }
    /**
     * Sets the credit card's balance to the value specified in the input arguemnt
     * @param balance the new credit card balance to set to
     */
    public void setBalance(double balance){
        this.balance = balance;
    }
        
    /**
     * Sets the credit card's number to the value specified in the input arguemnt
     * @param number the new card number to set to
     */
    public void setCardNumber(long number){
        this.cardNumber = number;
    }
    /**
     * Sets the credit card's account ID to the value specified in the input argument
     * @param accountID the new account ID to set to
     */
    public void setAccountID(Integer accountID){
        this.accountID = accountID;
    }
    /**
     * This method prints out the credit card object's attributes on System Terminal for viewing.
     */
    public void printCreditCardDetails(){
        System.out.println("Card Number: " + cardNumber
                + "\nCustomer Name: " + customerName
                + "\nCredit Limit: " + creditLimit
                + "\nExpiry Date: " + expiryDate
                + "\nAccount ID: " + accountID
                + "\nBalance " + balance);
    }


}
