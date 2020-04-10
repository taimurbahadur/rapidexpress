package com.khaksar.rapidex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.khaksar.rapidex.Adapter.ProductAdapter;
import com.khaksar.rapidex.Model.Product;
import com.khaksar.rapidex.Retrofit.IRapidExpressAPI;
import com.khaksar.rapidex.Utils.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    List<String> suggestList = new ArrayList<>();
    List<Product> localDataSource = new ArrayList<>();
    MaterialSearchBar searchBar;

    IRapidExpressAPI mService;

    RecyclerView recycler_search;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ProductAdapter searchAdapter,adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Init Service
        mService = Common.getAPI();

        recycler_search = (RecyclerView)findViewById(R.id.recycler_search);
        recycler_search.setLayoutManager(new GridLayoutManager(this,2));

        searchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        searchBar.setHint("Enter your products");

        loadAllProducts();

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for(String search:suggestList)
                    {
                        if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recycler_search.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<Product> result = new ArrayList<>();
        for(Product product:localDataSource)
            if(product.Name.contains(text))
                result.add(product);
            searchAdapter = new ProductAdapter(this,result);
            recycler_search.setAdapter(searchAdapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void loadAllProducts() {
        compositeDisposable.add(mService.getAllProducts().observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe(new Consumer<List<Product>>() {
                  @Override
                  public void accept(List<Product> products) throws Exception {
                    displayListProduct(products);
                    buildSuggestList(products);
                  }
              }));
    }

    private void buildSuggestList(List<Product> products) {
        for (Product product:products)
            suggestList.add(product.Name);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayListProduct(List<Product> products) {
        localDataSource = products;
        adapter = new ProductAdapter(this, products);
        recycler_search.setAdapter(adapter);
    }
}
