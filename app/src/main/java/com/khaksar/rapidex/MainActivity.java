package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                    gotoHomeActivity(); //if already loign then it will go to home
                } else {
                    gotToLoginActivity(); //else it will load login screen
                }
            }

        });
    }

    public void gotToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void gotoHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private boolean checkAlreadyLoginOrNot() {
        sharedPreferences = getSharedPreferences("Rapid Express", MODE_PRIVATE);
        return sharedPreferences.getBoolean("checkLogin", false);
    }

}