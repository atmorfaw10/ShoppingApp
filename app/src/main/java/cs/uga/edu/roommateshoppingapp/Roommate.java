package cs.uga.edu.roommateshoppingapp;

public class Roommate {

    private String name;
    private String email;
    private String username;
    private String password;
    private RoommateGroup roommateGroup;

    Roommate() {
        this.name = null;
        this.email = null;
        this.username = null;
        this.password = null;
        this.roommateGroup = null;
    }

    Roommate(String name, String email, String username){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = null;
        this.roommateGroup = null;
    }

    Roommate(String name, String email, String username, RoommateGroup roommateGroup){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = null;
        this.roommateGroup = roommateGroup;
    }

    Roommate(String name, String email, String username, String password){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roommateGroup = null;
    }

    Roommate(String name, String email, String username, String password, RoommateGroup roommateGroup){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roommateGroup = roommateGroup;
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

    public void setRoommateGroup(RoommateGroup roommateGroup){
        this.roommateGroup = roommateGroup;
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

    public RoommateGroup getRoommateGroup(){
        return this.roommateGroup;
    }
}
