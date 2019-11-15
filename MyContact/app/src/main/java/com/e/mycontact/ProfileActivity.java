package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.database.Profile;
import com.e.mycontact.provider.ContactProvider;
import com.e.mycontact.provider.ProfileProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        FloatingActionButton fab2 = findViewById(R.id.fab);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeEdit();
            }
        });
        getIntentValue();
    }
    private void ChangeEdit(){
        Intent i = new Intent(this,EditProfileActivity.class);
        startActivity(i);
    }
    private void getIntentValue(){
        Uri uri_get= ContentUris.withAppendedId(ProfileProvider.CONTENT_URI,1);

        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_IMAGE,ContactDatabase.COL_DATE_OF_BORN };
        Cursor p = getContentResolver().query(uri_get,projection,null,null,"name asc");
        ArrayList<Profile> profiles = ProfileProvider.getAllContact(p);
        profile = profiles.get(0);

        TextView txt_name = findViewById(R.id.txt_name);
        txt_name.setText(profile.getName());
        TextView txt_phone = findViewById(R.id.txt_phone);
        txt_phone.setText(profile.getPhone());
        TextView txt_email = findViewById(R.id.txt_email);
        txt_email.setText(profile.getEmail());
        TextView txt_address = findViewById(R.id.txt_address);
        txt_address.setText(profile.getAddress());

        TextView txt_dateofborn = findViewById(R.id.txt_dob);
        txt_dateofborn.setText(profile.getDateofborn());
        ImageView avt = findViewById(R.id.img_profile);
        if(profile.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getImage(), 0, profile.getImage().length);
            avt.setImageBitmap(bitmap);
        }
    }

}
