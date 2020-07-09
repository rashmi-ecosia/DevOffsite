package com.android.airquality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityLocationSearch {
    @SerializedName("results")
    @Expose
    private List<CityLocations> resultList;

    public List<CityLocations> getResultList() {
        return resultList;
    }

    public void setResultList(List<CityLocations> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "CityLocationSearch{" +
                "resultList=" + resultList +
                '}';
    }
}
