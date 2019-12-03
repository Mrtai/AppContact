package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.e.mycontact.Adapter.ContactAdapter;
import com.e.mycontact.Header.ChildModel;
import com.e.mycontact.Header.HeaderModel;
import com.e.mycontact.Header.Section;
import com.e.mycontact.Header.SectionAdapter;
import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.database.Profile;
import com.e.mycontact.provider.ContactProvider;
import com.e.mycontact.provider.ProfileProvider;
import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ListView list_user;
    ContactAdapter contactAdapter;

    private Profile profile ;
    private ArrayList<Section> sectionArrayList;
    ImageView btn_group;
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

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sectionArrayList = new ArrayList<>();

        setDataToListView();
        Log.e("size section",Integer.toString(sectionArrayList.size()));

        SectionAdapter adapter = new SectionAdapter(this,sectionArrayList);

        recyclerView.setAdapter(adapter);

        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);

        decorator.attachToRecyclerView(recyclerView);

        getProfile();


        if(profile.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getImage(), 0, profile.getImage().length);
            img.setImageBitmap(bitmap);
        }
        TextView searchView = findViewById(R.id.txtSearch);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSearch();
            }
        });
        btn_group = findViewById(R.id.btn_group);
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGroup();
            }
        });

    }
    @Override
    public  void onStart(){
        super.onStart();
    }
    private void changeGroup(){
        Intent i = new Intent(this, GroupActivity.class);
        startActivity(i);
    }
    private void changeActiveProfile(){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
    private void getProfile(){
        Uri uri_get= ContentUris.withAppendedId(ProfileProvider.CONTENT_URI,1);
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_IMAGE,ContactDatabase.COL_DATE_OF_BORN };
        Cursor p = getContentResolver().query(uri_get,projection,null,null,"name asc");
        ArrayList<Profile> profiles = ProfileProvider.getAllContact(p);

        if(profiles.size() == 0){
            profile= new Profile();
            createProfile();
        }
        else{
            profile = profiles.get(0);
        }
    }
    private void createProfile(){
        ContentValues contentValues = new ContentValues();
        String name = "My name";
        String phone = "";
        String address = "";
        String email ="";
        String dateBorn ="";
        //add value
        contentValues.put(ContactDatabase.COL_NAME,name);
        contentValues.put(ContactDatabase.COL_PHONE,phone);
        contentValues.put(ContactDatabase.COL_ADDRESS,address);
        contentValues.put(ContactDatabase.COL_EMAIL,email);
        contentValues.put(ContactDatabase.COL_DATE_OF_BORN,dateBorn);
        Uri uri=null;
        try {
            uri = getContentResolver().insert(ProfileProvider.CONTENT_URI,contentValues);
            Log.d("uri tai",uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void changeActivityAdd(){
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }
    private void changeSearch(){
        Intent i = new Intent(this,SearchActivity.class);
        startActivity(i);
    }
    private void setDataToListView(){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN ,ContactDatabase.COL_FAVOURITE,ContactDatabase.COL_GROUP};
        Cursor contacts = getContentResolver().query(ContactProvider.CONTENT_URI,projection,null,null,"name asc");
        ArrayList<Contact> arrayList = ContactProvider.getAllContact(contacts);

        Collections.sort(arrayList, new Comparator<Contact>() {
            @Override
            public int compare(Contact user1, Contact user2) {

                return String.valueOf(removeAccent(user1.getName()).charAt(0)).toUpperCase().compareTo(String.valueOf(removeAccent(user2.getName()).charAt(0)).toUpperCase());
            }
        });

        String lastHeader = "";
        int ichild = 0;
        int ic = 0;
        int size = arrayList.size();
        Log.d("size",String.valueOf(size));
        Log.e("lenght array",String.valueOf(size));
        for (int i = 0 ; i < size;i++){
            String header = String.valueOf(arrayList.get(i).getName().charAt(0)).toUpperCase();

            if(!TextUtils.equals(lastHeader,removeAccent(header))) {
                ichild = i+ic;
                HeaderModel alpha = new HeaderModel(ichild);
                alpha.setheader(removeAccent(header));
                sectionArrayList.add(alpha);
                lastHeader = removeAccent(header);
                ic++;
            }
            ChildModel childModel= new ChildModel(ichild);
            childModel.setChild(arrayList.get(i).getName());
            childModel.setImageChild(arrayList.get(i).getImage());
            childModel.setFavouriteChild(arrayList.get(i).getFavourite());
            childModel.setIdChild(arrayList.get(i).getId());
            sectionArrayList.add(childModel);
        }

    }
    public String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
