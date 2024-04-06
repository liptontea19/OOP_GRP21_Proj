package account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoanUtil {

    private static final String LOANS_FILE_PATH = "data/Loans.csv";

    public static void saveLoanToCSV(Loan loan) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // Read existing loans and check if the loan already exists
        try (BufferedReader br = new BufferedReader(new FileReader(LOANS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(loan.getLoanID().toString() + ",")) {
                    // Construct the new line for the existing loan
                    StringBuilder sb = new StringBuilder();
                    sb.append(loan.getLoanID()).append(',');
                    sb.append(loan.getCustomerID()).append(',');
                    sb.append(loan.getPrincipalAmount()).append(',');
                    sb.append(loan.getInterestRate()).append(',');
                    sb.append(loan.getTermInMonths()).append(',');
                    sb.append(loan.getOutstandingAmount()).append(',');
                    sb.append(loan.getStartDate()).append(',');
                    sb.append(loan.getEndDate()).append(',');
                    sb.append(loan.getStatus()).append(',');

                    String paymentDatesString = loan.getPaymentDates().stream()
                                                    .map(Object::toString)
                                                    .collect(Collectors.joining(";"));
                    sb.append(paymentDatesString);

                    lines.add(sb.toString());
                    found = true;
                } else {
                    lines.add(line);
                }
            }
            // If the loan is new, append its data at the end of the list
            if (!found) {
                StringBuilder sb = new StringBuilder();
                sb.append(loan.getLoanID()).append(',');
                sb.append(loan.getCustomerID()).append(',');
                sb.append(loan.getPrincipalAmount()).append(',');
                sb.append(loan.getInterestRate()).append(',');
                sb.append(loan.getTermInMonths()).append(',');
                sb.append(loan.getOutstandingAmount()).append(',');
                sb.append(loan.getStartDate()).append(',');
                sb.append(loan.getEndDate()).append(',');
                sb.append(loan.getStatus()).append(',');

                String paymentDatesString = loan.getPaymentDates().stream()
                                                .map(Object::toString)
                                                .collect(Collectors.joining(";"));
                sb.append(paymentDatesString);

                lines.add(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the loan file: " + e.getMessage());
        }

        // Write all lines back to the file, overwriting the existing content
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOANS_FILE_PATH))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the loan file: " + e.getMessage());
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