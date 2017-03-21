package com.caloriewatch.yelpapi;

/**
 * Created by bumbl on 3/8/2017.
 */

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

// Class for Querying YelpAPI
public class YelpAPI {

    private static final String API_HOST = "api.yelp.com";

    // Search params
    private static final int SEARCH_LIMIT = 50;
    private static final int INDIVIDUAL_BUSINESS_SEARCH_LIMIT = 3;
    private static final String DEFAULT_CATEGORY_FILTER = "food";

    // API Paths
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    // OAuth credentials
    private static final String CONSUMER_KEY = "Yk540wpoQIo9OEz7rc7zEQ";
    private static final String CONSUMER_SECRET = "VJmqT9pPO2RrO2LqhyDM8IwvAOQ";
    private static final String TOKEN = "1QLaj41nGT0zzd4_ssqvMnnTPW9vAMFJ";
    private static final String TOKEN_SECRET = "dVRVWbGgj7814omIrO9_ga8dGQ8";

    private OAuthService service;
    private Token accessToken;

    // Constructor to setup Yelp API OAuth credentials
    public YelpAPI() {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                        .apiSecret(CONSUMER_SECRET).build();
        this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    }

    // Creates and sends a request to the Search API by term and location.
    public String searchForBusinessesByLocation(String term, float lat, float lng) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        String ll = String.valueOf(lat) + "," + String.valueOf(lng) + ",1";
        request.addQuerystringParameter("ll", ll);
        //request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        //request.addQuerystringParameter("category_filter", DEFAULT_CATEGORY_FILTER);
        return sendRequestAndGetResponse(request);
    }

    // Creates and sends a request to the Business API by business ID.
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    // Creates and returns an OAuthRequest based on specified API endpoint
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }

    // Sends an OAuthRequest and returns a Response body
    private String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    // Queries Search API and returns list (JSONArray) of businesses
    public ArrayList<Restaurant> queryAPIForBusinessList(YelpAPIRequest yelpApiReq) {
        String searchResponseJSON =
                this.searchForBusinessesByLocation(yelpApiReq.term, yelpApiReq.lat, yelpApiReq.lng);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            return null;
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        for(int i = 0; i < businesses.size(); i++) {
            JSONObject business = (JSONObject) businesses.get(i);

            // JSON Object handling will occur in Restaurant constructor
            Restaurant r = new Restaurant(
                    business.get("name"),
                    business.get("id"),
                    business.get("rating"),
                    business.get("location"),
                    business.get("phone"),
                    business.get("categories"),
                    business.get("url"));
            restaurants.add(r);
        }

        return restaurants;
    }

    // Queries Search API and returns the top result
    public Restaurant queryAPIForBusiness(YelpAPIRequest yelpApiReq) {
        String searchResponseJSON =
                this.searchForBusinessesByLocation(yelpApiReq.term, yelpApiReq.lat, yelpApiReq.lng);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            return null;
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject business = (JSONObject) businesses.get(0);

        // JSON Object handling will occur in Restaurant constructor
        Restaurant r = new Restaurant(
                business.get("name"),
                business.get("id"),
                business.get("rating"),
                business.get("location"),
                business.get("phone"),
                business.get("categories"),
                business.get("url"));

        return r;
    }


  /* NOTE:
   * To access elements in JSONArray: http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONArray.html
   * To access members in JSONObject: http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONObject.html
   * */
}

