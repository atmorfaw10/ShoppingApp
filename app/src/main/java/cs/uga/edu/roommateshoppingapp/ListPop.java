package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ListPop extends AppCompatActivity {
    private static final String TAG = "ListPop";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText itemName, itemPrice;
    private Button addItemToList;
    private Button backToList;
    private Button markAsPurchased;
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
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        FirebaseDBConnection dbConnection = new FirebaseDBConnection();

        user = (FirebaseUser) getIntent().getExtras().get("FirebaseUser");
        currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(user.getUid());
        RoommateGroup group = currentRoommate.getRoommateGroup();
        ArrayList<Roommate> roommates = group.getRoommates();
        ShoppingList shoppingList = group.getShoppingList();
        ArrayList<Item> shoppingListArrayList = shoppingList.getShoppingListItems();

        markAsPurchased = (Button) findViewById(R.id.mark_as_purchased);
        addItemToList = (Button) findViewById(R.id.add_item_button);
        backToList = (Button) findViewById(R.id.back_button);
        EditText item = (EditText) findViewById(R.id.item_name);
        EditText price = (EditText) findViewById(R.id.item_price);

       markAsPurchased.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean markItemAsPurchased = true;
               double item_Price = 0;
               String item_Name = item.getText().toString().trim();
               if(item_Name.equals("")){
                   item.setError("Field cannot be empty");
                   markItemAsPurchased = false;
               }

               if(price.getText().toString().equals("")){
                   price.setError("Field cannot be empty");
                   markItemAsPurchased = false;
               } else {
                   item_Price = Double.parseDouble(price.getText().toString());
                   if(item_Price <= 0.0){
                       price.setError("To mark item as purchased, price must be greater than $0.00");
                       markItemAsPurchased = false;
                   }
               }

               if(markItemAsPurchased){
                   listItem = new Item(item_Name, item_Price);

                   boolean itemMarkedAsPurchased = false;
                   for(int i = 0; i < shoppingListArrayList.size(); i++){
                       if(shoppingListArrayList.get(i).getName().equalsIgnoreCase(item_Name)){
                           itemMarkedAsPurchased = true;

                           listItem.markAsPurchased(currentRoommate);
                           shoppingListArrayList.set(i, listItem);

                           RoommateGroup currentGroup = currentRoommate.getRoommateGroup();
                           shoppingList.setShoppingListItems(shoppingListArrayList);
                           currentGroup.setShoppingList(shoppingList);
                           currentRoommate.setRoommateGroup(currentGroup);
                           dbConnection.modifyShoppingListItem(ListPop.this, group.getGroupName(), shoppingList.getShoppingListSize(), listItem);
                           item.setText("");
                           price.setText("");
                           break;
                       }
                   }
                   if(!itemMarkedAsPurchased){
                       item.setError("Item specified is not in shopping list");
                   }
               }
           }
       });

       addItemToList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean addItemToList = true;
               double item_Price = 0;
               String item_Name = item.getText().toString().trim();
               if(item_Name.equals("")){
                   item.setError("Field cannot be empty");
                   addItemToList = false;
               }

               if(!price.getText().toString().equals("")){
                   item_Price = Double.parseDouble(price.getText().toString());
                   if(item_Price < 0.0){
                       item_Price = 0.0;
                   }
               }

               if(addItemToList){
                   listItem = new Item(item_Name, item_Price);
                   if(item_Price > 0.0){
                       listItem.markAsPurchased(currentRoommate);
                   }
                   shoppingList.addShoppingListItem(listItem);
                   RoommateGroup currentGroup = currentRoommate.getRoommateGroup();
                   currentGroup.setShoppingList(shoppingList);
                   currentRoommate.setRoommateGroup(currentGroup);
                   dbConnection.modifyShoppingListItem(ListPop.this, group.getGroupName(), shoppingList.getShoppingListSize(), listItem);
                   item.setText("");
                   price.setText("");
               }
           }
       });

       backToList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent add = new Intent(ListPop.this, List.class);
               add.putExtra("FirebaseUser", user);
               add.putExtra("currentRoommate", currentRoommate);
               startActivity(add);
           }
       });
    }
}