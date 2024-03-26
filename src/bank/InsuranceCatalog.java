package bank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class used by the {@link Bank} class to get information on the Bank's insurance policies.
 * Available policies are stored on the InsuranceCatalog.csv file
 *
 * Dependency: InsuranceCatalog.csv 
 * @see account.Insurance Account-Specific Insurance Policy Information Class
 */
public class InsuranceCatalog {
    /** Array to store all the available insurance policy objects*/
    //private InsuranceCatalogEntry[] catalog;
    private HashMap<Integer,InsuranceCatalogEntry> catalog;

    public InsuranceCatalog(){
        catalog = new HashMap<Integer,InsuranceCatalogEntry>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data\\InsuranceCatalog.csv"));
            String line;
            int i=1;
            reader.readLine(); // skip column name line of CSV
            while ((line = reader.readLine()) != null) {    
                catalog.put(i, new InsuranceCatalogEntry(line));
                i++;
            }
            reader.close();
        } catch(FileNotFoundException error){
            System.err.println("Unable to locate InsuranceCatalog.csv file during Insurance Catalog instantiaition.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the selected catalog entry as an object.
     * @param policyRowNum Row number of insurance policy (e.g. 1 for 1st policy)
     * @return {@link InsuranceCatalogEntry} object of policy information
     */
    public InsuranceCatalogEntry retrievePolicy(int policyRowNum){
        return catalog.get(policyRowNum);
    }

    /** 
     * Prints information stored in the policyInfo arraylist for users to view available policies.
     * Displays: policy name, ID, Annual Premium Due, Start Date and End Date
     */
    public void printInsuranceCatalog(){
        System.out.println("Name                    ID     Annual Premium     Claim Up To     Duration");
        for (int i=1;i<=catalog.size();i++){
            System.out.println(catalog.get(i).getEntryString());
        }
    }

    /**
     * Overload variant of {@link #printInsuranceCatalog()} with Index
     * @param withIndex true to include index (1-9), false without
     */    
    public void printInsuranceCatalog(boolean withIndex){
        if(withIndex){
            System.out.println("No.|Name                    ID     Annual Premium     Claim Up To     Duration");
            for (int i=1;i<=catalog.size();i++){
                System.out.println(Integer.toString(i)+ "   " +catalog.get(i).getEntryString());
            }
        } else {
            printInsuranceCatalog();
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