// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class Loan$AmortizationSchedule {
   private UUID loanID;
   private int month;
   private BigDecimal monthlyPayment;
   private BigDecimal principalAmount;
   private BigDecimal interestPayment;
   private BigDecimal remainingBalance;
   private String paymentStatus;

   public Loan$AmortizationSchedule(UUID var1, int var2, BigDecimal var3, BigDecimal var4, BigDecimal var5, BigDecimal var6) {
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

   public static ArrayList<Loan$AmortizationSchedule> readAmortizationScheduleFromCSV(String var0, Loan var1) {
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
                     Loan$AmortizationSchedule var13 = new Loan$AmortizationSchedule(var7, var8, var9, var10, var11, var12);
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