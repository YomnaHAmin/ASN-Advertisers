package com.example.android.advertisers;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment{
    TextView name, slogan, serviceType, availibility, adrs, phone, email, licence, lat, lng, atit;
    ImageView icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        icon = (ImageView) rootView.findViewById(R.id.icon);
        name = (TextView) rootView.findViewById(R.id.name);
        slogan = (TextView) rootView.findViewById(R.id.slogan);
        serviceType = (TextView) rootView.findViewById(R.id.serviceType);
        availibility = (TextView) rootView.findViewById(R.id.availibilityValue);
        adrs = (TextView) rootView.findViewById(R.id.adrsValue);
        phone = (TextView) rootView.findViewById(R.id.phoneValue);
        email = (TextView) rootView.findViewById(R.id.emailValue);
        licence = (TextView) rootView.findViewById(R.id.licenceValue);
        lat = (TextView) rootView.findViewById(R.id.locationLat);
        lng = (TextView) rootView.findViewById(R.id.locationLng);
        atit = (TextView) rootView.findViewById(R.id.locationAtit);

        name.setText(__Info.name);
//        name.setText("Yomna");
        slogan.setText(__Info.slogan);
        serviceType.setText(__Info.serviceType);
        availibility.setText(__Info.workTime);
        adrs.setText(__Info.adrs);
        phone.setText(__Info.phone);
        email.setText(__Info.email);
        licence.setText(__Info.licence);
        lat.setText(String.valueOf(__Info.lat));
        lng.setText(String.valueOf(__Info.lng));
        atit.setText(String.valueOf(__Info.atit));

        Picasso.with(this.getContext()).load(__Info.iconURL).into(icon);
        return rootView;
    }

    public void editDataBtnOnClick(View view){
        startActivity(new Intent(getActivity(), EditDataActivity.class));
    }
    public void logoutOnClick(View view){
        SharedPrefManager.getInstance(this.getContext()).logout();
        startActivity(new Intent(this.getContext(), HomeActivity.class));
    }
}
