package com.example.android.advertisers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
    }

    public void signupBtnOnClick(View view){
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void loginBtnOnClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
