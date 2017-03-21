package com.caloriewatch.yelpapi;

/**
 * Created by bumbl on 3/8/2017.
 */

//Request object w/ params
public class YelpAPIRequest {
    private static final String DEFAULT_TERM = "dinner";
    private static final String DEFAULT_LOCATION = "Irvine, CA";

    // instance vars
    public String term = DEFAULT_TERM;
    public String location = DEFAULT_LOCATION;
    public float lat = 0;
    public float lng = 0;

    // Constructor
    public YelpAPIRequest(String term) {
        this.term = term;
        //this.location = location;
    }

    public void setLocation(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }
}