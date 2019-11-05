package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.e.mycontact.Adapter.ContactAdapter;
import com.e.mycontact.Header.ChildModel;
import com.e.mycontact.Header.HeaderModel;
import com.e.mycontact.Header.Section;
import com.e.mycontact.Header.SectionAdapter;
import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;
import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ListView list_user;
    ContactAdapter contactAdapter;

    private ArrayList<Section> sectionArrayList;

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

    }
    @Override
    public  void onStart(){
        super.onStart();
    }
    private void changeActiveProfile(){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private void changeActivityAdd(){
        Intent i = new Intent(this, EditUserActivity.class);
        startActivity(i);
    }
    private void setDataToListView(){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN };
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
