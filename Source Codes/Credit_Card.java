import java.time.LocalDate;

public class Credit_Card {
    private double balance = 0, creditLimit;
    private String cardHolderName, id;
    private int cardNumber;
    private LocalDate expiryDate;

    public Credit_Card(String cardHolderName, String id, int cardNumber){
        this.cardHolderName = cardHolderName;
        this.id = id;
        this.cardNumber = cardNumber;
        LocalDate currentDate = LocalDate.now(); // LocalDate is to retrieve year-month-day
        expiryDate = currentDate.plusYears(5);  // Expiry set to current date + 5 years
    }

    public void chargeCredit(double amount){
        balance += amount;
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

    public int getCardNumber(){
        return cardNumber;
    }

    public String getID(){
        return id;
    }

    public void setCreditBalance(double balance){
        this.balance = balance;
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
