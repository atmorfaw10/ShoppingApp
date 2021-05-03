package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.content.Context;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private ArrayList<AddedFeatures> addFeaturesList;
    private FeatureAdapter featureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initList();
        Spinner spinnerFeatures = findViewById(R.id.spinner_add_options);
        // = new featureAdapter(this, addFeaturesList);

        ImageButton settings;

        Button createGroup = (Button) findViewById(R.id.create_group);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
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
                         startActivity(selectedIntent);
                    return true;
                }
            };
}