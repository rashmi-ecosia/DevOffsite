package com.android.airquality.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AirQualRecyclerViewModel extends AndroidViewModel {
    public AirQualRecyclerViewModel(@NonNull Application application) {
        super(application);
    }

    //Based on the DB changes if needed , MVVM architecture will be modified accordingly
}
