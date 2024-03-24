package bank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.Period;

/**
 * Class used by the {@link Bank} class to get information on the Bank's insurance policies.
 * Available policies are stored on the InsuranceCatalog.csv file
 *
 * Dependency: InsuranceCatalog.csv 
 * @see account.Insurance Account-Specific Insurance Policy Information Class
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
     * parts[4]: Max Coverage Amt
     * parts[5]: Policy Duration 
     * </pre>
     */
    public InsuranceCatalog(){  // constructor for all insurance obj
        policyInfo = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\InsuranceCatalog.csv"));
            String line;
            String[] parts;
            reader.readLine(); // skip column name line of CSV
            while ((line = reader.readLine()) != null) {    
                parts = line.split(","); // Split by comma since it's CSV
                parts[5] = convertISOtoString(parts[5]);
                policyInfo.add(parts);
            }
            reader.close();
        } catch(FileNotFoundException error){
            System.err.println("Unable to locate InsuranceCatalog.csv file during Insurance Catalog instantiaition.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the selected catalog row's policy information in an array.
     * Array: Policy Id, Name, Type, Annual Premium, Claim Balance, Duration(ISO8601 format)
     * @param policyRowNum Row number of insurance policy (e.g. 1 for 1st policy)
     * @return policy information as elements in array
     */
    public String[] retrievePolicy(int policyRowNum) {
        return policyInfo.get(policyRowNum-1);
    }

    /**
     * Returns a Hashmap of insurance policy values
     * @param policyRowNum Index number of policy stored in <code>policyInfo</code> arrayList
     * @return mapped insurance policy information 
     */
    public HashMap<String,String> retrievePolicyMap(int policyRowNum) {
        HashMap<String,String> policyMap = new HashMap<String,String>();
        policyMap.put("code", policyInfo.get(policyRowNum-1)[0]);
        policyMap.put("name", policyInfo.get(policyRowNum-1)[1]);
        policyMap.put("type", policyInfo.get(policyRowNum-1)[2]);
        policyMap.put("annualCost", policyInfo.get(policyRowNum-1)[3]);
        policyMap.put("coverage", policyInfo.get(policyRowNum-1)[4]);
        policyMap.put("duration", policyInfo.get(policyRowNum-1)[5]);

        return policyMap;
    }

    /**
     * Converts the ISO8061 formatted string into a human readable one.
     * <br /> e.g. P4Y6M7D -> "4 Yrs 6 Mths 7 Days"
     * Will skip time denominations of 0: P2Y4D -> "2 Yrs 4 Days"
     * @param periodString ISO8061 Formatted String
     * @return Human Readable String "X Yrs Y Mths Z Days"
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO8601 Format</a>
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

    /**
     * Overload variant of {@link #printInsuranceCatalog()} with Index
     * @param withIndex true to include index (1-9), false without
     */    
    public void printInsuranceCatalog(boolean withIndex){
        if(withIndex){
            System.out.println("No.| Name                    ID     Annual Premium     Claim Up To     Duration");
            for(int i=0; i< policyInfo.size(); i++){
                System.out.println((i+1) + "  | " + policyInfo.get(i)[1] + "       " + policyInfo.get(i)[0] + "   $" + 
                policyInfo.get(i)[3] + "            $" + policyInfo.get(i)[4] + "        " + policyInfo.get(i)[5]); 
            }
        } else {
            System.out.println("Name                    ID     Annual Premium     Claim Up To     Duration");
            for (String[] policyDetails: this.policyInfo){
                System.out.println(policyDetails[1] + "       " + policyDetails[0] + "   $" + 
                policyDetails[3] + "            $" + policyDetails[4] + "        " + policyDetails[5]);
        }
        }
    }

    /**
     *  Main tester method 
     */ 
    public static void main(String[] args) {
        InsuranceCatalog cat = new InsuranceCatalog();
        System.out.println("Print Insurance Catalog Feature");
        cat.printInsuranceCatalog();
        cat.printInsuranceCatalog(true);
    }
}