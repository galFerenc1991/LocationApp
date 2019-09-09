package com.example.locationapp.data;

public abstract class RestConstants {
    static final String BASE_URL = "https://nominatim.openstreetmap.org/";

    static final String CONTENT_TYPE_KEY = "format";
    public static final String VALUE_JSON = "json";

    static final long TIMEOUT = 30; //seconds
    static final long TIMEOUT_READ = 30; //seconds
    static final long TIMEOUT_WRITE = 60; //seconds
}