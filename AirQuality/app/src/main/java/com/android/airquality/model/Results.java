package com.android.airquality.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    @SerializedName("location")
    @Expose
    private String locationId;

    @SerializedName("parameter")
    @Expose
    private String parameter;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("country")
    @Expose
    private String country;

    public Results(String locationId, String parameter, String value, String unit, String city, String country) {
        this.locationId = locationId;
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
        this.city = city;
        this.country = country;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Results results = (Results) obj;
        if(this.getCity().equals(results.getCity()) && (this.getCountry().equals(results.getCountry()))
                && (this.getParameter().equals(results.getParameter())))
            return true;

        return false;
    }
}