package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_schedule;
    private EditText edt_dateofborn;
    private DatePickerDialog scheduleDatepicker;
    private DatePickerDialog dateofbornDatepicker;
    private SimpleDateFormat dateFormatter;
    final int RESULT_LOAD_IMAGE = 1;
    final int CAMERA = 2;
    Uri uri = null;
    CircleImageView imageView;
    int flag = 0;
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
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

        Button btnsave= findViewById(R.id.btn_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Savedata();
            }
        });

    }

    private Uri Savedata(){
        ContentValues contentValues = new ContentValues();
        EditText editText = this.findViewById(R.id.txt_name);
        String name = editText.getText().toString();
        EditText editphone = this.findViewById(R.id.txt_phone);
        String phone = editphone.getText().toString();
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
        EditText edtDateofborn = this.findViewById(R.id.txt_dateofborn);
        String dateofborn = edtDateofborn.getText().toString();
        //get image
        if(bitmap != null){
            byte[] bytes = getByteArray(bitmap);
            contentValues.put(ContactDatabase.COL_IMAGE,bytes);
            bitmap = null;
        }
        if( uri != null){
            try {
                InputStream imageStream = getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                byte[] bytes = getByteArray(selectedImage);
                contentValues.put(ContactDatabase.COL_IMAGE,bytes);
                uri = null;
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
        contentValues.put(ContactDatabase.COL_FACEBOOK,fb);
        contentValues.put(ContactDatabase.COL_NOTE,note);
        contentValues.put(ContactDatabase.COL_SCHEDULE,schedule);
        contentValues.put(ContactDatabase.COL_DATE_OF_BORN,dateofborn);

        uri = getContentResolver().insert(ContactProvider.CONTENT_URI,contentValues);
        return uri;
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
        uri = null;
        bitmap = null;
        if (resultCode == RESULT_OK) {
            if(reqCode == RESULT_LOAD_IMAGE ){
                try {
                    uri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    imageView.setImageBitmap(selectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if(reqCode == CAMERA){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                bitmap = thumbnail;
                imageView.setImageBitmap(thumbnail);
            }
            flag = 0;


        }else {
//            Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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
