package com.khaksar.rapidex.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.Model.Order;
import com.khaksar.rapidex.OrderDetailActivity;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Utils.Common;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_layout,parent,false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        holder.text_order_id.setText(new StringBuilder("#").append(orderList.get(position).getOrderId()));
        holder.text_order_price.setText(new StringBuilder("Rs").append(orderList.get(position).getOrderPrice()));
        holder.text_order_address.setText(orderList.get(position).getOrderAddress());
        holder.text_order_comment.setText(orderList.get(position).getOrderComment());
        holder.text_order_status.setText(new StringBuilder("Order Status: ").append(Common.convertCodeToStatus(orderList.get(position).getOrderStatus())));

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentOrder = orderList.get(position);
                context.startActivity(new Intent(context, OrderDetailActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}






































