package com.example.myservice.FunctionWindows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myservice.Model.Products;
import com.example.myservice.Prevalent.Prevalent;
import com.example.myservice.R;
import com.example.myservice.StartWindows.HomeActivity;
import com.example.myservice.StartWindows.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableOnBackpressureLatest;

public class ProfileActivity extends AppCompatActivity {

    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;
    private ImageView userImage, productImage;
    private TextView userName, userPhone, productName, allProducts;
    private LinearLayout userInfo;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        getUserInfo();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MessageActivity.class));
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });

        allProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MyItemsActivity.class));
            }
        });

        productsRef.orderByChild("date").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Products product = data.getValue(Products.class);
                    if (product.getCreater().equals(Prevalent.currentOnlineUser.getPhone())) {
                        Picasso.get().load(product.getImage()).into(productImage);
                        productName.setText(product.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Ошибка при получении товаров", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        if (Prevalent.currentOnlineUser.getImage() != null)
        {
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(userImage);
        }
        userName.setText(Prevalent.currentOnlineUser.getName());
        userPhone.setText(Prevalent.currentOnlineUser.getPhone());
    }

    private void init()
    {
        homeButton = (Button) findViewById(R.id.home_button);
        searchButton = (Button) findViewById(R.id.search_button);
        addButton = (Button) findViewById(R.id.add_button);
        messageButton = (Button) findViewById(R.id.messsage_button);
        favouritesButton = (Button) findViewById(R.id.favourites_button);
        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        userInfo = findViewById(R.id.user_info);
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productsRef = FirebaseDatabase.getInstance().getReference("User_products").child(Prevalent.currentOnlineUser.getPhone());
        allProducts = findViewById(R.id.all_products);
    }
}