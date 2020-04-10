package com.khaksar.rapidex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.khaksar.rapidex.Database.ModelDB.Cart;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Picasso.with(context)
                .load(cartList.get(position).link)
                .into(holder.img_product);

        holder.text_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.text_price.setText(new StringBuilder("Rs").append(cartList.get(position).price));
        holder.text_product_name.setText(cartList.get(position).name);

        //Auto save item when user change amount

        holder.text_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                Cart cart = cartList.get(position);
                cart.amount = newValue;

                Common.cartRepository.updateCart(cart);

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_product;
        TextView text_product_name,text_price;
        ElegantNumberButton text_amount;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = (ImageView)itemView.findViewById(R.id.img_product);
            text_amount = (ElegantNumberButton)itemView.findViewById(R.id.text_amount);
            text_product_name = (TextView)itemView.findViewById(R.id.text_product_name);
            text_price = (TextView)itemView.findViewById(R.id.text_price);
            text_amount = (ElegantNumberButton)itemView.findViewById(R.id.text_amount);
        }

    }
}
