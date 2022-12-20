package com.example.splitx;

import java.util.ArrayList;
import java.util.List;

public class PU_Object {
    List<String> paidList = new ArrayList<>();
    List<String> UnPaidList = new ArrayList<>();
    public PU_Object(){
    }
    public PU_Object(List<String> paidList, List<String> unPaidList){
        this.paidList = paidList;
        this.UnPaidList = unPaidList;
    }
    public List<String> getPaidList() {
        return paidList;
    }

    public void setPaidList(List<String> paidList) {
        this.paidList = paidList;
    }

    public List<String> getUnPaidList() {
        return UnPaidList;
    }

    public void setUnPaidList(List<String> unPaidList) {
        UnPaidList = unPaidList;
    }
}
