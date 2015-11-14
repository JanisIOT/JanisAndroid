package com.meridian.hackbo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.meridian.hackbo.Config;
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

import java.util.Random;

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


public class JanisControl extends Fragment implements OnClickListener ,ColorPicker.OnColorChangedListener{
	
	private static final boolean DEBUG = Config.DEBUG;
    public static final String TAG = JanisControl.class.getSimpleName();
    
    private View view;
    private boolean CoapConOpen=false;

    
    private ListView favorite_sites_list;
	private EditText et_name;
	private EditText et_street;
	private EditText et_phone;
	private EditText et_website;
	private EditText et_cousine;
	private Button bt_save;
    private String name;
    private String f_name;
    private String f_address;
    private String f_phone;
    private String f_web;
    private String f_cousine;
    private boolean isDetail;

    @Inject
    CoapTaskQueue queue; // NOTE: Injection starts queue processing!
    @Inject
    Bus bus;
    private ColorPicker picker;
    private ImageView janis;
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
                    CoapTask t_color = new CoapTask(CoapTask.rgb);
                    t_color.setIp(ip);
                    t_color.setPayload(payload.toString());
                    queue.add(t_color);
                break;
            case CoapTask.reset:
                CoapTask t_reset= new CoapTask(req);
                Log.d(TAG, "reset" + ip);
                t_reset.setIp(ip);
                queue.add(t_reset);
                break;
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
    	if(DEBUG) Log.d(TAG, "[FavoriteSites] onCreateView");
        Bundle args=getArguments();
        ((JanisApplication) getActivity().getApplication()).inject(this);
        if(bus!=null)
            bus.register(this);
        view =  inflater.inflate(R.layout.controljanis, container, false);
    	instanceUI();
    	return view;
    	
    }


    public void instanceUI(){
        picker = (ColorPicker) view.findViewById(R.id.picker);
        janis = (ImageView) view.findViewById(R.id.im_janis);
        picker.setOnColorChangedListener(this);

    }
    




    @Override
    public void onClick(View v) {


    }


    @SuppressWarnings("UnusedDeclaration") // Used by event bus.
    @Subscribe
    public void onCoapSuccessEvent(CoapSuccessEvent event) {
        int type=event.event;
        CoapConOpen=true;
        switch (type){
            case CoapTask.DISCOVER:
                CoapConOpen=true;
                break;


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


    public void reset() {
        coapRequest(CoapTask.reset,null);
    }
}
