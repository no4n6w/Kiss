package com.parse.starter.profileimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.SenderActivity;
import com.parse.starter.Toolbox;

import java.io.ByteArrayOutputStream;


/**
 * @author GT
 */
public class ImageViewActivity extends Activity implements PicModeSelectDialogFragment.IPicModeSelectListener{

    public static final String TAG = "ImageViewActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    private String imgUri;

    private Button mBtnUpdatePic;
    private ImageView mImageView;
    private CardView mCardView;
    private ImageView profileImage;
    private Button finished;
    private EditText nickname;
    private Bitmap myBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "here in new view");
        super.onCreate(savedInstanceState);
        Log.d("test", "here in new view1");
        setContentView(R.layout.activity_image_view);
        Log.d("test", "here in new view2");
        profileImage = (ImageView) findViewById(R.id.newImageView);
//        mCardView = (CardView) findViewById(R.id.cv_image_container);
        finished = (Button) findViewById(R.id.goToSender);
        initCardView(); //Resize card view according to activity dimension
        Log.d("test", "here in new view3");
        nickname = (EditText) findViewById(R.id.etDispName);

        if (ParseUser.getCurrentUser().get("nick") != null)
            nickname.setText(ParseUser.getCurrentUser().get("nick").toString());

        if (ParseUser.getCurrentUser().get("ProfileImageFile") != null)
            profileImage.setImageBitmap(Toolbox.getProfilePic(ParseUser.getCurrentUser().getEmail().toString(), getApplicationContext()));

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfilePicDialog();
            }
        });

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();

                currentUser.put("nick",nickname.getText().toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (myBitmap == null){
                   Toast.makeText(ImageViewActivity.this, "Please upload an image!",Toast.LENGTH_LONG).show();
                    return;
                }
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("profileimage.jpeg", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();
                currentUser.put("ProfileImageFile", file);
                currentUser.saveInBackground();
                Toolbox.setMyID(getApplicationContext(), nickname.getText().toString());
                Intent intent = new Intent(ImageViewActivity.this, SenderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        Log.d("test", "onActivityResult");
        if(requestCode == REQUEST_CODE_UPDATE_PIC){
            if(resultCode == RESULT_OK){
                String imagePath = result.getStringExtra(Toolbox.IntentExtras.IMAGE_PATH);
                showCroppedImage(imagePath);
            }else if(resultCode == RESULT_CANCELED){
                //TODO : Handle case
            }else{
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(this,errorMsg,Toast.LENGTH_LONG).show();
            }
        }

    }

    private void showCroppedImage(String mImagePath) {
        if(mImagePath != null){
            myBitmap = BitmapFactory.decodeFile(mImagePath);
            ImageView imv = (ImageView) findViewById(R.id.newImageView);
            imv.setImageBitmap(myBitmap);
            imv.setBackgroundResource(R.color.trans);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--------Private methods --------

    private void initCardView(){
//        mCardView.setPreventCornerOverlap(false);
//        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
//        //We are implementing this only for portrait mode so width will be always less
//        int w = displayMetrics.widthPixels;
//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mCardView.getLayoutParams();
//        int leftMargin = lp.leftMargin;
//        int topMargin = lp.topMargin;
//        int rightMargin = lp.rightMargin;
//        int paddingLeft = mCardView.getPaddingLeft();
//        int paddingRight = mCardView.getPaddingLeft();
//        int ch = w - leftMargin - rightMargin + paddingLeft + paddingRight;
//        mCardView.getLayoutParams().height = ch;
    }


    private void showAddProfilePicDialog(){
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    private void actionProfilePic(String action){
        Intent intent = new Intent(this,ImageCropActivity.class);
        intent.putExtra("ACTION",action);
        Log.d("test", action + " is where we are now");
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
        Log.d("test","got here");
    }


    @Override
    public void onPicModeSelected(String mode) {
        Log.d("test","here - recieved" + mode);
        String action = mode.equalsIgnoreCase(Toolbox.PicModes.CAMERA) ? Toolbox.IntentExtras.ACTION_CAMERA : Toolbox.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }


}
