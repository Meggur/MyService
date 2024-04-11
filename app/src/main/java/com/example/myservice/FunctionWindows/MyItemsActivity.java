package com.example.myservice.FunctionWindows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myservice.Adapter.MyAdapter;
import com.example.myservice.Model.Products;
import com.example.myservice.Prevalent.Prevalent;
import com.example.myservice.R;
import com.example.myservice.StartWindows.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {

    private RecyclerView productList;
    private DatabaseReference productRef;
    private MyAdapter myAdapter;
    private ArrayList<Products> allProductsList;
    private ArrayList<Products> filteredProductsList;
    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        init();

        getProductInfo();
    }

    private void getProductInfo() {
        productList = findViewById(R.id.product_list);
        productRef = FirebaseDatabase.getInstance().getReference("User_products").child(Prevalent.currentOnlineUser.getPhone());
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyItemsActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyItemsActivity.this, SearchActivity.class));
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyItemsActivity.this, MessageActivity.class));
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyItemsActivity.this, FavouriteActivity.class));
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyItemsActivity.this, HomeActivity.class));
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
    }
}