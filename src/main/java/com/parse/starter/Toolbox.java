package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.starter.db.ContactsContract;
import com.parse.starter.db.ContactsDBHelper;

import java.util.ArrayList;


/**
 * Created by Michael on 9/3/2015.
 */
public class Toolbox{

    public static SQLiteDatabase initializeDB(Context appContext){
        ContactsDBHelper helper = new ContactsDBHelper(appContext);
        return helper.getWritableDatabase();
    }


    public static ArrayList<String> getMyContacts(SQLiteDatabase sqlDB){
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

    public static SQLiteDatabase insertContact(SQLiteDatabase sqlDB, String newContact){
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ContactsContract.Columns.CONTACTITEM, newContact);
        sqlDB.insertWithOnConflict(ContactsContract.TABLE, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        return sqlDB;
    }

    public static String getMyID(Context appContext){
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserID", null);
        return restoredText;
    }

    public static String getMyUserType(Context appContext){
        SharedPreferences prefs = appContext.getSharedPreferences("kiss", 0);
        String restoredText = prefs.getString("MyUserType", null);
        return restoredText;
    }

    public static boolean setMyID(Context appContext, String userID){
        SharedPreferences.Editor editor = appContext.getSharedPreferences("kiss", appContext.MODE_PRIVATE).edit();
        editor.putString("MyUserID", userID);
        return editor.commit();
    }

    public static boolean setMyUserType(Context appContext, String userType){
        SharedPreferences.Editor editor = appContext.getSharedPreferences("kiss", appContext.MODE_PRIVATE).edit();
        editor.putString("MyUserType", userType);
        return editor.commit();
    }
}
