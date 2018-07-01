package com.example.android.advertisers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditAdvActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_PROCESS_IMG = 100;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    ProgressDialog progressDialog;

    String title, desc, img, expirationDate;
    int ID, ownerID;
    EditText titleEdit, descEdit;
    TextView imgValue, expirationDateValue, rmvExpirationDate, rmvNewImg;
    Uri imgUri;
    Bitmap imgBitmap;

    int adPosition;
    boolean deleteAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_adv);

        deleteAd = false;
        progressDialog = new ProgressDialog(this);

        Intent i = getIntent();
        adPosition = i.getIntExtra("adPosition", -1);

        expirationDate = " ";
        img = " ";

        titleEdit = (EditText) findViewById(R.id.titleEdit);
        descEdit = (EditText) findViewById(R.id.descEdit);
        expirationDateValue = (TextView) findViewById(R.id.expDateBtn);
        imgValue = (TextView) findViewById(R.id.setImgBtn);
        rmvExpirationDate = (TextView) findViewById(R.id.expDateRmvBtn);
        rmvNewImg = (TextView)findViewById(R.id.newImgRmvBtn);

        titleEdit.setText(AdvFragment.ads.get(adPosition).getTitle());
        descEdit.setText(AdvFragment.ads.get(adPosition).getDescription());
        if(AdvFragment.ads.get(adPosition).getExpirationDate() != null) {
            expirationDateValue.setText(AdvFragment.ads.get(adPosition).getExpirationDate());
            rmvExpirationDate.setVisibility(View.VISIBLE);
        }

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String strYear = String.valueOf(year);
                String strMonth = String.valueOf(month);
                String strDay = String.valueOf(day);

                if(month < 10){
                    strMonth = "0"+strMonth;
                }
                if(day < 10){
                    strDay = "0"+strDay;
                }
//                expirationDate = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                expirationDate = strYear+"-"+strMonth+"-"+strDay;
                expirationDateValue.setText(expirationDate);
                rmvExpirationDate.setVisibility(View.VISIBLE);
            }
        };
    }

    public void setImgBtnOnClick(View view){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();

            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            img = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            imgValue.setText(imgUri.toString());
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
                    Toast.makeText(this, "Gallery Permission not granted",Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void expDateBtnOnClick(View view){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                onDateSetListener,
                year, month, day
        );
//        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public void expDateRmvBtnOnClick(View view){
        expirationDate = " ";
        expirationDateValue.setText("Pick Expiration Date");
        view.setVisibility(View.GONE);
    }

    public void newImgRmvBtnOnClick(View view){
        img = null;
        imgValue.setText("Choose Image");
        view.setVisibility(View.GONE);
    }

    private void editAdRequest(final boolean deleteAd){
        ID = AdvFragment.ads.get(adPosition).getID();
        ownerID = __Info.ID;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                __Constants.URL_EDIT_AD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("Edit Adv Response :", response);
                        try {
                            if(!deleteAd) {
                                JSONObject obj = new JSONObject(response);

                                AdvFragment.ads.get(adPosition).setTitle(title);
                                AdvFragment.ads.get(adPosition).setDescription(desc);
                                AdvFragment.ads.get(adPosition).setExpirationDate(expirationDate);
                                if (!img.equals(" ")) {
                                    AdvFragment.ads.get(adPosition).setImgURL(obj.getString("imgURL"));
                                }
                            }
                            else{
                                AdvFragment.ads.remove(adPosition);
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

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
                params.put("advID", String.valueOf(ID));
                params.put("ownerID", String.valueOf(ownerID));
                if(deleteAd){
//                    params.put("delete", "true");
                    params.put("delete", "1");
                }
                else {
//                    params.put("delete", "false");
                    params.put("delete", "0");
                    params.put("title", title);
                    params.put("desc", desc);
                    params.put("expirationDate", expirationDate);
                    params.put("img", img);
                    params.put("imgName", String.valueOf(__Info.ID) + "_" + title);
                }
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

    public void editAdBtnOnClick(View view){
        title = titleEdit.getText().toString().trim();
        desc = descEdit.getText().toString().trim();

        if(title == null || desc == null) {
            Toast.makeText(this, "Please Fill Required Fields", Toast.LENGTH_LONG);
        }
        else {
            progressDialog.setMessage("Saving Your Data ... ");
            progressDialog.show();
            editAdRequest(false);
        }
    }

    public void rmvAdBtnOnClick(View view){
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Are You Sure You Want To Remove This Ad");
        dlgAlert.setTitle("Remove Ad Confirmation");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
                progressDialog.setMessage("Deleting This Ad ... ");
                progressDialog.show();
                editAdRequest(true);
            }
        });
        dlgAlert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();


    }
}
