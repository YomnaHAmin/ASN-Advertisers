package com.example.android.advertisers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignIn";

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextUsernameid);
        editTextPassword = (EditText) findViewById(R.id.editeTextEmailid);
        buttonLogin = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


    }

    private void userLogin(){
        Log.d(TAG, "userLogin: starts");
        final String userEmail = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                __Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: starts");
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: "+response);
                            Toast.makeText(getApplicationContext(),"response is "+response,Toast.LENGTH_LONG).show();
                            if(!obj.getBoolean("error")){
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
//                                        obj.getString("iconURL"),
                                        "https://asnasucse18.000webhostapp.com/res/AdvertisersApp/AdsImgs/Test.jpg",
                                        obj.getDouble("lng"),
                                        obj.getDouble("lat"),
                                        obj.getDouble("atit")
                                );
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error"+response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: starts");
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: starts with "+userEmail+" , "+password);
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onClick(View view) {
        if(view == buttonLogin){
            Log.d(TAG, "onClick: clicked login");
            userLogin();
        }
    }
}
