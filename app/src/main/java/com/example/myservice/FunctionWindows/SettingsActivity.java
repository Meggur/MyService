package com.example.myservice.FunctionWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myservice.Prevalent.Prevalent;
import com.example.myservice.R;
import com.example.myservice.StartWindows.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText settingsUsername, settingsAddress;
    private Button leaveButton, saveButton;
    private String checker = "";
    private Uri imageUri;
    private StorageReference storageProfileImageRef;
    private StorageTask uploadTask;
    private static final int GALLERYPICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        userInfoDisplay(profileImage, settingsUsername, settingsAddress);

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                OpenGallery();
            }
        });
    }

    private void userInfoDisplay(final ImageView profileImage, final EditText settingsUsername, final EditText settingsAddress) {
        String phone = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImage);
                    }
                    if (snapshot.child("name").exists())
                    {
                        String name = snapshot.child("name").getValue().toString();
                        settingsUsername.setText(name);
                    }
                    if (snapshot.child("address").exists())
                    {
                        String address = snapshot.child("address").getValue().toString();
                        settingsAddress.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(settingsUsername.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "Заполните имя", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(settingsAddress.getText().toString()))
        {
            Toast.makeText(SettingsActivity.this, "Заполните адрес", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Обновление");
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null)
        {
            final StorageReference fileRef = storageProfileImageRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        String myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", settingsUsername.getText().toString());
                        userMap.put("address", settingsAddress.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        Prevalent.currentOnlineUser.setName(settingsUsername.getText().toString());
                        Prevalent.currentOnlineUser.setImage(myUrl);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                        Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(SettingsActivity.this, "Изображение не выбрано", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", settingsUsername.getText().toString());
        userMap.put("address", settingsAddress.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        Prevalent.currentOnlineUser.setName(settingsUsername.getText().toString());

        startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
        Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null)
        {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(SettingsActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void init()
    {
        leaveButton = (Button) findViewById(R.id.leave_button);
        saveButton = (Button) findViewById(R.id.save_button);
        profileImage = findViewById(R.id.settings_user_image);
        settingsUsername = findViewById(R.id.settings_username);
        settingsAddress = findViewById(R.id.settings_address);
        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile images");
    }
}