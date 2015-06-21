package com.ghiyats.fish.temannelayan.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ghiyats on 6/19/2015.
 */
public class KonservasiModel extends RealmObject {

    @PrimaryKey
    private String ID;

    private String namaKonservasi;
    private String alamat;
    private String deskripsi;
    private String telepon;
    private String logoURL;
    private RealmList<RangerModel> rangers;
    private RealmList<TurtleModel> locations;

    public KonservasiModel() {
    }

    public KonservasiModel(String ID, String namaKonservasi, String alamat, String deskripsi, String telepon, String logoURL, RealmList<RangerModel> rangers, RealmList<TurtleModel> locations) {
        this.ID = ID;
        this.namaKonservasi = namaKonservasi;
        this.alamat = alamat;
        this.deskripsi = deskripsi;
        this.telepon = telepon;
        this.logoURL = logoURL;
        this.rangers = rangers;
        this.locations = locations;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNamaKonservasi() {
        return namaKonservasi;
    }

    public void setNamaKonservasi(String namaKonservasi) {
        this.namaKonservasi = namaKonservasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public RealmList<RangerModel> getRangers() {
        return rangers;
    }

    public void setRangers(RealmList<RangerModel> rangers) {
        this.rangers = rangers;
    }

    public RealmList<TurtleModel> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<TurtleModel> locations) {
        this.locations = locations;
    }
}
