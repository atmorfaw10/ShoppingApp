package cs.uga.edu.roommateshoppingapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class FirebaseDBConnection {
    private static final String TAG = "FirebaseDBConnection";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    FirebaseDBConnection(){
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = null;
    }

    public boolean createNewRoommate(Activity context, Roommate newRoommate){
        String email = newRoommate.getEmail();
        String password = newRoommate.getPassword();
        boolean newAccountCreated = false;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateCurrentUser(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateCurrentUser(null);
                        }
                    }
                });
        if(this.currentUser != null){
            newAccountCreated = true;
        }
        return newAccountCreated;
    }

    public boolean signInRoommate(Activity context, String email, String password){
        boolean signInSuccessful = false;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification(context);
                            updateCurrentUser(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateCurrentUser(null);
                        }
                    }
                });
        if(this.currentUser != null){
            signInSuccessful = true;
        }
        return signInSuccessful;
    }

    private void sendEmailVerification(Activity context){
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Log.d(TAG, "sendEmailVerification:success");
                        else
                            Log.d(TAG, "sendEmailVerification:failure");
                    }
                });
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }

    private void updateCurrentUser(FirebaseUser newUser){
        this.currentUser = newUser;
    }
}
