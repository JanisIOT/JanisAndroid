package com.meridian.hackbo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

/**
 * Created by meridianc4m1l0 on 11/8/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String getLocalIpAddress() {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetAddress : Collections.list(intf.getInetAddresses())) {
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
                        String ip_address = inetAddress.getHostAddress();
                        return ip_address;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG,"socket error"+ex.getMessage());
        }
        return null;
    }

    public static String[] getLanIP(Context ctx){
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        if(ipAddress==0)return null;
        else return new String []{String.valueOf((ipAddress & 0xff)), String.valueOf(ipAddress >> 8 & 0xff),
                String.valueOf(ipAddress >> 16 & 0xff)};
    }

    public static String getSSID(Context ctx){
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }




}
