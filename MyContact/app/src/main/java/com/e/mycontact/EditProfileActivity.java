package com.e.mycontact;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.database.Profile;
import com.e.mycontact.provider.ContactProvider;
import com.e.mycontact.provider.ProfileProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Profile profile;
    private EditText edt_dateofborn;
    private DatePickerDialog dateofbornDatepicker;
    private SimpleDateFormat dateFormatter;
    final int RESULT_LOAD_IMAGE = 1;
    final int CAMERA = 2;
    CircleImageView imageView;
    int flag = 0;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getIntentValue();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewBySchedule();
        setDateTimeField();
        Button btn = findViewById(R.id.btn_camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });
        imageView   = findViewById(R.id.avt);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAlbum();
            }
        });

        Button btn_save= findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_data();
                changeProfile();

            }
        });
    }

    private void changeProfile() {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    private void save_data(){
        ContentValues contentValues = new ContentValues();
        EditText editText = this.findViewById(R.id.txt_name);
        String name = editText.getText().toString();

        EditText editPhone = this.findViewById(R.id.txt_phone);
        String phone = editPhone.getText().toString();

        EditText editAddress = this.findViewById(R.id.txt_address);
        String address = editAddress.getText().toString();

        EditText editMail = this.findViewById(R.id.txt_email);
        String email = editMail.getText().toString();

        EditText edtBorn = this.findViewById(R.id.txt_dateofborn);
        String dateBorn = edtBorn.getText().toString();
        //get image
        if(bitmap != null){
            try {
                byte[] bytes = getByteArray(bitmap);
                contentValues.put(ContactDatabase.COL_IMAGE, bytes);
                bitmap = null;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //end get image
        //add value
        contentValues.put(ContactDatabase.COL_NAME,name);
        contentValues.put(ContactDatabase.COL_PHONE,phone);
        contentValues.put(ContactDatabase.COL_ADDRESS,address);
        contentValues.put(ContactDatabase.COL_EMAIL,email);
        contentValues.put(ContactDatabase.COL_DATE_OF_BORN,dateBorn);
        Uri uri=null;
        try {

            Uri uri_get= ContentUris.withAppendedId(ProfileProvider.CONTENT_URI,1);
            int result = getContentResolver().update(uri_get,contentValues,null,null);
            if(result == 1){
                Toast.makeText(this, "Update success", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Update fail", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getIntentValue() {
        Uri uri_get= ContentUris.withAppendedId(ProfileProvider.CONTENT_URI,1);

        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_IMAGE,ContactDatabase.COL_DATE_OF_BORN };
        Cursor p = getContentResolver().query(uri_get,projection,null,null,"name asc");
        ArrayList<Profile> profiles = ProfileProvider.getAllContact(p);

        profile = profiles.get(0);

        EditText txt_name = findViewById(R.id.txt_name);
        txt_name.setText(profile.getName());

        EditText txt_phone = findViewById(R.id.txt_phone);
        txt_phone.setText(profile.getPhone());

        EditText txt_email = findViewById(R.id.txt_email);
        txt_email.setText(profile.getEmail());

        EditText txt_address = findViewById(R.id.txt_address);
        txt_address.setText(profile.getAddress());

        EditText txt_dateofborn = findViewById(R.id.txt_dateofborn);
        txt_dateofborn.setText(profile.getDateofborn());

        ImageView avt = findViewById(R.id.avt);
        if(profile.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getImage(), 0, profile.getImage().length);
            avt.setImageBitmap(bitmap);
        }
    }
    private void findViewBySchedule(){
        edt_dateofborn = findViewById(R.id.txt_dateofborn);
        edt_dateofborn.setInputType(InputType.TYPE_NULL);

    }

    private void setDateTimeField(){
        edt_dateofborn.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        dateofbornDatepicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_dateofborn.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onClick(View view){
        if(view == edt_dateofborn){
            dateofbornDatepicker.show();
        }
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        bitmap = null;
        Uri uri=null;
        if (resultCode == RESULT_OK) {
            if(reqCode == RESULT_LOAD_IMAGE ){
                try {
                    uri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    bitmap = selectedImage;
                    imageView.setImageBitmap(selectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(EditUserActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if(reqCode == CAMERA){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                bitmap = thumbnail;
                imageView.setImageBitmap(thumbnail);
            }
            flag = 0;
        }else {
            //Toast.makeText(EditUserActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    //Custome function
    private void getImageFromAlbum(){
        try{
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            flag = 1;
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }catch(Exception exp){
            Log.i("Error",exp.toString());
        }
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        flag = 2;
        startActivityForResult(intent, CAMERA);
    }

    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}
