package com.trolleyhut.toptrendy;

public class UserWithdrawData {
    // string variable for
    // storing user's name.
    private String userName;
    private String userEmail;
    private String userDateJoined;
    private String estEarnings;
    private String factsSeen;
    private String adsWatched;
    private String adsSkipped;
    private String totalStreaks;
    private String totalWithdrawals;
    private String points;
    private String pointsDeducted;
    private String pointsLifeTime;
    private String userDate;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public UserWithdrawData() {

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

    public String getUserDateJoined() {
        return userDateJoined;
    }

    public void setUserDateJoined(String userDateJoined) {
        this.userDateJoined = userDateJoined;
    }

    public String getEstEarnings() {
        return estEarnings;
    }

    public void setEstEarnings(String estEarnings) {
        this.estEarnings = estEarnings;
    }

    public String getFactsSeen() {
        return factsSeen;
    }

    public void setFactsSeen(String factsSeen) {
        this.factsSeen = factsSeen;
    }

    public String getAdsWatched() {
        return adsWatched;
    }

    public void setAdsWatched(String adsWatched) {
        this.adsWatched = adsWatched;
    }

    public String getAdsSkipped() {
        return adsSkipped;
    }

    public void setAdsSkipped(String adsSkipped) {
        this.adsSkipped = adsSkipped;
    }

    public String getTotalStreaks() {
        return totalStreaks;
    }

    public void setTotalStreaks(String totalStreaks) {
        this.totalStreaks = totalStreaks;
    }

    public String getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(String totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPointsDeducted() {
        return pointsDeducted;
    }

    public void setPointsDeducted(String pointsDeducted) {
        this.pointsDeducted = pointsDeducted;
    }

    public String getPointsLifeTime() {
        return pointsLifeTime;
    }

    public void setPointsLifeTime(String pointsLifeTime) {
        this.pointsLifeTime = pointsLifeTime;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

}
