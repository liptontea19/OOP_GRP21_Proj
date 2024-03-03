import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Customer {

    private int Age, ContactNo;
    private String Address, ID, CustomerName, MaritalStatus, Country, EmailAddress, Occupation, Employer, DateOfBirth;


    /* public Customer(String customerName, String id, int age,int ContactNo, String address, String MaritalStatus, String Country,
    String EmailAddress, String Occupation, String Employer, Date dateOfBirth){
        this.customerName = customerName;
        this.id = id;
        this.age = age;
        this.ContactNo = ContactNo;
        this.address = address;
        this.MaritalStatus = MaritalStatus;
        this.Country = Country;
        this.EmailAddress = EmailAddress;
        this.Occupation = Occupation;
        this.Employer = Employer;
        this.dateOfBirth = dateOfBirth;
    } */

    public Customer(String customerID){
        String Path = "src/Project/data/Customer.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            String line = "";
            String delimiter = ",";
            Boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine == true){
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(delimiter);
                if (data[0].equals(customerID)){
                    this.ID = data[0];
                    this.CustomerName = data[1];
                    this.Age = Integer.parseInt(data[2]);
                    this.ContactNo = Integer.parseInt(data[3]);
                    this.Address = data[4];
                    this.MaritalStatus = data[5];
                    this.Country = data[6];
                    this.EmailAddress = data[7];
                    this.Occupation = data[8];
                    this.Employer = data[9];
                    this.DateOfBirth = data[10];
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to locate Customer.csv");
        }
    }



    public int getContactNo(){
        return ContactNo;
    }

    public String getMaritalStatus(){
        return MaritalStatus;
    }

    public String getCountry(){
        return Country;
    }
    public String getEmailAddress(){
        return EmailAddress;
    }
    public String getOccupation(){
        return Occupation;
    }
    public String getEmployer(){
        return Employer;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public int getCustomerAge(){
        return Age;
    }

    public String getCustomerAddress(){
        return Address;
    }

    public String getCustomerDOB(){
        return DateOfBirth;
    }

    public String getCustomerNRIC(){
        return ID;
    }
    public void setContactNo(int ContactNo){
        this.ContactNo = ContactNo;
    }
    public void setMaritalStatus(String maritalStatus){
        this.MaritalStatus = maritalStatus;
    }
    public void setCountry(String Country){
        this.Country = Country;
    }
    public void setEmailAddress(String emailAddress){
        this.EmailAddress = emailAddress;
    }
    public void setOccupation(String occupation){
        this.Occupation = occupation;
    }
    public void setEmployer(String employer){
        this.Employer = employer;
    }
    public void setCustomerAge(int age){
        this.Age = age;
    }

    public void setCustomerAddress(String address){
        this.Address = address;
    }
    public void setCustomerDOB(String dob){
        this.DateOfBirth = dob;
    }
    public void setCustomerName(String name){
        this.CustomerName = name;
    }
    public void setCustomerNRIC(String id){
        this.ID = id;
    }

    public void printCustomerDetails(){
        System.out.println("Name: " + CustomerName
                + "\nAge: " + Age
                + "\nAddress: " + Address
                + "\nDate of Birth: " + DateOfBirth
                + "\nID: " + ID
                + "\nContact Number: " + ContactNo
                + "\nMarital Status: " + MaritalStatus
                + "\nCountry: " + Country
                + "\nEmail Address: " + EmailAddress
                + "\nOccupation: " + Occupation
                + "\nEmployer: " + Employer);
    }
}
