package com.meridian.hackbo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by hasus on 2/26/15.
 */
public class Config {

    //http://api10.savoritelist.com:8080/service/api/
    //public static final String TEST_ROOT       = "http://api10.savoritelist.com:7771/api";
        public static final String TEST_ROOT       = "http://api10.savoritelist.com:3001/api";
    //public static final String TEST_ROOT       = "http://api10.savoritelist.com:8080/service/api";
    public static final String PROD_ROOT        = "http://.globalgw.com";
    public static final String PROD_ROOT_PORT   = ":8084";
    public static final String PROD_MEDIA_PORT  = ":8888";
    public static final String PROD_CONTROLLER  = "//";

    public static final String PROD_SERVER        = PROD_ROOT+PROD_ROOT_PORT+PROD_CONTROLLER;
    public static final String PROD_MEDIA_SERVER  = PROD_ROOT+PROD_MEDIA_PORT+"/";


    //--------------------------------------------------------------------------------------------------------//
    public static final String API_SAVORITE         = TEST_ROOT;  // ==>  R E V I S A R  P A R A  T I E N D A S //


    //GOOGLE PLACES
    public static final String API_GOOGLE ="https://maps.googleapis.com/maps/api/place/textsearch/json";
    public static final String API_GOOGLE_NEAR ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    // PLATFORM SAVORITE

    public static final String API_USERS_SAVORITE              = API_SAVORITE+"/users";
    public static final String API_POIS_SAVORITE              = API_SAVORITE+"/Pois";
    public static final String API_LISTS_SAVORITE              = API_SAVORITE+"/Lists";
    public static final String API_LISTSSHARED_SAVORITE              = API_SAVORITE+"/list/sharedwith";
    public static final String API_SESSION_SAVORITE              = API_USERS_SAVORITE+"/login";
    public static final String API_POISFIND_SAVORITE              = API_SAVORITE+"/Pois/find";
    public static final String API_USERS_IDS_SAVORITE              = API_USERS_SAVORITE+"/ids";

	public static final boolean DEBUG=true;    // ==> R E V I S A R  P A R A  T I E N D A S //
	public static final boolean DEBUG_LOCATION = DEBUG&&true;
	public static final boolean DEBUG_MAP = DEBUG&&true;
	public static final boolean DEBUG_TASKS = DEBUG&&true;

    //--------------------------------------------------------------------------------------------------------//

    public static final String SERVER="http://190.26.198.130:8088/NIVOItinerary";
    public static final String SERVER_MGPLATFORM = "http://terpelservices.globalgw.com:8080/terpel-services";
//    public static final String SERVER="http://terpeltest.meridiangroupsa.com:8080/NIVOItinerary";

    //------------------------------------------=== API ITINERARY ===--------------------------------------------------//
    // itinerary API headers
    public static final String KEY_MGTOKEN = "mgToken";
    public static final String KEY_API   = "apiKey";
    public static final String MERIDIAN_API_KEY   = "4752a5435c81f36cf760a0a00b551e2b";
    // itinerary Keys
    public static final String KEY_PUSHTOKEN = "CloudPush-Id";
    public static final String KEY_DEVICEID = "deviceId";
    public static final String KEY_DEVICETYPE = "deviceType";
    // Devices
	public static final String ANDROID              = "ANDROID";
    public static final String ANDROID_PHONE        = "ANDROID_PHONE";
	public static final String ANDROID_TABLET       = "ANDROID_TABLET";

    //------------------------------------------=== API MG PLATFORM ===--------------------------------------------------//
    // MGPlatform Headers
    public static final String PLATFORM_API_KEY   = "4aa77450e2f343c03125e48dfa1dc2f7";





    //------------------------------------------=== MAP ===--------------------------------------------------//
 	public static final int map_request_limit        = 50;   // Points by request
 	public static final int map_request_maxRadius    = 4500;
 	public static final int map_zoom_init            = 14;
 	public static final int map_zoom_center_point    = 16;
 	public static final int route_request_limit      = 2500;   // Points by request over route
 	public static final int route_request_maxRadius  = 500;  // MaxRadius over route


    //------------------------------------------=== TRACKING ===--------------------------------------------------//
    public static final long LOCATION_INTERVAL = 15000;

    //------------------------------------------=== PUSH ===--------------------------------------------------//

    public static final String SENDER_ID = "980254054239"; // DEBUG TEST
	/**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION ="com.meridian.itinerary.Config.DISPLAY_MESSAGE";
    public static final String TAG = "Savorite";

    public static void displayMessage(Context context, String message, String command, String url, String usr) {
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.GCM_MSG, message);
        intent.putExtra(Config.GCM_CMD, command);
        intent.putExtra(Config.GCM_URL, url);
        intent.putExtra(Config.GCM_USR, usr);
        context.sendBroadcast(intent);
    }

    /**
     * Intent"s extra that contains the message to be displayed.
     */
	public static final String GCM_URL = "url";
    public static final String GCM_MSG = "msg";
    public static final String GCM_CMD = "cmd";
    public static final String GCM_USR = "usr";


}
