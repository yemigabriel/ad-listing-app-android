package com.doxa360.android.dutch;

import android.app.Application;


/**
 * Created by Apple on 12/04/16.
 */
public class Dutch extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qD7VMOEhMBMrUz7uT2wnMm78J";
    private static final String TWITTER_SECRET = "aCcLnHZwQqbBv5ljQZ7UB55IcBNmHteZAGDb3lJ4AHNTw6vIZN";
    private static final String GOOGLE_API_KEY ="AIzaSyCKCYNKw7QnqER_Ig4N3VCkKk59eEaJJVQ";

    public static final String SERVER_BASE_URL = "http://www.dutch.ng/";
    public static final String PHOTO_URL = "http://dutch.ng/uploads/";

    public static final String SIGN_UP_API = "doxa360/api/v1/user/create"; //method = post; params = email,username,name,password
    public static final String SIGN_IN_API = "doxa360/api/v1/user/login"; //method = post; params = email,password
//    public static final String GET_USER_DETAILS_API = "doxa360/api/v1/user/authenticate"; //method = get; params = token
    public static final String EDIT_PROFILE_API = "doxa360/api/v1/user/edit";
//    public static final String EDIT_PHONE_API = "doxa360/api/v1/user/edit_profile";

    public static final String ALL_CATEGORIES = "doxa360/api/v1/categories";
    public static final String ALL_STATES = "doxa360/api/v1/states";
    public static final String ALL_RECENT_ITEMS = "doxa360/api/v1/home";
    public static final String ALL_USER_ADS = "doxa360/api/v1/user/ads";
    public static final String ALL_CATEGORY_ITEMS = "doxa360/api/v1/adsByCategory";//category/items";
    public static final String SEARCH_API = "doxa360/api/v1/search"; //params = q= search query, l=location

    public static final String GET_INDUSTRY_API = "doxa360/api/v1/industry"; //method = get; params = token

    public static final String UPLOAD_PHOTO_API = "doxa360/api/v1/media/upload";

    public static final String TOKEN = "token";
    public static final String SEARCH = "search";
    public static final String Q = "q";

    public static final String AD = "AD";
    public static final String CATEGORY = "CATEGORY";
    public static final String STATE = "STATE";

//    public static final String PARSE_APP_ID = "anSwU1zWd1pJvRnm7ajbkPr7L83ZBsZex04uPm5j";
//    public static final String PARSE_CLIENT_KEY = "SOWnAIYJWbN5lmbapcEvoaYFwd4d4t1dHhrIDHRq";
    public static final String CONTACT_ID = "CONTACT_ID";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String CONTACT_PHONE = "CONTACT_PHONE";
    public static final String CONTACT_PHOTO = "CONTACT_PHOTO";
    public static final int LOCATION_DISTANCE_CLOSE = 1;
    public static final int LOCATION_DISTANCE_MEDIUM = 2;
    public static final int LOCATION_DISTANCE_FARTHER = 4;
//    public static final String PHOTO_URL = "http://hollanow.com/assets/photos/users/";
    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String GET_USER_BY_INDUSTRY_API = "doxa360/api/v1/user/industry";
    public static final String INDUSTRY = "industry";
    public static final String NEARBY_USERS_API = "doxa360/api/v1/user/nearby_users";
    public static final String LATTITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String DISTANCE_KM = "distKm";
    public static final String CALLNOTE_BY_PHONE = "doxa360/api/v1/user/callnote";
    public static final String USERS_BY_CONTACTS = "doxa360/api/v1/user/contacts";
    public static final String CONTACT_LIST = "contacts";
    public static final String PHONE = "phone";
    public static final String PASSWORD_RESET = "http://hollanow.com/password/reset";
    public static final String SAVE_CALLNOTE = "doxa360/api/v1/user/callnote/create";


    @Override
    public void onCreate() {
        super.onCreate();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//                Fabric.with(com.doxa360.android.dutch.Dutch.this, new TwitterCore(authConfig), new Crashlytics(), new Digits.Builder().build());
//
//            }
//        };
//        runnable.run();

    }
}
