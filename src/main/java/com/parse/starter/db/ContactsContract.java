package com.parse.starter.db;

/**
 * Created by Michael on 9/3/2015.
 */

import android.provider.BaseColumns;


public class ContactsContract {

    public static final String DB_NAME = "com.parse.starter.db.contacts";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "contacts";

    public class Columns {
        public static final String CONTACTITEM = "contact";
        public static final String _ID = BaseColumns._ID;
    }
}
