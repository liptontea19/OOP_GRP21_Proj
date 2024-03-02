import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class Customer {

    private int age, contactNo;
    private String address, id, customerName, maritalStatus, country, emailAddress, occupation, employer;
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
                this.customerName = data[1];
                this.age = Integer.parseInt(data[2]);
                this.contactNo = Integer.parseInt(data[3]);
                this.address = data[4];
                this.maritalStatus = data[5];
                this.country = data[6];
                this.emailAddress = data[7];
                this.occupation = data[8];
                this.employer = data[9];
                LocalDate localDate = LocalDate.parse(data[10], format);
                this.dateOfBirth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                //Date dateOfBirth =  new Date(data[10]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public int getContactNo(){
        return contactNo;
    }

    public String getMaritalStatus(){
        return maritalStatus;
    }

    public String getCountry(){
        return country;
    }
    public String getEmailAddress(){
        return emailAddress;
    }
    public String getOccupation(){
        return occupation;
    }
    public String getEmployer(){
        return employer;
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
        this.contactNo = ContactNo;
    }
    public void setMaritalStatus(String maritalStatus){
        this.maritalStatus = maritalStatus;
    }
    public void setCountry(String Country){
        this.country = Country;
    }
    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
    public void setOccupation(String occupation){
        this.occupation = occupation;
    }
    public void setEmployer(String employer){
        this.employer = employer;
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
                + "\nContact Number: " + contactNo
                + "\nMarital Status: " + maritalStatus
                + "\nCountry: " + country
                + "\nEmail Address: " + emailAddress
                + "\nOccupation: " + occupation
                + "\nEmployer: " + employer);
    }
}

