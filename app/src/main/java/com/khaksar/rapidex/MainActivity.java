package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.khaksar.rapidex.resgistration.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    Button btn_continue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAlreadyLoginOrNot()) {
                    gotoHomeActivity(); //if already login then it will go to home
                } else {
                    gotToLoginActivity(); //else it will load login screen
                }
            }

        });
    }

    public void gotToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void gotoHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean checkAlreadyLoginOrNot() {
        sharedPreferences = getSharedPreferences("Rapid Express", MODE_PRIVATE);
        return sharedPreferences.getBoolean("checkLogin", false);
    }

    //Exit Application when BACK button is clicked
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        if (isBackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackButtonClicked=false;

    }
}