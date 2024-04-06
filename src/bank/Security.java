package bank;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * We made minor edits to Group 9's security class file so it can handle existing passwords
 */
public class Security {
    private Map<String, String> passwordMap = new HashMap<String,String>();
    private Map<String, Integer> otpMap = new HashMap<String,Integer>();

    public Security() {

    }

    public static void main(String[] args){
        Security secure = new Security();
        Scanner input = new Scanner(System.in);
        System.out.println("1) Generate new passwords and shit\n2)Test PW");
        int mode = Integer.parseInt(input.nextLine());
        String username, password="", salt, hashedPw;
        boolean validPw = false;
        while(true){
            System.out.println("Username:");
            username = input.nextLine();
            if (username.equals("abc")){
                break;
            }

            System.out.println("Password:");
            /*while(!validPw){
                password = input.nextLine();
                if (!secure.validatePassword(password)){
                    System.out.println("Entered password does not pass the requirements.");
                } else {
                    validPw = true;
                }
            }*/
            password = input.nextLine();
            if (mode == 1){
                salt = generateSalt();
                hashedPw = hashPassword(password, salt);
                System.out.println("PW: " + hashedPw + "\nSalt: " + salt);
                System.out.println("Testing plain text and hash");
                secure.setLoginAccount(username, password, salt);
                System.out.println(Boolean.toString(secure.authenticateUser(username, password, salt)));
            }
        }
        input.close();
    }

    public boolean authenticateWithOTP(String var1, int var2) {
        return this.otpMap.containsKey(var1) && this.otpMap.containsValue(var2);
    }

    public int generateOTP(String var1) {
        SecureRandom var2 = new SecureRandom();
        int var3 = var2.nextInt(100000, 1000000);
        this.otpMap.put(var1, var3);
        System.out.println(var3);
        return var3;
    }

    public boolean validatePassword(String var1) {
        String var2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,20}$";
        Pattern var3 = Pattern.compile(var2);
        Matcher var4 = var3.matcher(var1);
        return var4.matches();
    }

    public static String hashPassword(String var0, String var1) {
        try {
            PBEKeySpec var2 = new PBEKeySpec(var0.toCharArray(), var1.getBytes(), 65536, 256);
            SecretKeyFactory var3 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] var4 = var3.generateSecret(var2).getEncoded();
            return Base64.getEncoder().encodeToString(var4);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException var5) {
            throw new RuntimeException("Error during password hashing", var5);
        }
    }

    /**
    * Generates a new random key value using 16 bytes to be used as salt for the password.
    */
    public static String generateSalt() {
        SecureRandom var0 = new SecureRandom();
        byte[] var1 = new byte[16];
        var0.nextBytes(var1);
        return Base64.getEncoder().encodeToString(var1);
    }

    public static String hashCVV(String var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("SHA-256");
            byte[] var2 = var1.digest(var0.getBytes());
            StringBuilder var3 = new StringBuilder();
            byte[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte var7 = var4[var6];
                String var8 = Integer.toHexString(255 & var7);
                if (var8.length() == 1) {
                    var3.append('0');
                }

                var3.append(var8);
            }

            return var3.toString();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static boolean verifyCVV(String var0, String var1) {
        String var2 = hashCVV(var0);
        return var2 != null && var2.equals(var1);
    }

    /**
    * 
    * @param var1 username
    * @param var2 newPassword
    * @return
    */
    public ArrayList<String> resetPassword(String var1, String var2) {
        String var3 = generateSalt(); // generates salt
        String var4 = hashPassword(var2, var3);   // generates password
        this.setLoginAccount(var1, var2, var3);
        ArrayList var5 = new ArrayList();
        var5.add(var4);
        var5.add(var3);
        return var5;
    }

    public void setLoginAccount(String var1, String var2, String var3) {
        String var4 = hashPassword(var2, var3);
        if (!this.passwordMap.isEmpty()) {
            this.passwordMap.replace(var1, var4);
        }

        this.passwordMap.put(var1, var4);
    }

    /**
     * Overload method of {@link #setLoginAccount()} for adding existing accounts into passwordMa
     * @param accountName
     * @param hashedPW
     */
    public void setLoginAccount(String accountName, String hashedPW){
        if(this.passwordMap.containsKey(accountName)) {
            this.passwordMap.replace(accountName,hashedPW);
        } else {
            this.passwordMap.put(accountName, hashedPW);
        }
    }

    /**
     *
     * @param var1 username
     * @param var2 plain text password
     * @param var3 account's salt
     * @return
     */
    public boolean authenticateUser(String var1, String var2, String var3) {
        String var4 = hashPassword(var2, var3);
        return this.passwordMap.containsKey(var1) && this.passwordMap.containsValue(var4);
    }

    public boolean validateUsername(String var1) {
        return this.passwordMap.containsKey(var1);
    }

    public Map<String,String> getPasswordMap(){
        return passwordMap;
    }

    /*public void logActivity(int var1, int var2) {
        switch (var2) {
            case 1:
            csv_update_help.generateCSVofSecurity("Login", var1);
            break;
            case 2:
            csv_update_help.generateCSVofSecurity("Transfer Initialized", var1);
            break;
            case 3:
            csv_update_help.generateCSVofSecurity("Logout", var1);
            break;
            case 4:
            csv_update_help.generateCSVofSecurity("Deposit", var1);
            break;
            case 5:
            csv_update_help.generateCSVofSecurity("Withdraw", var1);
        }

    }*/
}
