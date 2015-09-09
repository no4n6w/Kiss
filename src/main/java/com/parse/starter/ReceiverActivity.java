package com.parse.starter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.starter.util.SystemUiHider;

import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ReceiverActivity extends Activity{

    SQLiteDatabase sqlDB;
    String myID;
    String channel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        TextView idText = (TextView) findViewById(R.id.idView);
        idText.setText("ID: "+ParseUser.getCurrentUser().getUsername().toString());
        Log.d("test", "how are we doing?");
        myID = ParseUser.getCurrentUser().getUsername().toString();
        channel = "id"+myID;
        ParsePush.subscribeInBackground(channel);
//        ,new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("test", "successfully subscribed to the broadcast channel with "+channel);
//                } else {
//                    Log.e("test", " failed to subscribe for push "+channel, e);
//                }
//            }
//        });
//        sqlDB = Toolbox.initializeDB(getApplicationContext());
//        ArrayList<String> contacts = Toolbox.getMyContacts(sqlDB);
        if (!Toolbox.hasContacts())

            return;
        ArrayList<String> contacts = Toolbox.getMyParseContacts();

//         Toast.makeText(ReceiverActivity.this, contacts.get(0).toString() + " of size " + Integer.toString(contacts.size()), Toast.LENGTH_LONG).show();
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new ItemListBaseAdapter(this, contacts));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                String senderName = lv1.getItemAtPosition(position).toString();
                Toast.makeText(ReceiverActivity.this, "You have chosen : " + " " + senderName, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
