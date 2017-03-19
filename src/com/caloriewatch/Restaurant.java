package com.caloriewatch;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    public String name;
    public float healthScore;
    public ArrayList<String> categories;
    public ArrayList<Entry> entries;

    public Restaurant(String name, float healthscore) {
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
}
