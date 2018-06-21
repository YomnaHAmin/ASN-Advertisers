package com.example.android.advertisers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

public class EditDataActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_PROCESS_IMG = 100;

    EditText name, slogan, serviceType, availibility, adrs, phone, email, licence;
    String newIcon, newName, newSlogan, newServiceType, newAvailibility, newAdrs, newPhone, newEmail, newLicence;
    TextView newIconValue, rmvNewImg;
    Uri newIconUri;
    Bitmap newIconBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        name = (EditText) findViewById(R.id.nameEdit);
        slogan = (EditText) findViewById(R.id.sloganEdit);
        serviceType = (EditText) findViewById(R.id.serviceTypeEdit);
        availibility = (EditText) findViewById(R.id.availibilityEdit);
        adrs = (EditText) findViewById(R.id.adrsEdit);
        phone = (EditText) findViewById(R.id.phoneEdit);
        email = (EditText) findViewById(R.id.emailEdit);
        licence = (EditText) findViewById(R.id.licenceEdit);

        newIconValue = (TextView)findViewById(R.id.chsImgBtn);
        rmvNewImg = (TextView)findViewById(R.id.newImgRmvBtn);

        name.setText(__Info.name);
        slogan.setText(__Info.slogan);
        serviceType.setText(__Info.serviceType);
        availibility.setText(__Info.workTime);
        adrs.setText(__Info.adrs);
        phone.setText(__Info.phone);
        email.setText(__Info.email);
        licence.setText(__Info.licence);

    }

    public void chsImgBtnOnClick(View v){
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
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null){
            newIconUri = data.getData();

            try {
                newIconBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),newIconUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newIconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            newIcon = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            newIconValue.setText(newIconUri.toString());
            rmvNewImg.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
//            case RESULT_ACCESS_LOCATION:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    getLocation();
//                }
//                else{
//                    Toast.makeText(this, "Location Permissions not granted",Toast.LENGTH_LONG);
//                }
//                return;
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

    public void newImgRmvBtnOnClick(View view){
        newIcon = null;
        newIconValue.setText("Choose Image");
        view.setVisibility(View.GONE);
    }

    public void svChngsBtnOnClick(View view){

        newName = name.getText().toString().trim();
        newSlogan = slogan.getText().toString().trim();
        newServiceType = serviceType.getText().toString().trim();
        newAvailibility = availibility.getText().toString().trim();
        newAdrs = adrs.getText().toString().trim();
        newPhone = phone.getText().toString().trim();
        newEmail = email.getText().toString().trim();
        newLicence = licence.getText().toString().trim();

        if(newName == null || newServiceType == null || newAdrs == null || newEmail == null || newLicence == null ){
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_LONG);
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving Changes ... ");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    __Constants.URL_EDIT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);

                            /*
                                Parse the JSON object here and update _info
                             */

                                SharedPrefManager.getInstance(getApplicationContext()).editUserInfo(
                                        obj.getString("name"),
                                        obj.getString("slogan"),
                                        obj.getString("serviceType"),
                                        obj.getString("adrs"),
                                        obj.getString("workingTime"),
                                        obj.getString("phone"),
                                        obj.getString("email"),
                                        obj.getString("licence"),
                                        obj.getString("iconURL")
                                );

//                                __Info.name = obj.getString("name");
//                                __Info.slogan = obj.getString("slogan");
//                                __Info.serviceType = obj.getString("serviceType");
//                                __Info.adrs = obj.getString("adrs");
//                                __Info.workTime = obj.getString("workingTime");
//                                __Info.phone = obj.getString("phone");
//                                __Info.email = obj.getString("email");
//                                __Info.licence = obj.getString("licence");
//                                __Info.iconURL = obj.getString("iconURL");


                                // CHECK HOW TO START A TABBED ACTIVITY ON CERTAIN TAB !!! 
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

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
                    params.put("ID", String.valueOf(__Info.ID));
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

//    class DataPart {
//        private String fileName;
//        private byte[] content;
//        private String type;
//
//        public DataPart() {
//        }
//
//        DataPart(String name, byte[] data) {
//            fileName = name;
//            content = data;
//        }
//
//        String getFileName() {
//            return fileName;
//        }
//
//        byte[] getContent() {
//            return content;
//        }
//
//        String getType() {
//            return type;
//        }
//
//    }
//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
}
