package account;

import java.io.*;
import java.util.*;

/**
 * ForeignX class is used to store information of the user's foreign currency balance of different currencies
 * It also stores the currency rates from SGD to different countries
 * The class is connected to the Account class as it uses the same account ID to perform the transactions for foreign exchange
 */
public class ForeignX {

    private String CurrencyCode;

    private static String Currency;

    private int accountID;

    private float balance;

    private float ExchangeRate;

    private float ExchangedAmount;

    private float USDamt;
    private float JPYamt;
    private float MYRamt;
    private float AUDamt;
    private float GBPamt;

    HashMap<String, Float> dictionary = new HashMap<>();

    /**Special class constructor for class private attributes
     * @param accountID unique identifier for different FX accounts
     * @param balance the current balance of the user's account
     * It fetches the data from FXacc CSV containing the accountID and the balance of the different currencies
     * By iterating through the csv, we use the accountID to retrieve the balances and assign them to the private attributes
     * It calls the Foreign currencies class to retrieve the latest currency rates
     */
    public ForeignX(int accountID,float balance) {
        this.accountID = accountID;
        this.balance = balance;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("OOP_GRP21_Proj-main/data/FXacc.csv"));
            String line;
            boolean firstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }
                String[] parts = line.split(","); // Split by comma since it's CSV
                if (Integer.parseInt(parts[0]) == accountID) {
                    this.accountID = accountID;
                    this.USDamt = Float.parseFloat(parts[1]);
                    this.MYRamt = Float.parseFloat(parts[2]);
                    this.JPYamt = Float.parseFloat(parts[3]);
                    this.AUDamt = Float.parseFloat(parts[4]);
                    this.GBPamt = Float.parseFloat(parts[5]);
                    ForeignCurrencies();
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getCurrencyCode returns the currency code of the country
     * @return String of the Currency Code
     */
    public String getCurrencyCode() {
        return CurrencyCode;
    }

    /**
     * setCurrencyCode sets the latest currency code
     * @param CurrencyCode sets the code of the currency from a specific country
     */
    public void setCurrencyCode(String CurrencyCode) {
        this.CurrencyCode = CurrencyCode;
    }

    /**
     * getExchangeRate returns the exchange rate of the country
     * @return float value of the currency rate
     */
    public float getExchangeRate() {
        return ExchangeRate;
    }

    /**
     * setExchangeRate sets the latest exchange rate
     * @param ExchangeRate sets the rate of the currency from a specific country
     */
    public void setExchangeRate(float ExchangeRate) {
        this.ExchangeRate = ExchangeRate;
    }


    /**
     * ForeignCurrencies() reads the data from FX csv to retrieve the latest currency rates
     * A dictionary is created to store the values to the specific currency for efficiency
     * By initializing the currencies as "Keys", we can access the values of the currency assigned
     */
    public void ForeignCurrencies() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("OOP_GRP21_Proj-main/data/FX.csv"));
            String line;
            boolean firstLine = true; // Flag to skip the first line
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the first line
                }
                String[] parts = line.split(","); // Split by comma since it's CSV
                if (parts.length == 2) { // Ensure there are two parts (key, value)
                    String key = parts[0].trim();
                    float value = Float.parseFloat(parts[1].trim());
                    dictionary.put(key, value); // Populate HashMap with key-value pair
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * printCurrencies() prints the details of the specific account and its balances
     * The balances consist of the SGD, USD, MYR, JPY, AUD and GBP currencies
     */
    public void printCurrencies(){
        System.out.println("Account ID is: " + accountID +
                "\nSGD: " + balance +
                "\nUSD: " + USDamt +
                "\nMYR: " + MYRamt +
                "\nJPY: " + JPYamt +
                "\nAUD: " + AUDamt +
                "\nGBP: " + GBPamt);
    }

    /**
     * viewCurrencies() is for the purpose of printing the latest currency rates for user to keep up to date
     * It prints out the dictionary in a form of a 'key : value'
     */
    public void viewCurrencies(){    // for insurance menu object
        //Print all data
        List<Map.Entry<String, Float>> currencyList = new ArrayList<>(dictionary.entrySet());
        System.out.println("Current Currency Rates\n");
        for (Map.Entry<String, Float> entry : currencyList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /** The convertInput method is to convert the user's input from the menu page to specify the currency they want to exchange to
     * @param input the specific input of a user from a menu
     * @return the value of the currency being used for the methods in this class
     * Switch and case is used here for returning different values based on the user's input
     */
    public static String convertInput(int input) {
        switch (input) {
            case 1:
                Currency = "USD";
                return "United States(USD)";
            case 2:
                Currency = "Yen";
                return "Japan(JPY)";
            case 3:
                Currency = "Ringgit";
                return "Malaysia(MYR)";
            case 4:
                Currency = "AUD";
                return "Australia(AUD)";
            case 5:
                Currency = "Pounds";
                return "United Kingdom(GBP)";
            default:
                return "Invalid input";
        }
    }


    /**
     * exchangeToForeign is a method for exchanging an amount from the user's balance to the foreign currency
     * @param amount the value input by the user to exchange to the foreign currency
     * @param exchangeChoice the input that is made by the user to specify the type of foreign currency
     * By using dictionary, we retrieve the currency rate of the user's choice
     * The amount of the user's account will be reduced and that amount is used for the exchange
     * By having switch and case, we update the balance of the foreign currency that the user has exchanged for
     */
    public void exchangeToForeign(float amount,int exchangeChoice) {
        String currencyCode = convertInput(exchangeChoice);
        float exchangeRate = dictionary.get(currencyCode);
        balance -= amount;
        ExchangedAmount = amount * exchangeRate;
        switch(exchangeChoice){
            case 1:
                USDamt += ExchangedAmount;
                break;
            case 2:
                JPYamt += ExchangedAmount;
                break;
            case 3:
                MYRamt += ExchangedAmount;
                break;
            case 4:
                AUDamt += ExchangedAmount;
                break;
            case 5:
                GBPamt += ExchangedAmount;
                break;
            default:
                System.out.println("Invalid entry!");
        }
        System.out.println("You have exchanged " + amount
                + " SGD to " + ExchangedAmount + " " + Currency +
                "\nYour current balance: " + balance);
    }

    /**
     * exchangeToSGD is a method for exchanging an amount from the specific foreign currency of the user's choice to SGD
     * @param amount the value inputted by the user to exchange to SGD
     * It prints out the current balances of the foreign currencies for the user's input
     * Switch and case is used for doing the update of the user's balance and foreign balance based on the input
     * After exchange is successful, it will print the latest balance of the account
     */
    public void exchangeToSGD(float amount){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which currency do you want to exchange to SGD?" +
                "\n(1) USD balance: " + USDamt +
                "\n(2) MYR balance: " + MYRamt +
                "\n(3) JPY balance: " + JPYamt +
                "\n(4) AUD balance: " + AUDamt +
                "\n(5) GBP balance: " + GBPamt);
        int exchangeChoice = scanner.nextInt();
        scanner.nextLine();
        switch(exchangeChoice){
            case 1:
                if(amount <= USDamt){
                    USDamt -= amount;
                    balance += amount * 1.3372f;
                    System.out.println("Exchange successful!" +
                            "\nCurrent balance: " + balance +
                            "\nCurrent USD balance: " + USDamt);
                }
                else
                {
                    System.out.println("Insufficient balance!");

                }
                break;
            case 2:
                if(amount <= MYRamt){
                    MYRamt -= amount;
                    balance += amount * 0.28409f;
                    System.out.println("Exchange successful!" +
                            "\nCurrent balance: " + balance +
                            "\nCurrent MYR balance: " + MYRamt);
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 3:
                if(amount <= JPYamt){
                    JPYamt -= amount;
                    balance += amount * 0.00897f;
                    System.out.println("Exchange successful!" +
                            "\nCurrent balance: " + balance +
                            "\nCurrent JPY balance: " + JPYamt);
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 4:
                if(amount <= AUDamt){
                    AUDamt -= amount;
                    balance += amount * 0.87696f;
                    System.out.println("Exchange successful!" +
                            "\nCurrent balance: " + balance +
                            "\nCurrent AUD balance: " + AUDamt);
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 5:
                if(amount <= GBPamt){
                    GBPamt -= amount;
                    balance += amount * 1.70299f;
                    System.out.println("Exchange successful!" +
                            "\nCurrent balance: " + balance +
                            "\nCurrent GBP balance: " + GBPamt);
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            default:
                System.out.println("Invalid input! Please try again!");
                break;
        }
    }

    /**
     *Testing main function
     */
    public static void main(String[] args) {
        ForeignX FXacc = new ForeignX(1,10000);
        FXacc.printCurrencies();
        FXacc.exchangeToSGD(5000);

    }
}




