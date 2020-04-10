package com.khaksar.rapidex.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.Model.Category;
import com.khaksar.rapidex.ProductActivity;
import com.khaksar.rapidex.R;
import com.khaksar.rapidex.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{


    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        //Load Image
        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.img_produt);

        holder.text_menu_name.setText(categories.get(position).Name);

        //Event
        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentCategory = categories.get(position);

                //Start new Activity
                context.startActivity(new Intent(context, ProductActivity.class));


            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
