package com.example.locationapp.presentation.screens.map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.locationapp.R;
import com.example.locationapp.domain.AddressRepository.AddressRepositoryImpl;
import com.example.locationapp.presentation.utils.Constants;
import com.example.locationapp.presentation.utils.LocationManager;
import com.example.locationapp.presentation.utils.ToastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;


@EFragment
public class MapFragment extends Fragment implements MapContract.View, OnMapReadyCallback {

    private MapContract.Presenter mPresenter;
    private GoogleMap mMap;
    @ViewById(R.id.tvAddress)
    protected TextView tvAddress;
    @ViewById(R.id.pbMain)
    protected ProgressBar pbMain;
    protected AppCompatActivity mActivity;
    @Bean
    protected AddressRepositoryImpl mAddressRepository;

    private SupportMapFragment mapFragment;
    private LocationManager mLocationManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();

        mLocationManager = new LocationManager(mActivity);
        getLifecycle().addObserver(mLocationManager);
        initLocationLiveData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        return rootView;
    }

    private void initLocationLiveData() {
        LiveData<Location> liveData = mLocationManager.getData();
        liveData.observe(this, location -> {
            mPresenter.getAddress(location);
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, Constants.GOOGLE_MAP_ZOOM));
            mMap.addMarker(new MarkerOptions().position(currentPosition));
        });
    }

    @Override
    public void showAddress(String _address) {
        tvAddress.setVisibility(View.VISIBLE);
        tvAddress.setText(_address);
    }

    @AfterInject
    @Override
    public void initPresenter() {
        mPresenter = new MapPresenter(mAddressRepository, this);
    }

    @Override
    public void showProgressMain() {
        pbMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbMain.setVisibility(View.GONE);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.updateLocation();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                ToastManager.showToast("Please, allow for Application access to Find Location.");
            }
        }
    }

    @OnActivityResult(Constants.PERMISSIONS_REQUEST_CHECK_SETTINGS)
    void result(int resultCode) {
        if (resultCode == Activity.RESULT_OK)
            mLocationManager.updateLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }
}
