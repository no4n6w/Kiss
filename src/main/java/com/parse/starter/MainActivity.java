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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.profileimage.ImageViewActivity;

public class MainActivity extends Activity {
   public  Intent intentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (ParseUser.getCurrentUser().getEmail() == null) {
                    Log.d("test", "No email registered");
                    intentView = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(intentView);
                    finish();
                } else {

                    if (!(Boolean)ParseUser.getCurrentUser().get("emailVerified")) {
                        if (Toolbox.isReceiver(getApplicationContext())) {
                            Log.d("test", "A receiver entered");
                            intentView = new Intent(MainActivity.this, ReceiverActivity.class);
                        } else {
                            Log.d("test", "A sender sent to verify email");
                            intentView = new Intent(MainActivity.this, SplashScreen.class);
                        }
                    } else {
                        if (ParseUser.getCurrentUser().get("nick") == null) {
                            Log.d("test", "A sender which has a verified email but no profile");
                            intentView = new Intent(MainActivity.this, ImageViewActivity.class);
                        } else {
                            Log.d("test", "A sender, just a regular sender");
                            intentView = new Intent(MainActivity.this, SenderActivity.class);
                        }
                    }

                    startActivity(intentView);
                    finish();
                }
            }
        });

    }
}
//
//public class MainActivity extends ActionBarActivity {
//
//    String sender = "michael";
//    String receiver = "sara";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        Log.d("test", "hereMain");
//        setContentView(R.layout.activity_main);
////      ParseObject testObject = new ParseObject("TestObject");
////      testObject.put("moooo", "mar");
////      testObject.saveInBackground();
//
//
//
//
//      Button upload = (Button) findViewById(R.id.Upload);
//      upload.setOnClickListener(new View.OnClickListener() {
//          public void onClick(View arg0) {
//
//        selectImage();
//        ParseAnalytics.trackAppOpenedInBackground(getIntent());
//          }
//      });
//        Button download = (Button) findViewById(R.id.Download);
//        download.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                downloadImages("sara");
//            }
//        });
//
//    }
//
//    public boolean selectImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select picture to upload "), 10);
//        return true;
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == RESULT_OK) {
//            Uri imageUri = data.getData();
//            ImageView imView = (ImageView) findViewById(R.id.imView);
//            imView.setImageURI(imageUri);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                uploadImage(bitmap, receiver);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * Uploads an image to the main table in parse, with field "uploader" and "recipient"
//     * @return
//     */
//    public boolean uploadImage(Bitmap bitmap, String receiverID) {
//        Log.v("tag", "beforeImagePath");
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        // Compress image to lower quality scale 1 - 100
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] image = stream.toByteArray();
//
//        // Create the ParseFile
//              ParseFile file = new ParseFile("image.jpeg", image);
//              // Upload the image into Parse Cloud
//              file.saveInBackground();
//
//        // Create a New Class called "Image Table" in Parse
//        ParseObject imgupload = new ParseObject("ImageTable");
//
//        // Create a column named "Sender ID" and set the string
//        imgupload.put("SenderID", sender);
//
//        // Create a column named "Sender ID" and set the string
//        imgupload.put("ReceiverID", receiverID);
//
////              // Create a column named "ImageFile" and insert the image
//              imgupload.put("ImageFile", file);
////
//        // Create the class and the columns
//        imgupload.saveInBackground();
//
//        // Show a simple toast message
//        Toast.makeText(MainActivity.this, "Image Uploaded",
//                Toast.LENGTH_SHORT).show();
//        return true;
//    }
//
//    /**
//     * Uploads an image to the main table in parse, with field "uploader" and "recipient"
//     * @return
//     */
//    public boolean downloadImages(String receiverID) {
//
//
//        // Locate the class table named "ImageUpload" in Parse.com
//
//
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageTable");
//        query.whereEqualTo("ReceiverID", "sara");
//        // Locate the objectId from the class
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objectList, ParseException e) {
//                // TODO Auto-generated method stub
//                if (!objectList.isEmpty()) {
//                    ParseObject object = objectList.get(objectList.size()-1);
//
//                    ParseFile fileObject = object.getParseFile("ImageFile");
//
//                    fileObject.getDataInBackground(new GetDataCallback() {
//                        @Override
//                        public void done(byte[] data, ParseException e) {
//
//                            if (e == null) {
//                                Log.d("test", "We've got data in data.");
//                                // Decode the Byte[] into // Bitmap
//                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                                // Get the ImageView from
//                                // main.xml
//                                ImageView image = (ImageView) findViewById(R.id.imDownloadView);
//
//                                // Set the Bitmap into the
//                                // ImageView
//                                image.setImageBitmap(bmp);
//
//
//                            } else {
//                                Log.d("test", "There was a problem downloading the data.");
//                            }
//                        }
//                    });
//
//                } else {
//                    int size = objectList.size();
//                    String StrSize = Integer.toString(size);
//                    Toast.makeText(MainActivity.this, StrSize, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//
//
//        return true;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
