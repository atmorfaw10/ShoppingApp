package cs.uga.edu.roommateshoppingapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RoommateGroup {

    private String groupName;
    private ArrayList<Roommate> roommates;
    private ShoppingList shoppingList;

    RoommateGroup() {
        this.groupName = null;
        this.roommates = null;
        shoppingList = null;
    }

    RoommateGroup(String groupName){
        this.groupName = groupName;
        this.roommates = new ArrayList<>();
        this.shoppingList = new ShoppingList();
    }

    RoommateGroup(String groupName, ArrayList<Roommate> roommates){
        this.groupName = groupName;
        this.roommates = new ArrayList<>();
        for(int i = 0; i < roommates.size(); i++){
            this.roommates.add(roommates.get(i));
        }
        this.shoppingList = new ShoppingList();
    }

    RoommateGroup(String groupName, ArrayList<Roommate> roommates, ShoppingList shoppingList){
        this.groupName = groupName;
        this.roommates = new ArrayList<>();
        for(int i = 0; i < roommates.size(); i++){
            this.roommates.add(roommates.get(i));
        }
        this.shoppingList = shoppingList;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public void addRoommate(Roommate newRoommate){
        if(this.roommates == null){
            this.roommates = new ArrayList<>();
        }
        this.roommates.add(newRoommate);
    }

    public void removeRoommate(Roommate roommate){
        if(roommates.size() > 0){
            this.roommates.remove(roommate);
        } //else throw an exception
    }

    public ArrayList<Roommate> getRoommates(){
        ArrayList<Roommate> roommatesList = new ArrayList<>();
        for(int i = 0; i < this.roommates.size(); i++){
            roommatesList.add(this.roommates.get(i));
        }
        return roommatesList;
    }

    public void setRoommates(ArrayList<Roommate> roommates){
        for(int i = 0; i < roommates.size(); i++) {
            this.roommates.add(roommates.get(i));
        }
    }

    public Roommate getRoommate(String id){
        Roommate newRoommate = null;
        for(int i = 0; i < roommates.size(); i++){
            if(roommates.get(i).getId().equals(id)){
                return roommates.get(i);
            }
        }

        return newRoommate;
    }

    public void setShoppingList(ShoppingList newShoppingList){
        this.shoppingList = newShoppingList;
    }

    public ShoppingList getShoppingList(){
        return this.shoppingList;
    }

}
