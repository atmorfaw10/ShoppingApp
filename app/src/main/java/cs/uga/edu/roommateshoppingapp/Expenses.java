package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class Expenses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

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
                            selectedIntent = new Intent(Expenses.this, Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(Expenses.this, Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(Expenses.this, List.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(Expenses.this, Expenses.class);
                    }
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(selectedIntent);
                    return true;
                }
            };
}