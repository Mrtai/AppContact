package com.e.mycontact.Header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.e.mycontact.InfoUserActivity;
import com.e.mycontact.MainActivity;
import com.e.mycontact.R;
import com.shuhart.stickyheader.StickyAdapter;

import java.util.ArrayList;

public class SectionAdapter extends StickyAdapter<RecyclerView.ViewHolder,RecyclerView.ViewHolder> {
    private ArrayList<Section> sectionArrayList;
    private static final int LAYOUT_HEADER = 0;
    private static final int LAYOUT_CHILD = 1;

    public SectionAdapter(Context context, ArrayList<Section> sectionArrayList) {

        // inflater = LayoutInflater.from(context);
        // this.context = context;
        this.sectionArrayList = sectionArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == LAYOUT_HEADER) {
            return new HeaderViewholder(inflater.inflate(R.layout.recycler_view_header_item, parent, false));
        } else {
            final ItemViewHolder itemViewHolder = new ItemViewHolder(inflater.inflate(R.layout.recycler_view_item, parent, false));
            itemViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView txtId = itemViewHolder.textViewId;
                    String id = txtId.getText().toString();
                    Integer uId = Integer.valueOf(id);
                    changeInfoUser(parent.getContext(),uId);

                }
            });

            return itemViewHolder;
        }
    }
    private void changeInfoUser(Context context,Integer id){
        Intent i = new Intent(context,InfoUserActivity.class);
        if(id != null ){
            i.putExtra("id",id);
        }
        context.startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (sectionArrayList.get(position).isHeader()) {
            ((HeaderViewholder) holder).textView.setText(sectionArrayList.get(position).getName());
        } else {
            ((ItemViewHolder) holder).textView.setText(sectionArrayList.get(position).getName());
            if(sectionArrayList.get(position).getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(sectionArrayList.get(position).getImage(), 0, sectionArrayList.get(position).getImage().length);
                ((ItemViewHolder) holder).imageView.setImageBitmap(bitmap);
            }
            if(sectionArrayList.get(position).getFavourite() == 1){

                Drawable placeholder = ((ItemViewHolder) holder).favouriteView.getContext().getResources().getDrawable(R.drawable.like);
                ((ItemViewHolder) holder).favouriteView.setImageDrawable(placeholder);
            }
            ((ItemViewHolder) holder).textViewId.setText(String.valueOf(sectionArrayList.get(position).getId()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionArrayList.get(position).isHeader()) {
            return LAYOUT_HEADER;
        } else
            return LAYOUT_CHILD;
    }

    @Override
    public int getItemCount() {
        return sectionArrayList.size();
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return sectionArrayList.get(itemPosition).sectionPosition();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int headerPosition) {
        ((HeaderViewholder) holder).textView.setText(sectionArrayList.get(headerPosition).getName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return createViewHolder(parent, LAYOUT_HEADER);
    }

    public static class HeaderViewholder extends RecyclerView.ViewHolder {
        TextView textView;

        HeaderViewholder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView textViewId;
        ImageView favouriteView;
        ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.avt_user);
            textViewId = itemView.findViewById(R.id.txt_id);
            favouriteView = itemView.findViewById(R.id.favourite);

        }

    }
}
