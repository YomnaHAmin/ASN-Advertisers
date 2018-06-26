package com.example.android.advertisers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_ACCESS_LOCATION = 10;
    private static final int RESULT_PROCESS_IMG = 100;

    TextView viewLocationValue;
    LocationManager locationManager;
    LocationListener locationListener;

    ProgressDialog progressDialog;

    EditText name, slogan, serviceType, availibility, adrs, phone, email, licence, password, cnfrmPassword;
    String newIcon, newName, newSlogan, newServiceType, newAvailibility, newAdrs, newPhone, newEmail, newLicence,
            newLat, newLng, newAtit, newPassword, newCnfrmPassword;
    Uri newIconUri;
    Bitmap newIconBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.nameEdit);
        slogan = (EditText) findViewById(R.id.sloganEdit);
        serviceType = (EditText) findViewById(R.id.serviceTypeEdit);
        availibility = (EditText) findViewById(R.id.availibilityEdit);
        adrs = (EditText) findViewById(R.id.adrsEdit);
        phone = (EditText) findViewById(R.id.phoneEdit);
        email = (EditText) findViewById(R.id.emailEdit);
        licence = (EditText) findViewById(R.id.licenceEdit);
        password = (EditText) findViewById(R.id.passwordEdit);
        cnfrmPassword = (EditText) findViewById(R.id.passwordCnfrmEdit);

        viewLocationValue = (TextView) findViewById(R.id.viewLoactionValue);

        progressDialog = new ProgressDialog(this);
    }

    // Handling uploading image from gallery
    public void setImgBtnOnClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, RESULT_PROCESS_IMG);
            }
            return;
        }else{
            openGallery();
        }
    }
    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null){
            newIconUri = data.getData();
            TextView x = new TextView(this);
            x = (TextView) findViewById(R.id.setImgBtn);
            x.setText(newIconUri.toString());

            try {
                newIconBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),newIconUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newIconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            newIcon = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }

    }

    // Handling getting location from GPS
    public void setLocBtnOnClick(View view) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                newLat = String.valueOf(location.getLatitude());
                newLng = String.valueOf(location.getLongitude());
                newAtit = String.valueOf(location.getAltitude());

                viewLocationValue.setText(newLat + "  " + newLng + "  " + newAtit);
                locationManager.removeUpdates(locationListener);
                progressDialog.dismiss();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, RESULT_ACCESS_LOCATION);
            }
            return;
        }else{
            getLocation();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case RESULT_ACCESS_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else{
                    Toast.makeText(this, "Location Permissions not granted",Toast.LENGTH_LONG);
                }
                return;
            case RESULT_PROCESS_IMG:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else{
                    Toast.makeText(this, "Gallery Permission not granted",Toast.LENGTH_LONG);
                }
                return;
        }
    }
    @SuppressLint("MissingPermission")
    private void getLocation(){
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
        progressDialog.setMessage("Collecting Data ... ");
        progressDialog.show();
    }

    // Sending advertiser's info to the server
    public void signupBtnOnClick(View view){
        newName = name.getText().toString().trim();
        newSlogan = slogan.getText().toString().trim();
        newServiceType = serviceType.getText().toString().trim();
        newAvailibility = availibility.getText().toString().trim();
        newAdrs = adrs.getText().toString().trim();
        newPhone = phone.getText().toString().trim();
        newEmail = email.getText().toString().trim();
        newLicence = licence.getText().toString().trim();
        newPassword = password.getText().toString().trim();
        newCnfrmPassword = cnfrmPassword.getText().toString().trim();

        if(newName == null || newServiceType == null || newAdrs == null || newEmail == null || newLicence == null
                || newIcon == null || newLng == null || newLat == null || newAtit == null
                || newPassword == null || newCnfrmPassword == null){
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_LONG).show();
        }
        else if(!newPassword.equals(newCnfrmPassword)){
            Toast.makeText(this, "The two passwords must be the same", Toast.LENGTH_LONG).show();
        }
        else{
            progressDialog.setMessage("Saving Your Data ... ");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    __Constants.URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);

                            /*
                                Parse the JSON object here and update _info
                             */

                                Log.d("Response Error", obj.getString("error"));
                                Log.d("Response Msg", obj.getString("message"));
                                SharedPrefManager.getInstance(getApplicationContext()).setUserInfo(
                                    obj.getInt("ID"),
                                    obj.getString("name"),
                                    obj.getString("slogan"),
                                    obj.getString("serviceType"),
                                    obj.getString("adrs"),
                                    obj.getString("workingTime"),
                                    obj.getString("phone"),
                                    obj.getString("email"),
                                    obj.getString("licence"),
//                                    obj.getString("iconURL"),
                                        "https://asnasucse18.000webhostapp.com/res/AdvertisersApp/AdsImgs/Test.jpg",
                                    obj.getDouble("lng"),
                                    obj.getDouble("lat"),
                                    obj.getDouble("atit")
                                );


                                // CHECK HOW TO START A TABBED ACTIVITY ON CERTAIN TAB !!!
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("in JSON exception", "There's JSON exception - " + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("in error listener", "Error in response - " + error);

                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", newName);
                    params.put("slogan", newSlogan);
                    params.put("serviceType", newServiceType);
                    params.put("adrs", newAdrs);
                    params.put("workingTime", newAvailibility);
                    params.put("phone", newPhone);
                    params.put("email", newEmail);
                    params.put("licence", newLicence);
                    params.put("icon", newIcon);
                    params.put("iconName", String.valueOf(__Info.ID) + "_" + __Info.name);
                    params.put("lat", newLat);
                    params.put("lng", newLng);
                    params.put("atit", newAtit);
                    params.put("password", newPassword);

                    Log.d("in signup request", "request sent");

                    return params;
                }
//                protected Map<String, DataPart> getByteData() {
//                    Map<String, DataPart> params = new HashMap<>();
//                    params.put("pic", new DataPart(__Info.ID + ".png", getFileDataFromDrawable(newIcon)));
//                    return params;
//                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

}
