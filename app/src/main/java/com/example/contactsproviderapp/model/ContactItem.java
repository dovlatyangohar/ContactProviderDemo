package com.example.contactsproviderapp.model;

public class ContactItem {
    private String contactItemName;
    private String contactItemNumber;
    private String contactId;

    public ContactItem(String contactItemName, String contactId) {
        this.contactItemName = contactItemName;
        this.contactId = contactId;
    }

    public ContactItem(String contactName) {
        this.contactItemName = contactName;
    }

    public String getContactItemNumber() {
        return contactItemNumber;
    }

    public String getContactItemName() {
        return contactItemName;
    }

    public String getContactId() {
        return contactId;
    }
}
