package com.example.locationapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocationResponse implements Parcelable {

    private double lat;
    private double lon;

    @SerializedName("display_name")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.displayName);
    }

    public LocationResponse() {
    }

    protected LocationResponse(Parcel in) {
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.displayName = in.readString();
    }

    public static final Creator<LocationResponse> CREATOR = new Creator<LocationResponse>() {
        @Override
        public LocationResponse createFromParcel(Parcel source) {
            return new LocationResponse(source);
        }

        @Override
        public LocationResponse[] newArray(int size) {
            return new LocationResponse[size];
        }
    };
}
