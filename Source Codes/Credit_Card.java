import java.util.Date;

public class Credit_Card {
    private double balance = 0, creditLimit;
    private String cardHolderName, id;
    private int cardNumber;
    private Date expiryDate;

    public void chargeCredit(double amount){
        this.balance += amount;
    }

    public double getCreditBalance(){
        return balance;
    }

    public Date getCardExpiry(){
        return expiryDate;
    }

    public String getCardHolderName(){
        return cardHolderName;
    }

    public double getCreditLimit(){
        return creditLimit;
    }

    public int getCardNumber(){
        return cardNumber;
    }

    public String getID(){
        return id;
    }

    public void setCreditBalance(double balance){
        this.balance = balance;
    }

    public void setCardExpiry(Date date){
        this.expiryDate = date;
    }

    public void setCardHolderName(String name){
        this.cardHolderName = name;
    }

    public void setCreditLimit(double limit){
        this.creditLimit = limit;
    }

    public void setCardNumber(int number){
        this.cardNumber = number;
    }

    public void setID(String id){
        this.id = id;
    }

    public void printCreditCardDetails(){
        System.out.println("Balance: " + balance
                + "\nCard Holder Name: " + cardHolderName
                + "\nCard Number: " + cardNumber
                + "\nCredit Limit: " + creditLimit
                + "\nExpiry Date: " + expiryDate
                + "\nID: " + id);
    }
}
