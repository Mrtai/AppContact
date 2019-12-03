package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getConutBB();
        getConutDN();
        getConutGD();
        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMain();
            }
        });
    }

    private void changeMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void getConutDN(){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN ,ContactDatabase.COL_FAVOURITE,ContactDatabase.COL_GROUP};
        String[] selectionArgs = {""};
        String selectionClause = ContactDatabase.COL_GROUP + " = ?";
        selectionArgs[0] = "1";
        Cursor contacts = getContentResolver().query(ContactProvider.CONTENT_URI,projection,selectionClause,selectionArgs,"name asc");
        ArrayList<Contact> arrayList = ContactProvider.getAllContact(contacts);
        TextView dn = findViewById(R.id.count_dn);
        dn.setText(String.valueOf(arrayList.size()));
    }
    private void getConutGD(){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN ,ContactDatabase.COL_FAVOURITE,ContactDatabase.COL_GROUP};
        String[] selectionArgs = {""};
        String selectionClause = ContactDatabase.COL_GROUP + " = ?";
        selectionArgs[0] = "2";
        Cursor contacts = getContentResolver().query(ContactProvider.CONTENT_URI,projection,selectionClause,selectionArgs,"name asc");
        ArrayList<Contact> arrayList = ContactProvider.getAllContact(contacts);
        TextView gd = findViewById(R.id.count_gd);
        gd.setText(String.valueOf(arrayList.size()));
    }
    private void getConutBB(){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN ,ContactDatabase.COL_FAVOURITE,ContactDatabase.COL_GROUP};
        String[] selectionArgs = {""};
        String selectionClause = ContactDatabase.COL_GROUP + " = ?";
        selectionArgs[0] = "3";
        Cursor contacts = getContentResolver().query(ContactProvider.CONTENT_URI,projection,selectionClause,selectionArgs,"name asc");
        ArrayList<Contact> arrayList = ContactProvider.getAllContact(contacts);
        TextView bb = findViewById(R.id.count_bb);
        bb.setText(String.valueOf(arrayList.size()));
    }
}
