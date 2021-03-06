package cs.uga.edu.roommateshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRoommateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRoommateFragment extends Fragment {

    private static final String TAG = "AddRoommateFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDBConnection dbConnection;

    private Button addRoommateButton;

    private String username;
    private boolean foundUser;
    private Roommate currentRoommate;
    private FirebaseUser user;

    public AddRoommateFragment() {
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
    public static AddRoommateFragment newInstance(String param1, String param2) {
        AddRoommateFragment fragment = new AddRoommateFragment();
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
        View addRoommateFragment = inflater.inflate(R.layout.fragment_add_roommate,container,false);

        final EditText usernameEditText = (EditText) addRoommateFragment.findViewById(R.id.price_purchased);
        addRoommateButton = (Button) addRoommateFragment.findViewById(R.id.modify_item_button);

        BottomNavigationView bottomNav = (BottomNavigationView) addRoommateFragment.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        dbConnection = new FirebaseDBConnection();

        user = (FirebaseUser) getArguments().get("FirebaseUser");
        final RoommateGroup group = (RoommateGroup) getArguments().getSerializable("roommateGroup");
        currentRoommate = (Roommate) getArguments().getSerializable("currentRoommate");
        currentRoommate.setId(user.getUid());
        final Roommate newRoommate = new Roommate();

        addRoommateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFoundUser(false);
                username = usernameEditText.getText().toString();
                if (username.equals("")) {
                    usernameEditText.setError("Field cannot be empty");
                } else {
                    //check in database if username is available
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                    userRef.orderByChild(user.getUid()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(!snapshot.getKey().equals("size")){
                                FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                                if (user.username.equals(username)) {
                                    if (group.getRoommate(snapshot.getKey()) == null) {
                                        usernameEditText.setError(null);
                                        setFoundUser(true);
                                        Log.d(TAG, "name: " + user.name);
                                        Log.d(TAG, "email: " + user.email);
                                        Log.d(TAG, "username: " + user.username);
                                        newRoommate.setId(snapshot.getKey());
                                        newRoommate.setName(user.name);
                                        newRoommate.setEmail(user.email);
                                        newRoommate.setUsername(user.username);
                                        group.addRoommate(newRoommate);
                                        newRoommate.setRoommateGroup(group);

                                        dbConnection.addRoommate(getActivity(), group.getGroupName(), group.getRoommates().size(), newRoommate.getId());
                                    } else {
                                        setFoundUser(true);
                                        usernameEditText.setError("User is already part of group");
                                    }

                                } else if (!isFoundUser()) {
                                    usernameEditText.setError("User was not found");
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
                }//if
            }
        });

        return addRoommateFragment;
    }

    public void setFoundUser(boolean foundUser){
        this.foundUser = foundUser;
    }

    public boolean isFoundUser(){
        return this.foundUser;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent selectedIntent = null;
                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedIntent = new Intent(getActivity(), Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(getActivity(), Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(getActivity(), List.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(getActivity(), Expenses.class);
                    }
                    selectedIntent.putExtra("FirebaseUser", user);
                    selectedIntent.putExtra("currentRoommate", currentRoommate);
                    startActivity(selectedIntent);
                    return true;
                }
            };
}