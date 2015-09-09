package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.db.ContactsContract;
import com.parse.starter.db.ContactsDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael on 9/3/2015.
 */
public class Toolbox {

    public static String Email;
    public static Context cont;
    public static Bitmap bit;

    public static SQLiteDatabase initializeDB(Context appContext) {
        ContactsDBHelper helper = new ContactsDBHelper(appContext);
        return helper.getWritableDatabase();
    }


    public static ArrayList<String> getMyContacts(SQLiteDatabase sqlDB) {
        ArrayList<String> newList = new ArrayList<String>();
        Cursor cursor = sqlDB.query(ContactsContract.TABLE,
                new String[]{ContactsContract.Columns.CONTACTITEM},
                null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            newList.add(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Columns.CONTACTITEM)));
        }
        return newList;
    }

    public static SQLiteDatabase insertContact(SQLiteDatabase sqlDB, String newContact) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ContactsContract.Columns.CONTACTITEM, newContact);
        sqlDB.insertWithOnConflict(ContactsContract.TABLE, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        return sqlDB;
    }

    public static String getMyID(Context appContext) {
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserID", null);
        return restoredText;
    }

    public static String getMyUserType(Context appContext) {
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserType", null);
        return restoredText;
    }

    public static boolean isReceiver(Context appContext) {
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserType", null);
        return (restoredText.equalsIgnoreCase("RECEIVER"));
    }

    public static boolean isSender(Context appContext) {
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserType", null);
        return (restoredText.equalsIgnoreCase("SENDER"));
    }

    public static boolean setMyID(Context appContext, String userID) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("kiss", appContext.MODE_PRIVATE).edit();
        editor.putString("MyUserID", userID);
        return editor.commit();
    }

    public static boolean setMyUserType(Context appContext, String userType) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("kiss", appContext.MODE_PRIVATE).edit();
        editor.putString("MyUserType", userType);
        return editor.commit();
    }

    public static boolean addPendingContact(String receiver) {
        ParseObject object = new ParseObject("PendingRequests");
        object.put("Sender", ParseUser.getCurrentUser().getEmail().toString());
        object.put("Receiver", receiver);
        object.saveInBackground();
        return true;
    }

    public static boolean hasContacts() {
        return ParseUser.getCurrentUser().has("contacts");
    }

    public static ArrayList<String> getMyParseContacts() {
        String strContacts = ParseUser.getCurrentUser().get("contacts").toString();
        if (strContacts != null) {
            ArrayList<String> contacts = new ArrayList<String>(Arrays.asList(strContacts.split(",")));
            return contacts;
        } else {
            ArrayList<String> contacts = new ArrayList<String>();
            contacts.add(0, "-");
            Log.d("test", contacts.get(0).toString());
            return contacts;
        }
    }

    public static ArrayList<String> getMyParseNicks() {
        String strContacts = ParseUser.getCurrentUser().get("nicks").toString();
        if (strContacts != null) {
            ArrayList<String> contacts = new ArrayList<String>(Arrays.asList(strContacts.split(",")));
            return contacts;
        } else {
            ArrayList<String> contacts = new ArrayList<String>();
            contacts.add(0, "-");
            Log.d("test", contacts.get(0).toString());
            return contacts;
        }
    }

    //    public static Bitmap getProfileImageBySenderEmail(Context ctnx, String email){
