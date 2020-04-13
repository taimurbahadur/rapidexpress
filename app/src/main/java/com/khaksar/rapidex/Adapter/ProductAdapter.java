package com.khaksar.rapidex.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.khaksar.rapidex.Database.ModelDB.Cart;
import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.Model.Product;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Utils.Common;
import com.squareup.picasso.Picasso;


import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    Context context;
    List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_item_layout,null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.text_price.setText(new StringBuilder("Rs").append(productList.get(position).Price).toString());
        holder.text_product_name.setText(productList.get(position).Name);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddToCartDialog(position);

            }
        });

        Picasso.with(context)
                .load(productList.get(position).Link)
                .into(holder.img_product);

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

    }

    private void showAddToCartDialog(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);

        //View
        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton text_count = (ElegantNumberButton)itemView.findViewById(R.id.text_count);
        TextView text_product_diaog = (TextView)itemView.findViewById(R.id.text_cart_product_name);

        EditText edit_comment = (EditText)itemView.findViewById(R.id.edit_comment);

        RadioButton rdi_sizeS = (RadioButton)itemView.findViewById(R.id.rdi_sizeS);
        RadioButton rdi_sizeL = (RadioButton)itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    Common.sizeOfItem=0;
            }
        });


        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    Common.sizeOfItem=1;
            }
        });

        //Set data
        Picasso.with(context)
                .load(productList.get(position).Link)
                .into(img_product_dialog);
        text_product_diaog.setText(productList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Common.sizeOfItem == -1)
                        {
                            Toast.makeText(context, "Please Choose Item Size", Toast.LENGTH_SHORT).show();
                        }
                        showConfirmDialog(position,text_count.getNumber());
                        dialog.dismiss();
                    }
                });

                builder.show();
    }

    private void showConfirmDialog(int position, String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_cart_layout,null);

        //View
        ImageView img_product_dialog = (ImageView)itemView.findViewById(R.id.img_product);
        TextView text_product_diaog = (TextView)itemView.findViewById(R.id.text_cart_product_name);
        TextView text_product_price = (TextView)itemView.findViewById(R.id.text_cart_product_price);

        //Set data

        Picasso.with(context).load(productList.get(position).Link).into(img_product_dialog);
        text_product_diaog.setText(new StringBuilder(productList.get(position).Name).append(" x")
                .append(Common.sizeOfItem == 0 ? " Size Small":" Size Large")
                .append(number).toString());


        double price =(Double.parseDouble(productList.get(position).Price))* Double.parseDouble(number);

        if (Common.sizeOfItem == 1)
            price+=(100*Double.parseDouble(number));

        final double finalPrice = Math.round(price);

        text_product_price.setText(new StringBuilder("Rs").append(finalPrice));

        builder.setNegativeButton("CONFIRM", (dialog, which) -> {


            dialog.dismiss();
            try {
                //Add to SQLite later
                //Create new cart item
                Cart cartItem = new Cart();
                cartItem.name = productList.get(position).Name;
                cartItem.amount = Integer.parseInt(number);
                cartItem.price = finalPrice;
                cartItem.size = Common.sizeOfItem;
                cartItem.link = productList.get(position).Link;

                //Add to DB
                Common.cartRepository.insertToCart(cartItem);

                Log.d("rapidex_rapid_db", new Gson().toJson(cartItem));

                Toast.makeText(context, "Item Saved to Cart Successfully", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex)
            {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        builder.setView(itemView);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
