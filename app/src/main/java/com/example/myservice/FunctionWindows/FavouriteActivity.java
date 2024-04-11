package com.example.myservice.FunctionWindows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myservice.R;
import com.example.myservice.StartWindows.HomeActivity;

public class FavouriteActivity extends AppCompatActivity {

    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        init();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, SearchActivity.class));
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, MessageActivity.class));
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FavouriteActivity.this, "Вы уже находитесь здесь", Toast.LENGTH_SHORT).show();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavouriteActivity.this, HomeActivity.class));
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
        favouritesButton.setBackgroundResource(R.drawable.favourite_image_dark);
    }
}