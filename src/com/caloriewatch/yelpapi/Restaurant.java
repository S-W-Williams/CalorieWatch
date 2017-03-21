package com.caloriewatch.yelpapi;

/**
 * Created by bumbl on 3/15/2017.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class Restaurant implements java.io.Serializable
{
    private String name;
    private String id;
    private float rating;
    private ArrayList<String> location;
    private String phoneNumber;
    private ArrayList<String> categories;
    private String yelpUrl;
    private String displayAddress;
    private String address;

    public Restaurant(Object name, Object id, Object rating, Object location, Object phoneNumber, Object categories, Object yelpUrl) {
        // must check if null b/c some attributes are not always there (i.e phone number)
        if (name != null) {
            this.name = name.toString();
        }
        if (id != null) {
            this.id = id.toString();
        }
        if (rating != null) {
            this.rating = Float.parseFloat(rating.toString());
        }
        if (location != null) {
            this.location = new ArrayList<String>();

            JSONObject loc = (JSONObject) location;
            if (loc.get("display_address") != null) {
                this.displayAddress = loc.get("display_address").toString();
                //initLocationHelper(loc, "display_address");
            } else if (loc.get("address") != null) { // fallback
                this.address = loc.get("address").toString();
                //initLocationHelper(loc, "address");
            } else if (loc.get("coordinate") != null) { // final fallback
                this.location.add(loc.get("latitude").toString());
                this.location.add(loc.get("longitude").toString());
            }
        }
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber.toString();
        }
        if (categories != null) {
            JSONArray categoriesJson = (JSONArray) categories;
            this.categories = new ArrayList<String>();

            for(int j = 0; j < categoriesJson.size(); j++) {
                this.categories.add(((JSONArray) categoriesJson.get(j)).get(0).toString());
            }
        }
        if (yelpUrl != null) {
            this.yelpUrl = yelpUrl.toString();
        }
    }

    private void initLocationHelper(JSONObject locationAttribute, String attribute) {
        if (this.location != null) {
            for (Object obj : (ArrayList<Object>) locationAttribute.get(attribute)) {
                this.location.add(obj.toString());
            }
        }
    }

    // Getter methods
    // - NOTE: required for each attribute you want to get in the .jsp files
    public String getName() { return name; }
    public String getId() { return id; }
    public float getRating() { return rating; }
    public ArrayList<String> getLocation() { return location; }
    public String getPhoneNumber() { return phoneNumber; }
    public ArrayList<String> getCategories() { return categories; }
    public java.lang.String getYelpUrl() { return yelpUrl; }
    public String getDisplayAddress() { return displayAddress; }
    public String getAddress() { return address; }
}
