package com.ghiyats.fish.temannelayan.Helper;

import android.content.Context;

import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class RangerDbHelper {
    private Context context;
    private KonservasiDbHelper konservasiHelper;

    public RangerDbHelper(Context context) {
        this.context = context;
        konservasiHelper = new KonservasiDbHelper(context);
    }

    public ArrayList<RangerModel> load(){
        ArrayList<RangerModel> rangers = new ArrayList<RangerModel>();
        Realm realm = Realm.getInstance(context);
        RealmResults<RangerModel> results = realm.where(RangerModel.class).findAll();

        for (RangerModel rangerModel : results){
            rangers.add(rangerModel);
        }
        return rangers;
    }

    public ArrayList<RangerModel> loadByKonservasi(String ID){
        ArrayList<RangerModel> rangers = new ArrayList<RangerModel>();
        Realm realm = Realm.getInstance(context);
        RealmResults<RangerModel> results = realm.where(RangerModel.class).equalTo("memberOf.ID",ID).findAll();

        for (RangerModel rangerModel : results){
            rangers.add(rangerModel);
        }
        return rangers;
    }

    public void edit(String ID, RangerModel ranger){
        Realm realm = Realm.getInstance(context);
        RangerModel edit = new RangerModel();

        edit = realm.where(RangerModel.class).equalTo("uniqueID",ID).findFirst();

        realm.beginTransaction();

        edit.setRangerName(ranger.getRangerName());
        edit.setPassword(ranger.getPassword());
        edit.setPhoneNumber(ranger.getPhoneNumber());
        edit.setMemberOf(ranger.getMemberOf());
        edit.setThumbnail(ranger.getThumbnail());

        realm.commitTransaction();
    }

    public void delete(String ID){
        Realm realm = Realm.getInstance(context);

        RangerModel delete = new RangerModel();
        delete = realm.where(RangerModel.class).equalTo("uniqueID",ID).findFirst();

        realm.beginTransaction();
        delete.removeFromRealm();
        realm.commitTransaction();

        konservasiHelper.removeRanger(delete.getMemberOf().getID(),delete);
    }

    public void add(RangerModel ranger){
        Realm realm = Realm.getInstance(context);

        RangerModel add = realm.createObject(RangerModel.class);

        realm.beginTransaction();
        add.setUniqueID(UUID.randomUUID().toString());
        add.setRangerID(ranger.getRangerID());
        add.setRangerName(ranger.getRangerName());
        add.setPassword(ranger.getPassword());
        add.setPhoneNumber(ranger.getPhoneNumber());
        add.setMemberOf(ranger.getMemberOf());
        add.setThumbnail(ranger.getThumbnail());
        realm.commitTransaction();

        konservasiHelper.addRanger(ranger.getMemberOf().getID(),add);
    }

    public RangerModel get(String rangerID){
        Realm realm = Realm.getInstance(context);

        RangerModel get = new RangerModel();
        get = realm.where(RangerModel.class).equalTo("rangerID",rangerID).findFirst();

        return get;
    }

    public void clear(){
        Realm realm = Realm.getInstance(context);
        RealmResults<RangerModel> query = realm.where(RangerModel.class).findAll();

        realm.beginTransaction();
        query.clear();
        realm.commitTransaction();
    }

    public ArrayList<String> getRangerNames(){
        Realm realm = Realm.getInstance(context);
        ArrayList<String> rangers = new ArrayList<String>();

        RealmResults<RangerModel> query = realm.where(RangerModel.class).findAll();
        for (RangerModel rm : query){
            rangers.add(rm.getRangerID()+" "+getRangerNames());
        }
        return rangers;
    }


    public void init(String rangerID, String rangerName, KonservasiModel memberOf, String phoneNumber, String password, String thumbnail){
        Realm realm = Realm.getInstance(context);

        RealmList<TurtleModel> inCharge = new RealmList<TurtleModel>();


        realm.beginTransaction();
        RangerModel init = realm.createObject(RangerModel.class);
        init.setUniqueID(UUID.randomUUID().toString());
        init.setRangerID(rangerID);
        init.setRangerName(rangerName);
        init.setMemberOf(memberOf);
        init.setPhoneNumber(phoneNumber);
        init.setPassword(password);
        init.setThumbnail(thumbnail);
        realm.commitTransaction();

        konservasiHelper.addRanger(memberOf.getID(),init);
    }


}
