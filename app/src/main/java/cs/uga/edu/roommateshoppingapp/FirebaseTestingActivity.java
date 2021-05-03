package cs.uga.edu.roommateshoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseTestingActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseTestingActivity";

    private Button createGroupButton;
    private Button addRoommateButton;
    private Button createShoppingListButton;
    private Button addItemButton;
    private Button markItemButton;
    private Button retrieveGroupDataButton;
    private FirebaseDBConnection dbConnection;

    public static class User {
        public String name;
        public String email;
        public String roommateGroup;
        public String username;

        public User(){
            name = null;
            email = null;
            username = null;
            roommateGroup = null;
        }

        public User(String name, String email, String roommateGroup, String username){
            this.name = name;
            this.email = email;
            this.roommateGroup = roommateGroup;
            this.username = username;
        }
    }

    public static class ShoppingListItem {
        public String name;
        public String price;
        public String purchased;
        public String purchaser;

        public ShoppingListItem(){
            name = null;
            price = null;
            purchased = null;
            purchaser = null;
        }

        public ShoppingListItem(String name, String price, String purchased, String purchaser){
            this.name = name;
            this.price = price;
            this.purchased = purchased;
            this.purchaser = purchaser;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_testing);

        Bundle extras = getIntent().getExtras();
        FirebaseUser user = (FirebaseUser) extras.get("FirebaseUser");

        dbConnection = new FirebaseDBConnection();

        createGroupButton = (Button) findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoommateGroup group1 = new RoommateGroup("group1");

                //Roommate roommate1 = dbConnection.getUserData(user);
                Roommate roommate1 = new Roommate();
                roommate1.setId(user.getUid());
                //get firebase user's data
                final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                userRef.orderByChild(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                        Log.d(TAG, "name: " + user.name);
                        Log.d(TAG, "email: " + user.email);
                        Log.d(TAG, "username: " + user.username);
                        roommate1.setName(user.name);
                        roommate1.setEmail(user.email);
                        roommate1.setUsername(user.username);
                        group1.addRoommate(roommate1);
                        dbConnection.createRoommateGroup(FirebaseTestingActivity.this, group1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });

        addRoommateButton = (Button) findViewById(R.id.add_roommate);
        addRoommateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //To add a roommate to a group
                //1. You need to know the group name
                String groupName = "group1";
                //2. You need to know the roommate's userId
                String newRoommateId = user.getUid();
                //3. You need to know how many people are already in the group
                //You will have the roommate object of the group so you can use that
                dbConnection.addRoommate(FirebaseTestingActivity.this, groupName, 1, newRoommateId);
            }
        });

        createShoppingListButton = (Button) findViewById(R.id.create_shopping_list);
        createShoppingListButton.setOnClickListener(new View.OnClickListener() {
            //shopping list
            //group name
            //item name
                //- purchased
                //- price
                //- purchaser
            @Override
            public void onClick(View v){
                String groupName = "group1";
                dbConnection.createShoppingList("group1");
            }
        });

        addItemButton = (Button) findViewById(R.id.add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get group name
                //get item name
                    //- purchased
                    //- price
                    //- purchaser
                String groupName = "group1";
                Item newItem = new Item("Milk", 0, false, null);
                dbConnection.modifyShoppingListItem(groupName, 1, newItem);
            }
        });

        markItemButton = (Button) findViewById(R.id.mark_item);
        markItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to mark an item, you need
                //group of shopping list
                //item name
                //purchased - key
                //price - key
                //purchaser - key

                String groupName = "group1";
                Roommate roommate1 = new Roommate();
                roommate1.setId(user.getUid());
                roommate1.setEmail(user.getEmail());
                Item newItem = new Item("Milk", 5.99, true, roommate1);
                dbConnection.modifyShoppingListItem(groupName, 1, newItem);
            }
        });

        retrieveGroupDataButton = (Button) findViewById(R.id.retrieve_group_data);
        retrieveGroupDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //you need the group name
                //first you need to know the number of roommates
                     //for each roommate, get their info in the users section
                //then, get the shopping list
                     //get each item

                String groupName = "group1";
                final RoommateGroup group = new RoommateGroup();
                group.setGroupName(groupName);
                final DatabaseReference sizeRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/size");
                sizeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final long size = (long) snapshot.getValue();
                        Log.d(TAG, "Size of " + groupName + " is " + size);
                        final DatabaseReference roommatesRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/roommates");
                        final ArrayList<String> roommateIds = new ArrayList<>();
                        roommatesRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                roommateIds.add((String) snapshot.getValue());
                                Log.d(TAG, "roommate Id " + snapshot.getValue() + " has been added");

                                if(roommateIds.size() == size){
                                    //get each roommates information
                                    for(int i = 0; i < roommateIds.size(); i++){
                                        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                        final String key = roommateIds.get(i);
                                        userRef.orderByChild(roommateIds.get(i)).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getKey().equalsIgnoreCase(key)){
                                                    Log.d(TAG, "roommate " + snapshot.getKey() + " has been retrieved");
                                                    User user = snapshot.getValue(User.class);
                                                    Log.d(TAG, "name: " + user.name);
                                                    Log.d(TAG, "email: " + user.email);
                                                    Log.d(TAG, "username: " + user.username);
                                                    Roommate roommate1 = new Roommate();
                                                    roommate1.setId(key);
                                                    roommate1.setName(user.name);
                                                    roommate1.setEmail(user.email);
                                                    roommate1.setUsername(user.username);
                                                    roommate1.setRoommateGroup(group);
                                                    group.addRoommate(roommate1);
                                                    if(group.getRoommates().size() == size){
                                                        ShoppingList list = new ShoppingList();
                                                        final DatabaseReference listSizeRef = FirebaseDatabase.getInstance().getReference("shoppingList/" + groupName + "/size");
                                                        listSizeRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                final long shoppingListSize = (long) snapshot.getValue();
                                                                final DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("shoppingList/" + groupName);
                                                                itemRef.addChildEventListener(new ChildEventListener() {
                                                                    @Override
                                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                        if(!snapshot.getKey().equals("size")){
                                                                            Log.d(TAG, "Shopping List item " + snapshot.getKey() + " has been retrieved");
                                                                            ShoppingListItem item = snapshot.getValue(ShoppingListItem.class);
                                                                            Log.d(TAG, "price: " + item.price);
                                                                            Log.d(TAG, "purchased: " + item.purchased);
                                                                            Log.d(TAG, "purchaser: " + item.purchaser);

                                                                            list.addShoppingListItem(
                                                                                    new Item(snapshot.getKey(), Double.parseDouble(item.price), Boolean.parseBoolean(item.purchased), group.getRoommate(item.purchaser)));

                                                                            if(list.getShoppingListSize() == shoppingListSize){
                                                                                group.setShoppingList(list);
                                                                                Log.d(TAG, "Shopping list for " + groupName + "has been added to group data");
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                                                                    @Override
                                                                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                                                                    @Override
                                                                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {}
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });
    }




}