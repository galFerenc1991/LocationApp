package com.example.locationapp;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.android.utils.FlipperUtils;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;
import com.facebook.soloader.SoLoader;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;

@EApplication
public class LocationApp extends Application {

    @SystemService
    protected ConnectivityManager mConnectivityManager;
    private NetworkFlipperPlugin mNetworkFlipperPlugin;


    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);

        mNetworkFlipperPlugin = new NetworkFlipperPlugin();


        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            final FlipperClient client = AndroidFlipperClient.getInstance(this);
            client.addPlugin(new InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()));
            client.addPlugin(mNetworkFlipperPlugin);
            client.start();
        }
    }

    public  NetworkFlipperPlugin getNetworkFlipperPlugin(){
        return mNetworkFlipperPlugin;
    }

    public boolean hasInternetConnection() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
