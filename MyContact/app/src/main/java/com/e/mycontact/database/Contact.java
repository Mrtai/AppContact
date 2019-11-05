package com.e.mycontact.database;

public class Contact {
    int id;
    String name;
    String phone;
    String address;
    String email;
    String facebook;
    String note;
    byte[] image;
    String schedule;
    String dateofborn;


    public Contact(int id, String name, String phone, String address, String email,
                   String facebook, String note, byte[] image, String schedule, String dateofborn) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.facebook = facebook;
        this.note = note;
        this.image = image;
        this.schedule = schedule;
        this.dateofborn = dateofborn;
    }

    public String getDateofborn() {
        return dateofborn;
    }

    public void setDateofborn(String dateofborn) {
        this.dateofborn = dateofborn;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
