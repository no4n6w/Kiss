package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/4/2015.
 */
public class ItemListBaseAdapter extends BaseAdapter {
    private static ArrayList<String> itemDetailsArrayList;
    public Bitmap profileImage;
    public String nickStr;
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

        // Get nick name and set it
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", itemDetailsArrayList.get(position).toString());
        Log.d("test", itemDetailsArrayList.get(position).toString() + " to make sure");
        List<ParseUser> userList = new ArrayList<ParseUser>();
        try {
            userList = query.find();
            ParseObject object = userList.get(0);
            nickStr = object.getString("nick").toString();
            ParseFile fileObject = object.getParseFile("ProfileImageFile");
            holder.txt_itemDescription.setText(nickStr);
            try {
                byte[] data = fileObject.getData();
                Bitmap profileIm = BitmapFactory.decodeByteArray(data, 0, data.length);
                holder.itemImage.setImageBitmap(profileIm);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



//
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> objects, ParseException e) {
//                if (e == null) {
//                    ParseObject object = objects.get(0);
//                    nickStr = object.getString("nick").toString();
//                    Log.d("test",nickStr + " is the nickname");
////                    holder.txt_itemDescription.setText("Hello");
//                    ParseFile fileObject = object.getParseFile("ProfileImageFile");
//                    fileObject.getDataInBackground(new GetDataCallback() {
//                        public void done(byte[] data, ParseException e) {
//                            if (e == null) {
//                                Log.d("test", "here4");
//                                Bitmap profileIm = BitmapFactory.decodeByteArray(data, 0, data.length);
////                                File file = addProfilePicDirectory();
////                                saveImg(file.getAbsolutePath(), Email, profileImage,cont);
//                                profileImage = profileIm;
//                                holder.itemImage.setImageBitmap(profileImage);
//                            } else {
//
//                            }
//                        }
//                    });
//
//                } else {
//                    // Something went wrong.
//                }
//            }
//        });
//
//
//        holder.txt_itemDescription.setText(nickStr + "text");

//        holder.itemImage.setImageBitmap(getProfileImnByEmail(itemDetailsArrayList.get(position).toString()));
//        holder.txt_itemDescription.setText(itemDetailsArrayList.get(position).toString());
//        File imgFile = new File(itemDetailsArrayList.get(position).toString()+".jpg");
//        if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            holder.itemImage.setImageBitmap(myBitmap);
//        }
        return convertView;
    }

    static class ViewHolder {
        TextView txt_itemDescription;
        ImageView itemImage;
    }

//
//    public Bitmap getProfileImnByEmail(String email){
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        Log.d("test", email);
//        Log.d("test", "vcvcv");
//        query.whereEqualTo("username", email);
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> objects, ParseException e) {
//                Log.d("test", "weve got something from the query");
//                if (e == null) {
//                    Log.d("test", Integer.toString(objects.size()));
//                    ParseObject object = objects.get(0);
//                    Log.d("test", "here2");
//                    ParseFile fileObject = object.getParseFile("ProfileImageFile");
//                    Log.d("test", "here3");
//                    fileObject.getDataInBackground(new GetDataCallback() {
//                        public void done(byte[] data, ParseException e) {
//                            if (e == null) {
//                                Log.d("test", "here4");
//                                Bitmap profileIm = BitmapFactory.decodeByteArray(data, 0, data.length);
////                                File file = addProfilePicDirectory();
////                                saveImg(file.getAbsolutePath(), Email, profileImage,cont);
//                                profileImage = profileIm;
//                            } else {
//
//                            }
//                        }
//                    });
//                } else {
//                    // Something went wrong.
//                }
//            }
//        });
//        return profileImage;
//    }
//
//    public String getNickByEmail(String email){
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", email);
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> objects, ParseException e) {
//                if (e == null) {
//                    nickStr = objects.get(0).getString("nick").toString();
//                } else {
//                    // Something went wrong.
//                }
//            }
//        });
//        return nickStr;
//    }
}
