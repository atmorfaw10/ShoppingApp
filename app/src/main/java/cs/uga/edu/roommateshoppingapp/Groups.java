package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Groups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

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
                    startActivity(selectedIntent);
                    return true;
                }
            };
}