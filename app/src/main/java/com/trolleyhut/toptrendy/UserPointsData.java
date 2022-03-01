package com.trolleyhut.toptrendy;

public class UserPointsData {
    // string variable for
    // storing user's name.
    private String userName;
    private String userEmail;
    private String userDate;
    private String userPoints;
    private String userPointsLifetime;

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

}