//
//
//        Email = email;
//        cont = ctnx;
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
//                                Bitmap profileImage = BitmapFactory.decodeByteArray(data, 0, data.length);
////                                File file = addProfilePicDirectory();
////                                saveImg(file.getAbsolutePath(), Email, profileImage,cont);
//                                bit = profileImage;
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
//        return bit;
//    }
    ////////////////////////
    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private static final String TAG = "ImageZoomCrop";

    public static void e(Throwable e) {
        Log.e(TAG, e.getMessage(), e);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public interface IntentExtras {
        String ACTION_CAMERA = "action-camera";
        String ACTION_GALLERY = "action-gallery";
        String IMAGE_PATH = "image-path";
    }

    public interface PicModes {
        String CAMERA = "Camera";
        String GALLERY = "Gallery";
    }


    /**
     * create a directory
     *
     * @param albumName - name of album
     * @return File - the new directory
     */
    public static File getAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

        return file;
    }

    public static File addProfilePicDirectory() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "KISS_PROFILES");
    }

    public static Bitmap getProfilePic(String contactName, Context ctx) {

        File pic = new File(addProfilePicDirectory().getAbsolutePath() + "/" + contactName + ".jpeg");
        Uri uri = Uri.fromFile(pic);
        try {
            return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * @param fullPath - directory to save to
     * @param name     - seconderyDirectory (for profile pictures empty)
     * @param bmp      - the img
     * @param ctx
     * @return
     */
    public static boolean saveImg(String fullPath, String name, Bitmap bmp, Context ctx) {

        OutputStream fOut = null;
        File file = new File(fullPath, name); // the File to save to
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        try {
            fOut.flush();
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(ctx.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * resize and set picture in right direction
     *
     * @param pic
     * @return
     */
    public static boolean getPicture(File pic) {

        Bitmap res = null;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(pic.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        boolean isVertical = true;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                isVertical = false;
                return false;

            case ExifInterface.ORIENTATION_ROTATE_90:
                isVertical = false;
                return true;

        }
        //return res;
        return false;
    }


    public static boolean addNewContact(String ID, String nick) {
        ParsePush push = new ParsePush();
        push.setChannel("id" + ID);
        push.setMessage(ID+"-"+ParseUser.getCurrentUser().getUsername().toString());
        push.sendInBackground();
        String newContacts;
        String newNicks;
        if (hasContacts()) {
            ArrayList<String> conts = getMyParseContacts();
            ArrayList<String> nicks = getMyParseNicks();
            if (conts.contains(ID))
                return true;
            else {
                conts.add(ID);
                nicks.add(nick);
            }

            newContacts = android.text.TextUtils.join(",", conts);
            newNicks = android.text.TextUtils.join(",", nicks);

        } else {
            newContacts = ID;
            newNicks = nick;
        }
        ParseUser.getCurrentUser().put("contacts",newContacts);
        ParseUser.getCurrentUser().put("nicks",newNicks);
        ParseUser.getCurrentUser().saveInBackground();
        return true;
    }


    public static boolean addSubscriber(String sender){
        ParsePush.subscribeInBackground(sender);

        return true;
    }

public static void signIn(){
    ParseInstallation parseinst = ParseInstallation.getCurrentInstallation();
    String id = parseinst.getInstallationId();
    id = id.substring(id.lastIndexOf("-") + 1, id.length());
    ParseUser user = new ParseUser();
    user.setEmail(id + "@kiss.com");
    user.setUsername(id);
    user.setPassword(id);
    user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {

            } else {

            }
        }
    });
}

    public static void finishedSendingImages() {
        ParsePush push = new ParsePush();
        push.setChannel(ParseUser.getCurrentUser().getUsername().toString());
        push.setMessage("");
        push.sendInBackground();
    }

    public static void addNewSenderContact(String ID){
        String newContacts;

        if (hasContacts()) {
            ArrayList<String> conts = getMyParseContacts();
            if (conts.contains(ID))
                return;
            else {
                conts.add(ID);
            }

            newContacts = android.text.TextUtils.join(",", conts);
        } else {
            newContacts = ID;

        }
        ParseUser.getCurrentUser().put("contacts",newContacts);
        ParseUser.getCurrentUser().saveInBackground();
        return;
    }

    public static void finishedSendingImagesToID(String receiverID) {
        ParsePush push = new ParsePush();
        push.setChannel(("id"+receiverID));
        push.setMessage(receiverID+"-"+"IMAGES READY");
        push.sendInBackground();
    }

    public static String getNickById(String senderId){
        String nick = null;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", senderId);
        List<ParseUser> userList = new ArrayList<ParseUser>();
        try {
            userList = query.find();
            if (userList.size()==0)
                return "";
            ParseObject object = userList.get(0);
            nick = object.getString("nick").toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nick;
    }
}