package com.example.locationapp.presentation.screens.map;

import android.location.Location;

import com.example.locationapp.presentation.base.BasePresenter;
import com.example.locationapp.presentation.base.BaseView;

public interface MapContract {
    interface View extends BaseView<Presenter> {
        void showAddress(String _address);
    }

    interface Presenter extends BasePresenter {

        void getAddress(Location _location);
    }
}
