package com.khaksar.rapidex;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khaksar.rapidex.Model.CheckUserResponseModel;
import com.khaksar.rapidex.Model.User;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.khaksar.rapidex.resgistration.RegisterActivity;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    Button btn_continue;

    IRapidExpressAPI mService;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI();
        firebaseAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        listener = firebaseAuthLocal -> {
            FirebaseUser user = firebaseAuthLocal.getCurrentUser();
            if (user != null) {
                //User already auth

                final android.app.AlertDialog alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
                alertDialog.show();
                alertDialog.setMessage("Please Waiting...");

                mService.checkUserExists(user.getPhoneNumber())
                        .enqueue(new Callback<CheckUserResponseModel>()
                        {
                            @Override
                            public void onResponse(Call<CheckUserResponseModel> call, Response<CheckUserResponseModel> response)
                            {
                                if (response.body() != null) {

                                    //Fetch Information
                                    mService.getUserInformation(user.getPhoneNumber())
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {

                                                    //If user already exists, just start new activity
                                                    alertDialog.dismiss();
                                                    Common.currentUser = response.body(); // Fix here

                                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                    finish(); //Close Mainactivity

                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {
                                                    alertDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                } else {
                                    //Else, need register
                                    alertDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                                    finish();

                                }
                            }
                            @Override
                            public void onFailure(Call<CheckUserResponseModel> call, Throwable t) {
                                  alertDialog.dismiss();
                                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        };

        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLoginPage();
            }


        });

    }


    private void startLoginPage() {

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), REQUEST_CODE);

    }

    //ctrl+O

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {

            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
            }


        }
    }



}