package com.khaksar.rapidex.resgistration;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.khaksar.rapidex.HomeActivity;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.databinding.ActivityRegisterBinding;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setRegistere(viewModel);


        viewModel.getUserData().observe(this, registerModel -> {
            if (registerModel != null) {
                if (registerModel.getErrorMsg().equals("User Register Successfully")) {
                    Toast.makeText(this, "" + registerModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(this, "" + registerModel.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}