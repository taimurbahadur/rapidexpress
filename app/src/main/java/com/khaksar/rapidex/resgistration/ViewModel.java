package com.khaksar.rapidex.resgistration;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.khaksar.rapidex.HomeActivity;
import com.khaksar.rapidex.Model.RegisterModel;
import com.khaksar.rapidex.Model.User;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModel extends AndroidViewModel {
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> address = new MutableLiveData<>();

    public MutableLiveData<RegisterModel> userRegisterData = new MutableLiveData<>();


    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                userRegistration();
                break;
        }
    }


    private void userRegistration() {
        IRapidExpressAPI mService = Common.getAPI();
        mService.registerNewUser(
                phone.getValue(),
                name.getValue(),
                email.getValue(),
                address.getValue())
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
                        } else {
                            userRegisterData.setValue(response.body());

                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterModel> call, Throwable t) {
                        Toast.makeText(getApplication().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public MutableLiveData<RegisterModel> getUserData() {
        return userRegisterData;
    }
}
