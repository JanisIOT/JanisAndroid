package com.meridian.hackbo;

import android.app.Application;
import android.content.Context;
import android.text.format.DateUtils;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meridian.hackbo.coap.CoapInstance;
import com.meridian.hackbo.coap.CoapTaskQueue;
import com.meridian.hackbo.coap.CoapTaskService;
import com.meridian.hackbo.ui.JanisControl;
import com.meridian.hackbo.ui.StepOne;
import com.meridian.hackbo.ui.StepTwo;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;
import javax.inject.Singleton;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;


public class JanisApplication extends Application {
    private ObjectGraph objectGraph;
    private static final long PROVIDER_ANR_TIMEOUT = 20 * DateUtils.SECOND_IN_MILLIS;


    @Override
    public void onCreate() {
        CoapInit();
        //CoapInitServer();
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        objectGraph = ObjectGraph.create(new CoapModule(this));


    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }




    @Module(
            entryPoints = {
                    JanisControl.class, //
                    StepOne.class, //
                    StepTwo.class, //
                    CoapTaskQueue.class, //
                    CoapTaskService.class //
            }
    )

    static class CoapModule {
        private final Context appContext;

        CoapModule(Context appContext) {
            this.appContext = appContext;
        }

        @Provides
        @Singleton
        CoapTaskQueue provideTaskQueue(Gson gson, Bus bus) {
            return CoapTaskQueue.create(appContext, gson, bus);
        }

        @Provides
        @Singleton
        Bus provideBus() {
            return new Bus();
        }

        @Provides
        @Singleton
        Gson provideGson() {
            return new GsonBuilder().create();
        }
    }



    private void CoapInit() {
        CoapInstance.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }



}

