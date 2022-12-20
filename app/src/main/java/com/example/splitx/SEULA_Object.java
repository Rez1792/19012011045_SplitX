package com.example.splitx;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

public class SEULA_Object {

    @Exclude String userPhoto, name,email;
    @Exclude boolean selected;

    public SEULA_Object(){

    }
    public SEULA_Object(String name, String userPhoto, String email, boolean selected){
        this.name = name;
        this.userPhoto = userPhoto;
        this.email = email;
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }








    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


}
