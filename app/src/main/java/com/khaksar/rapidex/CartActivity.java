package com.khaksar.rapidex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.khaksar.rapidex.Adapter.CartAdapter;
import com.khaksar.rapidex.Database.ModelDB.Cart;
import com.khaksar.rapidex.Model.Order;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.khaksar.rapidex.Utils.RecyclerItemTouchHelper;
import com.khaksar.rapidex.Utils.RecyclerItemTouchHelperListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_cart;
    Button btn_place_order;

    List<Cart> cartList = new ArrayList<>();

    CompositeDisposable compositeDisposable;
    SharedPreferences sharedPreferences;

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    IRapidExpressAPI mService;
    private String strAddress,strPhone,strName;
    private List<Cart> cartsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        sharedPreferences = getSharedPreferences("Rapid Express", MODE_PRIVATE);
        strAddress = sharedPreferences.getString("address", "");
        strPhone = sharedPreferences.getString("phone", "");
        strName = sharedPreferences.getString("username", "");
        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        recycler_cart = (RecyclerView) findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        loadCartItems();
    }

    private void placeOrder() {
        Dialog dialog = new Dialog(CartActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.submit_order_layout);


        TextView tvUserName = dialog.findViewById(R.id.user_name);
        TextView tvUserPhone = dialog.findViewById(R.id.user_phone);

        EditText edit_comment = (EditText) dialog.findViewById(R.id.edit_comment);
        EditText edit_other_address = (EditText) dialog.findViewById(R.id.edit_other_address);

        RadioButton rdi_user_address = (RadioButton) dialog.findViewById(R.id.rdi_user_address);
        RadioButton rdi_other_address = (RadioButton) dialog.findViewById(R.id.rdi_other_address);

        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        tvUserName.setText(strName);
        tvUserPhone.setText(strPhone);

        //Event
        rdi_user_address.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                edit_other_address.setEnabled(false);
        });

        rdi_other_address.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                edit_other_address.setEnabled(true);
        });

        btnSubmit.setOnClickListener(v -> {
            final String orderComment = ""+edit_comment.getText().toString();
            final String orderAddress;
            if (rdi_user_address.isChecked())
                orderAddress = strAddress;
            else if (rdi_other_address.isChecked())
                orderAddress = edit_other_address.getText().toString();
            else
                orderAddress = strAddress;

            //Submit order
            compositeDisposable.add(
                    Common.cartRepository.getCartItems()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Cart>>() {
                        @Override
                        public void accept(List<Cart> carts) throws Exception {



                        }
                    })
            );
            sendOrderToServer(
                    Common.cartRepository.sumPrice(),
                    cartsList,
                    orderComment,
                    orderAddress);

            Common.cartRepository.emptyCart();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderComment, String orderAddress) {

        if (carts.size() > 0) {

            String detail = new Gson().toJson(carts);

            IRapidExpressAPI mService = Common.getAPI();
            mService.submitOrder(String.valueOf(sumPrice), detail, orderComment, orderAddress, strPhone)
                    .enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if (response.body() == null) {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(getApplication().getApplicationContext(), "" + jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplication().getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else if (response.body().getStatus()) {
                                Toast.makeText(CartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CartActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(getApplication().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

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
        cartList = carts; // For removing cart item
        cartsList = carts;
        cartAdapter = new CartAdapter(this, carts);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //Delete item from adapter
            cartAdapter.removeItem(deletedIndex);
            //Delete item from database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append("Remove from Carts List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }


}
