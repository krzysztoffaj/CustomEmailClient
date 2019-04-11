package com.app.models;

public class User implements EntityId {
    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private boolean isInAddressBook;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isInAddressBook() {
        return isInAddressBook;
    }

    public void setInAddressBook(boolean isInAddressBook) {
        this.isInAddressBook = isInAddressBook;
    }
}
