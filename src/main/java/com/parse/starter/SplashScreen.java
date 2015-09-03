package com.parse.starter;

/**
 * Created by Michael on 9/2/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.parse.starter.Toolbox;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imView = (ImageView) findViewById(R.id.imgLogo);
        final Button sonButton = (Button) findViewById(R.id.sonButton);
        final Button grandmaButton = (Button) findViewById(R.id.grandmaButton);
        Animation kissTranslate = AnimationUtils.loadAnimation(this,R.anim.translatekiss);
        Animation buttonTranslate = AnimationUtils.loadAnimation(this,R.anim.buttonsappear);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        final Button oldUserButton = (Button) findViewById(R.id.oldGrandma);
        final Button newUserButton = (Button) findViewById(R.id.newGrandma);


        imView.startAnimation(kissTranslate);
//
//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("kiss", 0);
//        String restoredText = prefs.getString("MyUserID", null);
        if (Toolbox.getMyID(getApplicationContext()) == null) {
            Log.d("test", "No shared prefs");
            sonButton.startAnimation(buttonTranslate);
            grandmaButton.startAnimation(buttonTranslate);

            sonButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toolbox.setMyUserType(getApplicationContext(),getString(R.string.sender));
                    loginButton.setVisibility(View.VISIBLE);
                    registerButton.setVisibility(View.VISIBLE);
                    sonButton.setVisibility(View.INVISIBLE);
                    grandmaButton.setVisibility(View.INVISIBLE);

                }
            });
            grandmaButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toolbox.setMyUserType(getApplicationContext(),getString(R.string.receiver));
                    oldUserButton.setVisibility(View.VISIBLE);
                    newUserButton.setVisibility(View.VISIBLE);
                    sonButton.setVisibility(View.INVISIBLE);
                    grandmaButton.setVisibility(View.INVISIBLE);
                }
            });

            registerButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.putExtra(getString(R.string.loginOrRegister), getString(R.string.register));
                    startActivity(i);
                }
            });

            loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.putExtra(getString(R.string.loginOrRegister), getString(R.string.login));
                    startActivity(i);
                }
            });





        } else {
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

            Log.d("test", Toolbox.getMyID(getApplicationContext()));
        }





    }

}