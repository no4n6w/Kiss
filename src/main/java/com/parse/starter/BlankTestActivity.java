package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParsePush;


public class BlankTestActivity extends Activity {

    public String nickStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_test);

        Button butt = (Button) findViewById(R.id.button);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
         Button butt = (Button) findViewById(R.id.button);
         butt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toolbox.addSubscriber("Adamssara@gmail.com");
                 ParsePush.subscribeInBackground("blabla");
             }
         });
// ParseUser user = ParseUser.getCurrentUser();
//                user.put("contacts", "theharshan@gmail.com");
//                user.saveInBackground();
//                Button butt = (Button) findViewById(R.id.button);
//                String str = getNickByEmail("theharshan@gmail.com");
//                Log.d("test", str + "really???");
//                butt.setText(getNickByEmail("theharshan@gmail.com"));
//                getProfileImageBySenderEmail(getApplicationContext(),"theharshan@gmail.com");
//                ImageView imv = (ImageView) findViewById(R.id.imageView);
//                imv.setImageBitmap(image);
            }
        });
    }

//    public String getNickByEmail(String email){
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", email);
//        List<ParseObject> objects = query.find();
//            public void done(, ParseException e) {
//                if (e == null) {
//                    Toast.makeText(BlankTestActivity.this, Integer.toString(objects.size()),Toast.LENGTH_LONG).show();
//                    ParseObject obj = objects.get(0);
//                    nickStr = obj.getString("nick");
//                    Log.d("test", nickStr);
//                } else {
//                    // Something went wrong.
//                }
//                return nickStr;
//            }
//        });

//    }

//    public Bitmap getProfileImageBySenderEmail(String email){
//
//
//        String Email = email;
//        Context cont = ctnx;
//        Bitmap bit;
//
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
//                                ImageView imv = (ImageView) findViewById(R.id.imageView);
//                                imv.setImageBitmap(profileImage);
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
//        return true;
//    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank_test, menu);
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
