package com.example.splitx;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JoinedRoomObject {
    public List<String> getJoinedRooms() {
        return joinedRooms;
    }
    public void setJoinedRooms(List<String> joinedRooms) {
        this.joinedRooms = joinedRooms;
    }
    List<String> joinedRooms = new ArrayList<>();
    public JoinedRoomObject(List<String> joinedRooms){
        this.joinedRooms = joinedRooms;
    }
    public JoinedRoomObject(){
    }
}
