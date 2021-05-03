package cs.uga.edu.roommateshoppingapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {
    private ArrayList<Item> shoppingListItems;

    ShoppingList() {
        shoppingListItems = new ArrayList<>();
    }

    ShoppingList(ArrayList<Item> shoppingListItems){
        this.shoppingListItems = new ArrayList<>();
        for(Item item: shoppingListItems){
            this.shoppingListItems.add(item);
        }
    }

    public void setShoppingListItems(ArrayList<Item> shoppingListItems) {
        for(Item item: shoppingListItems){
            this.shoppingListItems.add(item);
        }
    }

    public ArrayList<Item> getShoppingListItems(){
        ArrayList<Item> newShoppingListItems = new ArrayList<>();
        for(Item item: this.shoppingListItems){
            newShoppingListItems.add(item);
        }
        return newShoppingListItems;
    }

    public double calculateTotalCostOfPurchases(){
        double totalCostOfPurchases = 0;
        for(Item item: this.shoppingListItems){
            if(item.isPurchased()){
                totalCostOfPurchases += item.getPrice();
            }
        }
        return totalCostOfPurchases;
    }

    public double totalCostOfRoommatePurchases(Roommate roommate){
        double totalCostOfRoommatePurchases = 0;
        for(Item item: this.shoppingListItems){
            if ( item.isPurchased()
                    && item.getPurchaser().getName().equals(roommate.getName()) ) {
                totalCostOfRoommatePurchases += item.getPrice();
            }
        }
        return totalCostOfRoommatePurchases;
    }

    public double calculateAverageCostPerRoommate(int roommateGroupSize){
        double averageCostPerRoommate = this.calculateTotalCostOfPurchases() / roommateGroupSize;
        return averageCostPerRoommate;
    }

    public void clearShoppingList(){
        for(Item item: this.shoppingListItems){
            shoppingListItems.remove(item);
        }
    }
    
    public void addShoppingListItem(Item newItem){
        shoppingListItems.add(newItem);
    }

    public int getShoppingListSize(){
        return shoppingListItems.size();
    }

}
