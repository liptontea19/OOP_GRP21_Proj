import java.util.Date;

public class Customer {

    private int age;
    private String address, id, customerName;
    private Date dateOfBirth;

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
                + "\nID: " + id);
    }
}
