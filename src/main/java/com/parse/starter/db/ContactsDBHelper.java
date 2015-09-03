package com.parse.starter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael on 9/3/2015.
 */
public class ContactsDBHelper extends SQLiteOpenHelper {

    public ContactsDBHelper(Context context) {
        super(context, ContactsContract.DB_NAME, null, ContactsContract.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT)", ContactsContract.TABLE,
                        ContactsContract.Columns.CONTACTITEM);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + ContactsContract.TABLE);
        onCreate(sqlDB);
    }
}
