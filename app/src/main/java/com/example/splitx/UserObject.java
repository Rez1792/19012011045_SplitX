package com.example.splitx;
public class UserObject {
    String name, email, upi, profileUri;

    public UserObject(){

    }
    public UserObject(String name, String email, String upi, String profileUri){
        this.name = name;
        this.email = email;
        this.upi = upi;
        this.profileUri = profileUri;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUpi() {
        return upi;
    }
    public void setUpi(String upi) {
        this.upi = upi;
    }
    public String getProfileUri() {
        return profileUri;
    }
    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }
}
