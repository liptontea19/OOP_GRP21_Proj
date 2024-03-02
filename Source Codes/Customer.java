import java.util.Date;

public class Customer {

    private int age, ContactNo;
    private String address, id, customerName, MaritalStatus, Country, EmailAddress, Occupation, Employer;
    private Date dateOfBirth;

    public Customer(String customerName, String id, int age,int ContactNo, String address, String MaritalStatus, String Country, 
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
