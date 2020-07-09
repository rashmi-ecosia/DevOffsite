package com.android.airquality.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirQualMeasurement {
    @SerializedName("results")
    @Expose
    private List<Results> resultList;

    public List<Results> getResultList() {
        return resultList;
    }

    public void setResultList(List<Results> resultList) {
        this.resultList = resultList;
    }

    public Map<String, Results> getLatestResults() {
        Map<String, Results> output = new HashMap<>();
        for(Results res: resultList) {
            output.put(res.getLocationId(), res);
        }
        return output;
    }

    @Override
    public String toString() {
        return "AirQualMeasurement{" +
                "resultList=" + resultList +
                '}';
    }
}
