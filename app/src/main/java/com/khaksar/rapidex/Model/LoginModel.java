package com.khaksar.rapidex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("exists")
    @Expose
    private boolean errorMsg;

    public boolean checkExist() {
        return errorMsg;
    }

    public void setCheckExist(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }
}
