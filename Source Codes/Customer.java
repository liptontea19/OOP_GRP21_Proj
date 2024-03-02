import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class Customer {

    private int age, ContactNo;
    private String address, id, customerName, MaritalStatus, Country, EmailAddress, Occupation, Employer;
    private Date dateOfBirth;
    

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

    public Customer(int customerID){
        String Path = "Source_Codes/Customer.csv";
  
        try (BufferedReader br = new BufferedReader(new FileReader(Path))) {
            String line = "";
            String delimiter = ",";
            DateTimeFormatter format = DateTimeFormatter.ofPattern("ddmmyyyy"); 
            
              while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                String id = data[0];
                String customerName = data[1];
                int age = Integer.parseInt(data[2]);
                int contactNo = Integer.parseInt(data[3]);
                String address = data[4];
                String maritalStatus = data[5];
                String country = data[6];
                String emailAddress = data[7];
                String occupation = data[8];
                String employer = data[9];
                LocalDate localDate = LocalDate.parse(data[10], format);
                this.dateOfBirth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                //Date dateOfBirth =  new Date(data[10]);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        return customerName;
    }
    public int getCustomerAge(){
        return age;
    }

    public String getCustomerAddress(){
        return address;
    }

    public Date getCustomerDOB(){
        return dateOfBirth;
    }

    public String getCustomerNRIC(){
        return id;
    }
    public void setContactNo(int ContactNo){
        this.ContactNo = ContactNo;
    }
    public void setMaritalStatus(String MaritalStatus){
        this.MaritalStatus = MaritalStatus;
    }
    public void setCountry(String Country){
        this.Country = Country;
    }
    public void setEmailAddress(String EmailAddress){
        this.EmailAddress = EmailAddress;
    }
    public void setOccupation(String Occupation){
        this.Occupation = Occupation;
    }
    public void setEmployer(String Employer){
        this.Employer = Employer;
    }
    public void setCustomerAge(int age){
        this.age = age;
    }

    public void setCustomerAddress(String address){
        this.address = address;
    }
    public void setCustomerDOB(Date dob){
        this.dateOfBirth = dob;
    }
    public void setCustomerName(String name){
        this.customerName = name;
    }
    public void setCustomerNRIC(String id){
        this.id = id;
    }

    public void printCustomerDetails(){
        System.out.println("Name: " + customerName
                + "\nAge: " + age
                + "\nAddress: " + address
                + "\nDate of Birth: " + dateOfBirth
                + "\nID: " + id
                + "\nContact Number: " + ContactNo
                + "\nMarital Status: " + MaritalStatus
                + "\nCountry: " + Country
                + "\nEmail Address: " + EmailAddress
                + "\nOccupation: " + Occupation
                + "\nEmployer: " + Employer);
    }
}

