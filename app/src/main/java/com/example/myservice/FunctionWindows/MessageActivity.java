package com.example.myservice.FunctionWindows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myservice.R;
import com.example.myservice.StartWindows.HomeActivity;

public class MessageActivity extends AppCompatActivity {

    private Button homeButton, searchButton, addButton, messageButton, favouritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, AddNewItemActivity.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, SearchActivity.class));
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageActivity.this, "Вы уже находитесь здесь", Toast.LENGTH_SHORT).show();
            }
        });

        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, FavouriteActivity.class));
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, HomeActivity.class));
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
        messageButton.setBackgroundResource(R.drawable.message_image_dark);
    }
}