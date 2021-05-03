package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.content.Context;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private ArrayList<AddedFeatures> addFeaturesList;
    private FeatureAdapter featureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        FirebaseUser user = (FirebaseUser) extras.get("FirebaseUser");
        Roommate currentRoommate = (Roommate) extras.get("currentRoommate");
        currentRoommate.setId(new FirebaseDBConnection().getmAuth().getUid());

        try {
            if (currentRoommate.getRoommateGroup() != null) {
                initList();
                setContentView(R.layout.activity_home2);
                Button modifyListButton = (Button) findViewById(R.id.modify_list);
                modifyListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent selectedIntent = new Intent(Home.this, List.class);
                        selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                        selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                        startActivity(selectedIntent);
                    }
                });

                Button addRoommatesButton = (Button) findViewById(R.id.add_roommates);
                addRoommatesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent selectedIntent = new Intent(Home.this, Groups.class);
                        selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                        selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                        startActivity(selectedIntent);
                    }
                });

                Button viewExpensesButton = (Button) findViewById(R.id.view_expenses);
                viewExpensesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent selectedIntent = new Intent(Home.this, Expenses.class);
                        selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                        selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                        startActivity(selectedIntent);
                    }
                });

                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnNavigationItemSelectedListener(navListener);
            }
        } catch (NullPointerException e) {
            setContentView(R.layout.activity_home);
            initList();
            ImageButton settings;


            ImageButton createGroupButton = (ImageButton) findViewById(R.id.imageButton2);
            /**
             * on click listener method for opening the fragment that takes the user to create a group
             */
            createGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RelativeLayout) findViewById(R.id.activity_home)).removeAllViews();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    CreateGroupFragment groupFragment = new CreateGroupFragment();
                    groupFragment.setArguments(extras);
                    fragmentTransaction.replace(R.id.activity_home, groupFragment).commit();
                }
            });

            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);
        }
    }

    private void initList() {
        addFeaturesList = new ArrayList<>();
        addFeaturesList.add(new AddedFeatures("Create Group", R.drawable.ic_baseline_group_add_24));
        addFeaturesList.add(new AddedFeatures("Add Members", R.drawable.ic_baseline_person_add_24));
        addFeaturesList.add(new AddedFeatures("Search", R.drawable.search));
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent selectedIntent = null;

                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedIntent = new Intent(Home.this, Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(Home.this, Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(Home.this, List.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(Home.this, Expenses.class);
                    }
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(selectedIntent);
                    return true;
                }
            };
}