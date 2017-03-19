package com.caloriewatch;

/**
 * Created by shwil on 3/19/2017.
 */
public class Entry {
    public String item;
    public int calories;

    public Entry(String item, int calories) {
        this.item = item;
        this.calories = calories;
    }

    public String getItem() {
        return this.item;
    }

    public int getCalories() {
        return this.calories;
    }
}
