/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;


public class LoginActivity extends Activity {

    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        mode = b.getString(getString(R.string.loginOrRegister));


//      ParseObject testObject = new ParseObject("TestObject");
//      testObject.put("moooo", "mar");
//      testObject.saveInBackground();

        final Button searchButton = (Button) findViewById(R.id.btnGo);
        final EditText ID = (EditText) findViewById(R.id.etUserName);
        final EditText Password = (EditText) findViewById(R.id.etPass);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(String.valueOf(R.string.usersTableName));
                Log.d("test", "here1");
                query.whereEqualTo("ID", ID.getText().toString());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objectList, ParseException e) {
                        // TODO Auto-generated method stub
                        Log.d("test", "inside DOne");

                        if ((objectList != null) && !objectList.isEmpty()) {
                            Log.d("test", "not empty");
                            ParseObject object = objectList.get(0);
                            String parsePassword = object.getString(String.valueOf(R.string.password));
                            if (parsePassword.equals(Password.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "password correct", Toast.LENGTH_SHORT).show();
                                Toolbox.setMyID(getApplicationContext(),ID.getText().toString());
                            } else {
                                if (mode.equals(R.string.login))
                                    Toast.makeText(LoginActivity.this, "password INcorrect!!!", Toast.LENGTH_LONG).show();
                                else if (mode.equals(R.string.register)) {
                                    Toast.makeText(LoginActivity.this, "Occupied User Name - Please pick another one", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d("test", "is empty");
                            if (mode.equals(R.string.login))
                                Toast.makeText(LoginActivity.this, "No such user exist!!!", Toast.LENGTH_LONG).show();
                            else if (mode.equals(R.string.register)) {
                                ParseObject register = new ParseObject(getString(R.string.usersTableName));
                                register.put(getString(R.string.id), ID.getText().toString());
                                register.put(getString(R.string.password), Password.getText().toString());
                                register.saveInBackground();
                                Toolbox.setMyID(getApplicationContext(),ID.getText().toString());
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
