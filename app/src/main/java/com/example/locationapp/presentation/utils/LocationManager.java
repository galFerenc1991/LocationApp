package com.example.locationapp.presentation.utils;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class LocationManager implements LifecycleObserver {

    private final AppCompatActivity mActivity;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private MutableLiveData<Location> liveData = new MutableLiveData<>();


    public LocationManager(AppCompatActivity _activity) {
        this.mActivity = _activity;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);

    }

    public LiveData<Location> getData() {
        return liveData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void updateLocation() {
        checkLocationRequestSettings();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                liveData.setValue(locationResult.getLastLocation());
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopUpdateLocation() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private void checkLocationRequestSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(getLocationRequest());

        SettingsClient client = LocationServices.getSettingsClient(mActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(mActivity, locationSettingsResponse -> {
            startUpdateLocation();
        });

        task.addOnFailureListener(mActivity, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(mActivity,
                            Constants.PERMISSIONS_REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                }
            }
        });
    }

    private void startUpdateLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mFusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, null);
    }


    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }
        return mLocationRequest;
    }

}
