package com.e.mycontact.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.e.mycontact.R;
import com.e.mycontact.database.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Context context;
    int layout;
    ArrayList<Contact> arrayList;

    public ContactAdapter(@NonNull Context context, @LayoutRes int resource,@NonNull ArrayList<Contact> object){
        super(context,resource,object);
        this.context = context;
        this.layout = resource;
        this.arrayList = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View covertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        covertView = inflater.inflate(layout,null);

        ImageView img = covertView.findViewById(R.id.avt);
        TextView txt1 = covertView.findViewById(R.id.txt_row_name);
        TextView txtId = covertView.findViewById(R.id.txt_id);

        if(arrayList.get(position).getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(arrayList.get(position).getImage(), 0, arrayList.get(position).getImage().length);
            img.setImageBitmap(bitmap);
        }
        txt1.setText(arrayList.get(position).getName());
        txtId.setText(String.valueOf(arrayList.get(position).getId()));
        return covertView;

    }
}
