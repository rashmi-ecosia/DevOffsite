package com.android.airquality.model.api;

import com.android.airquality.model.AirQualMeasurement;
import com.android.airquality.model.CityLocationSearch;
import com.android.airquality.model.CityLocations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AirQualityAPI {

    //Currently the default limit is 100 , we can expand based on the need

    @GET("measurements?")
    Call<AirQualMeasurement> getData(@Query("city") String city);

    @GET("locations?")
    Call<CityLocationSearch> getLocations(@Query("city") String city);

}
