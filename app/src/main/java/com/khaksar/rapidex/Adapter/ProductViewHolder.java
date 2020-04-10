package com.khaksar.rapidex.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView img_product;
    TextView text_product_name,text_price;

    IItemClickListener itemClickListener;
    Button btn_add_to_cart;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = (ImageView)itemView.findViewById(R.id.image_product);
        text_product_name = (TextView)itemView.findViewById(R.id.text_product_name);
        text_price = (TextView)itemView.findViewById(R.id.text_price);
        btn_add_to_cart = (Button)itemView.findViewById(R.id.btn_add_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);

    }
}
