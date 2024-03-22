package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.time.Period;

/**
 * Class used by the {@link Bank} class to get information on the Bank's insurance policies.
 * Available policies are stored on the InsuranceCatalog.csv file
 *
 * Dependency: InsuranceCatalog.csv 
 * {@link insurance} Account-Specific Insurance Policy Information
 */
public class InsuranceCatalog {
    /** Arraylist to store all the available insurance policy filed values as string arrays with 6 column values */
    private ArrayList<String[]> policyInfo;

    /** 
     * Constructor that gets insurance policy information stored on Insurance.csv.
     * Initialises the arraylist, <code>policyInfo</code> with String[] list values of the records
     * <pre>
     * Parts Element: Variable Stored
     * parts[0]: Policy Code
     * parts[1]: Name
     * parts[2]: Type
     * parts[3]: Annual Premium Cost
     * parts[4]: Policy Duration 
     * </pre>
     */
    public InsuranceCatalog(){  // constructor for all insurance obj
        policyInfo = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\InsuranceCatalog.csv"));
            String line;
            reader.readLine(); // skip column name line of CSV
            while ((line = reader.readLine()) != null) {    
                String[] parts = line.split(","); // Split by comma since it's CSV
                parts[5] = convertISOtoString(parts[5]);
                policyInfo.add(parts);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the ISO8061 formatted string into a human readable one
     * e.g. P4Y6M7D -> "4 Yrs 6 Mths 7 Days"
     * @param periodString ISO8061 Formatted String
     * @return Human Readable String
     */
    private String convertISOtoString(String periodString){
        Period span = Period.parse(periodString);
        int years = span.getYears();
        int months = span.getMonths();
        int days = span.getDays();

        String output = "";
        if (years > 0) {
            output += Integer.toString(years) + " Yrs ";
        }

        if (months > 0){
            output += Integer.toString(months) + " Mths ";
        }

        if (days > 0 ){
            output += Integer.toString(days) + " Days";
        }

        return output;
    }

    /** 
     * Prints information stored in the policyInfo arraylist for users to view available policies.
     * Displays: policy name, ID, Annual Premium Due, Start Date and End Date
     */
    public void printInsuranceCatalog(){
        
        System.out.println("Name                    ID     Annual Premium     Claim Up To     Duration");
        for (String[] policyDetails: this.policyInfo){
            System.out.println(policyDetails[1] + "       " + policyDetails[0] + "   $" + 
            policyDetails[3] + "            $" + policyDetails[4] + "        " + policyDetails[5]);
        }
    }

    public static void main(String[] args) {
        InsuranceCatalog cat = new InsuranceCatalog();
        System.out.println("Print Insurance Catalog Feature");
        cat.printInsuranceCatalog();
    }
}