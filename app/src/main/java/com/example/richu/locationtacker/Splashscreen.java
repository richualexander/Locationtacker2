package com.example.richu.locationtacker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



//        Animation hyperspaceJumpAnimation1 = AnimationUtils.loadAnimation(this, R.anim.slide);
//        logotext.startAnimation(hyperspaceJumpAnimation1);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences pref = getSharedPreferences("richu", 1);

                if((pref.getBoolean("remember",false)))
                {

                    SharedPreferences preferences = getSharedPreferences("login",0);
                    if(preferences.getBoolean("loginremember",false)){

                        Intent intent = new Intent(Splashscreen.this, Homepage.class);

                        startActivity(intent);
                    }

                    else {

                        Intent intent = new Intent(Splashscreen.this, Loginpage.class);

                        startActivity(intent);
                    }
                } else {
                    Intent i = new Intent(Splashscreen.this, Signup_class.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
