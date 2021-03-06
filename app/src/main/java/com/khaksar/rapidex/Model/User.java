package com.khaksar.rapidex.Model;

public class User {

    private  String phone;
    private  String name;
    private  String email;
    private  String address;
    private String error_msg;  //It will be empty if user return object.

    public User() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String birthdate) {
        this.email = birthdate;
    }

    public static String getError_msg() {
        return getError_msg();
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
