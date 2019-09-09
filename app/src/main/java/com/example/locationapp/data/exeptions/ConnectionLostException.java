package com.example.locationapp.data.exeptions;

public class ConnectionLostException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Internet connection lost";
    }
}

