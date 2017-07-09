package org.enventureenterprises.enventure.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.UUID;

import timber.log.Timber;


public class PrefUtils {
    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static final String PREF_AUTH_TOKEN_TOKEN = "auth_token_token";
    private static final String PREF_AUTH_TOKEN_SECRET = "auth_token_secret";
    private static final String PREF_POST_PHOTO = "pref_post_photo";
    private static final String PREF_POST_LOCATION_LAT = "pref_post_location_lat";
    private static final String PREF_POST_LOCATION_LNG = "pref_post_location_lng";
    private static final String PREF_POST_LOCATION = "pref_post_location";
    private static final String PREF_GCM_KEY = "gcm_key";
    private static final String KEY_SEARCH_DISTANCE = "search_distance";
    private static final String DEFAULT_SEARCH_DISTANCE = "200";

    private static final String PREFERENCES_LAT = "lat";
    private static final String ISSUE_PHOTO = "issue_photo";
    private static final String ISSUE_CATEGORY = "issue_category";
    private static final String ISSUE_TITLE = "issue_title";
    private static final String ISSUE_DESCRIPTION = "issue_descriptiom";

    private static final String PREFERENCES_LNG = "lng";
    private static final String ISSUE_PREFERENCES_LAT = "issue_lat";
    private static final String ISSUE_PREFERENCES_LNG = "issue_lng";
    private static final String PREFERENCES_GEOFENCE_ENABLED = "geofence";
    private static final String DISTANCE_KM_POSTFIX = "km";

    private static final String DISTANCE_M_POSTFIX = "m";
    private static final String IS_LOGGEDIN = "is_loggedin";
    private static final String HAS_JUST_LOGGEDIN = "just_loggedin";
    private static final String PREF_FIXED_LOCATION = "pref_fixed_location";

    private static final String PREF_FIREBASE_TOKEN = "pref_firebase_token";
    private static final String PREF_TOKEN_EXPIRES = "pref_token_expires";

    private static final String FIRST_RUN_FAB = "first_run_fab";
    private static final String FIRST_RUN_SHARE = "first_run_share";
    private static final String FIRST_RUN_SEARCH = "first_run_search";

