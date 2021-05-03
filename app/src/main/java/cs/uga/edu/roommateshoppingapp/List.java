package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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
                            selectedIntent = new Intent(List.this, Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(List.this, Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(List.this, List.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(List.this, Expenses.class);
                    }
                        startActivity(selectedIntent);
                    return true;
                }
            };
}