package com.example.covidselfcareapp;

public class User {
    public String username;
    public String email;
    public String password;
    public String age;
    public String gender;
    public boolean ishealthy;
    public User(){

    }
    public User(String username,String age,String gender,String email,String password){
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.password = password;
        this.ishealthy = Boolean.parseBoolean(null);

    }
}
