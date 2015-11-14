package com.meridian.hackbo.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meridian.hackbo.Config;
import com.meridian.hackbo.JanisA;
import com.meridian.hackbo.JanisApplication;
import com.meridian.hackbo.R;
import com.meridian.hackbo.Settings;
import com.meridian.hackbo.Utils;
import com.meridian.hackbo.coap.events.CoapQueueSizeEvent;
import com.meridian.hackbo.coap.events.CoapSuccessEvent;
import com.meridian.hackbo.coap.CoapTask;
import com.meridian.hackbo.coap.CoapTaskQueue;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;


/**
 * Favorite sites list fragment.
 * 
 * <p>
 * List of favorite sites saved previously by the user.
 * The items are brought from the server.
 * </p>
 * <p>
 * it uses setNewSubWindow class
 * </p>
 *  
 * @see
 * @return favoriteSites FavoriteSitesFragment
 *
 */


public class StepOne extends Fragment implements OnClickListener,ColorPicker.OnColorChangedListener {
	
	private static final boolean DEBUG = Config.DEBUG;
    public static final String TAG = StepOne.class.getSimpleName();
    
    private View view;




    @Inject
    CoapTaskQueue queue; // NOTE: Injection starts queue processing!
    @Inject
    Bus bus;

    private ColorPicker picker;
    private Timer timer;
    private ImageView bt_step;
    private boolean CoapConOpen=false;
    private ImageView janis;

    private String ip_a;
    private String curip;
    private int curMask;


    private String[] masks;
    private Timer timer_discovery;
    private Timer timer_network;


    public  int red (int color) {
        return  (color & 0x00ff0000) >> 16;
    }
    public  int  green(int color) {
        return (color & 0x0000ff00) >> 8;
    }

    public  int  blue(int color) {
        return (color & 0x000000ff);
    }

    public void sendColor(int color) {
        int c_color=picker.getColor();
        int r = red(color);
        int g = green(color);
        int b= blue(color);
        //Log.e(TAG, c_color + ",  " + r + "=" + g + "=" + b +(char) r + "=" + (char)g + "=" + (char)b);
        byte lights=16;
        int i=0;
        int jump=0;
        StringBuilder str= new StringBuilder();
        str.append(String.format("%02X", g) );
        str.append(String.format("%02X", r));
        str.append(String.format("%02X", b));
        str.append(String.format("%02X", lights));

        janis.setBackgroundColor(color);
        coapRequest(CoapTask.rgb, str.toString());
    }

    @Override
    public void onColorChanged(int color) {
        sendColor(color);

    }



    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
    	
    	if(DEBUG) Log.d(TAG, "[FavoriteSites] onCreateView");
        Bundle args=getArguments();
        view =  inflater.inflate(R.layout.step_one, container, false);
        ((JanisApplication) getActivity().getApplication()).inject(this);
        picker = (ColorPicker) view.findViewById(R.id.picker);
        janis = (ImageView) view.findViewById(R.id.im_janis);

        picker.setOnColorChangedListener(this);
        bt_step = (ImageView) view.findViewById(R.id.bt_step);
        bt_step.setOnClickListener(this);
        picker.setOnColorChangedListener(this);
        if(bus!=null)
         bus.register(this);

        performAsyncPing();
       return view;
    	
    }

    public boolean isValidNetworkforSSID(String ssid){
        //this should a broadcast receiver to the whole system, to know that is not in a janis_menu network.
        String masks[]= Utils.getLanIP(getActivity());
        //String masks[]=ip.split("\\.");
        return (ssid!=null&&masks!=null)&&(ssid.indexOf("JANIS")!=-1&&masks.length==3);
    }

    public synchronized  void coapRequest(int req, String payload){
            String ip= Settings.getIP(getActivity());
            switch (req){
             case CoapTask.ping:
                    CoapTask t_ping= new CoapTask(req);
                    //if(BuildConfig.DEBUG)
                        Log.d(TAG,"ping"+ ip);
                    t_ping.setIp(ip);
                    queue.add(t_ping);
                    break;
                case CoapTask.rgb:
                    if(CoapConOpen) {
                        CoapTask t_color = new CoapTask(CoapTask.rgb);
                        t_color.setIp(ip);
                        t_color.setPayload(payload.toString());
                        queue.add(t_color);
                    }
                    break;
                case CoapTask.disco:
                    final String endpoint=String.format("%s%s",curip,curMask);
                    Log.d(TAG,"cur endpoint:"+endpoint);
                    CoapTask t_disco = new CoapTask(req);
                    t_disco.setIp(endpoint);
                    queue.add(t_disco);
                    break;
            }
    }




    public void performAsyncPing() {
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask ping = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            coapRequest(CoapTask.ping, null);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(ping, 0, 500); //execute in every 50000 ms
    }


    @SuppressWarnings("UnusedDeclaration") // Used by event bus.
    @Subscribe
    public void onCoapSuccessEvent(CoapSuccessEvent event) {
        String ip=event.url;
        if(ip!=null){
            ((JanisA)getActivity()).dismissLoader();
            Log.e(TAG, "correct disco on IP->" + ip);
            if(timer_discovery!=null)
                timer_discovery.cancel();
            String ip_saved= Settings.getIP(getActivity());
            if(ip_saved.contains("192.168.4.1"))
                Settings.setIP(getActivity(), ip);
            CoapConOpen=true;
            ((JanisA)getActivity()).showControl();
        }
        else{
            int type=event.event;
            switch (type){
                case CoapTask.PONG:
                    timer.cancel();
                    CoapConOpen=true;
                    break;
                case CoapTask.RAINBOW:
                    CoapConOpen=true;
                    break;

            }

        }

    }

    @SuppressWarnings("UnusedDeclaration") // Used by event bus.
    @Subscribe
    public void onQueueSizeChanged(CoapQueueSizeEvent event) {


    }

    @SuppressWarnings("UnusedDeclaration") // Used by event bus.
    @Subscribe
    public void CoapFailEvent(int reason){
        CoapConOpen=false;
        performAsyncPing();


    }









    @Override
	public void onDestroy() {
    	
		//((Main)getSherlockActivity()).titleLeftWindowFlag = false;
		super.onDestroy();
	}






    @Override
    public void onClick(View v) {


        String ssid=Utils.getSSID(getActivity());
        if(isValidNetworkforSSID(ssid)){
            ((JanisA)getActivity()).showStepTwo();
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            alertDialog.setTitle("Janis");
            alertDialog.setMessage("Conectate a Janis para poder continuar");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }





    }
    public void discovery(){
        ((JanisA)getActivity()).showLoader();
        curip=masks[0]+"."+masks[1]+"."+masks[2]+".";
        curMask=1;
        for(;curMask<=255;curMask++){
            //if(curMask>255)curMask=0;
            coapRequest(CoapTask.disco,null);

        }

        //performAsyncDiscovery();
    }

    public void help() {

        discovery();
    }
}
