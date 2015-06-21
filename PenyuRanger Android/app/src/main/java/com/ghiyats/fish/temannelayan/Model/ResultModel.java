package com.ghiyats.fish.temannelayan.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ResultModel extends RealmObject {

    @PrimaryKey
    private String ID;

    private String date;
    private int value;

    public ResultModel() {
    }

    public ResultModel(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
