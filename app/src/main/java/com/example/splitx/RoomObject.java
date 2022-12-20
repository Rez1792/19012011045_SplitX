package com.example.splitx;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class RoomObject {
    String roomName, roomId, numberOfpeople, numberOfCards;

    public List<String> getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(List<String> userPhoto) {
        this.userPhoto = userPhoto;
    }

    List<String> userPhoto = new ArrayList<>();

    public RoomObject(String roomId, String roomName, String numberOfpeople, String numberOfCards,List<String> userPhoto){
        this.roomId = roomId;
        this.roomName = roomName;
        this.numberOfpeople = numberOfpeople;
        this.numberOfCards = numberOfCards;
        this.userPhoto = userPhoto;
    }
    public RoomObject(){

    }
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getNumberOfpeople() {
        return numberOfpeople;
    }

    public void setNumberOfpeople(String numberOfpeople) {
        this.numberOfpeople = numberOfpeople;
    }

    public String getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(String numberOfCards) {
        this.numberOfCards = numberOfCards;
    }



}
