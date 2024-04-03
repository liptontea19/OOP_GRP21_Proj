// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Credit {
   private String customerID;
   private int score;

   public Credit(String var1, int var2) {
      try {
         this.validateCustomerID(var1);
         this.validateCreditScore(var2);
         this.customerID = var1;
         this.score = var2;
      } catch (IllegalArgumentException var4) {
         System.out.println("Error creating Credit object: " + var4.getMessage());
      }

   }

   public String getCustomerID() {
      return this.customerID;
   }

   public void setCustomerID(String var1) {
      try {
         this.validateCustomerID(var1);
         this.customerID = var1;
      } catch (IllegalArgumentException var3) {
         System.out.println("Error setting customerID: " + var3.getMessage());
      }

   }

   public int getScore() {
      return this.score;
   }

   public void setScore(int var1) {
      try {
         this.validateCreditScore(var1);
         this.score = var1;
      } catch (IllegalArgumentException var3) {
         System.out.println("Error setting credit score: " + var3.getMessage());
      }

   }

   public void deductScore() {
      this.score -= 100;
   }

   public boolean checkEligiblity() {
      return this.score >= 600;
   }

   public void validateCustomerID(String var1) {
      if (var1 == null || var1.isEmpty()) {
         throw new IllegalArgumentException("Invalid customerID: customerID cannot be null or empty.");
      }
   }

   public void validateCreditScore(int var1) {
      if (var1 < 0 || var1 > 1000) {
         throw new IllegalArgumentException("Invalid credit score: credit score must be between 0 and 1000.");
      }
   }

   public static Credit readCreditFromCSV(String var0, String var1) {
      Credit var2 = null;

      try {
         BufferedReader var3 = new BufferedReader(new FileReader(var0));

         try {
            String var4 = var3.readLine();

            String var5;
            while((var5 = var3.readLine()) != null) {
               String[] var6 = var5.split(",");
               if (var6[0].equals(var1)) {
                  String var7 = var6[0];
                  int var8 = Integer.parseInt(var6[1]);
                  var2 = new Credit(var7, var8);
               }
            }
         } catch (Throwable var10) {
            try {
               var3.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         var3.close();
      } catch (FileNotFoundException var11) {
         System.out.println("File not found: " + var0);
      } catch (IOException var12) {
         System.out.println("Error reading file: " + var12.getMessage());
      }

      return var2;
   }
}