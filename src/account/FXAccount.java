package account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FXAccount extends Account{
    private int accountID;
    private double JPYbalance = 0;
    private double USDbalance = 0;
    public g11_FXE FXE;
    public FXAccount(int accountID) {
        super(accountID);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/FXacc.csv"));
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
                    this.USDbalance = Float.parseFloat(parts[1]);
                    this.JPYbalance = Float.parseFloat(parts[2]);
                    this.FXE = new g11_FXE();
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void makeExchange(int firstChoice, int secondChoice, double exchangeAmt){
        if(firstChoice == 1 && secondChoice == 1){
            double targetAmt = FXE.convert(SGDcurrency,JPYcurrency,exchangeAmt);
            JPYbalance += targetAmt;
            System.out.println("Current JPY Balance: " + JPYbalance);
        }
        else if(firstChoice == 1 && secondChoice == 2){
            double targetAmt = FXE.convert(SGDcurrency,USDcurrency,exchangeAmt);
            USDbalance += targetAmt;
            System.out.println("Current USD Balance: " + USDbalance);
        }
        else if(firstChoice == 2 && secondChoice == 1){
            if(exchangeAmt > JPYbalance){
                System.out.println("Insufficient balance!");
            }
            else {
                JPYbalance -= exchangeAmt;
                System.out.println("Current JPY Balance: " + JPYbalance);
            }
        }
        else if(firstChoice == 2 && secondChoice == 2){
            if(exchangeAmt > USDbalance){
                System.out.println("Insufficient balance!");
            }
            else {
                USDbalance -= exchangeAmt;
                System.out.println("Current USD Balance: " + USDbalance);
            }
        }
        else {
            System.out.println("Invalid Entry! Please try again!");
        }
    }

    public void printFXBalance(){
        System.out.println("Account ID: " + accountID + "\nForeign Balances:\nUSD: " + USDbalance + "\nJPY " + JPYbalance);
    }
}
