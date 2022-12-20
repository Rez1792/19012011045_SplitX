package com.example.splitx;

import java.util.ArrayList;
import java.util.List;

public class DetailsObject {
    List<String> joinedUsers = new ArrayList<>();

    public DetailsObject(List<String> joinedUsers){
        this.joinedUsers = joinedUsers;
    }
    public DetailsObject(){

    }
    public List<String> getJoinedUsers() {
        return joinedUsers;
    }
    public void setJoinedUsers(List<String> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }
}
