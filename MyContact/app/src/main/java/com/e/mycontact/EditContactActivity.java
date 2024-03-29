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

import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactActivity extends AppCompatActivity implements View.OnClickListener{
    Contact contact2;
    private EditText edt_schedule;
    private EditText edt_dateofborn;
    private DatePickerDialog scheduleDatepicker;
    private DatePickerDialog dateofbornDatepicker;
    private SimpleDateFormat dateFormatter;
    final int RESULT_LOAD_IMAGE = 1;
    final int CAMERA = 2;
    CircleImageView imageView;
    int flag = 0;
    Bitmap bitmap = null;
    ArrayAdapter<String> dataAdapter;
    Spinner siSpinner ;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);


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
                changeMain();

            }
        });
        List<String> listGroup = new ArrayList<>();
        listGroup.add("Không");
        listGroup.add("Đồng nghiệp");
        listGroup.add("Gia đình");
        listGroup.add("Bạn bè");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listGroup);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        siSpinner = findViewById(R.id.spinerGroup);

        siSpinner.setAdapter(dataAdapter);

        bt = findViewById(R.id.btn_like);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFavourite();
            }
        });
        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMain();
            }
        });
        getIntentValue();

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

        EditText txt_name = findViewById(R.id.txt_name);
        txt_name.setText(contact2.getName());

        EditText txt_phone = findViewById(R.id.txt_phone);
        txt_phone.setText(contact2.getPhone());

        EditText txt_email = findViewById(R.id.txt_email);
        txt_email.setText(contact2.getEmail());

        EditText txt_address = findViewById(R.id.txt_address);
        txt_address.setText(contact2.getAddress());

        EditText txt_note = findViewById(R.id.txt_note);
        txt_note.setText(contact2.getNote());

        EditText txt_facebook = findViewById(R.id.txt_facebook);
        txt_facebook.setText(contact2.getFacebook());

        EditText txt_schedule = findViewById(R.id.txt_schedule);
        txt_schedule.setText(contact2.getSchedule());

        EditText txt_dateofborn = findViewById(R.id.txt_dateofborn);
        txt_dateofborn.setText(contact2.getDateofborn());

        ImageView avt = findViewById(R.id.avt);
        if(contact2.getImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(contact2.getImage(), 0, contact2.getImage().length);
            avt.setImageBitmap(bitmap);
        }
        if(contact2.getC_group() == 0){
            siSpinner.setSelection(0);
        }
        else if(contact2.getC_group() == 1){
            siSpinner.setSelection(1);
        }
        else if(contact2.getC_group() == 2){
            siSpinner.setSelection(2);
        }
        else{
            siSpinner.setSelection(3);
        }
        if(contact2.getFavourite() == 1){

            bt.setBackground(getResources().getDrawable(R.drawable.like));
        }


    }

    private void changeFavourite(){
        Intent intent = this.getIntent();
        Integer id = intent.getIntExtra("id",0);
        Uri uri_get= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,id);
        ContentValues contentValues = new ContentValues();
        if(contact2.getFavourite() == 0){
            contentValues.put(ContactDatabase.COL_FAVOURITE,1);
            try {
                int result = getContentResolver().update(uri_get,contentValues,null,null);
                if(result == 1){
                    Toast.makeText(this, "My favourite", Toast.LENGTH_LONG).show();
                    contact2.setFavourite(1);
                    bt.setBackground(getResources().getDrawable(R.drawable.like));
                }
                else{
                    Toast.makeText(this, "Update fail", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else{
            contentValues.put(ContactDatabase.COL_FAVOURITE,0);
            try {
                int result = getContentResolver().update(uri_get,contentValues,null,null);
                if(result == 1){
                    Toast.makeText(this, "Normal", Toast.LENGTH_LONG).show();
                    bt.setBackground(getResources().getDrawable(R.drawable.dislike));
                    contact2.setFavourite(0);
                }
                else{
                    Toast.makeText(this, "Update fail", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
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

        EditText editFB = this.findViewById(R.id.txt_facebook);
        String fb = editFB.getText().toString();

        EditText editNote = this.findViewById(R.id.txt_note);
        String note = editNote.getText().toString();

        EditText edtSchedule = this.findViewById(R.id.txt_schedule);
        String schedule = edtSchedule.getText().toString();

        EditText edtBorn = this.findViewById(R.id.txt_dateofborn);
        String dateBorn = edtBorn.getText().toString();

        Integer c_group = 0;
        String text = siSpinner.getSelectedItem().toString();
        if(text.equals("Không")){
            c_group = 0;
        }
        else if(text.equals("Đồng nghiệp")){
            c_group = 1;
        }
        else if(text.equals("Gia đình")){
            c_group = 2;
        }
        else{
            c_group = 3;
        }
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
//        if( uri != null){
//            try {
//                InputStream imageStream = getContentResolver().openInputStream(uri);
//                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                byte[] bytes = getByteArray(selectedImage);
//                contentValues.put(ContactDatabase.COL_IMAGE,bytes);
//                uri = null;
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//        }
        //end get image
        //add value
        contentValues.put(ContactDatabase.COL_NAME,name);
        contentValues.put(ContactDatabase.COL_PHONE,phone);
        contentValues.put(ContactDatabase.COL_ADDRESS,address);
        contentValues.put(ContactDatabase.COL_EMAIL,email);
        contentValues.put(ContactDatabase.COL_FACEBOOK,fb);
        contentValues.put(ContactDatabase.COL_NOTE,note);
        contentValues.put(ContactDatabase.COL_SCHEDULE,schedule);
        contentValues.put(ContactDatabase.COL_DATE_OF_BORN,dateBorn);
        contentValues.put(ContactDatabase.COL_GROUP,c_group);
        Uri uri=null;
        try {
            Intent intent = this.getIntent();
            Integer id = intent.getIntExtra("id",0);
            Uri uri_get= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,id);
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
    private void findViewBySchedule(){
        edt_schedule = findViewById(R.id.txt_schedule);
        edt_schedule.setInputType(InputType.TYPE_NULL);

        edt_dateofborn = findViewById(R.id.txt_dateofborn);
        edt_dateofborn.setInputType(InputType.TYPE_NULL);


    }
    private void setDateTimeField(){
        edt_schedule.setOnClickListener(this);
        edt_dateofborn.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        scheduleDatepicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_schedule.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
        if(view == edt_schedule){
            scheduleDatepicker.show();
        }
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
