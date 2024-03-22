package account;

import java.io.*;
import java.util.*;

/**
 * ForeignX class is used for storing information of the user's foreign currency balance of different currencies
 * It also stores the currency rates of the different countries
 * This class is connected to the Account class as it borrows the same account ID to perform the foreign exchanging transactions
 */
public class ForeignX {

    /** CurrencyCode is a string that stores the name of the country that belongs to the currency  */
    private String CurrencyCode;

    /** Currency is a string that stores the currency name */
    private static String Currency;

    /** accountID is an int that is retrieved from the account class to be used for referencing to specific data from FXacc.csv */
    private int accountID;

    /** ExchangeRate is a float used for storing the currency rate that is pulled from the FX.csv */
    private float ExchangeRate;

    /** ExchangeAmount is a float to store the calculation of the amount and the exchange rate */
    private float ExchangedAmount;

    /**
     * USDamt is a float that represents the balance of US dollars from the FX.csv
     * */
    private float USDamt;

    /**
     * JPYamt is a float that represents the balance of Japan Yen from the FX.csv
     * */
    private float JPYamt;

    /**
     * MYRamt is a float that represents the balance of Malaysian Ringgit from the FX.csv
     * */
    private float MYRamt;

    /**
     * AUDamt is a float that represents the balance of Australian dollars from the FX.csv
     * */
    private float AUDamt;

    /**
     * GBPamt is a float that represents the balance of UK Pounds from the FX.csv
     * */
    private float GBPamt;

    /**
     * <p>A dictionary is used for storing of the currency code being tied to a specific currency rate</p>
     * <p>e.g. USD is tied to the rate: 0.74955</p>
     * <p>This is used for easy access of the currency rate allowing for precise calculations</p>
     * */
    HashMap<String, Float> FXdictionary = new HashMap<>();

    /**
     * <p>Special class constructor for ForeignX class with a unique identifier accountID, to extract specific data tagged to the ID and use for currency exchanges</p>
     * <p>It fetches the data from FXacc CSV containing the accountID and balance of currencies from different countries</p>
     * <p>By iterating through the csv, we use the accountID to retrieve the balances and assign them to the private attributes in this class</p>
     * <p>It calls the Foreign currencies class to retrieve the latest currency rates</p>
     * @param accountID unique identifier for different FX accounts
     */
    public ForeignX(int accountID) {
        this.accountID = accountID;
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
     * <p>ForeignCurrencies() reads the data from FX csv to retrieve the latest currency rates</p>
     * <p>A dictionary is created to store the values to the specific currency for efficiency</p>
     * <p>By initializing the currencies as "Keys", we can access the values of the currency assigned</p>
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
                    FXdictionary.put(key, value); // Populate HashMap with key-value pair
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
     * <p>viewCurrencyBalances() prints the details of the specific account and its balances</p>
     * <p>The balances consist of the USD, MYR, JPY, AUD and GBP currencies</p>
     */
    public void viewCurrencyBalances(){
        System.out.println("Account ID is: " + accountID +
                "\nUSD: " + USDamt +
                "\nMYR: " + MYRamt +
                "\nJPY: " + JPYamt +
                "\nAUD: " + AUDamt +
                "\nGBP: " + GBPamt);
    }

    /**
     * <p>viewCurrencyRates() is for the purpose of printing the latest currency rates for user to keep up to date</p>
     * <p>It prints out the dictionary in a form of a 'key : value'</p>
     * <p>e.g. United States(USD) : 0.74955</p>
     */
    public void viewCurrencyRates(){
        //Print all data
        List<Map.Entry<String, Float>> currencyList = new ArrayList<>(FXdictionary.entrySet());
        System.out.println("Current Currency Rates\n");
        for (Map.Entry<String, Float> entry : currencyList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * <p>retrieveCountryName method is to convert the user's input from the menu page to the currency they want to exchange to</p>
     * <p>Switch and case is used here for returning different values based on the user's input</p>
     * <p>Currency is being assigned to specific currency names based on user input</p>
     * @param input the specific input of a user from a menu
     * @return the currency name being used for the transaction methods in this class
     */
    public static String retrieveCountryName(int input) {
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
     * <p>exchangeToForeign method is for exchanging an amount from the user's balance to the foreign currency</p>
     * <p>By using dictionary, we retrieve the currency rate of the user's choice</p>
     * <p>The amount from the user's input will be used for the exchange and calculated into ExchangedAmount</p>
     * <p>By having switch and case, we update the balance of the foreign currency that the user has exchanged for</p>
     * @param amount the value input by the user to exchange to the foreign currency of their choice
     * @param exchangeChoice the input that is made by the user to specify the type of foreign currency
     */
    public void exchangeToForeign(float amount,int exchangeChoice) {
        CurrencyCode = retrieveCountryName(exchangeChoice);
        ExchangeRate = FXdictionary.get(CurrencyCode);
        ExchangedAmount = amount * ExchangeRate;
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
                + " SGD to " + ExchangedAmount + " " + Currency);
    }

    /**
     * <p>exchangeToSGD is a method for exchanging an amount from the specific foreign currency of the user's choice to SGD</p>
     * <p>It prints out the current balances of the foreign currencies for the user's input</p>
     * <p>Switch and case is used for doing the update of the user's foreign balances based on the input</p>
     * <p>After exchange is successful, it will print the latest exchanged amount and returns it to the account class where the account's balance will be updated</p>
     * <p>The foreign balance will be updated after the exchange as well</p>
     * @param amount the value inputted by the user to exchange to SGD
     */
    public float exchangeToSGD(float amount){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which currency do you want to exchange to SGD?" +
                "\n(1) USD balance: " + USDamt +
                "\n(2) JPY balance: " + JPYamt +
                "\n(3) MYR balance: " + MYRamt +
                "\n(4) AUD balance: " + AUDamt +
                "\n(5) GBP balance: " + GBPamt);
        int exchangeChoice = scanner.nextInt();
        scanner.nextLine();
        CurrencyCode = retrieveCountryName(exchangeChoice);
        ExchangeRate = FXdictionary.get(CurrencyCode);
        float latestCurrBalance = 0;

        switch(exchangeChoice){
            case 1:
                if(amount <= USDamt){
                    USDamt -= amount;
                    ExchangedAmount = amount/ExchangeRate;
                    latestCurrBalance = USDamt;
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 2:
                if(amount <= MYRamt){
                    MYRamt -= amount;
                    ExchangedAmount = amount/ExchangeRate;
                    latestCurrBalance = MYRamt;
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 3:
                if(amount <= JPYamt){
                    JPYamt -= amount;
                    ExchangedAmount = amount/ExchangeRate;
                    latestCurrBalance = JPYamt;

                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 4:
                if(amount <= AUDamt){
                    AUDamt -= amount;
                    ExchangedAmount = amount/ExchangeRate;
                    latestCurrBalance = AUDamt;
                }
                else
                {
                    System.out.println("Insufficient balance!");
                }
                break;
            case 5:
                if(amount <= GBPamt){
                    GBPamt -= amount;
                    ExchangedAmount = amount/ExchangeRate;
                    latestCurrBalance = GBPamt;
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
        System.out.println("Exchange successful!" +
                "\nCurrent " + CurrencyCode + " balance: " + latestCurrBalance);

        return ExchangedAmount;
    }

    /**
     * Testing with main function
     */
    public static void main(String[] args) {
        ForeignX FXacc = new ForeignX(1);
        FXacc.viewCurrencyRates();
        FXacc.exchangeToSGD(5000);

    }
}
