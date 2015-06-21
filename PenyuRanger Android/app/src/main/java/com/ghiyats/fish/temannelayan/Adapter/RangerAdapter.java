package com.ghiyats.fish.temannelayan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RangerAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<RangerModel> rangers;
    private LayoutInflater inflater;

    public RangerAdapter(ArrayList<RangerModel> rangers, Activity activity) {
        this.rangers = rangers;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return rangers.size();
    }

    @Override
    public Object getItem(int position) {
        return rangers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.ranger_row,null);
        }

        RangerModel ranger = rangers.get(position);

        CircleImageView thumbnail =(CircleImageView) convertView.findViewById(R.id.thumbnail);
        TextView rangerName = (TextView) convertView.findViewById(R.id.rangerName);
        TextView memberOf = (TextView) convertView.findViewById(R.id.memberOf);

        //thumbneil with glide
        Glide.with(activity)
                .load(ranger.getThumbnail())
                .override(100, 100)
                .centerCrop()
                .into(thumbnail);

        rangerName.setText(ranger.getRangerName());
        memberOf.setText(ranger.getMemberOf().getNamaKonservasi());

        return convertView;
    }
}
