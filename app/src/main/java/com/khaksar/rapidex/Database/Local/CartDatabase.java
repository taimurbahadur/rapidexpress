package com.khaksar.rapidex.Database.Local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.khaksar.rapidex.Database.ModelDB.Cart;

@Database(entities = {Cart.class}, version =  1, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = Room.databaseBuilder(context,CartDatabase.class, "rapidex_rapid_db")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }

}
