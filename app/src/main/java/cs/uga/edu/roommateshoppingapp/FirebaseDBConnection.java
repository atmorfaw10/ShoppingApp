package cs.uga.edu.roommateshoppingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class FirebaseDBConnection {
    private static final String TAG = "FirebaseDBConnection";

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    FirebaseDBConnection(){
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = null;
    }

    public void modifyShoppingListItem(String groupName, int listSize, Item itemToModify){
        final DatabaseReference groupListRef = FirebaseDatabase.getInstance().getReference("shoppingLists/" + groupName + "/size");
        groupListRef.setValue(listSize);

        final DatabaseReference listRef = FirebaseDatabase.getInstance().getReference("shoppingLists/" + groupName + "/" + itemToModify.getName());
        Map<String, String> itemsMap = new HashMap<>();
        itemsMap.put("purchased", Boolean.toString(itemToModify.isPurchased()));
        itemsMap.put("price", Double.toString(itemToModify.getPrice()));
        if(itemToModify.isPurchased()){
            itemsMap.put("purchaser", ""+itemToModify.getPurchaser().getId());
        }
        else {
            itemsMap.put("purchaser", "null");
        }
        listRef.setValue(itemsMap);
        Log.d(TAG, itemToModify.getName() + " has been modified in shopping list for " + groupName);
    }

    public void addRoommate(Activity context, String groupName, int groupSize, String roommateId){
        final DatabaseReference userGroupRef = FirebaseDatabase.getInstance().getReference("users/" + roommateId + "/" + "roommateGroup");
        userGroupRef.setValue(groupName);
        Log.d(TAG, "Added group name to user data");

        final DatabaseReference roommatesRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/roommates/Roommate" + groupSize);
        roommatesRef.setValue(roommateId);
        Log.d(TAG, "Roommate has been added to the group data");

        final DatabaseReference sizeRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/size");
        sizeRef.setValue(groupSize);
        Log.d(TAG, "Group size has been increased");

        Toast.makeText(context, "Roommate has been added to the group",
                Toast.LENGTH_SHORT).show();
    }

    public void createRoommateGroup(Activity context, RoommateGroup group){
        ArrayList<Roommate> roommates = group.getRoommates();
        DatabaseReference ref = database.getReference("/roommateGroups/" + group.getGroupName());
        DatabaseReference sizeRef = ref.child("size");
        int numOfRoommates = roommates.size();
        sizeRef.setValue(numOfRoommates);

        if(roommates.size() == 1){
            DatabaseReference rmGroupRef = ref.child("roommates");
            Map<String, String> roommateGroupsMap = new HashMap<>();
            roommateGroupsMap.put("Roommate1", roommates.get(0).getId());
            rmGroupRef.setValue(roommateGroupsMap);
            Log.d(TAG, "Roommate Group " + group.getGroupName() + " added to db");

            final DatabaseReference userGroupRef = FirebaseDatabase.getInstance().getReference("users/" + roommates.get(0).getId() + "/" + "roommateGroup");
            userGroupRef.setValue(group.getGroupName());
            Log.d(TAG, "Added group name to user data");
        } //else
    }

    public void createShoppingList(String groupName){
        DatabaseReference ref = database.getReference("/shoppingLists/" + groupName + "/size");
        ref.setValue(0);
        Log.d(TAG, "Shopping List for " + groupName + " has been created.");
    }

    public void createNewRoommate(Activity context, Roommate newRoommate){
        String email = newRoommate.getEmail();
        String password = newRoommate.getPassword();
        boolean newAccountCreated = false;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            newRoommate.setId(user.getUid());
                            DatabaseReference ref = database.getReference("/users");
                            DatabaseReference usersRef = ref.child(user.getUid());
                            Map<String, String> roommateMap = new HashMap<>();
                            roommateMap.put("name", newRoommate.getName());
                            roommateMap.put("email", newRoommate.getEmail());
                            roommateMap.put("username", newRoommate.getUsername());
                            roommateMap.put("roommateGroup", "");
                            usersRef.setValue(roommateMap);
                            Log.d(TAG, "new user's data has been added to database");

                            sendEmailVerification(context);
                            updateCurrentUser(user);

                            // Home class
                            Intent home = new Intent();
                            home.setClass(context, Home.class);
                            home.putExtra("FirebaseUser", mAuth.getCurrentUser());
                            home.putExtra("currentRoommate", newRoommate);
                            context.startActivity(home);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateCurrentUser(null);
                        }
                    }
                });
    }

    public void signInRoommate(Activity context, String email, String password){
        boolean signInSuccessful = false;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String key = user.getUid();
                            updateCurrentUser(user);

                            //get firebase user's data
                            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                            userRef.orderByChild(key).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                                    if(snapshot.getKey().equals(key)){
                                        final Roommate currentRoommate = new Roommate();
                                        Log.d(TAG, "name: " + user.name);
                                        Log.d(TAG, "email: " + user.email);
                                        Log.d(TAG, "username: " + user.username);
                                        Log.d(TAG, "roommateGroup: " + user.roommateGroup);
                                        currentRoommate.setName(user.name);
                                        currentRoommate.setEmail(user.email);
                                        currentRoommate.setUsername(user.username);

                                        if(user.roommateGroup.equals("")){
                                            // Home class
                                            Intent home = new Intent();
                                            home.setClass(context, Home.class);
                                            home.putExtra("FirebaseUser", mAuth.getCurrentUser());
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("currentRoommate", currentRoommate);
                                            home.putExtra("bundle", bundle);
                                            context.startActivity(home);
                                        } else {
                                            String groupName = user.roommateGroup;
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
                                                                                FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
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
                                                                                    final DatabaseReference listSizeRef = FirebaseDatabase.getInstance().getReference("shoppingLists/" + groupName + "/size");
                                                                                    listSizeRef.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            final long shoppingListSize = (long) snapshot.getValue();
                                                                                            final DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("shoppingLists/" + groupName);
                                                                                            itemRef.addChildEventListener(new ChildEventListener() {
                                                                                                @Override
                                                                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                                                    if(!snapshot.getKey().equals("size")){
                                                                                                        Log.d(TAG, "Shopping List item " + snapshot.getKey() + " has been retrieved");
                                                                                                        FirebaseTestingActivity.ShoppingListItem item = snapshot.getValue(FirebaseTestingActivity.ShoppingListItem.class);
                                                                                                        Log.d(TAG, "price: " + item.price);
                                                                                                        Log.d(TAG, "purchased: " + item.purchased);
                                                                                                        Log.d(TAG, "purchaser: " + item.purchaser);

                                                                                                        list.addShoppingListItem(
                                                                                                                new Item(snapshot.getKey(), Double.parseDouble(item.price), Boolean.parseBoolean(item.purchased), group.getRoommate(item.purchaser)));

                                                                                                        if(list.getShoppingListSize() == shoppingListSize){
                                                                                                            group.setShoppingList(list);
                                                                                                            currentRoommate.setRoommateGroup(group);
                                                                                                            // Home class
                                                                                                            Intent home = new Intent();
                                                                                                            home.setClass(context, Home.class);
                                                                                                            home.putExtra("FirebaseUser", mAuth.getCurrentUser());
                                                                                                            home.putExtra("currentRoommate", currentRoommate);
                                                                                                            context.startActivity(home);
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
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed. Please try again",
                                    Toast.LENGTH_SHORT).show();
                            updateCurrentUser(null);
                        }
                    }
                });
    }

    public Roommate getUserData(FirebaseUser user){
        Roommate roommate = new Roommate();
        //get firebase user's data
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.orderByChild(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseTestingActivity.User user = snapshot.getValue(FirebaseTestingActivity.User.class);
                Log.d(TAG, "name: " + user.name);
                Log.d(TAG, "email: " + user.email);
                Log.d(TAG, "username: " + user.username);
                roommate.setName(user.name);
                roommate.setEmail(user.email);
                roommate.setUsername(user.username);
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

        return roommate;
    }

    private void sendEmailVerification(Activity context){
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Log.d(TAG, "sendEmailVerification:success");
                        else
                            Log.d(TAG, "sendEmailVerification:failure");
                    }
                });
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }

    public void updateCurrentUser(FirebaseUser newUser) {
        this.currentUser = newUser;
    }
}
