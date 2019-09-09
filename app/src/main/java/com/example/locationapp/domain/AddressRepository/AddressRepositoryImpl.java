package com.example.locationapp.domain.AddressRepository;

import com.example.locationapp.data.Rest;
import com.example.locationapp.data.model.LocationResponse;
import com.example.locationapp.data.service.AddressService;
import com.example.locationapp.domain.NetworkRepository;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import io.reactivex.Observable;

@EBean(scope = EBean.Scope.Singleton)
public class AddressRepositoryImpl extends NetworkRepository implements AddressRepository {

    @Bean
    protected Rest rest;

    private AddressService mAddressService;

    @AfterInject
    protected void initServices() {
        mAddressService = rest.getAddressService();
    }

    @Override
    public Observable<LocationResponse> getLocationAddress(double lat, double lon) {
        return getNetworkObservable(mAddressService.getAddress(lat, lon));
    }
}
