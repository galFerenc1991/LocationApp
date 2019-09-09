package com.example.locationapp.domain.AddressRepository;


import com.example.locationapp.data.model.LocationResponse;

import io.reactivex.Observable;

public interface AddressRepository {
    Observable<LocationResponse> getLocationAddress(double lat, double lon);
}
