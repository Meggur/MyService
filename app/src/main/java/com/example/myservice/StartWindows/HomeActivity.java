package com.example.myservice.StartWindows;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myservice.Adapter.MyAdapter;
import com.example.myservice.FunctionWindows.AddNewItemActivity;
import com.example.myservice.FunctionWindows.FavouriteActivity;
import com.example.myservice.FunctionWindows.MessageActivity;
import com.example.myservice.FunctionWindows.SearchActivity;
import com.example.myservice.FunctionWindows.ProfileActivity;
import com.example.myservice.Model.Products;
import com.example.myservice.Prevalent.Prevalent;
import com.example.myservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ImageView userProfileButton, adItem1, adItem2;
    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;
    RecyclerView productList;
    DatabaseReference productRef;
    MyAdapter myAdapter;
    ArrayList<Products> list;
    SearchView searchBar;
    private ArrayList<Products> allProductsList;
    private ArrayList<Products> filteredProductsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        getProductInfo();

        getUserInfo();

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
                startActivity(new Intent(HomeActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MessageActivity.class));
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FavouriteActivity.class));
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Вы уже находитесь здесь", Toast.LENGTH_SHORT).show();
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        adItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://sun9-17.userapi.com/impg/yOeLCim815bQ064COL0FvtLpg6aRpsR5z4Wv9g/jAOXSKWICOI.jpg?size=1369x800&quality=95&sign=96ebf6ee217b231e6a2c1bfe6d34807e&c_uniq_tag=1fPDC91Ov8e7FFikUjjDVmd9JlLw0gJYmkAxD2Pj_Ms&type=album";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        adItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://sun9-30.userapi.com/impf/c855732/v855732118/924a7/ifa3Z4eISK4.jpg?size=1120x380&quality=96&sign=25cb0077cb7a48a6987f19ec028d23e0&c_uniq_tag=4XSPaUdgC73d4yfaCGf9f1_JyrrNg9U9zTota3wqgNw&type=album";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
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

    private void getUserInfo() {
        if (Prevalent.currentOnlineUser.getImage() != null)
        {
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(userProfileButton);
        }
    }

    private void init()
    {
        userProfileButton = (ImageView) findViewById(R.id.user_profile_button);
        homeButton = (Button) findViewById(R.id.home_button);
        searchButton = (Button) findViewById(R.id.search_button);
        addButton = (Button) findViewById(R.id.add_button);
        messageButton = (Button) findViewById(R.id.messsage_button);
        favouritesButton = (Button) findViewById(R.id.favourites_button);
        searchBar = (SearchView) findViewById(R.id.search_bar);
        homeButton.setBackgroundResource(R.drawable.home_image_dark);
        adItem1 = findViewById(R.id.ad_item1);
        adItem2 = findViewById(R.id.ad_item2);
    }
}