package com.example.ex1;
public class User {
    private String name;
    private String city;
    private int imageResId;

    public User(String name, String city, int imageResId) {
        this.name = name;
        this.city = city;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public int getImageResId() { return imageResId; }
}