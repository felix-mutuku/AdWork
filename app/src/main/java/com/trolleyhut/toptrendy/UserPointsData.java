package com.trolleyhut.toptrendy;

public class UserPointsData {
    // string variable for
    // storing user's name.
    private String userName;

    // string variable for storing
    // user's contact email
    private String userEmail;

    // string variable for storing
    // user's points
    private String userPoints;

    // string variable for storing
    // user's points
    private String userPointsLifetime;

    // string variable for storing
    // user's current date
    private String userDate;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public UserPointsData() {

    }

    // created getter and setter methods
    // for all our variables.
    public String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        //this.userName = userName;
        userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(String userPoints) {
        this.userPoints = userPoints;
    }

    public String getUserPointsLifetime() {
        return userPointsLifetime;
    }

    public void setUserPointsLifetime(String userPointsLifetime) {
        this.userPointsLifetime = userPointsLifetime;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }
}
