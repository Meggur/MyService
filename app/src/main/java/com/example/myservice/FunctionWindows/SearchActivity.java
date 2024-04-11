package com.example.myservice.FunctionWindows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myservice.Adapter.MyAdapter;
import com.example.myservice.Model.Products;
import com.example.myservice.R;
import com.example.myservice.StartWindows.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchActivity extends AppCompatActivity {

    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;
    private Button sortByDateButton, sortByPriceButton;
    private RecyclerView productList;
    private DatabaseReference productRef;
    private MyAdapter myAdapter;
    private ArrayList<Products> list;
    private SearchView searchBar;
    private ArrayList<Products> allProductsList;
    private ArrayList<Products> filteredProductsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        getProductInfo();

        sortByDateButton.setVisibility(View.INVISIBLE);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchActivity.this, "Вы уже находитесь здесь", Toast.LENGTH_SHORT).show();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MessageActivity.class));
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, FavouriteActivity.class));
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, HomeActivity.class));
            }
        });

        sortByDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDate();
                sortByDateButton.setVisibility(View.INVISIBLE);
                sortByPriceButton.setVisibility(View.VISIBLE);
            }
        });

        sortByPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPrice();
                sortByPriceButton.setVisibility(View.INVISIBLE);
                sortByDateButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void searchProducts(String query) {
        filteredProductsList.clear();
        if (query.isEmpty()) {
            filteredProductsList.addAll(allProductsList);
        } else {
            for (Products product : allProductsList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProductsList.add(product);
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void sortByDate() {
        productRef.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProductsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    allProductsList.add(0, products);
                }
                filteredProductsList.clear();
                filteredProductsList.addAll(allProductsList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sortByPrice() {
        productRef.orderByChild("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProductsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    int price = Integer.parseInt(products.getPrice());
                    products.setPrice(String.valueOf(price));
                    allProductsList.add(products);
                }
                Collections.sort(allProductsList, new Comparator<Products>() {
                    @Override
                    public int compare(Products p1, Products p2) {
                        return Integer.compare(Integer.parseInt(p1.getPrice()), Integer.parseInt(p2.getPrice()));
                    }
                });
                filteredProductsList.clear();
                filteredProductsList.addAll(allProductsList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductInfo() {
        productList = findViewById(R.id.product_list);
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        productList.setHasFixedSize(true);
        productList.setLayoutManager(new LinearLayoutManager(this));

        allProductsList = new ArrayList<>();
        filteredProductsList = new ArrayList<>();
        myAdapter = new MyAdapter(this, filteredProductsList);
        productList.setAdapter(myAdapter);

        productRef.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProductsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products products = dataSnapshot.getValue(Products.class);
                    allProductsList.add(0, products);
                }
                filteredProductsList.addAll(allProductsList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init()
    {
        homeButton = (Button) findViewById(R.id.home_button);
        searchButton = (Button) findViewById(R.id.search_button);
        addButton = (Button) findViewById(R.id.add_button);
        messageButton = (Button) findViewById(R.id.messsage_button);
        favouritesButton = (Button) findViewById(R.id.favourites_button);
        searchBar = (SearchView) findViewById(R.id.search_bar);
        searchButton.setBackgroundResource(R.drawable.search_image_dark);
        sortByDateButton = (Button) findViewById(R.id.sort_by_date);
        sortByPriceButton = (Button) findViewById(R.id.sort_by_price);
    }
}