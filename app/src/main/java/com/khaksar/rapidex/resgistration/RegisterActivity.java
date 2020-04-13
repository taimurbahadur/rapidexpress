package com.khaksar.rapidex.resgistration;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khaksar.rapidex.HomeActivity;
import com.khaksar.rapidex.LoginActivity;
import com.khaksar.rapidex.Model.RegisterModel;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.khaksar.rapidex.VerifyPhoneNo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private EditText  etPhone, etName, etEmail, etAddress;
    private boolean valide = false;
    private String strPhone, strName, strEmail, strAddress;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.edit_name);
        etPhone = findViewById(R.id.edit_phone);
        etEmail = findViewById(R.id.edit_email);
        etAddress = findViewById(R.id.edit_address);
        btnSignup = findViewById(R.id.btn_register);


        btnSignup.setOnClickListener(v->{
            if(validate()){
                userRegistration();
            }
        });

    }


    // User Log in session?

    private void userRegistration() {
        IRapidExpressAPI mService = Common.getAPI();
        mService.registerNewUser(
                strPhone,
                strName,
                strEmail,
                strAddress)
                .enqueue(new Callback<RegisterModel>() {
                    @Override
                    public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                        if (response.body() == null) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplication().getApplicationContext(), "" + jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplication().getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if(response.body().getErrorMsg().equals("")) {
                            Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, ""+response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                       // startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo.class);
                        intent.putExtra("phoneNo", strPhone);
                        startActivity(intent);

                    }


                    @Override
                    public void onFailure(Call<RegisterModel> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private boolean validate() {
        valide = true;
        strName = etName.getText().toString();
        strPhone = etPhone.getText().toString();
        strEmail = etEmail.getText().toString();
        strAddress = etAddress.getText().toString();


        if (strPhone.isEmpty()) {
            valide = false;
            etPhone.setError("Please Enter Your Phone");
        }

        if (strName.isEmpty()) {
            valide = false;
            etName.setError("Please Enter Your Name");
        }

        if (strEmail.isEmpty()) {
            valide = false;
            etEmail.setError("Please Enter Your Email");
        }

        if (strAddress.isEmpty()) {
            valide = false;
            etAddress.setError("Please Enter Your Address");
        }

        return valide;
    }


    public void moveToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}