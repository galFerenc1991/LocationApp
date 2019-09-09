package com.example.locationapp.presentation.screens.map;

import android.location.Location;


import com.example.locationapp.data.exeptions.ConnectionLostException;
import com.example.locationapp.domain.AddressRepository.AddressRepository;
import com.example.locationapp.presentation.utils.ToastManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MapPresenter implements MapContract.Presenter {

    private AddressRepository mAddressRepository;
    private MapContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public MapPresenter(AddressRepository _addressRepository, MapContract.View _view) {
        this.mAddressRepository = _addressRepository;
        this.mView = _view;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void getAddress(Location _location) {
        mView.showProgressMain();
        mCompositeDisposable.add(mAddressRepository.getLocationAddress(_location.getLatitude(), _location.getLongitude())
                .subscribe(locationResponse -> {
                    mView.hideProgress();
                    mView.showAddress(locationResponse.getDisplayName());
                }, throwableConsumer));
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast("Connection Lost");
        } else {
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
