package com.khaksar.rapidex.Utils;

import com.khaksar.rapidex.Model.User;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Retrofit.RetrofitClient;

public class Common {
    //in Emulator, localhost = 10.0.2.2
    private static final String BASE_URL = "http://rapidex.pk/";

    public static  User currentUser = null;

    public static IRapidExpressAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IRapidExpressAPI.class);
    }
}
