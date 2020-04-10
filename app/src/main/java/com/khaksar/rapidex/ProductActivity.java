package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.khaksar.rapidex.Adapter.ProductAdapter;
import com.khaksar.rapidex.Model.Product;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends AppCompatActivity {

    IRapidExpressAPI mService;

    RecyclerView list_product;

    TextView text_banner_name;

    //Rxjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mService = Common.getAPI();

        list_product = (RecyclerView)findViewById(R.id.recycler_drinks);
        list_product.setLayoutManager(new GridLayoutManager(this,2));
        list_product.setHasFixedSize(true);

        text_banner_name = (TextView)findViewById(R.id.text_menu_name);
        text_banner_name.setText(Common.currentCategory.Name);

        loadListProduct(Common.currentCategory.ID);
    }

    private void loadListProduct(String menuId) {
        compositeDisposable.add(mService.getProduct(menuId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) throws Exception {
                displayProductList(products);
            }
        }));
    }

    private void displayProductList(List<Product> products) {
        ProductAdapter adapter = new ProductAdapter(this,products);
        list_product.setAdapter(adapter);

    }
}
