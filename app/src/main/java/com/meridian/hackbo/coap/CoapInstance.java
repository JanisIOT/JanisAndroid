package com.meridian.hackbo.coap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;


import com.meridian.hackbo.Settings;

import org.eclipse.californium.core.CoapClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper class that is used to provide references to initialized
 * RequestQueue(s) and ImageLoader(s)
 * 
 * @author Ognyan Bankov
 * 
 */
public class CoapInstance {
	// private static final int MAX_IMAGE_CACHE_ENTIRES = 100;
    private static final String COLLECTOR_TESTING4 = "coap://192.168.1.56:5683/r/hello";
    private static final String SERVER_A = "coap://192.168.1.51:5683/r/";
    //private static final String SERVER_PROD = "coap://52.6.145.227:5683/r/";
    //private static final String SERVER_PROD = "coap://52.6.129.243:5683/r/";

    private static final String SERVER_PROD = "coap://52.5.123.253:5683/r/";
    //private static final String SERVER_PROD = "coap://52.7.33.126:5683/r/";
    private static final String SERVER_B = "coap://192.168.2.216:5683/r/";
    private static final String COLLECTOR_PRODUCTION = "coap://54.68.246.223:5683/r/hello";
    private static final String COLLECTOR_TESTING2 = "coap://192.168.10.148:5683/r/hello";

    private static final String policy_report="dismiss";
    private static final String policy_order="new_policy";
    public static final String COLLECTOR_POLICIES_REPORT = SERVER_PROD+policy_report;
    public static final String COLLECTOR_POLICIES_ORDERS = SERVER_PROD+policy_order;

    private static final int EVENT = 0;
    private static URI uri_server;
    private static Context coapContext;
    private Context californiumCtx;
    private static CoapClient client;
    private static String TAG=CoapInstance.class.getSimpleName();
    public  static CoapListener coapCMD;

    public interface CoapListener{
        public void coap_cmd(String cmd);
    }

    public static void setCoapCMD(CoapListener listen) {
        coapCMD = listen;
    }

    private CoapInstance() {
		// no instances
	}

    public static void connect(Context context) {
            client = new CoapClient();

    }


	public static void init(Context context) {
        if(client==null){
            connect(context);
        }
        else{
            client.shutdown();
            client=null;
           connect(context);
        }
	}





    public static CoapClient getCoapInstance() {
        if (client == null) {
            client= new CoapClient();
        }
        return client;
    }

}