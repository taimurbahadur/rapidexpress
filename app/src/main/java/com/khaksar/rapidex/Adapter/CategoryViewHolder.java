package com.khaksar.rapidex.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    ImageView img_produt;
    TextView text_menu_name;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        img_produt = (ImageView)itemView.findViewById(R.id.image_product);
        text_menu_name = (TextView)itemView.findViewById(R.id.text_menu_name);
    }
}
