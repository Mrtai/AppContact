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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    CircleImageView imageView;
    int flag = 0;
    Bitmap bitmap = null;
    ArrayAdapter<String> dataAdapter;
    Spinner siSpinner ;
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
        Button btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMain();
            }
        });

    }
    private void changeMain(){
        Intent i = new Intent(this, MainActivity.class);
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
        contentValues.put(ContactDatabase.COL_FAVOURITE,0);
        contentValues.put(ContactDatabase.COL_GROUP,c_group);
        Uri uri=null;
        try {
            uri = getContentResolver().insert(ContactProvider.CONTENT_URI,contentValues);
            Log.d("uri tai",uri.toString());
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
                    Toast.makeText(EditUserActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if(reqCode == CAMERA){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                bitmap = thumbnail;
                imageView.setImageBitmap(thumbnail);
            }
            flag = 0;
        }else {
            Toast.makeText(EditUserActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
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
