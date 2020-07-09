package com.android.airquality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.airquality.R;
import com.android.airquality.model.CityLocations;
import com.android.airquality.model.Results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AirQualAdapter extends RecyclerView.Adapter<AirQualAdapter.AirQualViewHolder> implements Filterable {

    private static final String TAG = "AirQualAdapter";
    private Context mContext;
    private List<Results> resultsList;
    private List<CityLocations> locationsList;

    public AirQualAdapter(Context mContext) {
        this.mContext = mContext;
        this.resultsList = new ArrayList<>();
    }

    public void setResultsList(List<Results> resultsList) {
        this.resultsList = resultsList;
    }

    public void setLocationsList(List<CityLocations> locationsList) {
        this.locationsList = locationsList;
    }

    public List<Results> getResultsList() {
        return resultsList;
    }

    public List<CityLocations> getLocationsList() {
        return locationsList;
    }

    @NonNull
    @Override
    public AirQualViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creating holder from parent view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);
        AirQualViewHolder holder = new AirQualViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AirQualViewHolder holder, int position) {
        //Setting the content to the recycler holder in the given position
        holder.city.setText(resultsList.get(position).getCity());
        holder.country.setText(resultsList.get(position).getCountry());
        holder.location.setText(resultsList.get(position).getLocationId());
        //Currently parameter consists of param, value, unit for all the contents
        // in the given city location
        //This will be modified later and color content will be added
        // To indicate good/Avg/ bad
        holder.param.setText(resultsList.get(position).getParameter());

    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    @Override
    public Filter getFilter() {
        return locFilter;
    }

    private Filter locFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //Scenario where no results are seen
            // This will occur due to sync or when Live data is accessed from DB
            if(resultsList.size() < 1)
                return new FilterResults();

            //This filter will get the latest/ first (as results are published
            // in latest order) parameter value for every unique location in city

            //Normal results form API will give every param separately in the json format

            Map<String, Set<Results>> resultMap = new HashMap<>();

            for(Results res : resultsList) {
                Set<Results> resultset = new HashSet<>();
                if(resultMap.containsKey(res.getLocationId())) {
                    Set<Results> paramSet = resultMap.get(res.getLocationId());
                    for(Results objRes : paramSet) {
                        if(!objRes.equals(res)){
                            resultset.add(objRes);
                        }
                    }

                }
                resultset.add(res);
                resultMap.put(res.getLocationId(), resultset);
            }

            //Creating a map of location ID and location
            //When DB is created we can only check for new ones and add
            //than processing all for every API call

            Map<String, String> locationMap = new HashMap<>();

            for(CityLocations locations : locationsList) {
                if(resultMap.containsKey(locations.getLocationId())) {
                    locationMap.put(locations.getLocationId(), locations.getLocation().get(0));
                }
            }

            //Collect NO2, So2 etc in a list and replace it with location and send result
            List<Results> output = new ArrayList<>();
            for (Map.Entry<String, Set<Results>> entry : resultMap.entrySet()) {
               Set<Results> r = entry.getValue();
               StringBuilder param = new StringBuilder();
               String city = "", country = "";
               for(Results op : r) {
                   param.append(op.getParameter() + " : " + op.getValue() + " " + op.getUnit() );
                   param.append("\n");
                   if(city.isEmpty()) {
                       city = op.getCity();
                       country = op.getCountry();
                   }
               }
               String loc = locationMap.get(entry.getKey());
               output.add(new Results(loc, param.toString(), "", "", city, country ));

            }


            FilterResults result = new FilterResults();
            result.values = output;

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            resultsList.clear();
            resultsList.addAll((List) results.values);
            notifyDataSetChanged(); // Will reflect well when VM is created and DB is added
        }
    };


    //ViewHolder created based on the layout items shown to the user in Recycler view here
    public class AirQualViewHolder extends RecyclerView.ViewHolder {
        private TextView city;
        private TextView country;
        private TextView location;
        private TextView param;

        public AirQualViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
            country = itemView.findViewById(R.id.country);
            location = itemView.findViewById(R.id.location);
            param = itemView.findViewById(R.id.parameter);
        }
    }
}