    private Context context;


    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);


    }

    public static String getFirebaseToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);

        return sp.getString(PREF_FIREBASE_TOKEN, null);

    }

    public static void setFirebaseToken(final Context context,  final String token) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_FIREBASE_TOKEN,
                token).apply();
        Timber.d("Firebase Token: " + token);
    }

    public static Boolean getFirstRun(final Context context,final String type) {
        SharedPreferences sp = getSharedPreferences(context);

        return sp.getBoolean(type, true);

    }

    public static void setFirstRun(final Context context,  final String type) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(type,
                false).apply();
    }





    public static String getAuthToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);

        return sp.getString(PREF_AUTH_TOKEN, null);

    }

    public static void setAuthToken(final Context context,  final String authToken) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_AUTH_TOKEN,
                authToken).apply();
        Timber.d("Auth Token: " + authToken);
    }


    public static long getTokenExpires(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        Timber.d ("getting token");

        return sp.getLong(PREF_TOKEN_EXPIRES, 1L);

    }

    public static void setTokenExpires(final Context context,  final Long tokenExpires) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putLong(PREF_TOKEN_EXPIRES,
                tokenExpires).apply();
        //Timber.d("Token Expires @ " + tokenExpires);
    }


    public static Boolean getLoggedIn(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);

        return sp.getBoolean (IS_LOGGEDIN, false);

    }

    public static void setLoggedIn(final Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean (IS_LOGGEDIN,
                true).apply();
    }

    public static Boolean hasJustLoggedIn(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);

        return sp.getBoolean(HAS_JUST_LOGGEDIN, true);

    }

    public static void setJustLoggedIn(final Context context) {

        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(HAS_JUST_LOGGEDIN,
                false).apply();
    }





    static void invalidateAuthToken(final Context context) {
        // invalidate token
        //setAuthToken(context, null);
    }

    public static boolean hasToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return !TextUtils.isEmpty(sp.getString(PREF_AUTH_TOKEN, null));
    }

    public static void refreshAuthToken(Context mContext) {
        invalidateAuthToken(mContext);

    }

    public static void setGcmKey(final Context context,  final String gcmKey) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(PREF_GCM_KEY,
                gcmKey).apply();
        Timber.d("GCM key of account " +  " set to: " + sanitizeGcmKey(gcmKey));
    }

    public static String getGcmKey(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        String gcmKey = sp.getString(
                PREF_GCM_KEY, null);

        // if there is no current GCM key, generate a new random one
        if (TextUtils.isEmpty(gcmKey)) {
            gcmKey = UUID.randomUUID().toString();
            Timber.d("No GCM key on account " + ". Generating random one: "
                    + sanitizeGcmKey(gcmKey));
            setGcmKey(context, gcmKey);
        }

        return gcmKey;
    }




    public static void setFixedLocation(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean (PREF_FIXED_LOCATION,
                true).apply();
    }

    public static Boolean getFixedLocation(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean (
                PREF_FIXED_LOCATION, false);

    }











    public static String sanitizeGcmKey(String key) {
        if (key == null) {
            return "(null)";
        } else if (key.length() > 8) {
            return key.substring(0, 4) + "........" + key.substring(key.length() - 4);
        } else {
            return "........";
        }
    }

    /**
     * Helper method to register a settings_prefs listener. This method does not automatically handle
     * {@code unregisterOnSharedPreferenceChangeListener() un-registering} the listener at the end
     * of the {@code context} lifecycle.
     *
     * @param context  Context to be used to lookup the {@link SharedPreferences}.
     * @param listener Listener to register.
     */
    public static void registerOnSharedPreferenceChangeListener(final Context context,
                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Helper method to un-register a settings_prefs listener typically registered with
     * {@code registerOnSharedPreferenceChangeListener()}
     *
     * @param context  Context to be used to lookup the {@link SharedPreferences}.
     * @param listener Listener to un-register.
     */
    public static void unregisterOnSharedPreferenceChangeListener(final Context context,
                                                                  SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }


    /**
     * Check if the app has access to fine location permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkFineLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
    }



    public static float getSearchDistance(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return Float.parseFloat(sp.getString(KEY_SEARCH_DISTANCE, DEFAULT_SEARCH_DISTANCE));
    }




    public static void setSearchDistance(float value,Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }



    /**
     * Store if geofencing triggers will show a notification in app preferences.
     */
    public static void storeGeofenceEnabled(Context context, boolean enable) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFERENCES_GEOFENCE_ENABLED, enable);
        editor.apply();
    }


    /**
     * Retrieve if geofencing triggers should show a notification from app preferences.
     */
    public static boolean getGeofenceEnabled(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFERENCES_GEOFENCE_ENABLED, true);
    }




    public static  void clearKeys(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(ISSUE_PHOTO).commit();
        prefs.edit().remove(ISSUE_DESCRIPTION).commit();
        prefs.edit().remove(ISSUE_TITLE).commit();
        prefs.edit().remove(ISSUE_PREFERENCES_LAT).commit();
        prefs.edit().remove(ISSUE_PREFERENCES_LNG).commit();
    }

    public static void setPhoto(Context context,Uri photo){
        if (photo != null) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(ISSUE_PHOTO, photo.toString());

            editor.apply();
        }

    }

    public static Uri getPhoto(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String uri = prefs.getString(ISSUE_PHOTO,null);
        if (uri != null)
        {
            return Uri.parse(uri);
        }

        return null;


    }


    public static void setDescription(Context context,String desc){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ISSUE_DESCRIPTION, desc);

        editor.apply();

    }

    public static String  getDescription(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(ISSUE_DESCRIPTION,null);



    }




    public static  void setTitle(Context context,String title){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ISSUE_TITLE, title);

        editor.apply();

    }

    public static String  getTitle(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
       return prefs.getString(ISSUE_TITLE,null);
    }

    public static Boolean showNotifications(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("show_notifications_whenfriendslike",true);
    }







}
