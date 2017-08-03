package com.example.richu.locationtacker.model;

/**
 * Created by richu on 20/07/17.
 */
public class User {

    public String email;
    public String name;
public String number;
    public String image;

    public User(){


    }
    public User(String email , String name, String number, String image){
        this.email = email;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
