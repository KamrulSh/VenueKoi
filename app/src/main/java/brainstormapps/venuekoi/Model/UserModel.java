package brainstormapps.venuekoi.Model;

public class UserModel {

    private String Name, Phone, Address;

    public UserModel() {
    }

    public UserModel(String name, String phone, String address) {
        Name = name;
        Phone = phone;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
