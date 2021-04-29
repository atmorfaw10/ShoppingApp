package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    private Button createAccount;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        createAccount = (Button) findViewById(R.id.create_account); //button for account creation
        signIn = (Button) findViewById(R.id.signIn); // button for user to sign into account

        /**
         * on click listener method for opening the fragment that takes the user to set up an account
         */
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConstraintLayout)findViewById(R.id.second_splash)).removeAllViews();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.second_splash, new CreateAccountFragment()).commit();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ConstraintLayout)findViewById(R.id.second_splash)).removeAllViews();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.second_splash, loginFragment).commit();
            }
        });
    }
}