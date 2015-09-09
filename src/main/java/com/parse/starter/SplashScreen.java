package com.parse.starter;

/**
 * Created by Michael on 9/2/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.profileimage.ImageViewActivity;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    public String userMode = "SENDER";
    public EditText mPasswordField;
    public EditText mUsernameField;
    public TextView etTextLogin;
    public String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

            Log.d("test", "Not null");
        GifView gifView = (GifView) findViewById(R.id.gifview);

        ParseInstallation parseinst = ParseInstallation.getCurrentInstallation();
        id = parseinst.getInstallationId();
        id = id.substring(id.lastIndexOf("-") + 1, id.length());

//        String stringInfo = "";
//        stringInfo += "Duration: " + gifView.getMovieDuration() + "\n";
//        stringInfo += "W x H: "
//                + gifView.getMovieWidth() + " x "
//                + gifView.getMovieHeight() + "\n";


//        final ImageView sonButton = (ImageView) findViewById(R.id.sonButton);
//        final ImageView grandmaButton = (ImageView) findViewById(R.id.grandmaButton);
        Animation kissTranslate = AnimationUtils.loadAnimation(this, R.anim.translatekiss);
        gifView.startAnimation(kissTranslate);
        final Button butGo = (Button) findViewById(R.id.btnGo);
        mUsernameField = (EditText) findViewById(R.id.etUserName);
        mPasswordField = (EditText) findViewById(R.id.etPass);
        final Button recBut = (Button) findViewById(R.id.butReceiver);
        final Button sendBut = (Button) findViewById(R.id.butSender);
        final Button regBut = (Button) findViewById(R.id.registerBut);
        etTextLogin = (TextView) findViewById(R.id.loginText);



//        if (ParseUser.getCurrentUser().getEmail() == null) {
            Animation buttonsAppear = AnimationUtils.loadAnimation(this, R.anim.buttonsappear);


            butGo.startAnimation(buttonsAppear);
            mUsernameField.startAnimation(buttonsAppear);
            mPasswordField.startAnimation(buttonsAppear);
            recBut.startAnimation(buttonsAppear);
            sendBut.startAnimation(buttonsAppear);
            regBut.startAnimation(buttonsAppear);
//        } else {
//            Log.d("test","whattttt");
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

                    mUsernameField.setText("Your personal code is:\n" + id);
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
                    if (mUsernameField.getText().length() == 0 || mPasswordField.getText().length() == 0) {
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
                                etTextLogin.setText("We have sent you a verification email. " +
                                        "Please confirm your email and Sign In!");
                                mPasswordField.setText("");
                                regBut.setVisibility(View.GONE);
                            } else {

                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                switch (e.getCode()) {
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
                    if (userMode.equalsIgnoreCase("RECEIVER")) {


                        Toolbox.setMyUserType(getApplicationContext(),userMode);
                        Toolbox.setMyID(getApplicationContext(),id);
                        ParseUser user = new ParseUser();
                        user.setEmail(id + "@kiss.com");
                        user.setUsername(id);
                        user.setPassword(id);
                        etTextLogin.setText("");
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Intent intent = new Intent(SplashScreen.this, ReceiverActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    // Sign up didn't succeed. Look at the ParseException
                                    // to figure out what went wrong
                                    switch (e.getCode()) {
                                        default:
                                            etTextLogin.setText("Something went wrong, please try again");
                                    }
                                }
                            }
                        });
                        return;
                    } else { // User is a SENDER

                        ParseUser.logInInBackground(mUsernameField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    if (!user.isAuthenticated()) {
                                        etTextLogin.setText("email is not yet verified. " +
                                                "Please check your email box");
                                        user.setEmail(mUsernameField.getText().toString());
                                    } else {
                                        ///////////////// SUCCESSSSSSSSSSSSSSSSSSSSSSSSS
                                        etTextLogin.setText("");
                                        Toolbox.setMyUserType(getApplicationContext(), userMode);
                                        Log.d("test", "reached here");
                                        Intent intent = new Intent(SplashScreen.this, ImageViewActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // Signup failed. Look at the ParseException to see what happened.
                                    switch (e.getCode()) {
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
//
//            butGo.clearAnimation();
//            butGo.setVisibility(View.GONE);
//            mUsernameField.setVisibility(View.GONE);
//            mPasswordField.setVisibility(View.GONE);
//            recBut.setVisibility(View.GONE);
//            sendBut.setVisibility(View.GONE);
//            regBut.setVisibility(View.GONE);
//            etTextLogin.setVisibility(View.GONE);
//
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
//                    Intent i = new Intent(SplashScreen.this, SenderActivity.class);
//                    startActivity(i);
//
//                    // close this activity
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//
//            Log.d("test", Toolbox.getMyID(getApplicationContext()));
//        }
////
////
//
//
//        }


    }
    }
