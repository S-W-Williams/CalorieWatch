package com.caloriewatch.yelpapi;

/**
 * Created by bumbl on 3/8/2017.
 */

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
    private static final int SEARCH_LIMIT = 10;
    private static final String DEFAULT_CATEGORY_FILTER = "food";

    // API Paths
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    // OAuth credentials
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String TOKEN = "";
    private static final String TOKEN_SECRET = "";

    private OAuthService service;
    private Token accessToken;

    // Constructor to setup Yelp API OAuth credentials
    public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    // Creates and sends a request to the Search API by term and location.
    public String searchForBusinessesByLocation(String term, String location) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        request.addQuerystringParameter("category_filter", DEFAULT_CATEGORY_FILTER);
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

    /* Queries the Search API based request params and takes the first result to query
     * the Business API
     */
    private static void queryAPI(YelpAPI yelpApi, YelpAPIRequest yelpApiReq) {
        String searchResponseJSON =
                yelpApi.searchForBusinessesByLocation(yelpApiReq.term, yelpApiReq.location);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        System.out.format("Businesses %s", businesses.toString());
        JSONObject firstBusiness = (JSONObject) businesses.get(0);
        String firstBusinessID = firstBusiness.get("id").toString();
        System.out.println(String.format(
                "%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), firstBusinessID));

        // Select the first business and display business details
        String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
        System.out.println(businessResponseJSON);
    }

    // Queries Search API and returns list (JSONArray) of businesses
    private static JSONArray queryAPIForBusinessList(YelpAPI yelpApi, YelpAPIRequest yelpApiReq) {
        String searchResponseJSON =
                yelpApi.searchForBusinessesByLocation(yelpApiReq.term, yelpApiReq.location);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");

        return businesses;
    }

  /* NOTE:
   * To access elements in JSONArray: http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONArray.html
   * To access members in JSONObject: http://juliusdavies.ca/json-simple-1.1.1-javadocs/org/json/simple/JSONObject.html
   * */


  /*
  // Main function for testing
  public static void main(String[] args) {
    //YelpAPIRequest yelpApiReq = new YelpAPIRequest("japanese", "Irvine, CA");
	YelpAPIRequest yelpApiReq = new YelpAPIRequest("restaurants", "Irvine, CA");
	System.out.println(yelpApiReq.term + " " + yelpApiReq.location);
    YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
    System.out.println(queryAPIForBusinessList(yelpApi, yelpApiReq));
  }*/
}

