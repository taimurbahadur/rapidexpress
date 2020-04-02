package com.khaksar.rapidex.Retrofit;

import com.khaksar.rapidex.Model.Banner;
import com.khaksar.rapidex.Model.Category;
import com.khaksar.rapidex.Model.CheckUserResponse;
import com.khaksar.rapidex.Model.CheckUserResponseModel;
import com.khaksar.rapidex.Model.RegisterModel;
import com.khaksar.rapidex.Model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IRapidExpressAPI {

        @FormUrlEncoded
        @POST("checkuser.php")
        Call<CheckUserResponseModel> checkUserExists(@Field("phone") String phone);

        @FormUrlEncoded
        @POST("register.php")
        Call<RegisterModel> registerNewUser(@Field("phone") String phone,
                                            @Field("name") String name,
                                            @Field("address") String address,
                                            @Field("birthdate") String birthdate);


        @FormUrlEncoded
        @POST("getuser.php")
        Call<User> getUserInformation(@Field("phone") String phone);

        @GET("getbanner.php")
        Observable<List<Banner>> getBanners();

        @GET("getmenu.php")
        Observable<List<Category>> getMenu();

}

