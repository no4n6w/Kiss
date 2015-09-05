package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 9/4/2015.
 */
public class ItemListBaseAdapter extends BaseAdapter {
    private static ArrayList<String> itemDetailsArrayList;

    private LayoutInflater l_Inflater;

    public ItemListBaseAdapter(Context context, ArrayList<String> results) {
        itemDetailsArrayList = results;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsArrayList.size();
    }

    public String getItem(int position) {
        return itemDetailsArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_details_view, null);
            holder = new ViewHolder();
            holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.photo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_itemDescription.setText(itemDetailsArrayList.get(position).toString());
        File imgFile = new File(itemDetailsArrayList.get(position).toString()+".jpg");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.itemImage.setImageBitmap(myBitmap);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView txt_itemDescription;
        ImageView itemImage;
    }
}
