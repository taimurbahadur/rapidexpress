package com.khaksar.rapidex;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khaksar.rapidex.Model.User;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    EditText edit_phone,edit_name,edit_birthdate,edit_address;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IRapidExpressAPI mService = Common.getAPI();

        edit_phone = findViewById(R.id.edit_phone);
        edit_name = findViewById(R.id.edit_name);
        edit_birthdate = findViewById(R.id.edit_birthdate);
        edit_address = findViewById(R.id.edit_address);
        btn_register = findViewById(R.id.btn_register);

        edit_birthdate.addTextChangedListener(new PatternedTextWatcher("##-##-####"));


        btn_register.setOnClickListener((v) -> {


            if (TextUtils.isEmpty(edit_phone.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(edit_name.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(edit_birthdate.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(edit_address.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;
            }

            final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
            waitingDialog.show();
            waitingDialog.setMessage("Please Waiting...");

            mService.registerNewUser(edit_phone.getText().toString(),
                    edit_name.getText().toString(),
                    edit_address.getText().toString(),
                    edit_birthdate.getText().toString())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            waitingDialog.dismiss();

                            //Log.d("response",response.message());
                            User user = response.body();
                            if (TextUtils.isEmpty(user.getError_msg())) {
                                Toast.makeText(RegisterActivity.this, "User register successfully", Toast.LENGTH_SHORT).show();
                                Common.currentUser = response.body();
                                //Start new activity

                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                finish();
                            }


                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            waitingDialog.dismiss();
                        }
                    });

        });

    }

}