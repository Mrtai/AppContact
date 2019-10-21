package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img= findViewById(R.id.img_profile);
        Button btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivityAdd();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActiveProfile();
            }
        });

    }

    private void changeActiveProfile(){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private void changeActivityAdd(){
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }
}
