package com.android.airquality;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.airquality.adapter.AirQualAdapter;
import com.android.airquality.model.AirQualMeasurement;
import com.android.airquality.model.CityLocationSearch;
import com.android.airquality.model.CityLocations;
import com.android.airquality.model.Results;
import com.android.airquality.model.api.AirQualityAPI;
import com.android.airquality.util.Constants;
import com.android.airquality.viewmodel.AirQualRecyclerViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private AirQualAdapter mAdapter;
    private SearchView mSearch;
    private List<Results> airQualResult;
   // private AirQualRecyclerViewModel airQualViewModel;
    private String mSearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Recycler View
        initRecycler();

        //Setting default search on launch of the application
        //Current default string is "Berlin"
        mSearchKey = getString(R.string.default_string);
        querySearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Search view initialization
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearch = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearch.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Hide the keyboard to show results
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearch.getWindowToken(),0);

                //OpenAQ API is case sensitivite, hence based on user search the expected string of
                //that query is provided for search
                mSearchKey =  query.substring(0,1).toUpperCase() + query.substring(1);
                querySearch();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //Can be implemented later based on char input
                return false;
            }
        });
        return true;
    }

    //Initializing Recycler view
    private void initRecycler() {
        Log.d(TAG, "initRecycler: create");
        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new AirQualAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //Layout manager will be modified to Horizontal view
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    private void querySearch() {
        //Using Retrofit for the API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirQualityAPI api = retrofit.create(AirQualityAPI.class);
        Call<AirQualMeasurement> callSearch = api.getData(mSearchKey);

        final Call<CityLocationSearch> callLocation = api.getLocations(mSearchKey);


        callSearch.enqueue(new Callback<AirQualMeasurement>() {
            @Override
            public void onResponse(Call<AirQualMeasurement> call, Response<AirQualMeasurement> response) {
                if(!response.isSuccessful())
                    Log.d(TAG, "onResponse: Failure ");

                //Response receives the common results for the city - multiple hits of data
                airQualResult = response.body().getResultList();

                //Location ID if given needs to be matched to the location
                //Hence another call is initiated
                if(airQualResult.size() > 0) {

                    //Map the ID to location
                    callLocation.enqueue(new Callback<CityLocationSearch>() {
                        @Override
                        public void onResponse(Call<CityLocationSearch> call, Response<CityLocationSearch> response) {
                            if (!response.isSuccessful())
                                Log.d(TAG, "onResponse: Failure ");

                            //Currently receiving few params including location ,
                            // Will be extended more to get more data to the user
                            List<CityLocations> locations = response.body().getResultList();
                            mAdapter.setResultsList(airQualResult);
                            mAdapter.setLocationsList(locations);
                            //This is the key to filter all the searches and obtain the desired result
                            mAdapter.getFilter().filter(mSearchKey);

                        }

                        @Override
                        public void onFailure(Call<CityLocationSearch> call, Throwable t) {
                            Log.d(TAG, "onResponse: Failure ");
                        }
                    });
                }
                else {
                    Log.d(TAG, "onResponse: No response received");
                    Toast.makeText(getApplicationContext(),  getString(R.string.failure), Toast.LENGTH_LONG).show();

                }
                mAdapter = new AirQualAdapter(getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onFailure(Call<AirQualMeasurement> call, Throwable t) {
                Log.d(TAG, "onResponse: Failure ");
                Toast.makeText(getApplicationContext(), getString(R.string.failure), Toast.LENGTH_LONG).show();
            }
        });
    }
}
