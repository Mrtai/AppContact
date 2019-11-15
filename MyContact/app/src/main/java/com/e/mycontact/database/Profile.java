package com.e.mycontact.database;

public class Profile {

    int id;
    String name;
    String phone;
    String address;
    String email;
    byte[] image;
    String dateofborn;

    public  Profile(){

    }

    public Profile(int id, String name, String phone, String address, String email, byte[] image, String dateofborn) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.image = image;
        this.dateofborn = dateofborn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDateofborn() {
        return dateofborn;
    }

    public void setDateofborn(String dateofborn) {
        this.dateofborn = dateofborn;
    }
}
