package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemPrice;
    private Button addToList;
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
        setContentView(R.layout.activity_list);

        Button addToList = (Button) findViewById(R.id.add_to_list);
        Button modifyList = (Button) findViewById(R.id.modify_list);
        TextView listView = (TextView) findViewById(R.id.list_text_view);

        Item theItem = new Item();

        final Roommate currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
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
            for(int i = 0; i < shoppingListArrayList.size(); i++){
                listText = listText + "\n\n" + (i+1) + ". " + shoppingListArrayList.get(i).getName() + "\n Price: "
                        + shoppingListArrayList.get(i).getPrice() + "\n Purchased: " + shoppingListArrayList.get(i).isPurchased();
            }
            listView.setText(listText);
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent add = new Intent(List.this, ListPop.class);
                    add.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    add.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(add);
                }
            });

            modifyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDBConnection dbConnection = new FirebaseDBConnection();
                    for(int i = 0; i < shoppingListArrayList.size(); i++) {
                      //  if(shoppingListArrayList.get(i) == )
                    }
                    dbConnection.modifyShoppingListItem(List.this,currentRoommate.getRoommateGroup(), shoppingListArrayList.size(), );
                }
            });
        } catch (NullPointerException e){
            listView.setText("");
            addToList = (Button) findViewById(R.id.add_group_roommate);
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(List.this, "You must join or create a group",
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
                    selectedIntent.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
                    selectedIntent.putExtra("currentRoommate", (Roommate) getIntent().getExtras().get("currentRoommate"));
                    startActivity(selectedIntent);
                    return true;
                }
            };

}