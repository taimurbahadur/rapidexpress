package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khaksar.rapidex.Adapter.OrderDetailAdapter;
import com.khaksar.rapidex.Database.ModelDB.Cart;
import com.khaksar.rapidex.Utils.Common;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView text_order_id,text_order_price,text_order_address,text_order_comment,text_order_status;

    RecyclerView recycler_order_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        text_order_id = (TextView)findViewById(R.id.text_order_id);
        text_order_price = (TextView)findViewById(R.id.text_order_price);
        text_order_address = (TextView)findViewById(R.id.text_order_address);
        text_order_comment = (TextView)findViewById(R.id.text_order_comment);
        text_order_status = (TextView)findViewById(R.id.text_order_status);

        recycler_order_detail = (RecyclerView)findViewById(R.id.recycler_order_detail);
        recycler_order_detail.setLayoutManager(new LinearLayoutManager(this));
        recycler_order_detail.setHasFixedSize(true);

        text_order_id.setText(new StringBuilder("#").append(Common.currentOrder.getOrderId()));
        text_order_price.setText(new StringBuilder("Rs").append(Common.currentOrder.getOrderPrice()));
        text_order_address.setText(Common.currentOrder.getOrderAddress());
        text_order_comment.setText(Common.currentOrder.getOrderComment());
        text_order_status.setText(new StringBuilder("Order Status: ").append(Common.convertCodeToStatus(Common.currentOrder.getOrderStatus())));

        DisplayOrderDetail();

    }

    private void DisplayOrderDetail() {
        List<Cart> orderDetail = new Gson().fromJson(Common.currentOrder.getOrderDetail(),
              new TypeToken<List<Cart>>(){}.getType());
        recycler_order_detail.setAdapter(new OrderDetailAdapter(this,orderDetail));

    }
}



























