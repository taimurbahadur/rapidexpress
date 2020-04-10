package com.khaksar.rapidex.Retrofit;

import com.khaksar.rapidex.Model.Banner;
import com.khaksar.rapidex.Model.Category;
import com.khaksar.rapidex.Model.CheckUserResponseModel;
import com.khaksar.rapidex.Model.Order;
import com.khaksar.rapidex.Model.login.CheckUserDataModel;
import com.khaksar.rapidex.Model.Product;
import com.khaksar.rapidex.Model.RegisterModel;
import com.khaksar.rapidex.Model.User;
import com.khaksar.rapidex.Model.login.CheckUserResponse;

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
        @POST("checkuser.php")
        Call<CheckUserResponse> userLogin(@Field("phone") String phone);

        @FormUrlEncoded
        @POST("register.php")
        Call<RegisterModel> registerNewUser(@Field("phone") String phone,
                                            @Field("name") String name,
                                            @Field("email") String email,
                                            @Field("address") String address);

        @FormUrlEncoded
        @POST("getproduct.php")
        Observable<List<Product>> getProduct(@Field("menuid") String menuID);


        @FormUrlEncoded
        @POST("getuser.php")
        Call<User> getUserInformation(@Field("Phone") String phone);

        @GET("getbanner.php")
        Observable<List<Banner>> getBanners();

        @GET("getmenu.php")
        Observable<List<Category>> getMenu();

        @GET("getallproducts.php")
        Observable<List<Product>> getAllProducts();

        @FormUrlEncoded
        @POST("submitorder.php")
        Call<Order> submitOrder(@Field("price") String orderPrice,
                                @Field("detail") String detail,
                                @Field("comment") String comment,
                                @Field("address") String address,
                                @Field("phone") String phone);

}

