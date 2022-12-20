package com.example.splitx;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SEULA_Object_For_Fire {

    Map<String,String> otherDetailsMap = new HashMap<>();
    Map<String, List<String>> userData = new HashMap<>();

    public SEULA_Object_For_Fire(){

    }
    public SEULA_Object_For_Fire(Map<String,String> otherDetailsMap ,Map<String, List<String>> userData ){
        this.otherDetailsMap = otherDetailsMap;
        this.userData = userData;
    }
    public Map<String, String> getOtherDetailsMap() {
        return otherDetailsMap;
    }

    public void setOtherDetailsMap(Map<String, String> otherDetailsMap) {
        this.otherDetailsMap = otherDetailsMap;
    }

    public Map<String, List<String>> getUserData() {
        return userData;
    }

    public void setUserData(Map<String, List<String>> userData) {
        this.userData = userData;
    }
}
