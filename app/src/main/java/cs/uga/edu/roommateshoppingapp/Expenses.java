package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Expenses extends AppCompatActivity {

    private static final String TAG = "Expenses";

    private TextView totalCostView;
    private TextView averageCostView;
    private TextView totalCostByRoommateView;
    private Roommate currentRoommate;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);


        totalCostView = (TextView) findViewById(R.id.total_cost_view);
        averageCostView = (TextView) findViewById(R.id.average_cost_view);
        totalCostByRoommateView = (TextView) findViewById(R.id.total_cost_by_rmmate_view);
        Button settleCost = (Button) findViewById(R.id.settle_cost);

        user = (FirebaseUser) getIntent().getExtras().get("FirebaseUser");
        currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(user.getUid());
        RoommateGroup group;
        ArrayList<Roommate> roommates;
        ShoppingList shoppingList;
        ArrayList<Item> shoppingListArrayList;
        try{
            group = currentRoommate.getRoommateGroup();
            shoppingList = group.getShoppingList();
            shoppingListArrayList = shoppingList.getShoppingListItems();
            String totalCost = String.format("%,.2f", shoppingList.calculateTotalCostOfPurchases());
            totalCostView.setText("$" + totalCost);
            String averageCost = String.format("%,.2f", shoppingList.calculateAverageCostPerRoommate(group.getRoommates().size()));
            averageCostView.setText("$" + averageCost);
            Log.d(TAG, "Calculated average cost and total cost");

            String totalCostPerRoommate = "";
            for(int i = 0; i < shoppingListArrayList.size(); i++) {
                Item currentItem = shoppingListArrayList.get(i);
                if (currentItem.isPurchased()) {
                    group.getRoommate(currentItem.getPurchaser().getId()).addPurchase(currentItem.getPrice());
                }
            }
            Log.d(TAG, "Added purchase costs to roommate objects");
            
            roommates = group.getRoommates();
            for(int i = 0; i < roommates.size(); i++){
                Roommate roommate = roommates.get(i);
                totalCostPerRoommate += "\n\n" + roommate.getName() + " - $" + String.format("%,.2f", roommate.getPurchaseTotal());
                Log.d(TAG, totalCostPerRoommate);
            }
            totalCostByRoommateView.setText(totalCostPerRoommate);
            Log.d(TAG, "Added purchase costs for each roommate");

            settleCost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // Settle the cost [clear the recently purchased list
                    for(int i = 0; i < shoppingListArrayList.size(); i++){
                        if(shoppingListArrayList.get(i).isPurchased()){
                            new FirebaseDBConnection().removeShoppingListItem(Expenses.this,
                                    group.getGroupName(), shoppingListArrayList.size()-1, shoppingListArrayList.get(i));
                            group.getRoommate(shoppingListArrayList.get(i).getPurchaser().getId()).clearPurchaseTotal();
                            shoppingListArrayList.remove(i);
                        }
                    }
                    shoppingList.setShoppingListItems(shoppingListArrayList);
                    group.setShoppingList(shoppingList);
                    currentRoommate.setRoommateGroup(group);
                    Log.d(TAG, "Recently purchased list has been cleared");
                }
            });

        } catch(NullPointerException e){
            settleCost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // Settle the cost [clear the recently purchased list
                    Toast.makeText(Expenses.this, "You must join or create a group",
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
                    selectedIntent.putExtra("FirebaseUser", user);
                    selectedIntent.putExtra("currentRoommate", currentRoommate);
                    startActivity(selectedIntent);
                    return true;
                }
            };
}