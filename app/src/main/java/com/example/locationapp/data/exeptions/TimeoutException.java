package com.example.locationapp.data.exeptions;

public class TimeoutException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Server doesn't respond";
    }
}
