package com.ghiyats.fish.temannelayan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ghiyats on 6/20/2015.
 */
public class KonservasiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<KonservasiModel> konservasies;
    LayoutInflater inflater;

    public KonservasiAdapter(Context context, ArrayList<KonservasiModel> konservasies) {
        this.context = context;
        this.konservasies = konservasies;
    }

    @Override
    public int getCount() {
        return konservasies.size();
    }

    @Override
    public Object getItem(int position) {
        return konservasies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.konservasi_row,null);
        }

        TextView konservasiText = (TextView) convertView.findViewById(R.id.konservasiName);
        TextView alamatText = (TextView) convertView.findViewById(R.id.alamat);
        CircleImageView thumbnail = (CircleImageView) convertView.findViewById(R.id.thumbnailKonservasi);

        KonservasiModel konservasi = konservasies.get(position);

        if (konservasi.getLogoURL() != ""){
            Glide.with(context)
                    .load(konservasi.getLogoURL())
                    .centerCrop()
                    .placeholder(R.drawable.group_button_128)
                    .into(thumbnail);
        }

        konservasiText.setText(konservasi.getNamaKonservasi());
        alamatText.setText(konservasi.getAlamat());

        return convertView;
    }
}
