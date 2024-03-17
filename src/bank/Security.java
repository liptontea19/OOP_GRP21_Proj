package bank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
/** 
 * Security class will be in charge of the logging in process and authentication 
 * services for account transactions. A Security object will be generated when the
 * Bank is initialised. Object is bound to account and functions are utilised 
 * when required to validate the authenticity of a transaction. Uses the 
 * PBKDF2WithHmacSHA1 Algorithm for encryption.
 */
public class Security {
    /** Account Credentials of all bank customers 
     * Key: Int User ID, Val: [String Hashed Account Password Value,Salt Value] */    
    private HashMap<Integer,List<String>> userPwdMap = new HashMap<>();
    /** A record of currently active OTPs. Key: User ID Val:[otpVal,otpValidTime] */
    private HashMap<Integer, List<String>> otpMap = new HashMap<>();
    /** OTP will be valid for the duration in seconds */
    private int otpExpirySeconds;
    /** Standard date and time display format to be used for methods */
    private DateTimeFormatter standardDateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

    /**
     * Constructor for security class called on Bank initialisation. Creates a Security class object.
     * Sets the OTP validity duration 
     * and populates userPwdMap with values from the password CSV file.
     * @param otpExpiry OTP validity duration in seconds
     */
     public Security(int otpExpirySeconds){
        this.otpExpirySeconds = otpExpirySeconds;

        String filePath = "data\\UserPass.csv";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            String accInfo[];  
            Boolean firstLine = true;
            while ((line = reader.readLine()) != null){ // populates the userPwdMap with Account log-in credentials
                if (firstLine == true){
                    firstLine = false;
                    continue;
                }
                accInfo = line.split(",");
                userPwdMap.put(Integer.parseInt(accInfo[0]), List.of(accInfo[1],accInfo[2]));   // exception handler for parsing salt and pwd
            }
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Unable to locate UserPass.csv in path " + filePath);
        }
    }

    /**
     * Used to generate a one-time user password specific to the user's account for transaction validations. 
     * Account that OTP is generated for have an existing record in userPwdMap
     * Adds the OTP to the Hashmap otpMap <userId, [otpString,otpValidTime]>.
     * Password will be valid within the duration specified in otpExpirySeconds.
     * @param userId User ID value
     * @param passLength Specifies the OTP string length
     * @return A One-Time Password String value
     */
    public void generateTransactionOTP(int userId, int passLength){
        if (this.userPwdMap.containsKey(userId)){
            if (passLength <= 0){
                System.err.println("OTP Length cannot be less than 1.");
            } else {
                SecureRandom randomNum = new SecureRandom();
                String otpString = "";

                // generates the OTP String value additively using the RNG 
                for (int i = 0; i< passLength; ++i){
                    otpString = otpString + Integer.toString(randomNum.nextInt(10));
                }
                LocalDateTime otpValidTime = LocalDateTime.now().plusSeconds(otpExpirySeconds);   // sets the OTP expiry date and time
                String timeString = standardDateTimeFormat.format(otpValidTime);
                System.out.println("Generated OTP: " + otpString + " is valid until " + timeString);
                this.otpMap.put(userId, List.of(otpString, timeString));
            }

        } else {
            throw new IllegalArgumentException("User ID not found in database.");
        }
        
    }

    /**
     * Checks if the input argument matches the one found in OTP store
     * Method has 3 conditions: Valid User ID, Non-expired OTP and Correct OTP argument
     * If any of the three conditions fails, return False value. 
     * Otherwise, clear the OTP record from otpMap and return True.
     * @param userId Integer key variable to use for getting OTP information from otpMap
     * @param otpString One-time password user input value to compare against existing OTP records
     */
    public boolean validateOTP(int userId, String otpString){
        if (!otpMap.containsKey(userId)){
            System.err.println("User's OTP has not been generated yet.");
        }
        else {
            String expString = otpMap.get(userId).get(1);   // gets the expiry time-date string value from otpMap
            LocalDateTime expDateTime = LocalDateTime.parse(expString, standardDateTimeFormat); // casts String into a LDT datatype
            LocalDateTime nowTime = LocalDateTime.now();

            if (nowTime.isAfter(expDateTime)){
                System.out.println("OTP has expired, please generate a new OTP");
                otpMap.remove(userId);
            }
            else {
                String storedOtp = otpMap.get(userId).get(0);
                if (otpString.equals(storedOtp)){
                    System.out.println("OTP is correct.");
                    otpMap.remove(userId);  // removes generated OTP from the otpMap
                    return true;
                }
                else {
                    System.out.println("OTP is incorrect please try again.");
                }
            }
        }

        return false;
    }

    /**
     * Generates a randomised salt value to add to pre-hashed passwords.
     * @return 16 byte long salt value 
     */
    private byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Converts plain-text passString arg into an encrypted string by adding a 
     * cryptographic salt to the plain-text and then hashing it 
     * @param userId Key used to store password into userPwdMap
     * @param passString Plain-text password value 
     * @param saltString Account-specific salt value
     * @return Encrypted password in a byte array
     */
    public byte[] hashPassword(String passString, byte[] salt){
        //byte[] salt = saltString.getBytes();
        try {
            PBEKeySpec spec = new PBEKeySpec(passString.toCharArray(), salt, 1000, 64*8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedVal = factory.generateSecret(spec).getEncoded();
            return hashedVal;
        } catch (NoSuchAlgorithmException error) {
            System.err.println("Hashing Algo cannot be located, returning null value.");
        } catch (InvalidKeySpecException error) {
            System.err.println(error);
        }
        return null;
    }

    /**
     * Converts byte array into a hexadecimal string twice the length of the input array.
     * @param arr Byte array 
     * @return Converted character string encoded with byte array values
     */
    private String byteToString(byte[] arr){
        BigInteger bi = new BigInteger(1, arr);
        String hex = bi.toString(16);
    
        int paddingLength = (arr.length * 2) - hex.length();
        if(paddingLength > 0){
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    /**
     * Converts hexadecimal string into byte array by translating the character values 
     * into byte values.
     * @param hex The hexadecimal string
     * @return Converted byte array
     */
    private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * Iterates over the two input argument array bytes and 
     * checks for differences using bitwise XOR. 
     * @param arr1 First hashed byte array 
     * @param arr2 Second hashed byte array
     * @return True if arr1 and arr2 are same, False if different
     */
    private boolean iterCompare(byte[] arr1, byte[] arr2){
        int arr_diff = arr1.length ^ arr2.length;   // XOR()
        
        for(int i=0;i<arr1.length && i<arr2.length;i++){
            arr_diff = arr_diff | (arr1[i] ^ arr2[i]);  // arr_diff OR (arr1 byte XOR arr2 byte)
        }
        return arr_diff == 0;   
    }

    /**
     * Checks if input password matches password stored on file.
     * @param userId User's account identification number
     * @param password Plain-text password user is attempting to login with
     * @return Succesful Login: True, Unsuccesful: False
     */
     public Boolean validatePassword(int userId, String password){
        if (userPwdMap.containsKey(userId)){
            byte[] storedPass = fromHex(userPwdMap.get(userId).get(0));
            byte[] salt = fromHex(userPwdMap.get(userId).get(1));

            byte[] attemptPass = hashPassword(password, salt);
            if (iterCompare(storedPass, attemptPass)){
                System.out.println("Login successful.");
                return true;
            } else {
                System.out.println("Login unsuccesful, please try again.");
            }
        } else {
            System.err.println("User log in credentials not found.");
        }
        return false;
    }

    /**
     * Sets a new encrypted password and salt for user account in the userPwdMap.
     * Changes existing password and hash.
     * @param userId User account ID to add/change password for
     * @param password Plain-text password value
     */
    public void createNewPass(int userId, String password){
        byte[] salt = generateSalt();
        byte[] hashVal;
        hashVal = hashPassword(password, salt);
        if (userPwdMap.containsKey(userId)) {
            System.out.println("Password has been updated!");
        }
        else {
            System.out.println("Password has been set for account.");
        }
        userPwdMap.put(userId, List.of(byteToString(hashVal), byteToString(salt)));
    }

    public static void main(String[] args) {
        Security secSession = new Security(60);
        //Scanner scanner = new Scanner(System.in);

        //byte[] pwd = secSession.hashPassword(1, "tehcbing", secSession.generateSaltt());
        //System.out.println(pwd);
        //scanner.close();
        //secSession.createNewPass(2, "tehcbing");
        //secSession.createNewPass(3, "kopicbing");
        byte[] salty = fromHex("c053f9f5f74a8a9ab12a2ca39c42b128");
        byte[] pwd1 = secSession.hashPassword("icelemontea", salty);
        byte[] pwd2 = secSession.hashPassword("icelemonteh", salty);
        if(secSession.iterCompare(pwd1,pwd2)){
            System.out.println("Password match");
        }
        secSession.validatePassword(1,"icelemontea");
    }
}