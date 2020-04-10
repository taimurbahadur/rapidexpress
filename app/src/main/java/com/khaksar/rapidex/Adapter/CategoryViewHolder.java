package com.khaksar.rapidex.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khaksar.rapidex.Interface.IItemClickListener;
import com.khaksar.rapidex.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView img_produt;
    TextView text_menu_name;

    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        img_produt = (ImageView)itemView.findViewById(R.id.image_product);
        text_menu_name = (TextView)itemView.findViewById(R.id.text_menu_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);

    }
}
