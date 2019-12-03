package com.e.mycontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;

;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class InfoUserActivity extends AppCompatActivity {
    Contact contact2;
    private static final int REQUEST_CALL = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        FloatingActionButton fab = findViewById(R.id.btn_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ChangeEdit();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.btn_delete);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                       .setAction("Action", null).show();
                DeleteContact();

            }
        });
        Button btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMakeCall();
            }
        });
        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSendSMS();
            }
        });
        getIntentValue();

    }

    private void ChangeEdit(){
        Integer uid = contact2.getId();
        Intent i = new Intent(this,EditContactActivity.class);
        i.putExtra("id",uid);
        startActivity(i);
    }
    private void DeleteContact(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(InfoUserActivity.this);
        builder1.setTitle("Thông báo");
        builder1.setCancelable(false);
        builder1.setMessage("Bạn có chắc chắn muốn xóa \""+contact2.getName()+"\"");

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri_get= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,contact2.getId());
                int result = getContentResolver().delete(uri_get,null,null);
                if(result == 1){
                    Toast.makeText(InfoUserActivity.this, "Xóa thành công", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(InfoUserActivity.this, "Xóa lỗi", Toast.LENGTH_LONG).show();
                }
                changeMain();
            }
        });


        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();
    }
    private void changeMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void getIntentValue(){
        Intent intent = this.getIntent();
        Integer id = intent.getIntExtra("id",0);
        Uri uri_get= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,id);

        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN ,ContactDatabase.COL_FAVOURITE,ContactDatabase.COL_GROUP};
        Cursor contact = getContentResolver().query(uri_get,projection,null,null,"name asc");
        ArrayList<Contact> contact1 = ContactProvider.getAllContact(contact);
        contact2 = contact1.get(0);

        TextView txt_name = findViewById(R.id.txt_name);
        txt_name.setText(contact2.getName());
        TextView txt_phone = findViewById(R.id.txt_phone);
        txt_phone.setText(contact2.getPhone());
        TextView txt_email = findViewById(R.id.txt_email);
        txt_email.setText(contact2.getEmail());
        TextView txt_address = findViewById(R.id.txt_address);
        txt_address.setText(contact2.getAddress());
        TextView txt_note = findViewById(R.id.txt_note);
        txt_note.setText(contact2.getNote());
        TextView txt_facebook = findViewById(R.id.txt_facebook);
        txt_facebook.setText(contact2.getFacebook());
        TextView txt_schedule = findViewById(R.id.txt_schedule);
        txt_schedule.setText(contact2.getSchedule());
        TextView txt_dateofborn = findViewById(R.id.txt_dateofborn);
        txt_dateofborn.setText(contact2.getDateofborn());
        ImageView avt = findViewById(R.id.avt);
        if(contact2.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(contact2.getImage(), 0, contact2.getImage().length);
            avt.setImageBitmap(bitmap);
        }
        if(contact2.getFavourite() == 1){
            Drawable like = getResources().getDrawable(R.drawable.like);
            ImageView imagef = findViewById(R.id.img_favourite);
            imagef.setImageDrawable(like);
        }
        TextView txt_group = findViewById(R.id.txt_group);
        if(contact2.getC_group() == 1){
            txt_group.setText("Đồng nghiệp");
        }
        else if(contact2.getC_group() == 2){
            txt_group.setText("Gia đình");
        }
        else if(contact2.getC_group() == 3){
            txt_group.setText("Bạn bè");
        }
        else{
            txt_group.setText("Chưa có nhóm");
        }

    }
    /**
     * Thực hiện gọi điện thoại
     */
    public void doMakeCall() {
        Uri uri = Uri.parse("tel:" + contact2.getPhone());
        Intent i = new Intent(Intent.ACTION_CALL, uri);
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InfoUserActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else{
            startActivity(i);
        }

    }
    public  void doSendSMS(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            String number = contact2.getPhone();  // The number on which you want to send SMS
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doMakeCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doSendSMS();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
