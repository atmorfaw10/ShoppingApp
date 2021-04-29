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
    private FirebaseDBConnection dbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        dbConnection = new FirebaseDBConnection();

        createAccount = (Button) findViewById(R.id.create_account); //button for account creation
        signIn = (Button) findViewById(R.id.signIn); // button for user to sign into account

        /**
         * on click listener method for opening the fragment that takes the user to set up an account
         */
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Roommate newRoommate = new Roommate("Twumasi", "twumasi.pennoh@gmail.com", "tkp12", "hello123");
//                dbConnection.createNewRoommate(SplashActivity.this, newRoommate);
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
                fragmentTransaction.replace(R.id.second_splash, new LoginFragment()).commit();
//                Roommate newRoommate = new Roommate("Twumasi", "twumasi.pennoh@gmail.com", "tkp12", "hello123");
//                dbConnection.signInRoommate(SplashActivity.this, newRoommate.getEmail(), newRoommate.getPassword());
            }
        });
    }
}