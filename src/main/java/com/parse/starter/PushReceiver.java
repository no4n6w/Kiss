package com.parse.starter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Michael on 9/8/2015.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {
    public static final String ACTION="com.androidbook.parse.TestPushAction";
    public static final String PARSE_EXTRA_DATA_KEY="com.parse.Data";
    public static final String PARSE_JSON_ALERT_KEY="alert";
    public static final String PARSE_JSON_CHANNELS_KEY="com.parse.Channel";
    public Context cont;
    public String myId ;
    public int numOfImages = -2;
    public int counter = -2;
    public String message;
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        cont = context;
//        Intent i = new Intent(context, BroadcastReceiverActivity.class);
//        i.putExtras(intent.getExtras());
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
        if (ParseUser.getCurrentUser() == null){
            Log.d("test", "ParseUser null");
        }
        myId = ParseUser.getCurrentUser().getUsername().toString();
        if (myId == null){
            Log.d("test", "myId is null");
        }

        String channel = intent.getExtras().getString(PARSE_JSON_CHANNELS_KEY);
        message = intent.getExtras().getString(PARSE_EXTRA_DATA_KEY);


        Log.d("test","PARSE_EXTRA_DATA_KEY: " +intent.getExtras().getString(PARSE_EXTRA_DATA_KEY));
        Log.d("test","PARSE_JSON_ALERT_KEY: " +intent.getExtras().getString(PARSE_JSON_ALERT_KEY));
        Log.d("test","PARSE_JSON_CHANNELS_KEY: " +intent.getExtras().getString(PARSE_JSON_CHANNELS_KEY));


        if (!channel.contains(myId))
            return;

            message = message.substring(11+myId.length());
            message = message.substring(0,message.indexOf(","));
            message = message.substring(0,message.length()-1);

        if (!message.contains("IMAGES READY")){
//        Toolbox.addSubscriber(senderID);
            Toolbox.addNewSenderContact(message);



        Log.d("test","channel String: " +channel);

        Log.d("test","Sender ID: " +message);


            NotificationManager notificationManager =
                    (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(cont)
                    .setContentIntent(getDialogPendingIntent("New images", "bb"));

            builder.setContentTitle(Toolbox.getNickById(message) + " was added to your contacts!");
            builder.setContentText("Tap to see!");
            builder.setSmallIcon(R.drawable.kisslauncher);
            builder.setAutoCancel(true);

            // OPTIONAL create soundUri and set sound:
            builder.setSound(Uri.parse("android.resource://com.parse.starter/raw/newcontact"));

            notificationManager.notify("MyTag", 0, builder.build());



//            NotificationManager notificationManager =
//                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//            builder.setContentTitle(message + "was added to your contacts!");
//            builder.setContentText("You can now get images from him");
//            builder.setSmallIcon(R.drawable.kisslogosmallicon);
//            builder.setAutoCancel(true);
//
//            // OPTIONAL create soundUri and set sound:
//            builder.setSound(Uri.parse("android.resource://com.parse.starter/raw/song"));
//
//            notificationManager.notify("MyTag", 0, builder.build());


        } else {

            Log.d("test","RECEIVING IMAGESSSSS");



            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageTable");
            query.whereEqualTo("ReceiverID", myId);
            // Locate the objectId from the class

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objectList, ParseException e) {
                    if (numOfImages == -2){
                        numOfImages = objectList.size();
                        counter = numOfImages;
                    }

                    for (int i = 0; i < objectList.size(); i++) {

                        ParseObject object = objectList.get(i);
                        ParseFile fileObject = object.getParseFile("ImageFile");

                        String senderID = object.getString("SenderID");
                        String fileName = fileObject.getName();

                        object.deleteInBackground();

                        fileObject.getDataInBackground(new myGetDataCallback(senderID, fileName));
                        Log.d("test", "finished pic numer :" + i);
                    }

                }

            });







        }
    }

    private PendingIntent getDialogPendingIntent(String dialogText,
                                                 String intentname) {
        return PendingIntent.getActivity(
                cont,
                dialogText.hashCode(),
                new Intent(cont, ReceiverActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, dialogText)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setAction(intentname), 0);
    }


    @Override
    protected Notification getNotification(Context context, Intent intent) {
        // deactivate standard notification
        return null;
    }


    public boolean saveImg(String senderPath, String name, Bitmap bmp) {

//        OutputStream fOut = null;
//        Log.d("test", senderPath+ "       "+name);
//        File file = new File(name); // the File to save to
//        try {
//            fOut = openFileOutput(name, )
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//        try {
//            fOut.flush();
//            fOut.close(); // do not forget to close the stream

        String localPathWithName = "/Kiss/"+senderPath;
        String localNewDirPath = "/KISS/newPictures";

        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        if (height<width){
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        }
        int newHeight = 1200;
        int newWidth = (int) Math.round(newHeight*(width/(float)height));
//        Log.d("test", Double.toString(newHeight*(width/height)));
        bmp.createScaledBitmap(bmp, newWidth,newHeight, false);
        String pathStr;
        for (int i=1;i<=2;i++){

            if (i==1)
                pathStr = localNewDirPath;
            else
                pathStr = localPathWithName;

            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+pathStr;
            Log.d("test","------------------.=>>>>>"+file_path);
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, name);
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                try {
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


//        String place = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, Toolbox.getAlbumStorageDir("haim").getAbsolutePath()+name, "image");
//        TextView fileAddress = (TextView) findViewById(R.id.imAddress);
//        fileAddress.setText(place);
//            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return true;
    }


    public class myGetDataCallback implements GetDataCallback {

        private String senderId;
        private String fileName;

        private PendingIntent getDialogPendingIntent(String dialogText,
                                                     String intentname) {
            return PendingIntent.getActivity(
                    cont,
                    dialogText.hashCode(),
                    new Intent(cont, SlideshowActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, dialogText)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setAction(intentname), 0);
        }

        public myGetDataCallback(String senderID, String fileName) {

            this.senderId = senderID.substring(0,senderID.lastIndexOf("@"));
            this.fileName = fileName;

        }


        @Override
        public void done(byte[] data, ParseException e) {

            if (e == null) {

                // Decode the Byte[] into // Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //TODO: resize!!!
                //bmp = getResizedBitmap(bmp);
//                File relativePath = getAlbumStorageDir("KISS/" + senderId);
                saveImg(senderId, fileName, bmp);
//                Log.d("test", relativePath.getAbsolutePath());
                counter--;
                if (counter <=0){
//                    Intent i = new Intent(cont, SlideshowActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
                    NotificationManager notificationManager =
                            (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(cont)
                            .setContentIntent(getDialogPendingIntent("New images", "bb"));

                    if (message != null)
                        Log.d("test", message);
                    if (numOfImages == 1) {


                        builder.setContentTitle("You have a new image ready!");
                    } else {
                        builder.setContentTitle("You have "+ Integer.toString(numOfImages) + " new images ready!");
                    }
                    builder.setContentText("Tap to see them!");
                    builder.setSmallIcon(R.drawable.kisslauncher);
                    builder.setAutoCancel(true);

                    // OPTIONAL create soundUri and set sound:
                    builder.setSound(Uri.parse("android.resource://com.parse.starter/raw/song"));

                    notificationManager.notify("MyTag", 0, builder.build());







//                    NotificationManager notificationManager =
//                            (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(cont);
//                    builder.setContentTitle(Integer.toString(numOfImages)+" new images are here..");
//                    builder.setContentText("Tap to see them!");
//                    builder.setSmallIcon(R.drawable.kisslogosmallicon);
//                    builder.setAutoCancel(true);
//
//                    // OPTIONAL create soundUri and set sound:
//                    builder.setSound(Uri.parse("android.resource://com.parse.starter/raw/song"));
//
//                    notificationManager.notify("MyTag", 0, builder.build());
                }
                //save to "NEW" folder
//                relativePath = getAlbumStorageDir("KISS/" + "newPictures");
//                saveImg("/KISS/newPictures", fileName, bmp);

//                TextView filename = (TextView) findViewById(R.id.imName);
//                filename.setText(fileName);

//                TextView fileAddress = (TextView) findViewById(R.id.imAddress);
//                fileAddress.setText(relativePath.toString());

                // Get the ImageView from main.xml
//                ImageView image = (ImageView) findViewById(R.id.imDownloadView);
//
//                // Set the Bitmap into the ImageView
//                image.setImageBitmap(bmp);


            } else {
                Log.d("test", "There was a problem downloading the data.");
            }
        }
    }



//
//    public boolean downloadImages(String myId) {
//        Log.d("test", "I'm downloaing images!!!!");
//        // Locate the class table named "ImageUpload" in Parse.com
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageTable");
//        query.whereEqualTo("ReceiverID", myId);
//        // Locate the objectId from the class
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objectList, ParseException e) {
//
//                for (int i = 0; i < objectList.size(); i++) {
//
//                    ParseObject object = objectList.get(i);
//                    ParseFile fileObject = object.getParseFile("ImageFile");
//
//                    String senderID = object.getString("SenderID");
//                    String fileName = fileObject.getName();
//
//                    object.deleteInBackground();
//
//                    fileObject.getDataInBackground(new myGetDataCallback(senderID, fileName));
//                    Log.d("test", "finished pic numer :" + i);
//                }
//
//            }
//
//        });
//
//
//        return true;
//    }
//
//    /**
//     * create a directory
//     *
//     * @param albumName - name of album
//     * @return File - the new directory
//     */
//    public static File getAlbumStorageDir(String albumName) {
//        // Get the directory for the app's private pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
//
//        return file;
//    }
//
//
//    public boolean saveImg(String senderPath, String name, Bitmap bmp) {
//        Log.d("test", "saving image");
//        OutputStream fOut = null;
//        File file = new File(senderPath, name); // the File to save to
//        try {
//            fOut = new cont.openFileOutput()
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("test", Boolean.toString(fOut==null));
//        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//        try {
//            fOut.flush();
//            fOut.close(); // do not forget to close the stream
//            Log.d("test", "before inserting");
//            MediaStore.Images.Media.insertImage(cont.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//        } catch (IOException e) {
//            Log.d("test", "I/O ex");
//
//            e.printStackTrace();
//        }
//
//        return true;
//    }
//
//    /**
//     * resize bitmap
//     *
//     * @param image
//     * @return
//     */
//    public Bitmap getResizedBitmap(Bitmap image) {
//        int maxSize = 1300000;
//        int width = image.getWidth();
//        int height = image.getHeight();
//
//        float bitmapRatio = (float) width / (float) height;
//        if (bitmapRatio > 0) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }
//
//    public class myGetDataCallback implements GetDataCallback {
//
//        private String senderId;
//        private String fileName;
//
//
//        public myGetDataCallback(String senderID, String fileName) {
//            Log.d("test", senderID);
//
//            this.senderId = senderID.substring(0,senderID.lastIndexOf("@"));
//            this.fileName = fileName;
//
//        }
//
//
//        @Override
//        public void done(byte[] data, ParseException e) {
//
//            if (e == null) {
//
//                // Decode the Byte[] into // Bitmap
//                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                //TODO: resize!!!
//                //bmp = getResizedBitmap(bmp);
//
//                File relativePath = getAlbumStorageDir("KISS/" + senderId);
//                Log.d("test", relativePath.getAbsolutePath());
//
//                saveImg(relativePath.getAbsolutePath(), fileName, bmp);
//
//                //save to "NEW" folder
//                relativePath = getAlbumStorageDir("KISS/" + "newPictures");
//                saveImg(relativePath.getAbsolutePath(), fileName, bmp);
//
//
//            }
//        }
//    }
}
