package com.e.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.SearchView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.e.mycontact.Adapter.ContactAdapter;
import com.e.mycontact.database.Contact;
import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.provider.ContactProvider;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SearchView searchView;
    ListView listView;
    ArrayList<Contact> arrayList;
    ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        arrayList = new ArrayList<>();
        listView = findViewById(R.id.listSearch);
        contactAdapter = new ContactAdapter(SearchActivity.this,R.layout.layout_row,arrayList);
        listView.setAdapter(contactAdapter);
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchContact(newText);
                return false;
            }
        });
        listView.setOnItemClickListener(this);


    }

    private void SearchContact(String newText){
        String[] projection = { ContactDatabase.ID, ContactDatabase.COL_NAME,ContactDatabase.COL_PHONE,ContactDatabase.COL_ADDRESS,
                ContactDatabase.COL_EMAIL,ContactDatabase.COL_FACEBOOK,ContactDatabase.COL_IMAGE,ContactDatabase.COL_NOTE,ContactDatabase.COL_SCHEDULE,ContactDatabase.COL_DATE_OF_BORN };
        Cursor contacts = getContentResolver().query(ContactProvider.CONTENT_URI,projection,null,null,"name asc");
        ArrayList<Contact> arrayList = ContactProvider.getAllContact(contacts);
        ArrayList<Contact> tmp= new ArrayList<>();
        for(Contact contact:arrayList){
            if(contact.getName().toLowerCase().contains(newText.toLowerCase())){
                tmp.add(contact);
            }
        }
        if(tmp.size() > 0){
            contactAdapter.clear();
            contactAdapter.addAll(tmp);
            contactAdapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
        }

        if(newText.isEmpty()){
            listView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        TextView txtId = view.findViewById(R.id.txt_id);
        String uids = txtId.getText().toString();
        Integer uId = Integer.valueOf(uids);
        changeInfoUser(parent.getContext(),uId);
    }
    private void changeInfoUser(Context context, Integer id){
        Intent i = new Intent(context,InfoUserActivity.class);
        if(id != null ){
            i.putExtra("id",id);
        }
        context.startActivity(i);
    }
}
