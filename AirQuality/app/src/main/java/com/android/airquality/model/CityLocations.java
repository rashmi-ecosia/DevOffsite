package com.android.airquality.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityLocations {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("location")
    @Expose
    private String locationId;

    @SerializedName("locations")
    @Expose
    private List<String> location;

    public CityLocations(String id, String country, String city, String locationId, List<String> location) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.locationId = locationId;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
