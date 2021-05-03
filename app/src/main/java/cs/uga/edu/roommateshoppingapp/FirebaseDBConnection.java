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
        final DatabaseReference groupListRef = FirebaseDatabase.getInstance().getReference("shoppingList/" + groupName + "/size");
        groupListRef.setValue(listSize);

        final DatabaseReference listRef = FirebaseDatabase.getInstance().getReference("shoppingList/" + groupName + "/" + itemToModify.getName());
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

    public void addRoommate(String groupName, int groupSize, String roommateId){
        final DatabaseReference userGroupRef = FirebaseDatabase.getInstance().getReference("users/" + roommateId + "/" + "roommateGroup");
        userGroupRef.setValue(groupName);
        Log.d(TAG, "Added group name to user data");

        final DatabaseReference roommatesRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/roommates/Roommate" + groupSize);
        roommatesRef.setValue(roommateId);
        Log.d(TAG, "Roommate has been added to the group data");

        final DatabaseReference sizeRef = FirebaseDatabase.getInstance().getReference("roommateGroups/" + groupName + "/size");
        sizeRef.setValue(groupSize);
        Log.d(TAG, "Group size has been increased");
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
        DatabaseReference ref = database.getReference("/shoppingList/" + groupName + "/size");
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
                            roommateMap.put("roommateGroup", "null");
                            usersRef.setValue(roommateMap);
                            Log.d(TAG, "new user's data has been added to database");

                            sendEmailVerification(context);
                            updateCurrentUser(user);

                            Intent home = new Intent();
                            home.setClass(context, Home.class);
                            home.putExtra("FirebaseUser", user);

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
                            updateCurrentUser(user);

                            // Home class
                            Intent home = new Intent();
                            home.setClass(context, Home.class);
                            home.putExtra("FirebaseUser", user);

//                            // FirebaseTestingActivity class
//                            Intent firebaseTesting = new Intent();
//                            firebaseTesting.setClass(context, FirebaseTestingActivity.class);
//                            firebaseTesting.putExtra("FirebaseUser", user);
//                            context.startActivity(firebaseTesting);

                            context.startActivity(home);
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
