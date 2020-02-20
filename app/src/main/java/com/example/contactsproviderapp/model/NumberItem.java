package com.example.contactsproviderapp.model;

public class NumberItem {
    private String contactItemNumber;
    private String numberID;

    public NumberItem(String contactItemNumber, String numberID) {
        this.contactItemNumber = contactItemNumber;
        this.numberID = numberID;
    }

    public NumberItem(String contactItemNumber) {
        this.contactItemNumber = contactItemNumber;
    }

    public String getNumberID() {
        return numberID;
    }

    public String getContactItemNumber() {
        return contactItemNumber;
    }
}
