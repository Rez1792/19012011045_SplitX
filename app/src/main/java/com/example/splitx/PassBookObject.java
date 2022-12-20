package com.example.splitx;

public class PassBookObject {
    String date, time, name, upi, amount, status, upi2;
    int flag;
    public PassBookObject(){

    }
    public PassBookObject(String date, String time, String name, String upi,String upi2, String amount,String status,int flag){
        this.date = date;
        this.time = time;
        this.name = name;
        this.upi = upi;
        this.amount = amount;
        this.status = status;
        this.flag = flag;
        this.upi2 = upi2;
    }
    public String getDate() {
        return date;
    }
    public int getFlag(){
        return flag;
    }
    public void setFlag(int flag){
        this.flag = flag;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
    public String getUpi2() {
        return upi2;
    }

    public void setUpi2(String upi2) {
        this.upi2 = upi2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
