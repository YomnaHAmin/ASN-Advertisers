package com.example.android.advertisers;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public static List<AdvertisementClass> ads;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adv, container, false);

        // Refer to the RecyclerView in the layout file and set its parameters
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Define the array of advertisments and fill it
        ads = new ArrayList<>();
        ads.add(new AdvertisementClass(
                1,
                "Test title",
                "Test desc",
                "https://asnasucse18.000webhostapp.com/res/AdvertisersApp/AdsImgs/Test.jpg",
                "5/7/2018"
        ));
//        adapter = new RecyclerViewAdapterClass(ads, this.getContext());
//        recyclerView.setAdapter(adapter);

        loadRecyclerViewData();
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

    private void loadRecyclerViewData(){
        final AdvFragment self = this;

        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading ads ... ");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                __Constants.URL_FETCH_ADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
//                            JSONObject obj = new JSONObject(response);
                            JSONArray obj = new JSONArray(response);
                            Log.d("Fetch Ads Response", ""+response);


                            /*
                                Parse the JSON object here and fill the ads list
                             */

                            for(int i = 0; i < obj.length(); i++){
//                                JSONObject o = obj.getJSONObject(i);
                                JSONObject o = obj.getJSONObject(i);
                                boolean alreadyShownAd = false;
                                for(AdvertisementClass shownAd : ads) {
//                                    if (shownAd.getID() == o.getInt("ID")) {
                                    if (shownAd.getID() == o.getInt("ID")) {
                                        alreadyShownAd = true;
                                        break;
                                    }
                                }
                                if(!alreadyShownAd){
                                    AdvertisementClass ad = new AdvertisementClass(
                                            o.getInt("ID"),
                                            o.getString("title"),
                                            o.getString("description"),
                                            o.getString("imgURL"),
//                                            "https://asnasucse18.000webhostapp.com/res/AdvertisersApp/AdsImgs/Test.jpg",
                                            o.getString("expirationDate")
                                    );
                                    ads.add(ad);
                                    break;
                                }
                            }


                            // Define the adapter and bind it to the RecyclerView
                            adapter = new RecyclerViewAdapterClass(ads, self.getContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID", String.valueOf(__Info.ID));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(stringRequest);
    }
}
