package com.khaksar.rapidex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        holder.text_product_name.setText(new StringBuilder(cartList.get(position).name)
        .append(" x")
        .append(cartList.get(position).amount)
        .append(cartList.get(position).size == 0 ? " Size Small":"Size Large"));
        //Get Price of one item with all options

        double priceoneitem = cartList.get(position).price / cartList.get(position).amount;


        //Auto save item when user change amount

        holder.text_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                Cart cart = cartList.get(position);
                cart.amount = newValue;
                cart.price = Math.round(priceoneitem*newValue);

                Common.cartRepository.updateCart(cart);

                holder.text_price.setText(new StringBuilder("Rs").append(cartList.get(position).price));

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_product;
        TextView text_product_name,text_price;
        ElegantNumberButton text_amount;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;



        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = (ImageView)itemView.findViewById(R.id.img_product);
            text_amount = (ElegantNumberButton)itemView.findViewById(R.id.text_amount);
            text_product_name = (TextView)itemView.findViewById(R.id.text_product_name);
            text_price = (TextView)itemView.findViewById(R.id.text_price);
            text_amount = (ElegantNumberButton)itemView.findViewById(R.id.text_amount);

            view_background = (RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);

        }

    }

    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
