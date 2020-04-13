package com.khaksar.rapidex.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public TextView text_order_id,text_order_price,text_order_address,text_order_comment,text_order_status;
    public OrderViewHolder(View itemView){
        super(itemView);

        text_order_id = (TextView)itemView.findViewById(R.id.text_order_id);
        text_order_price = (TextView)itemView.findViewById(R.id.text_order_price);
        text_order_address = (TextView)itemView.findViewById(R.id.text_order_address);
        text_order_comment = (TextView)itemView.findViewById(R.id.text_order_comment);
        text_order_status = (TextView)itemView.findViewById(R.id.text_order_status);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
