package com.trolleyhut.toptrendy;

public class UserRegisterData {
    // string variable for
    // storing user's name.
    private String userName;
    private String userEmail;
    private String userDate;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public UserRegisterData() {

    }

    // created getter and setter methods
    // for all our variables.
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

}
