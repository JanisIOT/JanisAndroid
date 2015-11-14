package com.meridian.hackbo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.meridian.hackbo.Config;
import com.meridian.hackbo.R;
import com.meridian.hackbo.coap.CoapTask;
import com.meridian.hackbo.coap.CoapTaskQueue;
import com.squareup.otto.Bus;

import java.util.Timer;

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


public class Support extends Fragment  {
	
	private static final boolean DEBUG = Config.DEBUG;
    public static final String TAG = Support.class.getSimpleName();
    
    private View view;

    @Inject
    CoapTaskQueue queue; // NOTE: Injection starts queue processing!
    @Inject
    Bus bus;

    
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
    private ColorPicker picker;
    private Timer timer;
    private ImageView bt_step;


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
        Log.e(TAG, c_color + ",  " + r + "=" + g + "=" + b +(char) r + "=" + (char)g + "=" + (char)b);
        byte lights=16;
        int i=0;
        int jump=0;
        StringBuilder str= new StringBuilder();
        str.append(String.format("%02X", g) );
        str.append(String.format("%02X", r) );
        str.append(String.format("%02X", b) );
        str.append(String.format("%02X", lights) );
        queue.add(new CoapTask(CoapTask.rgb));
    }





    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
    	
    	if(DEBUG) Log.d(TAG, "[FavoriteSites] onCreateView");
        Bundle args=getArguments();
        view =  inflater.inflate(R.layout.support, container, false);


       return view;
    	
    }









    @Override
	public void onDestroy() {
    	
		//((Main)getSherlockActivity()).titleLeftWindowFlag = false;
		super.onDestroy();
	}





}
