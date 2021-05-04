package cs.uga.edu.roommateshoppingapp;

import java.io.Serializable;

public class Roommate implements Serializable{

    private String name;
    private String email;
    private String username;
    private String password;
    private String id;
    private RoommateGroup roommateGroup;
    private double purchaseTotal;

    Roommate() {
        this.name = null;
        this.email = null;
        this.username = null;
        this.password = null;
        this.id = null;
        this.roommateGroup = null;
        this.purchaseTotal = 0;
    }

    Roommate(String email, String password){
        this.name = null;
        this.email = email;
        this.username = null;
        this.password = password;
        this.id = null;
        this.roommateGroup = null;
        this.purchaseTotal = 0;
    }

    Roommate(String name, String email, String username){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = null;
        this.id = null;
        this.roommateGroup = null;
        this.purchaseTotal = 0;
    }

    Roommate(String name, String email, String username, RoommateGroup roommateGroup){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = null;
        this.id = null;
        this.roommateGroup = roommateGroup;
        this.purchaseTotal = 0;
    }

    Roommate(String name, String email, String username, String password){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = null;
        this.roommateGroup = null;
        this.purchaseTotal = 0;
    }

    Roommate(String name, String email, String username, String password, RoommateGroup roommateGroup){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = null;
        this.roommateGroup = roommateGroup;
        this.purchaseTotal = 0;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setRoommateGroup(RoommateGroup roommateGroup){
        this.roommateGroup = roommateGroup;
    }

    public void addPurchase(double price){
        this.purchaseTotal += price;
    }

    public void clearPurchaseTotal(){
        this.purchaseTotal = 0;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getId() {
        return this.id;
    }

    public double getPurchaseTotal(){
        return this.purchaseTotal;
    }

    public RoommateGroup getRoommateGroup(){
        return this.roommateGroup;
    }
}
