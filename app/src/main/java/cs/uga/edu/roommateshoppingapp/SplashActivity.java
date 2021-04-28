package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

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

        createAccount = (Button) findViewById(R.id.create_account);
        signIn = (Button) findViewById(R.id.signIn);
        dbConnection = new FirebaseDBConnection();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Roommate newRoommate = new Roommate("Twumasi", "twumasi.pennoh@gmail.com", "tkp12", "hello123");
//                dbConnection.createNewRoommate(SplashActivity.this, newRoommate);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Roommate newRoommate = new Roommate("Twumasi", "twumasi.pennoh@gmail.com", "tkp12", "hello123");
//                dbConnection.signInRoommate(SplashActivity.this, newRoommate.getEmail(), newRoommate.getPassword());
            }
        });
    }
}