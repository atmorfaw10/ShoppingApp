package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Groups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        final Roommate currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(new FirebaseDBConnection().getmAuth().getUid());
        TextView roommatesTextView = (TextView) findViewById(R.id.roommates_textview);
        RoommateGroup group;
        ArrayList<Roommate> roommates;
        try{
            group = currentRoommate.getRoommateGroup();
            roommates = group.getRoommates();
            String roommateText = "";
            for(int i = 0; i < roommates.size(); i++){
                roommateText = roommateText + "\n" + (i+1) + ". " + roommates.get(i).getName() + ", Username: " + roommates.get(i).getUsername();
            }
            roommatesTextView.setText(roommateText);
            Button addRoommateButton = (Button) findViewById(R.id.add_group_roommate);
            addRoommateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //continue to next page
                    AddRoommateFragment addRoommateFragment = new AddRoommateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("roommateGroup", group);
                    bundle.putSerializable("currentRoommate", currentRoommate);
                    bundle.putAll(getIntent().getExtras());
                    addRoommateFragment.setArguments(bundle);
                    FragmentManager fm = Groups.this.getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.view_group, addRoommateFragment);
                    transaction.commit();
                }
            });
        } catch (NullPointerException e){
            roommatesTextView.setText("");
            Button addRoommateButton = (Button) findViewById(R.id.add_group_roommate);
            addRoommateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Groups.this, "You must join or create a group",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent selectedIntent = null;

                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedIntent = new Intent(Groups.this, Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(Groups.this, Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(Groups.this, List.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(Groups.this, Expenses.class);
                    }
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(selectedIntent);
                    return true;
                }
            };
}