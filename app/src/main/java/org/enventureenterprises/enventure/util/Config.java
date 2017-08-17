package org.enventureenterprises.enventure.util;

import java.util.regex.Pattern;

/**
 * Created by Moses on 6/30/16.
 */
public class Config {


    public static final String TWITTER_KEY = "gDdFlOtB9uQyY5wQORLde9KFy";
    public static final String TWITTER_SECRET = "iO7XOEbczOR1lMYHBHVdUdRfO2ZD8tXralEYZTxVvV866aMxzw";
    public static final String PREF_SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    //public static final String DebugBaseEndpoint = "http://192.168.0.100:8000/api/v1/";
    public static final String DebugBaseEndpoint = "http://192.168.0.102:8000/api/v1/";

    public static final String BaseEndpoint = "http://enventure.sparkpl.ug/api/v1/";
    public static final String DEBUG_CLIENT_ID = "uQVxxypFe9H6EMuMbKulEvKlEZY3LW5Ran5irC2D";
    public static final String CLIENT_ID = "mCtR4PGxNhkjBhZRV62xCPESilV2kficJisaendz";

    //public static String ENDPOINT = BaseEndpoint+"/api/v1/";
    public static String AUTH = BaseEndpoint+"/social_auth/convert-token/";
    public static final String PREF_FILE_NAME = "org.enventureenterprises.enventure";

    public static final String FIELD_REPORT_EMAIL = "mossplix@gmail.com";



    public static final int  PLACE_PICKER_REQUEST = 1;
    public static final int  REQUEST_TAKE_PHOTO = 2;
    public static final int REQUEST_ADD_POST = 3;
    public static final int ACTIVITY_SELECT_IMAGE = 4;

    // Used to pass location from MainActivity to PostActivity
    public static final String INTENT_EXTRA_LOCATION = "location";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";

    // Maps values
    public static final String MAPS_INTENT_URI = "geo:0,0?q=";
    public static final String MAPS_NAVIGATION_INTENT_URI = "google.navigation:mode=w&q=";

    public static final int MOBILE_NOTIFICATION_ID = 100;

    public final static int LOGIN_FLOW_CODE = 592_1;

    public static final class RegExpPattern {
        public static final Pattern API = Pattern.compile("\\Aapi\\z");
        public static final Pattern ENVENTURE = Pattern.compile("\\Aenventure\\z");
        public static final Pattern LOCALHOST = Pattern.compile("\\Alocalhost\\z");
        public static final Pattern INTERNAL = Pattern.compile("\\Ainternal\\z");

    }

    public static final String FACEBOOK_LOGIN = "org.enventureenterprises.enventure.intent_facebook_login";
    public static final String FACEBOOK_TOKEN = "org.enventureenterprises.enventure.intent_facebook_token";
    public static final String TWITTER_TOKEN = "org.enventureenterprises.enventure.intent_twitter_token";
    public static final String FACEBOOK_USER = "org.enventureenterprises.enventure.intent_facebook_user";
    public static final String TWITTER_USER = "org.enventureenterprises.enventure.intent_twitter_user";
    public static final String LOGIN_REASON = "org.enventureenterprises.enventure.intent_login_reason";
    public static final String EMAIL = "org.enventureenterprises.enventure.intent_email";


}
