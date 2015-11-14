/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meridian.hackbo.coap;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.meridian.hackbo.Settings;
import com.squareup.tape.Task;


import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.Request;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class CoapTask implements Task<CoapTask.Callback> {

    private static final String TAG = CoapTask.class.getSimpleName();
    private static final String IMGUR_API_KEY = "74e20e836f0307a90683c4643a2b656e";
    private static final String IMGUR_UPLOAD_URL = "http://api.imgur.com/2/upload";
    private static final Pattern IMGUR_URL_REGEX = Pattern.compile("<imgur_page>(.+?)</imgur_page>");
    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());
    public static final int EVENT = 0;

    public static final int ping = 11;
    public static final int reset = 5;
    public static final int setup = 6;
    public static final int disco = 7;

    public static final int rgb = 3;

    //public String SERVER="coap://192.168.4.1:5683/";

    public static final String PORT=":5683";
    public static final String PROTOCOL="coap://";
    public static final String PING = "/ping";
    public static final String DISCO = "/disco";
    public static final String RGB = "/rgb";
    public static final String SETUP = "/setup";
    public static final String RESET = "/reset";
    public static final int PONG = 0;
    public static final int TIMEOUT = 1;
    public static final int RAINBOW = 2;
    public static final int DISCOVER = 3;
    public static final int SETUPUSER = 4;

    private final int type;
    private String payload;
    private String ip;
    private int ith;
    private int freq;
    private int var;
    private String value="";
    private int state;
    private int context;
    private int fm;
    public static int T_FM_SEEK_UP_FREQ_TUNNING=9;

    static final String end_p1="q?RAMIREZ_DANIEL=1075656913";
    static final String end_p2="photo?ping";
    static final String end_p3="size?ping";
    static final String end_p4="chunk?ping";
    static final String end_p5="reset?ping";
    static final String end_p6="don?ping";
    static final String end_p7="cen";
    static final String end_p8="c?ping";
    static final String end_p9="doff?ping";
    static final String end_p10="dst?ping";

    public int getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public interface Callback {
        void onSuccess(String url);
        void onSuccess(int event);
        void bytesSuccess(byte[] chunky);
        void onFailure(int reason);

    }
    public CoapTask(int type){
        this.type=type;
    }



    void ping(final Callback callback){
        URI uri_server = null;
        final String ip=new String (this.ip);
        try {
            String endpoint=PROTOCOL+ip+PORT+PING;
            uri_server = new URI(endpoint);
            //CoapClient coapCli = new CoapClient();
            CoapClient coapCli = CoapInstance.getCoapInstance();
           // CoapClient coapCli = new CoapClient()
            //Log.d(TAG, getIp() + "ping");
            coapCli.setURI(uri_server.toString());
            Request ping = Request.newGet();
            CoapResponse response=coapCli.advanced(ping);
            if (response!=null) {
                Log.e(TAG, "response received" + "  _> " + response.getResponseText() + " /" + Utils.prettyPrint(response));
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(PONG);
                    }
                });

            } else {
                Log.e(TAG,"dead");
                ping.cancel();
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(TIMEOUT);
                    }
                });
            }
        } catch (URISyntaxException e) {
            Log.e(TAG, "URI exception " +e.getMessage());
        }

    }

    void rgb(final Callback callback){
        URI uri_server = null;
        try {
            final String ip=new String (this.ip);
            String endpoint=PROTOCOL+ip+PORT+RGB;
            uri_server = new URI(endpoint);
            //CoapClient coapCli = new CoapClient();
            CoapClient coapCli = CoapInstance.getCoapInstance();
            coapCli.setURI(uri_server.toString());
            Request rgb = Request.newGet().setPayload(this.getPayload());
            CoapResponse response=coapCli.advanced(rgb);
        if (response!=null) {
            Log.e(TAG, "response received" + "  _> " + response.getResponseText() + " /" + Utils.prettyPrint(response));
            MAIN_THREAD.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(RAINBOW);
                }
            });

        } else {
            Log.e(TAG, "dead");
            rgb.cancel();
            MAIN_THREAD.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(TIMEOUT);
                }
            });
        }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    void setup(final Callback callback){

        URI uri_server = null;
        try {
            final String ip=new String (this.ip);
            String endpoint=PROTOCOL+ip+PORT+SETUP;
            uri_server = new URI(endpoint);
            //CoapClient coapCli = new CoapClient();
            CoapClient coapCli = CoapInstance.getCoapInstance();
            coapCli.setURI(uri_server.toString());
            Request setup = Request.newGet().setPayload(this.getPayload());
            CoapResponse response=coapCli.advanced(setup);
            if (response!=null) {
                //Log.e(TAG, "response received" + "  _> " + response.getResponseText() + " /" + Utils.prettyPrint(response));
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(SETUPUSER);
                    }
                });

            } else {
                setup.cancel();
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(SETUPUSER);
                    }
                });
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    void reset(final Callback callback){

        URI uri_server = null;
        try {
            final String ip=new String (this.ip);
            String endpoint=PROTOCOL+ip+PORT+RESET;
            uri_server = new URI(endpoint);
            //CoapClient coapCli = new CoapClient();
            CoapClient coapCli = CoapInstance.getCoapInstance();
            coapCli.setURI(uri_server.toString());
            Request rgb = Request.newGet();
            CoapResponse response=coapCli.advanced(rgb);
        if (response!=null) {
            //Log.e(TAG, "response received" + "  _> " + response.getResponseText() + " /" + Utils.prettyPrint(response));
            MAIN_THREAD.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(SETUPUSER);
                }
            });

        } else {
            rgb.cancel();
            MAIN_THREAD.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(SETUPUSER);
                }
            });
        }
        } catch (URISyntaxException e) {
        e.printStackTrace();
     }
    }




    void disco(final Callback callback){
        //Log.d(TAG, ip + "ping");
        // @"%@=%@="
        URI uri_server = null;
        try {
            final String ip=new String (this.ip);
            String endpoint=PROTOCOL+ip+PORT+DISCO;
            uri_server = new URI(endpoint);
            CoapClient coapCli = CoapInstance.getCoapInstance();
            //CoapClient coapCli = new CoapClient();
            coapCli.setURI(uri_server.toString());
            coapCli.setTimeout(100);
            Request discover = Request.newGet();

            CoapResponse response=coapCli.advanced(discover);
            if (response!=null) {
                Log.e(TAG, "response received" + "  _> " + response.getResponseText() + " /" + Utils.prettyPrint(response));
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(ip);
                    }
                });

            } else {
                Log.e(TAG, "not found"+ endpoint);
                discover.cancel();
                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(TIMEOUT);
                    }
                });
            }
        } catch (URISyntaxException e) {
            Log.e(TAG,"error"+e.getMessage());
        }
    }




    @Override
    public void execute(final Callback callback) {
        // Image uploading is slow. Execute HTTP POST on a background thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        switch (getType()){
                            case ping:
                                ping(callback);
                                break;
                            case reset:
                                reset(callback);
                                break;
                            case disco:
                                disco(callback);
                                break;
                            case setup:
                                setup(callback);
                            break;
                            case rgb:
                                rgb(callback);
                                break;

                        }

                    }catch (RuntimeException e) {
                        e.printStackTrace();
                        throw e;
                    }


            }
        }).start();

    }
}
