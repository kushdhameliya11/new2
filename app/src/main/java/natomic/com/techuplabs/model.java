package natomic.com.techuplabs;

import java.io.Serializable;

public class model implements Serializable {
    private String name;
    private String ellipsizedName;
    private String createdAt;
    public model() {
        this.name = "Default Name";
        this.createdAt = "Default Date";
        this.ellipsizedName = "";
    }

    // Constructors, getters, and setters as needed
    public model(String name,String createdAt) {
        this.name = name;
        // Initialize createdAt if needed, e.g., with the current date and time
        this.createdAt = createdAt;
    }

    public String getEllipsizedName() {
        return ellipsizedName;
    }

    public void setEllipsizedName(String ellipsizedName) {
        this.ellipsizedName = ellipsizedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }





    private String getCurrentDateTime() {
        // Implement logic to get the current date and time
        // This method might be similar to the one in the InsertData activity
        return ""; // Replace with actual implementation
    }

}
