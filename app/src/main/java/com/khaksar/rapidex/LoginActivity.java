package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.khaksar.rapidex.Model.login.CheckUserResponse;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.khaksar.rapidex.resgistration.RegisterActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    EditText ed_phone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_phone = findViewById(R.id.ed_phone);

    }

    public void Login(View view) {

        if (ed_phone.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show();

        }
        if (ed_phone.getText().toString().equalsIgnoreCase("Success")) {
            Toast.makeText(this, "User Login Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
        }

        userLogin(ed_phone.getText().toString());
    }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }


    private void userLogin(String phone) {
        IRapidExpressAPI mService = Common.getAPI();
        mService.userLogin(
                phone)
                .enqueue(new Callback<CheckUserResponse>() {
                    @Override
                    public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                        if (response.body() == null) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplication().getApplicationContext(), "" + jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplication().getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.body().getStatus()) {
                            checkUserLogin(
                                    true,
                                    response.body().getData().get(0).getName(),
                                    response.body().getData().get(0).getPhone(),
                                    response.body().getData().get(0).getAddress());
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void checkUserLogin(boolean check, String name, String phone,String address) {
        sharedPreferences = getSharedPreferences("Rapid Express", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("checkLogin", check).apply();
        editor.putString("username", name).apply();
        editor.putString("phone", phone).apply();
        editor.putString("address", address).apply();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

}
