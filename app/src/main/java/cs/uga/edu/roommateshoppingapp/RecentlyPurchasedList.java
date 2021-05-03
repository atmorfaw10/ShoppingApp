package cs.uga.edu.roommateshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RecentlyPurchasedList extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemPrice;
    private String item;
    private int price;
    private Item listItem;
    private Item[] itemsList;
    private ArrayList<Item> theItems = new ArrayList<Item>();
    private ListView itemList;
    private TextView textView;
    private ArrayAdapter arrayAdapter;
    private Button saveItem;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_purchased_list);

        TextView listView = (TextView) findViewById(R.id.list_text_view);

        final Roommate currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(new FirebaseDBConnection().getmAuth().getUid());
        RoommateGroup group;
        ArrayList<Roommate> roommates;
        ShoppingList shoppingList;
        ArrayList<Item> shoppingListArrayList;
        try{
            group = currentRoommate.getRoommateGroup();
            roommates = group.getRoommates();
            shoppingList = group.getShoppingList();
            shoppingListArrayList = shoppingList.getShoppingListItems();
            String listText = "";
            int count = 0;
            for(int i = 0; i < shoppingListArrayList.size(); i++){
                if(shoppingListArrayList.get(i).isPurchased()){
                    listText = listText + "\n\n" + (count+1) + ". " + shoppingListArrayList.get(i).getName() + "\n Price: "
                            + shoppingListArrayList.get(i).getPrice() + "\n Purchased: " + shoppingListArrayList.get(i).isPurchased();
                    count++;
                }
            }
            listView.setText(listText);
            listView.setMovementMethod(new ScrollingMovementMethod());
        } catch (NullPointerException e){
            listView.setText("");
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
                            selectedIntent = new Intent(RecentlyPurchasedList.this, Home.class);
                            break;

                        case R.id.nav_groups:
                            selectedIntent = new Intent(RecentlyPurchasedList.this, Groups.class);
                            break;

                        case R.id.nav_shopping_list:
                            selectedIntent = new Intent(RecentlyPurchasedList.this, RecentlyPurchasedList.class);
                            break;

                        case R.id.nav_expenses:
                            selectedIntent = new Intent(RecentlyPurchasedList.this, Expenses.class);
                    }
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(selectedIntent);
                    return true;
                }
            };

}