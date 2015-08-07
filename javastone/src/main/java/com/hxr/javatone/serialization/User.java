package com.hxr.javatone.serialization;

import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
  private List<User> friends;

    public User() {
    }

    public User(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

  public List<User> getFriends() {
      return friends;
  }

  public void setFriends(final List<User> friends) {
      this.friends = friends;
  }
}
