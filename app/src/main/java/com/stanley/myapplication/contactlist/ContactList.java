package com.stanley.myapplication.contactlist;

/**
 * Created by XieDugu on 2016/2/9.
 */
public class ContactList {
    private int ContactId;
    private String contactName;
    private int categoryTag = 0;

    //--------------setter and getter-----------------//
    public int getContactId() {
        return ContactId;
    }
    public void setContactId(int contactId) {
        ContactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getCategoryTag() {
        return categoryTag;
    }
    public void setCategoryTag(int categoryTag) {
        this.categoryTag = categoryTag;
    }

}
