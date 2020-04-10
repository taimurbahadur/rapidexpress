package com.khaksar.rapidex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.khaksar.rapidex.Adapter.CartAdapter;
import com.khaksar.rapidex.Database.ModelDB.Cart;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recycler_cart;
    Button btn_place_order;

    CompositeDisposable compositeDisposable;


    IRapidExpressAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        recycler_cart = (RecyclerView)findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        btn_place_order = (Button)findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        loadCartItems();
    }

    private void placeOrder() {

        //Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout,null);

        EditText edit_comment = (EditText)submit_order_layout.findViewById(R.id.edit_comment);
        EditText edit_other_address = (EditText)submit_order_layout.findViewById(R.id.edit_other_address);

        RadioButton rdi_user_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_user_address);
        RadioButton rdi_other_address = (RadioButton)submit_order_layout.findViewById(R.id.rdi_other_address);

        //Event
        rdi_user_address.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                edit_other_address.setEnabled(false);
        });

        rdi_other_address.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                edit_other_address.setEnabled(true);
        });

        builder.setView(submit_order_layout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String orderComment = edit_comment.getText().toString();
                final String orderAddress;
                if (rdi_user_address.isChecked())
                    orderAddress = Common.currentUser.getAddress();
                else if (rdi_other_address.isChecked())
                    orderAddress = edit_other_address.getText().toString();
                else
                    orderAddress="";

                //Submit order
                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                if (!TextUtils.isEmpty(orderAddress))
                                    sendOrderToServer(Common.cartRepository.sumPrice(),
                                            carts,
                                            orderComment,orderAddress);

                                else
                                    Toast.makeText(CartActivity.this, "Order Address Can't Be Null", Toast.LENGTH_SHORT).show();

                            }
                        })
                );

            }
        });
        builder.show();

    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderComment, String orderAddress) {

        if(carts.size() > 0)
        {
            String detail = new Gson().toJson(carts);

            mService.submitOrder(sumPrice,detail,orderComment,orderAddress,Common.currentUser.getPhone())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();

                            //Clear Cart
                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR",t.getMessage());
                        }
                    });
        }

    }


    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItem(carts);
                    }
                })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        CartAdapter cartAdapter = new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
