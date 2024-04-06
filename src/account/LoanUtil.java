package account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LoanUtil {

    private static final String LOANS_FILE_PATH = "liptontea19/OOP_GRP21_Proj/data/Loans.csv";

    //calls this method after applying for a loan 
    public static void saveLoanToCSV(Loan loan) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOANS_FILE_PATH, true))) {  // Append mode
            StringBuilder sb = new StringBuilder();
            sb.append(loan.getLoanID());
            sb.append(',');
            sb.append(loan.getCustomerID());
            sb.append(',');
            sb.append(loan.getPrincipalAmount());
            sb.append(',');
            sb.append(loan.getInterestRate());
            sb.append(',');
            sb.append(loan.getTermInMonths());
            sb.append('\n');

            pw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("An error occurred while saving the loan to CSV: " + e.getMessage());
        }
    }
}
     //DISREGARD the code below, i will load in the Customer constructor instead

/*  List<Loan> loans = LoanUtil.loadLoansFromCSV("path/to/loans.csv");
 *       for (Loan loan : loans) {
            Customer customer = findCustomerById(loan.getCustomerID());
            if (customer != null) {
                customer.getLoans().add(loan);
            }
        }
 */    
    /*  
    public static void loadLoansAndLinkToCustomers(List<Customer> customers) {
        try (BufferedReader br = new BufferedReader(new FileReader(LOANS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String loanID = data[0];
                String customerID = data[1];
                double principalAmount = Double.parseDouble(data[2]);
                double interestRate = Double.parseDouble(data[3]);
                int termInMonths = Integer.parseInt(data[4]);

                Loan loan = new Loan(loanID, principalAmount, interestRate, termInMonths); // Adjust constructor as needed
                Customer customer = findCustomerByID(customers, customerID);
                if (customer != null) {
                    customer.getLoans().add(loan);
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading loans from CSV: " + e.getMessage());
        }
    }

    private static Customer findCustomerByID(List<Customer> customers, String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerNRIC().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }
}
*/