package com.example.locationapp.data.service;


import com.example.locationapp.data.model.LocationResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AddressService {

    @GET("/reverse.php")
    Observable<LocationResponse> getAddress(@Query("lat") double _lat,
                                            @Query("lon") double _lon);
}
