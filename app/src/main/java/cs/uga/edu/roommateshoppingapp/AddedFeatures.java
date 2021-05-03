package cs.uga.edu.roommateshoppingapp;

public class AddedFeatures {
    private String buttonName;
    private int buttonImage;

    public AddedFeatures(String theButtonName, int theButtonImage) {
        buttonName = theButtonName;
        buttonImage = theButtonImage;
    }

    public String getButtonName() {
        return this.buttonName;
    }

    public int getButtonImage() {
        return this.buttonImage;
    }
}
