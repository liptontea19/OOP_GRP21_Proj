package bank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import javax.security.auth.login.FailedLoginException;

public class BankSecurity {
    //private Map<String, String> passwordMap = new HashMap<String,String>();
    private Map<Integer, String> saltMap = new HashMap<Integer,String>();
    private Map<Integer, String> plainMap = new HashMap<Integer,String>();
    private Security secSession = new Security();   // Security Class from Group 7
    private String filePath = "data\\Userpass.csv";

    public BankSecurity(String filePath) {
        this.filePath = filePath;   // sets the file path to input arg
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            String[] secDetails;
            int accountId;
            reader.readLine();
            while((line = reader.readLine()) != null){
               secDetails = line.split(",");
               accountId = Integer.parseInt(secDetails[0]);
               //passwordMap.put(secDetails[0],secDetails[1]);
               secSession.setLoginAccount(secDetails[0], secDetails[1]);
               this.saltMap.put(accountId,secDetails[2]);
               this.plainMap.put(accountId,secDetails[3]);
            }
        } catch (FileNotFoundException e){
            System.out.println("No user password file found, running empty file mode.\nInfo will be saved to: " + filePath);
        } catch (IOException e){
            System.out.println("Error found while reading Password file.");
        }
    }

    /**
     * Authencation process that queries password list for a match of the given password.
     * @param userId ID of account that wants to log in
     * @param plainTextPw Plain-text password entered by user
     * @return
     */
    public boolean accAuthenticate(int userId, String plainTextPw){
        String userIdString = Integer.toString(userId);
        return secSession.authenticateUser(userIdString, plainTextPw, saltMap.get(userId));
    }

    /**
     * Account log in process.
     * Gives user 3 chances to enter the correct password for their account or it'll return 0
     * @param userInput System input scanner object.
     * @return Returns the account ID number of the logged in account; 0 on failure.
     * @throws FailedLoginException Thrown when user has failed to log in after 3 tries.
     */
    public int accountLogin(Scanner userInput) throws FailedLoginException{
        int attemptCount = 1, userId;
        String password;
        
        while (attemptCount < 4){
            System.out.println("Enter User ID:");
            try { 
                userId = Integer.parseInt(userInput.nextLine());
                if(saltMap.containsKey(userId)){
                    while(attemptCount < 4){
                        System.out.println("Enter password:");
                        password = userInput.nextLine();
                        if(accAuthenticate(userId, password)){
                            return userId;
                        } else {
                            System.out.println("Wrong password.");
                            System.out.println("You have " + Integer.toString(3 - attemptCount) + " remaining attempts to enter the correct password.");
                        }
                        attemptCount++;
                    }             
                    if (attemptCount >= 4){
                        throw new FailedLoginException();
                    }
                } else {
                    System.out.println("Entered User ID was not in database.");
                }
            } catch(NullPointerException e){
                System.out.println("Entered User ID is null.");
            } catch(NumberFormatException e){
                System.err.println("Entered ID is incorrect.");
            }
        }
        
        return 0;
    }

    /**
     * User is given an OTP and has three attempts to enter the correct password.
     * @param accId Id of authenticating user
     * @param userInput System.input scanner object
     * @return  Success or failure of OTP authentication
     * @throws FailedLoginException
     */
    public boolean otpProcess(int accId, Scanner userInput){
        String accountName = Integer.toString(accId);
        int attemptCount = 1, otpVal=0;
        System.out.println("Generating OTP for your transaction...");
        System.out.println("OTP is: " + Integer.toString(secSession.generateOTP(accountName)));
        while(attemptCount<4){
            try{
                otpVal = Integer.parseInt(userInput.nextLine());
            } catch (NumberFormatException e){
                System.err.println("Your OTP is not valid.");
            }
            if(secSession.authenticateWithOTP(accountName, otpVal)){
                return true;
            } else {
                System.out.println("OTP is incorrect.\nYou have " + Integer.toString(3 - attemptCount) + " remaining attempts.");
            }
            attemptCount++;
        }

        return false;
    }

    /**
     * Updates UserPass.csv with the new account details stored pwMap 
     */
    private void editUserPassFile(){
        TreeMap<Integer,String> sortedPwMap = new TreeMap<>();   // creates a treemap of values sorted by the username key
        int accountId;
        for (Map.Entry<String,String> entry: secSession.getPasswordMap().entrySet()){   // sorts password map
            sortedPwMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        try (BufferedWriter uPassLine = new BufferedWriter(new FileWriter(filePath))){
            uPassLine.write("UserId,HashedPassword,Salt,Plaintext");  
            for(Map.Entry<Integer,String> entry: sortedPwMap.entrySet()){
                uPassLine.newLine();
                accountId = entry.getKey();
                uPassLine.write(entry.getKey() + "," + entry.getValue() + "," + saltMap.get(accountId) +
                 "," + plainMap.get(accountId));
            }
        } catch (NullPointerException e){
            System.err.println("Mismatched entries between existing and new data.");
        } catch (IOException e){
            System.err.println("Unable to find UserPass.csv or Invalid write value during UserPass.csv updating.");
        }
    }

    private String createAcceptedPw(Scanner userInput) throws ExitException{
        boolean validPw = false;
        String ptPassword = "null";
        while(!validPw){
            System.out.println("Password must have:\n- 1 digit(0-9)\n- 1 uppercase (A-Z)\n- 1 lowercase (a-z)\n" + 
            "- 1 special char\n- between 8 and 19 characters long\nPress Enter to quit");
            System.out.println("Enter password: ");
            ptPassword = userInput.nextLine();
            if(secSession.validatePassword(ptPassword)){
                validPw = true;
                return ptPassword;
            } else if(ptPassword.length() > 19){
                System.out.println("Your password is too long.");
            } else if (ptPassword.equals("")){
                throw new ExitException("User has quit password creation.");
            } else {
                System.out.println("Password does not pass requirements.");
            }
        }
        return ptPassword;
    }

    /**
     * New credential setup and dialogue.
     * @param userInput System.in Scanner object to take user inputs
     * @throws ExitException Thrown when user exits registration dialogue early
     */
    public void registerUserCreds(Scanner userInput) throws ExitException{
        boolean notDupe = false;    // flag to check if generated ID is not a duplicate of existing record
        int accountId=0;
        String accountIdString = "0", ptPassword = "";
        Random rand = new Random();

        while (!notDupe){   // creates a non duplicate user ID value
            System.out.println("Generating unique user ID...");
            accountId = rand.nextInt(100);  // generates a user Id value between 1 and 100
            accountIdString = Integer.toString(accountId);
            if(accountId!=0 && !saltMap.containsKey(accountId)){
                System.out.println("Your new ID is " + accountIdString);
                notDupe = true;
                ptPassword = createAcceptedPw(userInput);   // sets the validPw flag to allow the next part of the 
            }
        }

        if (!ptPassword.equals("")){
            saltMap.put(accountId,Security.generateSalt());
            plainMap.put(accountId, ptPassword);
            secSession.setLoginAccount(accountIdString, ptPassword, saltMap.get(accountId));
            editUserPassFile();
        } else {
            System.out.println("Exiting account creation.");
            throw new ExitException("User has ended the account creation early.");
        }
    }

    /**
     * Sets a new password and salt credentials for the specified user.
     * Prompts user for a new password and creates a new salt to encrypt their password with.
     * Saves information into UserPass.csv.
     * @param userId Account ID of user whose password is being changed
     * @param userInput System.in Scanner object to take user inputs
     * @throws ExitException Activates when the user exits the process before completion.
     */
    public void changeUserCreds(int userId, Scanner userInput) throws ExitException{
        String salt = Security.generateSalt();
        try {
            String pw = createAcceptedPw(userInput);
            secSession.setLoginAccount(Integer.toString(userId), pw, salt);
            saltMap.replace(userId, salt);
            plainMap.replace(userId, pw);
            editUserPassFile();
        } catch (ExitException e) {
            System.out.println("Exiting...\nNo changes have been made.");
            throw new ExitException("User has cancelled creating password.");
        } catch (NullPointerException e) {
            System.err.println("Specified ID is not in database.");
            throw new ExitException("ID not found");
        }
    }

    public static void main(String[] args){
        BankSecurity bigSecs = new BankSecurity("data\\UserPass.csv");
        Scanner input = new Scanner(System.in);

        //System.out.println("Test: Log in Function");
        //int accountNum = bigSecs.accountLogin(input);
        //System.out.println(Integer.toString(accountNum));   // verify

        /*System.out.println("\nTest: Adding Account credentials function");
        try{
            bigSecs.registerUserCreds(input);
            System.out.println("User has succesfully completed registration.");
            bigSecs.changeUserCreds(1, input);
        } catch (ExitException e){
            System.out.println("User ended their registration early.");
        }*/
        /*try{
            System.out.println(bigSecs.otpProcess(1, input));
        }catch(FailedLoginException e){
            System.err.println("You have failed to authenticate your account, logging out.");
        }*/
        //System.out.println("\nTest: Updating UserPass.csv");
        bigSecs.editUserPassFile();
        input.close();
    }
}
