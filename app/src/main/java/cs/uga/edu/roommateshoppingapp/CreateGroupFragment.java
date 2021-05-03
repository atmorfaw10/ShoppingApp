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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {

    private static final String TAG = "CreateGroupFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDBConnection dbConnection;

    private EditText groupNameEditText;
    private ArrayList<EditText> itemEditTexts;
    private Button continueButton;

    private String groupName;
    private ArrayList<String> itemNames;

    public CreateGroupFragment() {
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
    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        View createGroupFragment = inflater.inflate(R.layout.fragment_create_group,container,false);

        groupNameEditText = (EditText) createGroupFragment.findViewById(R.id.groupName);

        itemEditTexts = new ArrayList<>();
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item1));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item2));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item3));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item4));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item5));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item6));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item7));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item8));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item9));
        itemEditTexts.add((EditText) createGroupFragment.findViewById(R.id.item10));
        continueButton = (Button) createGroupFragment.findViewById(R.id.continueButton);

        BottomNavigationView bottomNav = (BottomNavigationView) createGroupFragment.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        dbConnection = new FirebaseDBConnection();

        final FirebaseUser user = (FirebaseUser) getArguments().get("FirebaseUser");

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean continueToNextPage = true;
                groupName = groupNameEditText.getText().toString();
                if (groupName.equals("")) {
                    groupNameEditText.setError("Field cannot be empty");
                    continueToNextPage = false;
                }

                if (continueToNextPage) {
                    //create shopping list in database
                    dbConnection.createShoppingList(groupName);

                    //create RoommateGroup object for new group
                    //IMPLEMENT LATER - CHECK IF GROUP NAME IS ALREADY IN THE DATABASE
                    RoommateGroup group = new RoommateGroup(groupName);
                    //create ShoppingList object for new group
                    ShoppingList groupShoppingList = new ShoppingList();
                    for (int i = 0; i < itemEditTexts.size(); i++) {
                        String itemName = itemEditTexts.get(i).getText().toString();
                        if (!itemName.equals("")) {
                            Item newItem = new Item(itemName);
                            //add item to shopping list
                            groupShoppingList.addShoppingListItem(new Item(itemName));
                            //add info for shoppingList item in database
                            dbConnection.modifyShoppingListItem(groupName, groupShoppingList.getShoppingListSize(), newItem);
                        }
                    }
                    group.setShoppingList(groupShoppingList);

                    //create roommate object for roommate making group
                    Roommate roommate1 = new Roommate();
                    final String userId = user.getUid();
                    roommate1.setId(userId);
                    //get firebase user's data
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                    userRef.orderByChild(userId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                            if(snapshot.getKey().equals(userId)){
                                Log.d(TAG, "name: " + user.name);
                                Log.d(TAG, "email: " + user.email);
                                Log.d(TAG, "username: " + user.username);
                                roommate1.setName(user.name);
                                roommate1.setEmail(user.email);
                                roommate1.setUsername(user.username);
                                roommate1.setRoommateGroup(group);
                                //add roommate to RoommateGroup object
                                group.addRoommate(roommate1);
                                //add roommate to group info in database
                                dbConnection.createRoommateGroup(getActivity(), group);

                                //continue to next page
                                AddRoommateFragment addRoommateFragment = new AddRoommateFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("roommateGroup", group);
                                bundle.putAll(getArguments());
                                addRoommateFragment.setArguments(bundle);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.fragment_create_group, addRoommateFragment);
                                transaction.commit();
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
            }
        });

        return createGroupFragment;
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
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getArguments().get("FirebaseUser"));
                    startActivity(selectedIntent);
                    return true;
                }
            };

}