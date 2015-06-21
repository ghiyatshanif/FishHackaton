package com.ghiyats.fish.temannelayan.Helper;

import android.content.Context;

import com.ghiyats.fish.temannelayan.Model.ResultModel;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Ghiyats on 6/18/2015.
 */
public class ResultDbHelper {
    private Context context;

    public ResultDbHelper(Context context) {
        this.context = context;
    }

    public void add(ResultModel resultModel){
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        ResultModel result = realm.createObject(ResultModel.class);

        result.setID(UUID.randomUUID().toString());
        result.setDate(resultModel.getDate());
        result.setValue(resultModel.getValue());

        realm.commitTransaction();
    }

    public void clear(){
        Realm realm = Realm.getInstance(context);

        RealmResults<ResultModel> results = realm.where(ResultModel.class).findAll();

        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
    }

}
