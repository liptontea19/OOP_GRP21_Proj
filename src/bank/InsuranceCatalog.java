package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * InsuranceCatalog class is used by the Bank main class to get information on available insurance policies sold by the bank
 * Available policies are stored on the Insurance.csv file and each entry has 6 fields
 * 1.PolicyNumber, 2.PolicyName, 3.Insurance Type, 4.Premium Balance, 5.Start Date and 6.End Date
 */
public class InsuranceCatalog {
    /** Arraylist to store all the available insurance policy filed values as string arrays with 6 column values */
    private ArrayList<String[]> policyInfo;

    /** 
     * Constructor that gets insurance policy information stored on the filepath data\\Insurance.csv and initialises the policyInfo 
     * arrayList attribute with String[] values 
     */
    public InsuranceCatalog(){  // constructor for all insurance obj
        policyInfo = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\Insurance.csv"));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {    // skip column name line of CSV
                String[] parts = line.split(","); // Split by comma since it's CSV
                policyInfo.add(parts);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Method to print information stored in the policyInfo arraylist for users to view available policies
     */
    public void printInsuranceCatalog(){
        
        System.out.println("Name                  Number     Annual Premium     Start     End");
        for (String[] policyDetails: this.policyInfo){
            System.out.println(policyDetails[1] + "       " + policyDetails[0] + "    $" + 
            policyDetails[3] + "   " + policyDetails[4] + "   " + policyDetails[5]);
        }
    }

    public static void main(String[] args) {
        InsuranceCatalog cat = new InsuranceCatalog();
        System.out.println("Print Insurance Catalog Feature");
        cat.printInsuranceCatalog();
    }
}