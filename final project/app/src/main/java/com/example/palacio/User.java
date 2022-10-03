package com.example.palacio;

public class User {
    private String email;
    private String Fname;
    private String Lname;
    private myDate Birthday;
    private String phone;
    private String pic;
    private String Gender;
    private String Weight;
    private String Height;
    private Boolean IsAdmin;
    private String Address;
    private String City;

    public User(String email, String fname, String lname, myDate birthday, String phone, String pic, String gender, String weight, String height, Boolean isAdmin, String address, String city) {
        this.email = email;
        Fname = fname;
        Lname = lname;
        Birthday = birthday;
        this.phone = phone;
        this.pic = pic;
        Gender = gender;
        Weight = weight;
        Height = height;
        IsAdmin = isAdmin;
        Address = address;
        City = city;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public myDate getBirthday() {
        return Birthday;
    }

    public void setBirthday(myDate birthday) {
        Birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public User()
    {

    }
}
