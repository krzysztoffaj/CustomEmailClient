package com.app.models;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User implements EntityId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "is_in_address_book")
    private boolean isInAddressBook;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String firstName, String lastName, String emailAddress, boolean isInAddressBook) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isInAddressBook = isInAddressBook;
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
