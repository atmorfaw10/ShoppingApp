package cs.uga.edu.roommateshoppingapp;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private double price;
    private boolean purchased;
    private Roommate purchaser;

    Item(){
        this.name = null;
        this.price = 0;
        this.purchased = false;
        this.purchaser = null;
    }

    Item(String name){
        this.name = name;
        this.price = 0;
        this.purchased = false;
        this.purchaser = null;
    }

    Item(String name, double price){
        this.name = name;
        this.price = price;
        this.purchased = false;
        this.purchaser = null;
    }

    Item(String name, double price, boolean purchased){
        this.name = name;
        this.price = price;
        this.purchased = purchased;
        this.purchaser = null;
    }

    Item(String name, double price, boolean purchased, Roommate purchaser){
        this.name = name;
        this.price = price;
        this.purchased = purchased;
        this.purchaser = purchaser;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }

    public void markAsPurchased(Roommate purchaser){
        this.purchased = true;
        this.purchaser = new Roommate(purchaser.getName(), purchaser.getEmail(),
                purchaser.getUsername(), purchaser.getPassword(), purchaser.getRoommateGroup());
        this.purchaser.setId(purchaser.getId());
    }

    public void removeMarkAsPurchased(){
        this.purchased = false;
        this.purchaser = null;
    }

    public boolean isPurchased(){
        return this.purchased;
    }

    public Roommate getPurchaser(){
        return this.purchaser;
    }
}
