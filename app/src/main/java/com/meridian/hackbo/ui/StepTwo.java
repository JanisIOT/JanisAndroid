package com.meridian.hackbo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.meridian.hackbo.Config;
import com.meridian.hackbo.JanisA;
import com.meridian.hackbo.JanisApplication;
import com.meridian.hackbo.R;
import com.meridian.hackbo.Settings;
import com.meridian.hackbo.Utils;
import com.meridian.hackbo.coap.CoapTask;
import com.meridian.hackbo.coap.CoapTaskQueue;
import com.meridian.hackbo.coap.events.CoapQueueSizeEvent;
import com.meridian.hackbo.coap.events.CoapSuccessEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
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


public class StepTwo extends Fragment implements OnClickListener {
	
	private static final boolean DEBUG = Config.DEBUG;
    public static final String TAG = StepTwo.class.getSimpleName();
    
    private View view;

    

    private boolean CoapConOpen=false;

    @Inject
    CoapTaskQueue queue; // NOTE: Injection starts queue processing!
    @Inject
    Bus bus;
    private EditText et_nname;
    private EditText et_pass;

    private String ip_a;
    private String curip;
    private int curMask;


    private String[] masks;
    private Timer timer_discovery;
    private Timer timer_network;
    private Button bt_control;
    private TimerTask disco;

    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
    	
    	if(DEBUG) Log.d(TAG, "[FavoriteSites] onCreateView");
        Bundle args=getArguments();
        ((JanisApplication) getActivity().getApplication()).inject(this);
        if(bus!=null)
            bus.register(this);


        view =  inflater.inflate(R.layout.step_two, container, false);
    	//Populate the favorite sites list
    	instanceUI(view);

        performAsyncNetworkCheck();
    	
    	
    	return view;
    	
    }
    public synchronized void coapRequest(int req, String payload){
        String ip= Settings.getIP(getActivity());
        switch (req){
            case CoapTask.setup:
                CoapTask t_setup= new CoapTask(req);
                t_setup.setIp(ip);
                t_setup.setPayload(payload);
                queue.add(t_setup);
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


    public void instanceUI(View view ){

        et_nname=(EditText)view.findViewById(R.id.et_nname);
        bt_control=(Button)view.findViewById(R.id.bt_control);
        bt_control.setOnClickListener(this);
        et_nname.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId== EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            et_pass.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
        et_pass=(EditText)view.findViewById(R.id.et_pass);
        et_pass.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            String name = et_nname.getText().toString();
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);

                            String pwd = et_pass.getText().toString();
                            String payload = String.format("%s=%s=", name, pwd);
                            coapRequest(CoapTask.setup, payload);
                            return true;
                        }
                        return false;
                    }
                });
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

    public boolean isValidNetworkforSSID(String ssid){
        masks= Utils.getLanIP(getActivity());
        //if(masks!=null)
       // Log.d(TAG, Arrays.toString(masks));
        String curSSID=et_nname.getText().toString();
        boolean valid=(ssid!=null&&masks!=null) && (ssid.contains(curSSID)) && (masks.length==3);
        return valid;
    }

    public void performAsyncDiscovery() {
        final Handler handler = new Handler();
        timer_discovery = new Timer();
        disco = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            curMask++;
                            if(curMask>255)curMask=0;
                            coapRequest(CoapTask.disco,null);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer_discovery.schedule(disco, 0,100); //execute in every 50000 ms
    }





    public void performAsyncNetworkCheck() {
        final Handler handler = new Handler();
        timer_network = new Timer();
        final TimerTask net = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            final String SSID= Utils.getSSID(getActivity());
                            if(isValidNetworkforSSID(SSID)){
                                Log.d(TAG, "starting discovery");
                                timer_network.cancel();
                                discovery();

                            }
                        } catch (Exception e) {
                            Log.e(TAG,e.getMessage());
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer_network.schedule(net, 0, 500); //execute in every 50000 ms
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
        }
        else{
            int type=event.event;
            switch (type){
                case CoapTask.DISCOVER:

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


    }





    @Override
    public void onClick(View v) {
        if(CoapConOpen){

//            while (queue.size()>0){
//                queue.remove();
//            }
            ((JanisA)getActivity()).showControl();
        }

        else{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            alertDialog.setTitle("Janis");
            alertDialog.setMessage("No se ah encontrado a Janis en tu red");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }
}
