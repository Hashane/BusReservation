public class Customer {
    private int customerID;
    private String name;
    private String mobile;
    private String email;
    private String city;
    private int age;

    public Customer(String name, String mobile, String email, String city, int age) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    public Customer(int customerID,String name, String mobile, String email, String city, int age) {
        this.customerID = customerID;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", age=" + age +
                '}';
    }

    // Method to display customer details
    public void getDetails() {
        System.out.println("Customer Details: " + name + ", " + email + ", " + city);
    }

    // Method to set/update customer details
    public void setDetails(String name, String mobile, String email, String city, int age) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.city = city;
        this.age = age;
    }
}
