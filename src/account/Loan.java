// Source code is decompiled from a .class file using FernFlower decompiler.
package account;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Loan {
   private UUID loanID = UUID.randomUUID();
   private BigDecimal principalAmount;
   private BigDecimal interestRate;
   private int termInMonths;
   private BigDecimal monthlyPayment;
   private BigDecimal outstandingAmount;
   private BigDecimal totalPayment;
   private LocalDate startDate;
   private LocalDate endDate;
   private String customerID;
   private String status;
   private ArrayList<LocalDate> paymentDates;
   private ArrayList<AmortizationSchedule> amortizationSchedules;

   private Loan(BigDecimal var1, BigDecimal var2, int var3, String var4) {
      this.principalAmount = var1;
      this.interestRate = var2;
      this.termInMonths = var3;
      this.monthlyPayment = this.calculateAmortizationPayment();
      this.outstandingAmount = this.monthlyPayment.multiply(BigDecimal.valueOf((long)var3));
      this.totalPayment = this.monthlyPayment.multiply(BigDecimal.valueOf((long)var3));
      this.startDate = LocalDate.now();
      this.endDate = this.startDate.plusMonths((long)var3);
      this.customerID = var4;
      this.status = "Pending";
      this.paymentDates = new ArrayList();
      this.amortizationSchedules = new ArrayList();
   }

   public UUID getLoanID() {
      return this.loanID;
   }

   public void setLoanID(UUID var1) {
      this.loanID = var1;
   }

   public BigDecimal getPrincipalAmount() {
      return this.principalAmount;
   }

   public void setPrincipalAmount(BigDecimal var1) {
      this.principalAmount = var1;
   }

   public BigDecimal getInterestRate() {
      return this.interestRate;
   }

   public void setInterestRate(BigDecimal var1) {
      this.interestRate = var1;
   }

   public int getTermInMonths() {
      return this.termInMonths;
   }

   public void setTermInMonths(int var1) {
      this.termInMonths = var1;
   }

   public BigDecimal getOutstandingAmount() {
      return this.outstandingAmount;
   }

   public void setOutstandingAmount(BigDecimal var1) {
      this.outstandingAmount = var1;
   }

   public BigDecimal getMonthlyPayment() {
      return this.monthlyPayment;
   }

   public void setMonthlyPayment(BigDecimal var1) {
      this.monthlyPayment = var1;
   }

   public BigDecimal getTotalPayment() {
      return this.totalPayment;
   }

   public void setTotalPayment(BigDecimal var1) {
      this.totalPayment = var1;
   }

   public LocalDate getStartDate() {
      return this.startDate;
   }

   public void setStartDate(LocalDate var1) {
      this.startDate = var1;
   }

   public LocalDate getEndDate() {
      return this.endDate;
   }

   public void setEndDate(LocalDate var1) {
      this.endDate = var1;
   }

   public String getCustomerID() {
      return this.customerID;
   }

   public void setCustomerID(String var1) {
      this.customerID = var1;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String var1) {
      this.status = var1;
   }

   public ArrayList<LocalDate> getPaymentDates() {
      return this.paymentDates;
   }

   public void setPaymentDates(ArrayList<LocalDate> var1) {
      this.paymentDates = var1;
   }

   public ArrayList<AmortizationSchedule> getAmortizationSchedules() {
      return this.amortizationSchedules;
   }

   public void setAmortizationSchedules(ArrayList<AmortizationSchedule> var1) {
      this.amortizationSchedules = var1;
   }

   public static Loan applyForLoan(List<Loan> var0, BigDecimal var1, BigDecimal var2, int var3, Credit var4) {
      if (var1.compareTo(BigDecimal.ZERO) > 0 && var2.compareTo(BigDecimal.ZERO) > 0 && var3 > 0) {
         if (var4.getCustomerID() == null) {
            System.out.println("Error. Customer ID cannot be null.");
            return null;
         } else if (var4.getScore() < 0) {
            System.out.println("Error. Please provide non-negative value for credit score.");
            return null;
         } else if (var4.checkEligiblity()) {
            Loan var5 = new Loan(var1, var2, var3, var4.getCustomerID());
            var0.add(var5);
            //System.out.println("Applying for Loan.");
            return var5;
         } else {
            System.out.println("Failure to apply for loan as credit score is too low.");
            return null;
         }
      } else {
         System.out.println("Error. Please provide non-negative values for principal amount, interest rate, and term in months.");
         return null;
      }
   }

   public void approveLoan() {
      this.setStatus("Approved");
      //System.out.println("Loan is approved.");
      BigDecimal var1 = this.principalAmount;

      for(int var2 = 1; var2 <= this.termInMonths; ++var2) {
         BigDecimal var3 = var1.multiply(this.interestRate).divide(BigDecimal.valueOf(12L)).divide(BigDecimal.valueOf(100L));
         BigDecimal var4 = this.monthlyPayment.subtract(var3);
         var1 = var1.subtract(var4);
         AmortizationSchedule var5 = new AmortizationSchedule(this.loanID, var2, this.monthlyPayment, var4, var3, var1.max(BigDecimal.ZERO));
         this.amortizationSchedules.add(var5);
      }

   }

   public void rejectLoan() {
      this.setStatus("Rejected");
      System.out.print("Loan is rejected.");
   }

   public void displayInfo() {
      System.out.println("\nLoan Information: ");
      System.out.println("Loan ID: " + this.loanID);
      String var1 = String.format("Principal Amount: $%.2f", this.principalAmount);
      System.out.println(var1);
      String var2 = String.format("Outstanding Amount: $%.2f", this.outstandingAmount);
      System.out.println(var2);
      String var3 = String.format("Monthly Payment: $%.2f", this.monthlyPayment);
      System.out.println(var3);
      System.out.println("Interest Rate in Month: " + this.interestRate + "%");
      System.out.println("Loan Period(Months): " + this.termInMonths);
      String var4 = String.format("Total Payment: $%.2f", this.totalPayment);
      System.out.println(var4);
      System.out.println("Start Date: " + this.startDate);
      System.out.println("End Date: " + this.endDate);
      System.out.println("Customer ID: " + this.customerID);
      //System.out.println("Payment Dates: " + this.paymentDates);
      System.out.println("Status: " + this.status);

      //for(int var5 = 0; var5 < this.paymentDates.size(); ++var5) {
      //   System.out.println("Payment Date " + (var5 + 1) + ": " + this.paymentDates.get(var5));
      //}

   }

   public void printAmortizationSchedule() {
      if (this.getStatus().equalsIgnoreCase("Approved")) {
         System.out.println("Month\tMonthly Payment\tPrincipal Amount\tInterest\tRemaining Principal Balance\tPayment Status");

         for(int var1 = 1; var1 <= this.termInMonths; ++var1) {
            String var2 = String.format("%-10d$%-10.2f\t$%-10.2f\t\t$%-10.2f\t$%-10.2f\t\t\t%s", ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getMonth(), ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getMonthlyPayment(), ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getPrincipalAmount(), ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getInterestPayment(), ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getRemainingBalance(), ((AmortizationSchedule)this.amortizationSchedules.get(var1 - 1)).getPaymentStatus());
            System.out.println(var2);
         }
      } else {
         System.out.println("Error. Loan is not approved. ");
      }

   }

   public void addPaymentDate(LocalDate var1) {
      this.paymentDates.add(var1);
   }

   public void repay(String var1, BigDecimal var2) {
      if (this.getStatus().equalsIgnoreCase("Approved")) {
         LocalDate var3 = LocalDate.now();
         int var4 = var3.getMonthValue();
         int var5 = var3.getYear();
         boolean var6 = false;
         if (!this.paymentDates.isEmpty()) {
            LocalDate var7 = (LocalDate)this.paymentDates.get(this.paymentDates.size() - 1);
            int var8 = var7.getMonthValue();
            int var9 = var7.getYear();
            if (var8 == var4 && var9 == var5) {
               var6 = true;
            }
         }

         if (var6) {
            System.out.println("Already paid this month.");
         } else if (var2.compareTo(this.monthlyPayment) >= 0) {
            this.addPaymentDate(var3);
            Iterator var10 = this.amortizationSchedules.iterator();

            while(var10.hasNext()) {
               AmortizationSchedule var12 = (AmortizationSchedule)var10.next();
               if (var12.getPaymentStatus().equalsIgnoreCase("None")) {
                  var12.setPaymentStatus("Paid");
                  break;
               }
            }

            this.outstandingAmount = this.outstandingAmount.subtract(this.monthlyPayment);
            String var11 = String.format("Account ID: %s. Original Account Balance: $%.2f. ", var1, var2);
            System.out.println(var11);
            String var13 = String.format("Repaid $%.2f. Outstanding Amount to pay: $%.2f.", this.monthlyPayment, this.outstandingAmount);
            System.out.println(var13);
            var2 = var2.subtract(this.monthlyPayment);
            String var14 = String.format("Account Balance: $%.2f", var2);
            System.out.println(var14);
            if (this.outstandingAmount.equals(BigDecimal.ZERO)) {
               System.out.println("Loan Cleared.");
               this.setStatus("Closed");
            }
         } else {
            System.out.println("Your account do not have sufficient funds to perform repayment of Loan");
         }
      } else {
         System.out.println("Error. Loan is not approved. ");
      }

   }

   public BigDecimal calculateAmortizationPayment() {
      BigDecimal var1 = this.interestRate.divide(BigDecimal.valueOf(1200.0), 5, RoundingMode.HALF_UP);
      BigDecimal var2 = this.principalAmount.multiply(var1.multiply(BigDecimal.valueOf(Math.pow(1.0 + var1.doubleValue(), (double)this.termInMonths)))).divide(BigDecimal.valueOf(Math.pow(1.0 + var1.doubleValue(), (double)this.termInMonths) - 1.0), 5, RoundingMode.HALF_UP);
      return var2;
   }

   public static List<Loan> readLoansFromCSV(String var0, String var1) {
      ArrayList var2 = new ArrayList();

      try {
         BufferedReader var3 = new BufferedReader(new FileReader(var0));

         try {
            String var4 = var3.readLine();

            String var5;
            while((var5 = var3.readLine()) != null) {
               String[] var6 = var5.split(",");
               if (var6[9].equalsIgnoreCase(var1) && var6[10].equalsIgnoreCase("Approved")) {
                  UUID var7 = UUID.fromString(var6[0]);
                  BigDecimal var8 = new BigDecimal(var6[1]);
                  BigDecimal var9 = new BigDecimal(var6[2]);
                  int var10 = Integer.parseInt(var6[3]);
                  BigDecimal var11 = new BigDecimal(var6[4]);
                  BigDecimal var12 = new BigDecimal(var6[5]);
                  BigDecimal var13 = new BigDecimal(var6[6]);
                  LocalDate var14 = LocalDate.parse(var6[7]);
                  LocalDate var15 = LocalDate.parse(var6[8]);
                  String var16 = var6[9];
                  String var17 = var6[10];
                  ArrayList var18 = new ArrayList();

                  for(int var19 = 11; var19 < var6.length; ++var19) {
                     var18.add(LocalDate.parse(var6[var19]));
                  }

                  Loan var24 = new Loan(var8, var9, var10, var16);
                  var24.setLoanID(var7);
                  var24.setMonthlyPayment(var11);
                  var24.setOutstandingAmount(var12);
                  var24.setTotalPayment(var13);
                  var24.setStartDate(var14);
                  var24.setEndDate(var15);
                  var24.setStatus(var17);
                  var24.setPaymentDates(var18);
                  var2.add(var24);
               }
            }
         } catch (Throwable var21) {
            try {
               var3.close();
            } catch (Throwable var20) {
               var21.addSuppressed(var20);
            }

            throw var21;
         }

         var3.close();
      } catch (FileNotFoundException var22) {
         System.out.println("File not found: " + var0);
      } catch (IOException var23) {
         System.out.println("Error reading file: " + var23.getMessage());
      }

      return var2;
   }

   public class AmortizationSchedule {
      private UUID loanID;
      private int month;
      private BigDecimal monthlyPayment;
      private BigDecimal principalAmount;
      private BigDecimal interestPayment;
      private BigDecimal remainingBalance;
      private String paymentStatus;
   
      public AmortizationSchedule(UUID var1, int var2, BigDecimal var3, BigDecimal var4, BigDecimal var5, BigDecimal var6) {
         this.loanID = var1;
         this.month = var2;
         this.monthlyPayment = var3;
         this.principalAmount = var4;
         this.interestPayment = var5;
         this.remainingBalance = var6;
         this.paymentStatus = "None";
      }
   
      public UUID getLoanID() {
         return this.loanID;
      }
   
      public int getMonth() {
         return this.month;
      }
   
      private void setMonth(int var1) {
         this.month = var1;
      }
   
      public BigDecimal getMonthlyPayment() {
         return this.monthlyPayment;
      }
   
      private void setMonthlyPayment(BigDecimal var1) {
         this.monthlyPayment = var1;
      }
   
      public BigDecimal getPrincipalAmount() {
         return this.principalAmount;
      }
   
      private void setPrincipalAmount(BigDecimal var1) {
         this.principalAmount = var1;
      }
   
      public BigDecimal getInterestPayment() {
         return this.interestPayment;
      }
   
      private void setInterestPayment(BigDecimal var1) {
         this.interestPayment = var1;
      }
   
      public BigDecimal getRemainingBalance() {
         return this.remainingBalance;
      }
   
      private void setRemainingBalance(BigDecimal var1) {
         this.remainingBalance = var1;
      }
   
      public String getPaymentStatus() {
         return this.paymentStatus;
      }
   
      private void setPaymentStatus(String var1) {
         this.paymentStatus = var1;
      }
   
      public ArrayList<AmortizationSchedule> readAmortizationScheduleFromCSV(String var0, Loan var1) {
         if (var1.getStatus().equalsIgnoreCase("Approved")) {
            ArrayList var2 = new ArrayList();
   
            try {
               BufferedReader var3 = new BufferedReader(new FileReader(var0));
   
               try {
                  String var4 = var3.readLine();
   
                  String var5;
                  while((var5 = var3.readLine()) != null) {
                     String[] var6 = var5.split(",");
                     if (var6[0].equalsIgnoreCase(var1.getLoanID().toString())) {
                        UUID var7 = UUID.fromString(var6[0]);
                        int var8 = Integer.parseInt(var6[1]);
                        BigDecimal var9 = new BigDecimal(var6[2]);
                        BigDecimal var10 = new BigDecimal(var6[3]);
                        BigDecimal var11 = new BigDecimal(var6[4]);
                        BigDecimal var12 = new BigDecimal(var6[5]);
                        AmortizationSchedule var13 = new AmortizationSchedule(var7, var8, var9, var10, var11, var12);
                        var2.add(var13);
                     }
                  }
               } catch (Throwable var15) {
                  try {
                     var3.close();
                  } catch (Throwable var14) {
                     var15.addSuppressed(var14);
                  }
   
                  throw var15;
               }
   
               var3.close();
            } catch (FileNotFoundException var16) {
               System.out.println("File not found: " + var0);
            } catch (IOException var17) {
               System.out.println("Error reading file: " + var17.getMessage());
            }
   
            var1.setAmortizationSchedules(var2);
            return var2;
         } else {
            System.out.println("Invalid Loan. Loan is not in approved status.");
            return null;
         }
      }
   }

}