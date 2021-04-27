package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DELAY = 3000; // Delay for the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);

        /*
         * Method that starts the second splash activity after a delayed time
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent secondSplash = new Intent(MainActivity.this, SplashActivity.class); // activity object that goes from current activity to new activity
                startActivity(secondSplash); // starts new activity
                finish();
            }
        }, SPLASH_DELAY);
    }
}