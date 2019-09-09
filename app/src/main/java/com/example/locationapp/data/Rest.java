package com.example.locationapp.data;

import com.example.locationapp.LocationApp;
import com.example.locationapp.data.exeptions.ConnectionLostException;
import com.example.locationapp.data.exeptions.TimeoutException;
import com.example.locationapp.data.service.AddressService;
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@EBean(scope = EBean.Scope.Singleton)
public class Rest {

    private Retrofit retrofit;

    @App
    protected LocationApp application;

    public Rest() {
    }

    @AfterInject
    public void initRest() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(RestConstants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RestConstants.TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(RestConstants.TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addNetworkInterceptor(new FlipperOkhttpInterceptor(application.getNetworkFlipperPlugin()))
                .addInterceptor(chain -> {
                    try {
                        if (!application.hasInternetConnection()) {
                            throw new ConnectionLostException();
                        } else {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();

                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter(RestConstants.CONTENT_TYPE_KEY, RestConstants.VALUE_JSON).build();
                            Request.Builder requestBuilder = chain.request().newBuilder()
                                    .header(RestConstants.CONTENT_TYPE_KEY, RestConstants.VALUE_JSON)
                                    .url(url);
                            return chain.proceed(requestBuilder.build());
                        }
                    } catch (SocketTimeoutException e) {
                        throw new TimeoutException();
                    }
                });


        retrofit = new Retrofit.Builder()
                .baseUrl(RestConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();

    }

    public AddressService getAddressService() {
        return retrofit.create(AddressService.class);
    }


}

