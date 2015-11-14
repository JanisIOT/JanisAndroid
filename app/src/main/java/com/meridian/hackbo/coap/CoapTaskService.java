// Copyright 2012 Square, Inc.
package com.meridian.hackbo.coap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.meridian.hackbo.JanisApplication;
import com.meridian.hackbo.coap.events.CoapFailEvent;
import com.meridian.hackbo.coap.events.CoapSuccessEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;


public class CoapTaskService extends Service implements CoapTask.Callback {
  private static final String TAG = "Tape:CoapTaskService";

  @Inject
  CoapTaskQueue queue;
  @Inject Bus bus;

  private boolean running;

  @Override
  public void onCreate() {
    super.onCreate();
    ((JanisApplication) getApplication()).inject(this);
    Log.i(TAG, "Service starting!");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    executeNext();
    return START_STICKY;
  }

  private void executeNext() {
    if (running) return; // Only one task at a time.

    CoapTask task = queue.peek();
    if (task != null) {
      running = true;
      task.execute(this);
    } else {
      Log.i(TAG, "Service stopping!");
      stopSelf(); // No more tasks are present. Stop.
    }
  }

//  @Override
//  public void onSuccess(final String url) {
//    running = false;
//    queue.remove();
//    bus.post(new CoapSuccessEvent(url));
//    executeNext();
//  }

  @Override
  public void onSuccess(String url) {
    running = false;
    queue.remove();
    bus.post(new CoapSuccessEvent(url));
    executeNext();
  }

  @Override
  public void onSuccess(int event) {
    running = false;
    queue.remove();
    bus.post(new CoapSuccessEvent(event));
    executeNext();
  }

  @Override
  public void bytesSuccess(byte[] chunky) {
    running = false;
    queue.remove();
    if(bus!=null)
    bus.post(new CoapSuccessEvent(chunky));
    executeNext();
  }

  @Override
  public void onFailure(int reason) {
    queue.remove();
    if(bus!=null)
      bus.post(new CoapFailEvent(reason));
    running=false;
    executeNext();


  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
