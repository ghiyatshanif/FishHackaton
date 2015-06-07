package com.ghiyats.fish.temannelayan.Model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ghiyats on 6/6/2015.
 */
public class TurtleModel extends RealmObject {
    @PrimaryKey
    private String ID;

    private String name;
    private String turtleCategory;
    private int jmlTelur;
    private int randomNum;
    private String longitude;
    private String latitude;
    private String dropboxLink;
    private Date savedOn;
    private String savedBy;

    public TurtleModel(String ID, String name, String turtleCategory, int jmlTelur, String longitude, String latitude, Date savedOn, String savedBy, String dropboxLink, int randomNum) {
        this.ID = ID;
        this.name = name;
        this.turtleCategory = turtleCategory;
        this.jmlTelur =jmlTelur;
        this.longitude = longitude;
        this.latitude = latitude;
        this.savedOn = savedOn;
        this.savedBy = savedBy;
        this.dropboxLink = dropboxLink;
        this.randomNum = randomNum;
    }

    public TurtleModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTurtleCategory() {
        return turtleCategory;
    }

    public void setTurtleCategory(String turtleCategory) {
        this.turtleCategory = turtleCategory;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getSavedOn() {
        return savedOn;
    }

    public void setSavedOn(Date savedOn) {
        this.savedOn = savedOn;
    }

    public String getSavedBy() {
        return savedBy;
    }

    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    public String getDropboxLink() {
        return dropboxLink;
    }

    public void setDropboxLink(String dropboxLink) {
        this.dropboxLink = dropboxLink;
    }

    public int getJmlTelur() {
        return jmlTelur;
    }

    public void setJmlTelur(int jmlTelur) {
        this.jmlTelur = jmlTelur;
    }

    public int getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(int randomNum) {
        this.randomNum = randomNum;
    }
}
