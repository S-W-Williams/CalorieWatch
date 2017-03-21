package com.caloriewatch;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    public String name;
    public float healthScore;
    public ArrayList<String> categories;
    public ArrayList<Entry> entries;
    public boolean available = false;

    //Yelp API data
    public float rating;
    public String phoneNumber;
    public String yelpUrl;
    public String displayAddress;
    public String address;

    public Restaurant(String name, float healthScore) {
        this.name = name;
        this.healthScore = healthScore;
    }

    public void addEntry(String name, int calories) {
        this.entries.add(new Entry(name, calories));
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

    public String getName() {
        return this.name;
    }

    public float getHealthScore() {
        return this.healthScore;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public ArrayList<Entry> getEntries() {
        return this.entries;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void copyFromYelp(com.caloriewatch.yelpapi.Restaurant yelpRestaurant) {
        this.rating = yelpRestaurant.getRating();
        this.phoneNumber = yelpRestaurant.getPhoneNumber();
        this.yelpUrl = yelpRestaurant.getYelpUrl();
        this.displayAddress = yelpRestaurant.getDisplayAddress();
        this.address = yelpRestaurant.getAddress();
        this.displayAddress = this.displayAddress.replaceAll("[\\[\\](){}]","");
        this.displayAddress = this.displayAddress.replaceAll("\"", "");
    }

    public float getRating() { return rating; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getYelpUrl() { return yelpUrl; }
    public String getDisplayAddress() { return displayAddress; }
    public String getAddress() { return address; }
}
