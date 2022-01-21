package com.trolleyhut.toptrendy;

public class UserRatingData {
    // string variable for
    // storing user's name.
    private String userName;

    // string variable for storing
    // user's contact email
    //private String userEmail;

    // string variable for storing
    // user's points
    private String userPoints;

    // string variable for storing
    // user's points
    private String userPointsLifetime;

    // string variable for storing
    // user's more details
    private String moreDetails;

    // string variable for storing
    // user's stars rated
    private String starsRated;

    // string variable for storing
    // user's current date
    private String userDate;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public UserRatingData() {

    }

    // created getter and setter methods
    // for all our variables.
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getStarsRated() {
        return starsRated;
    }

    public void setStarsRated(String starsRated) {
        this.starsRated = starsRated;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }
}
