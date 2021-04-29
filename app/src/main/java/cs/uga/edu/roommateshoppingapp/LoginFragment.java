package cs.uga.edu.roommateshoppingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDBConnection dbConnection;

    private EditText email;
    private EditText password;
    private Button signInButton;

    private String userName;
    private String userEmail;
    private String userPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View loginFragment = inflater.inflate(R.layout.fragment_login,container,false);

        email = (EditText) loginFragment.findViewById(R.id.login_email);
        password = (EditText) loginFragment.findViewById(R.id.password);
        signInButton = (Button) loginFragment.findViewById(R.id.login);
        userEmail = "";
        userPassword = "";

        dbConnection = new FirebaseDBConnection();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                if (userEmail.equals("") || userPassword.equals("")) {
                    Toast.makeText(getActivity(), "Missing email or password",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dbConnection.signInRoommate(getActivity(), userEmail, userPassword);
                }
            }
        });

        return loginFragment;
    }

    /**
     * @return the user's password
     */
    public String getUserPassword()
    {
        return this.userPassword;
    }

    /**
     * @param userPassword
     */
   public void setUserPassword(String userPassword)
   {
       this.userPassword = userPassword;
   }

    /**
     * @return the user's username or email
     */
   public String getUserName()
   {
       return this.userName;
   }

    /**
     * @param user_name
     */
   public void setUsername(String user_name)
   {
       this.userName = user_name;
   }

}