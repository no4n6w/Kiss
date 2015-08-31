/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class MainActivity extends ActionBarActivity {


    Uri selectedImageUri; // Global Variable
    String  selectedPath; // Global Variable


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
//      ParseObject testObject = new ParseObject("TestObject");
//      testObject.put("moooo", "mar");
//      testObject.saveInBackground();

      selectImage();
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

    public boolean selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture to upload "), 10);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
                    ImageView imView = (ImageView)findViewById(R.id.imView);
            imView.setImageURI(imageUri);
//            InputStream image_stream = getContentResolver().openInputStream(selectedImage);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                uploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//
//            if(data.getData() != null){
//                selectedImageUri = data.getData();
//            }else{
//                Log.d("selectedPath1 : ", "Came here its null !");
//                Toast.makeText(getApplicationContext(), "failed to get Image!", 500).show();
//            }
//
//            if (requestCode == 10)
//
//            {
//                selectedPath = selectedImageUri.getPath();
//
//                Log.d("selectedPath1 : " ,selectedPath);
//            }

//            uploadImage(bitmap);

        }

    }


    /**
     * Uploads an image to the main table in parse, with field "uploader" and "recipient"
     * @return
     */
    public boolean uploadImage(Bitmap bitmap){
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.androidbegin);
        Log.v("tag", "beforeImagePath");
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
//        File finalFile = new File(getRealPathFromURI(tempUri));
//        ImageView imView = (ImageView)findViewById(R.id.imView);
//        imView.setImageBitmap(bitmap);

////        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        // Convert it to byte
//        Log.v("tag","afterImagePath");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("androidbegin.jpeg", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("TestObject");

        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", "AndroidBegin Logo");

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);

        // Create the class and the columns
        imgupload.saveInBackground();

        // Show a simple toast message
        Toast.makeText(MainActivity.this, "Image Uploaded",
                Toast.LENGTH_SHORT).show();
        return true;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
