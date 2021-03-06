package cs.uga.edu.roommateshoppingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {
    private static final String TAG = "CreateAccountFragment";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +  // at least 1 digit
            "(?=.*[a-z])" +  // at least 1 lower case letter
            "(?=.*[A-Z])" +  // at least 1 upper case letter
            "(?=.*[@#$%^&+=!])" + // at least 1 special character
            "(?=\\S+$)" + // no white spaces
            ".{6,}" + // at least 6 characters
            "$");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDBConnection dbConnection;

    private EditText textInputName; //name edit text view
    private EditText textInputEmail; // email edit text view
    private EditText textUserName; // user name edit text view
    private EditText textPassword; // password edit text view
    private EditText reEnteredPassword; // re-entered password edit text view
    private Button backButton;

    String nameInput; // string of inputted name
    String usernameInput; // string of inputted username
    String emailInput; // string of inputted email
    String passwordInput; // string of inputted password
    String secondPasswordInput; // string of second inputted password

    private int counter = 0;

    private long numUsers = 0;

    private Button registration;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View createAccountFragment = inflater.inflate(R.layout.fragment_create_account, container,false);

        textUserName = (EditText) createAccountFragment.findViewById(R.id.user_name_text);
        textInputName = (EditText) createAccountFragment.findViewById(R.id.name);
        textInputEmail = (EditText) createAccountFragment.findViewById(R.id.email);
        textPassword = (EditText) createAccountFragment.findViewById(R.id.pass_word);
        reEnteredPassword = (EditText) createAccountFragment.findViewById(R.id.confirm_password);

        registration = (Button) createAccountFragment.findViewById(R.id.register);
        backButton = (Button) createAccountFragment.findViewById(R.id.back_to_main2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Splash class
                Intent splash = new Intent();
                splash.setClass(getActivity(), SplashActivity.class);
                getActivity().startActivity(splash);
                Log.d(TAG, "Returning to splash activity");
            }
        });

        dbConnection = new FirebaseDBConnection();

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateName() && validateUserName() && validateEmail() && validatePassword() && confirmPassword())
                {
                    //check in database if username is available
                    DatabaseReference sizeRef = FirebaseDatabase.getInstance().getReference("/users/size");
                    sizeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long size = (long) snapshot.getValue();
                            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                            userRef.orderByKey().addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (!snapshot.getKey().equals("size")) {
                                        increaseNumUsers();
                                        FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                                        if (user.username.equals(usernameInput)) {
                                            textUserName.setError("Username has been taken. Try a different username");
                                        } else if (getNumUsers() == size) {
                                            Roommate newRoommate = new Roommate(textInputEmail.getText().toString(), textPassword.getText().toString());
                                            newRoommate.setName(textInputName.getText().toString());
                                            newRoommate.setUsername(textUserName.getText().toString());

                                            dbConnection.createNewRoommate(getActivity(), newRoommate);
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    // do nothing
                }
            }
        });


        return createAccountFragment;
    }


    public long getNumUsers(){
        return numUsers;
    }

    public void increaseNumUsers(){
        this.numUsers += 1;
    }

    public void onBackPressed() {
        counter++;
        if(counter > 0) {
            getActivity().onBackPressed();
        }
    }

    /**
     * Validate the user's name
     * @return true or false if the name is valid
     */
    public boolean validateName() {
       nameInput = textInputName.getEditableText().toString().trim();

        if(nameInput.isEmpty())
        {
            textInputName.setError("Field cannot be empty");
            return false;
        } else
        {
            textInputName.setError(null);
            return true;
        }
    }

    /**
     * Validate the user's username
     * @return true or false if the username is valid
     */
    public boolean validateUserName() {
        usernameInput = textUserName.getEditableText().toString().trim();

        if(usernameInput.isEmpty())
        {
            textUserName.setError("Field cannot be empty");
            return false;
        } else
        {
            textUserName.setError(null);
            return true;
        }
    }

    /**
     * Validate the user's email
     * @return true or false if the email is valid
     */
    public boolean validateEmail() {
        emailInput = textInputEmail.getEditableText().toString().trim();

        if(emailInput.isEmpty())
        {
            textInputEmail.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            textInputEmail.setError("Please enter a valid email address ");
            return false;
        } else
        {
            textInputEmail.setError(null);
            return true;
        }
    }

    /**
     * Validate the user's password
     * @return true of false if password is valid
     */
    public boolean validatePassword() {
        passwordInput = textPassword.getEditableText().toString().trim();

        if(passwordInput.isEmpty())
        {
            textPassword.setError("Field cannot be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches())
        {
            textPassword.setError("Password too weak. It must be at least 6 characters long, contain 1" +
                    " digit, 1 lower case letter, 1 uppercase letter, and 1 special character");
            return false;
        } else
        {
            textPassword.setError(null);
            return true;
        }
    }

    /**
     * Check if the passwords match
     * @return true if the passwords match and false otherwise
     */
    public boolean confirmPassword() {
        secondPasswordInput = reEnteredPassword.getEditableText().toString().trim();

        if(secondPasswordInput.isEmpty())
        {
            reEnteredPassword.setError("Field cannot be empty");
            return false;
        } else if(!secondPasswordInput.equals(passwordInput))
        {
            reEnteredPassword.setError("Passwords do not match");
            return false;
        } else {
            reEnteredPassword.setError(null);
            return true;
        }
    }
}