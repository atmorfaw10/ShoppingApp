package cs.uga.edu.roommateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class ModifyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_list);

        final Roommate currentRoommate = (Roommate) getIntent().getExtras().getSerializable("currentRoommate");
        currentRoommate.setId(new FirebaseDBConnection().getmAuth().getUid());
        RoommateGroup group;
        ArrayList<Roommate> roommates;
        ShoppingList shoppingList;
        ArrayList<Item> shoppingListArrayList;


        FirebaseDBConnection dbConnection = new FirebaseDBConnection();
     //   for(int i = 0; i < shoppingListArrayList.size(); i++) {
           // if(shoppingListArrayList.get(i) == ()

       // dbConnection.modifyShoppingListItem(List.this,currentRoommate.getRoommateGroup(), shoppingListArrayList.size(), );
    }
}