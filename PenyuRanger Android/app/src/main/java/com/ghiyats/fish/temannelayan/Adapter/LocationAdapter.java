package com.ghiyats.fish.temannelayan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ghiyats on 6/6/2015.
 */
public class LocationAdapter extends BaseAdapter {

    private int [] randomColor  ={R.color.rank1,R.color.rank2,R.color.rank3,R.color.rank4,R.color.rank5,R.color.rank6};
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<TurtleModel> locationItems;


    public LocationAdapter(Activity activity, List<TurtleModel> locationItems) {
        this.activity = activity;
        this.locationItems = locationItems;
    }

    @Override
    public int getCount() {
        return locationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return locationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.location_row, null);
        }


        LinearLayout thumbnailCover = (LinearLayout) convertView.findViewById(R.id.thumbnailCover);
        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        TextView turtleCategory = (TextView) convertView.findViewById(R.id.turtleCategory);
        TextView butirTelur = (TextView) convertView.findViewById(R.id.butirTelur);
        TextView dateSaved = (TextView) convertView.findViewById(R.id.dateSaved);

        //getting location data for the row
        TurtleModel location= locationItems.get(position);

        //set location name
        locationName.setText(location.getName());

        //set coordinat
        turtleCategory.setText(location.getTurtleCategory());

        //set fish info
        butirTelur.setText(String.valueOf(location.getJmlTelur()+" Butir"));

        // date saved
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//                location.getSavedOn().getTime(),
//                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        Date date = location.getSavedOn();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yy");
        String timeUpdated = dateFormat.format(date);

        dateSaved.setText(timeUpdated);

        thumbnailCover.setBackgroundColor(convertView.getResources().getColor((randomColor[location.getRandomNum()])));

        return convertView;
    }
}
