package com.meridian.hackbo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.meridian.hackbo.ui.JanisControl;
import com.meridian.hackbo.ui.StepOne;
import com.meridian.hackbo.ui.StepTwo;
import com.meridian.hackbo.ui.Support;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class JanisA extends AppCompatActivity {

    private static final int JANIS = 1;
    private static final int SUPPORT = 2;
    private static final String TAG = JanisA.class.getSimpleName();
    private StepOne stepOne;
    private Toolbar toolbar;
    private Support support;
    private Drawer result;
    private StepTwo stepTwo;
    private JanisControl control;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_janis);

        initDrawer();
        String ip= Settings.getIP(this);
        if(ip.contains("192.168.4.1"))
            showStepOne();
        else{
            showControl();
            //Settings.setIP(this,"192.168.4");
            //showStepOne();
        }
        loader = new ProgressDialog(this);




    }


    public void initDrawer(){

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.BLUE);
        toolbar.setTitle("Janis");

        setSupportActionBar(toolbar);



        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
//                .withHeader(R.layout.header_profile)
                        //.withDrawerGravity(10)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Janis").withIdentifier(JANIS),
                        new PrimaryDrawerItem().withName("Soporte").withIdentifier(SUPPORT)

                )
                .withSelectedItem(JANIS)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == JANIS) {
                                showStepOne();
                                result.closeDrawer();

                            } else if (drawerItem.getIdentifier() == SUPPORT) {
                                showSuppport();
                                result.closeDrawer();
                            }

                        }
                        return true;
                    }


                })
                .build();

        //disable scrollbar :D it's ugly
        //result.getListView().setVerticalScrollBarEnabled(false);

        //drawerResult = result;





    }



    public void showStepOne() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        stepOne = new StepOne();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.content_default, stepOne);
        ft.addToBackStack(StepOne.TAG);
        ft.commitAllowingStateLoss();
        invalidateOptionsMenu();
    }

    public void showStepTwo(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        stepTwo = new StepTwo();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.content_default, stepTwo);
        ft.addToBackStack(StepTwo.TAG);
        ft.commitAllowingStateLoss();
        invalidateOptionsMenu();
    }

    public void showControl(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        control = new JanisControl();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.content_default, control);
        ft.addToBackStack(JanisControl.TAG);
        ft.commitAllowingStateLoss();
        invalidateOptionsMenu();
    }


    public void showSuppport() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        support = new Support();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.content_default, support);
        ft.addToBackStack(StepOne.TAG);
        ft.commitAllowingStateLoss();
        invalidateOptionsMenu();
    }

//    @Override
//    public void onBackPressed() {
//
//    }

    public boolean isFragmentUIActive(Fragment f) {
        return f!=null&& f.isVisible() && f.isAdded() && !f.isDetached() && !f.isRemoving();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.janis_menu, menu);
        if(isFragmentUIActive(stepOne)){
            menu.findItem(R.id.action_help).setVisible(true);
            menu.findItem(R.id.action_reset).setVisible(false);

        }
        else if(isFragmentUIActive(stepTwo)){
            menu.findItem(R.id.action_reset).setVisible(false);
            menu.findItem(R.id.action_help).setVisible(false);

        }
        else if(isFragmentUIActive(control)){
            menu.findItem(R.id.action_reset).setVisible(true);
            menu.findItem(R.id.action_help).setVisible(false);

        }
        return true;
    }


    public void showLoader() {
        if (loader != null &&!loader.isShowing()) {
            loader.show();
        }
    }
    public void dismissLoader() {
        if (loader != null &&loader.isShowing()) {
            loader.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       switch (id){
           case R.id.action_help:
               stepOne.help();
               break;
           case R.id.action_reset:
               Settings.setIP(this,"192.168.4.1");
               Settings.setSSID(this,null);
               control.reset();
               showStepOne();

               break;
       }

        return super.onOptionsItemSelected(item);
    }

//    public void discovery(){
//        curip=masks[0]+"."+masks[1]+"."+masks[2]+".";
//        curMask=1;
//        performAsyncDiscovery();
//    }
//
//    public boolean isValidNetworkforSSID(String ssid){
//        masks= Utils.getLanIP(this);
//        //if(masks!=null)
//        // Log.d(TAG, Arrays.toString(masks));
//        //String curSSID=et_nname.getText().toString();
//
//        //boolean valid=(ssid!=null&&masks!=null) && (ssid.contains(curSSID)) && (masks.length==3);
//        //return valid;
//        return true;
//    }
//
//    public void performAsyncDiscovery() {
//        final Handler handler = new Handler();
//        timer_discovery = new Timer();
//        TimerTask disco = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            curMask++;
//                            if(curMask>255)curMask=0;
//                            //coapRequest(CoapTask.disco,null);
//
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer_discovery.schedule(disco, 0, 500); //execute in every 50000 ms
//    }
//
//
//    public void performAsyncNetworkCheck() {
//        final Handler handler = new Handler();
//        timer_network = new Timer();
//        final TimerTask net = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            final String SSID = Utils.getSSID(JanisA.this);
//                            if (isValidNetworkforSSID(SSID)) {
//                                Log.d(TAG, "starting discovery");
//                                discovery();
//                                timer_network.cancel();
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.getMessage());
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer_network.schedule(net, 0, 500); //execute in every 50000 ms
//
//
//    }

    }
