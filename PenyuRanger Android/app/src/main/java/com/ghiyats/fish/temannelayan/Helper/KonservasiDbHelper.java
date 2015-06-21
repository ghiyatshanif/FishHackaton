package com.ghiyats.fish.temannelayan.Helper;

import android.content.Context;
import android.util.Log;

import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Ghiyats on 6/19/2015.
 */
public class KonservasiDbHelper {

    private Context context;

    public KonservasiDbHelper(Context context) {
        this.context = context;
    }

    public void add(KonservasiModel konservasi){
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        KonservasiModel add = realm.createObject(KonservasiModel.class);

        add.setID(UUID.randomUUID().toString());
        add.setNamaKonservasi(konservasi.getNamaKonservasi());
        add.setAlamat(konservasi.getAlamat());
        add.setDeskripsi(konservasi.getDeskripsi());
        add.setLogoURL(konservasi.getLogoURL());
        add.setTelepon(konservasi.getTelepon());
        add.setLocations(konservasi.getLocations());
        add.setRangers(konservasi.getRangers());

        realm.commitTransaction();
    }

    public void init(String nama, String alamat, String deskripsi, String logoURL, String telepon,
                     RealmList<TurtleModel> locations, RealmList<RangerModel> rangers ){
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        KonservasiModel add = realm.createObject(KonservasiModel.class);

        add.setID(UUID.randomUUID().toString());
        add.setNamaKonservasi(nama);
        add.setAlamat(alamat);
        add.setDeskripsi(deskripsi);
        add.setLogoURL(logoURL);
        add.setTelepon(telepon);
        add.setLocations(locations);
        add.setRangers(rangers);

        realm.commitTransaction();
    }

    public KonservasiModel get(String name){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;

        konservasi = realm.where(KonservasiModel.class).equalTo("namaKonservasi",name).findFirst();

        return konservasi;
    }

    public KonservasiModel getByID(String ID){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;

        konservasi = realm.where(KonservasiModel.class).equalTo("ID",ID).findFirst();

        return konservasi;
    }

    public ArrayList<KonservasiModel> load(){
        ArrayList<KonservasiModel> konservasi = new ArrayList<KonservasiModel>();
        Realm realm = Realm.getInstance(context);

        RealmResults<KonservasiModel> results = realm.where(KonservasiModel.class).findAll();

        for (KonservasiModel km : results){
            konservasi.add(km);
        }
        return konservasi;
    }

    public void addTurtles(String ID, TurtleModel turtle){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;

        konservasi = realm.where(KonservasiModel.class).equalTo("ID",ID).findFirst();

        realm.beginTransaction();
        konservasi.getLocations().add(turtle);
        realm.commitTransaction();
        Log.d("KonservasiDbHelper","turtle added to list");
    }

    public void removeTurtles(String ID,TurtleModel turtle){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;
        konservasi = realm.where(KonservasiModel.class).equalTo("ID",ID).findFirst();

        realm.beginTransaction();
        konservasi.getLocations().remove(turtle);
        realm.commitTransaction();
    }

    public void addRanger(String ID, RangerModel ranger){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;

        konservasi = realm.where(KonservasiModel.class).equalTo("ID",ID).findFirst();

        realm.beginTransaction();
        konservasi.getRangers().add(ranger);
        realm.commitTransaction();
        Log.d("KonservasiDbHelper", "Ranger added to list");
    }

    public void removeRanger (String ID,RangerModel ranger){
        Realm realm = Realm.getInstance(context);
        KonservasiModel konservasi;
        konservasi = realm.where(KonservasiModel.class).equalTo("ID",ID).findFirst();

        realm.beginTransaction();
        konservasi.getRangers().remove(ranger);
        realm.commitTransaction();
    }

}
