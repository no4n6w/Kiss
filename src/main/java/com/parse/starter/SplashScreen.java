package com.parse.starter;

/**
 * Created by Michael on 9/2/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.w3c.dom.Text;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4800;
    private PowerManager.WakeLock mWakeLock;
    public String userMode = "SENDER";
    public EditText mPasswordField;
    public EditText mUsernameField;
    public TextView etTextLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
       GifView gifView = (GifView)findViewById(R.id.gifview);
//
//        String stringInfo = "";
//        stringInfo += "Duration: " + gifView.getMovieDuration() + "\n";
//        stringInfo += "W x H: "
//                + gifView.getMovieWidth() + " x "
//                + gifView.getMovieHeight() + "\n";


//        final ImageView sonButton = (ImageView) findViewById(R.id.sonButton);
//        final ImageView grandmaButton = (ImageView) findViewById(R.id.grandmaButton);
       Animation kissTranslate = AnimationUtils.loadAnimation(this,R.anim.translatekiss);
        gifView.startAnimation(kissTranslate);
        final Button butGo = (Button) findViewById(R.id.btnGo);
        mUsernameField = (EditText) findViewById(R.id.etUserName);
        mPasswordField = (EditText) findViewById(R.id.etPass);
        final Button recBut = (Button) findViewById(R.id.butReceiver);
        final Button sendBut = (Button) findViewById(R.id.butSender);
        final Button regBut = (Button) findViewById(R.id.registerBut);
        etTextLogin = (TextView) findViewById(R.id.loginText);


        if (Toolbox.getMyID(getApplicationContext()) == null) {
            Animation buttonsAppear = AnimationUtils.loadAnimation(this, R.anim.buttonsappear);


            butGo.startAnimation(buttonsAppear);
            mUsernameField.startAnimation(buttonsAppear);
            mPasswordField.startAnimation(buttonsAppear);
            recBut.startAnimation(buttonsAppear);
            sendBut.startAnimation(buttonsAppear);
            regBut.startAnimation(buttonsAppear);
        }

        recBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordField.clearAnimation();
                regBut.clearAnimation();
                recBut.setBackgroundColor(0xfffbfffb);
                sendBut.setBackgroundColor(0xffb7bbbe);
                butGo.setText("Got it!");
                regBut.setVisibility(View.GONE);
                mPasswordField.setVisibility(View.GONE);

                mUsernameField.setText("Your personal code is:\n9473djfkGSR");
                mUsernameField.setEnabled(false);
                userMode = "RECEIVER";
            }
        });

        sendBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBut.setBackgroundColor(0xfffbfffb);
                recBut.setBackgroundColor(0xffb7bbbe);
                butGo.setText("Sign In");
                mPasswordField.setVisibility(View.VISIBLE);
                regBut.setVisibility(View.VISIBLE);
                mPasswordField.setHint("Password");
                mUsernameField.setEnabled(true);
                mUsernameField.setText("");
                userMode = "SENDER";

            }
        });

        regBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUsernameField.getText().length() == 0 || mPasswordField.getText().length() == 0) {
                    etTextLogin.setText("No details where entered");
                    return;
                }

                ParseUser user = new ParseUser();
                user.setEmail(mUsernameField.getText().toString());
                user.setUsername(mUsernameField.getText().toString());
                user.setPassword(mPasswordField.getText().toString());
                etTextLogin.setText("");
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            etTextLogin.setText("We have sent you a verificatio email\n" +
                                    "Please confirm your email and Sign In");
                            mPasswordField.setText("");
                        } else {

                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            switch(e.getCode()){
                                case ParseException.EMAIL_TAKEN:
                                    etTextLogin.setText("Sorry, this email is already registered");
                                    break;
                                case ParseException.INVALID_EMAIL_ADDRESS:
                                    etTextLogin.setText("Sorry, this is an invalid email");
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    etTextLogin.setText("Sorry, you must supply a password to register.");
                                    break;
                                default:
                                    etTextLogin.setText("Something went wrong, please try again");

                            }
                        }
                    }
                });
            }

//            user.setUsername(mUsernameField.getText().toString());
//            user.setPassword(mPasswordField.getText().toString());
        });

        butGo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(mUsernameField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            if (!user.isAuthenticated()){
                                etTextLogin.setText("email is not verified\n" +
                                        "Please enter your email and verify it");
                                user.setEmail(mUsernameField.getText().toString());
                            } else {
                                etTextLogin.setText("great");
//                                Intent intent = new Intent(SplashScreen.this, SetProfileActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            switch(e.getCode()){
                                case ParseException.USERNAME_MISSING:
                                    etTextLogin.setText("Sorry, you must supply an email to Sign In");
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    etTextLogin.setText("Sorry, you must supply a password to register.");
                                    break;
                                case ParseException.OBJECT_NOT_FOUND:
                                    etTextLogin.setText("Sorry, those credentials were invalid.");
                                    break;
                                default:
                                    etTextLogin.setText(e.toString());
                                    break;
                            }
                        }
                    }
                });
            }
        });




//
//        if (Toolbox.getMyID(getApplicationContext()) != null) {
//            sonButton.setVisibility(View.INVISIBLE);
//            grandmaButton.setVisibility(View.INVISIBLE);
//        }
////        imView.startAnimation(kissTranslate);
////
////        SharedPreferences prefs = getApplicationContext().getSharedPreferences("kiss", 0);
////        String restoredText = prefs.getString("MyUserID", null);
//        if (Toolbox.getMyID(getApplicationContext()) == null) {
//            Log.d("test", "No shared prefs");
//            sonButton.startAnimation(buttonTranslate);
//            grandmaButton.startAnimation(buttonTranslate);
//
//            sonButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toolbox.setMyUserType(getApplicationContext(),getString(R.string.sender));
//                    loginButton.setVisibility(View.VISIBLE);
//                    registerButton.setVisibility(View.VISIBLE);
//                    sonButton.setVisibility(View.INVISIBLE);
//                    grandmaButton.setVisibility(View.INVISIBLE);
//
//                }
//            });
//            grandmaButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toolbox.setMyUserType(getApplicationContext(),getString(R.string.receiver));
//                    oldUserButton.setVisibility(View.VISIBLE);
//                    newUserButton.setVisibility(View.VISIBLE);
//                    sonButton.setVisibility(View.INVISIBLE);
//                    grandmaButton.setVisibility(View.INVISIBLE);
//                }
//            });
//
//            registerButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
//                    i.putExtra(getString(R.string.loginOrRegister), getString(R.string.register));
//                    startActivity(i);
//                }
//            });
//
//            loginButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
//                    i.putExtra(getString(R.string.loginOrRegister), getString(R.string.login));
//                    startActivity(i);
//                }
//            });
//
//
//
//
//
//        } else {
//            new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//                @Override
//                public void run() {
//                    // This method will be executed once the timer is over
//                    // Start your app main activity
//
//
//                    Intent i = new Intent(SplashScreen.this, ReceiverActivity.class);
//                    startActivity(i);
//
//                    // close this activity
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//
//            Log.d("test", Toolbox.getMyID(getApplicationContext()));
//        }
//
//



    }

    @Override
    protected void onStop() {
        mWakeLock.release();
        super.onStop();
    }

}