package account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The LoanUtil class provides utility functions for managing loan data,
 * including saving to and modifying loan information in a CSV file.
 * This class ensures that loan data is persistent across program executions,
 * allowing for both the creation of new loans and the updating of existing ones.
 * 
 * <p>Key features include:
 * <ul>
 *     <li>Applying for a new loan and appending its information to the CSV file.</li>
 *     <li>Updating existing loan records, such as reducing the outstanding amount
 *         after a monthly payment has been made.</li>
 * 
 * <p>The CSV file serves as a simple database to store loan information,
 * making it possible to retrieve and modify loan data across different
 * instances of the application. This class handles the reading and writing
 * of this data, abstracting the file operations to provide a clean interface
 * for loan management.
 */

public class LoanUtil {

    private static final String LOANS_FILE_PATH = "data/Loans.csv";

    /**
     * Before saving Loan to the CSV,
     * method reads the csv file to check if the loan already exist in the CSV via LoanID
     * if it does exist, it will modify the existing loan.
     * 
     * If the loan does not exist in the CSV, 
     * append the loan at the end of the CSV.
     */

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