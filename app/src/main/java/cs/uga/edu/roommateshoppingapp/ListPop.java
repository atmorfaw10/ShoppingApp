package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListPop extends AppCompatActivity {
    private static final String TAG = "ListPop";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemPrice;
    private Button addToList;
    private Button backToList;
    private String item;
    private String price;
    private Item listItem;
    private Item[] itemsList;
    private ArrayList<Item> theItems = new ArrayList<Item>();
    private ListView itemList;
    private TextView textView;
    private Button saveItem;
    private ImageButton addButton;
    private ArrayAdapter arrayAdapter;
    private Roommate currentRoommate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        FirebaseDBConnection dbConnection = new FirebaseDBConnection();

        final FirebaseUser user = (FirebaseUser) getIntent().getExtras().get("FirebaseUser");
        currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(user.getUid());
        RoommateGroup group = currentRoommate.getRoommateGroup();
        ArrayList<Roommate> roommates = group.getRoommates();
        ShoppingList shoppingList = group.getShoppingList();
        ArrayList<Item> shoppingListArrayList = shoppingList.getShoppingListItems();

        saveItem = (Button) findViewById(R.id.save_list_button);
        backToList = (Button) findViewById(R.id.back_button);
        EditText item = (EditText) findViewById(R.id.item_name);
        EditText price = (EditText) findViewById(R.id.item_price);

       saveItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean addItemToShoppingList = true;
               double item_Price = 0;
               String item_Name = item.getText().toString().trim();
               if(item_Name.equals("")){
                   item.setError("Field cannot be empty");
                   addItemToShoppingList = false;
               }
               if(price.getText().toString().equals("")){
                   item_Price = 0;
               } else {
                   item_Price = Double.parseDouble(price.getText().toString());
               }

               if(addItemToShoppingList){
                   listItem = new Item(item_Name, item_Price);
                   if(item_Price > 0.0){
                       listItem.markAsPurchased(currentRoommate);
                   }
                   shoppingList.addShoppingListItem(listItem);

                   RoommateGroup currentGroup = currentRoommate.getRoommateGroup();
                   currentGroup.setShoppingList(shoppingList);
                   currentRoommate.setRoommateGroup(currentGroup);
                   dbConnection.modifyShoppingListItem(ListPop.this, group.getGroupName(), shoppingList.getShoppingListSize(), listItem);
               }
           }
       });

       backToList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent add = new Intent(ListPop.this, List.class);
               add.putExtra("FirebaseUser", (FirebaseUser) getIntent().getExtras().get("FirebaseUser"));
               add.putExtra("currentRoommate", currentRoommate);
               startActivity(add);
           }
       });
    }
}