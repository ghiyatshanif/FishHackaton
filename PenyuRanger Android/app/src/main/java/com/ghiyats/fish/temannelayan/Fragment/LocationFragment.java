package com.ghiyats.fish.temannelayan.Fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ghiyats.fish.temannelayan.Activity.AddLocation;
import com.ghiyats.fish.temannelayan.Activity.DetailActivity;
import com.ghiyats.fish.temannelayan.Activity.MapsView;
import com.ghiyats.fish.temannelayan.Adapter.LocationAdapter;
import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    ArrayList<TurtleModel> locations;
    LocationDbHelper dbHelper;
    SessionManager sessionManager;
    ListView locationList;
    LocationAdapter adapter;
    RangerModel ranger;
    RangerDbHelper rangerHelper;


    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());
        dbHelper = new LocationDbHelper(getActivity().getApplicationContext());
        rangerHelper = new RangerDbHelper(getActivity().getApplicationContext());
        ranger = rangerHelper.get(sessionManager.getUserId());
        Log.d("TAG",ranger.getRangerName()+ranger.getPhoneNumber()+ranger.getMemberOf());
//        dbHelper.clear();
//        initData();
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        //dbHelper.clear();
        Log.d("TAG",ranger.getMemberOf().toString());
        locations = dbHelper.loadByKonservasi(ranger.getMemberOf().getID());
        locationList = (ListView) view.findViewById(R.id.locationList);

        adapter = new LocationAdapter(getActivity(),locations);
        new setAdapterAsync().execute();

        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TurtleModel turtle = locations.get(position);
                String ID = turtle.getID();

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        Log.d("LocationFragment", "resumed");
        //locations.clear();
        locations = dbHelper.loadByKonservasi(ranger.getMemberOf().getID());
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d("LocationFragment","started");
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                startActivity(new Intent(this.getActivity(), AddLocation.class));
                //getActivity().finish();
                break;
            case R.id.action_map:
                startActivity(new Intent(this.getActivity(), MapsView.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public Date getCurentTime(){
        Calendar c = Calendar.getInstance();
        Date date= c.getTime();
        return date;
    }

    public class setAdapterAsync extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(adapter != null) locationList.setAdapter(adapter);
        }
    }

    public void initData(){
        Log.d("masukin data", "dsfsafdasfasdfda");
        dbHelper.init("Penyusuar pangandaran 1", "Penyu Hijau", 92, "-7.692016", "108.663652", getCurentTime(), sessionManager.getUsername(), "",ranger.getMemberOf());
        dbHelper.init("Penyusuar pangandaran 2", "Penyu Sisisk",100, "-7.730110", "108.670099", getCurentTime(), sessionManager.getUsername(),"",ranger.getMemberOf());
        dbHelper.init("Penyusuar Batu Hiu 1", "Penyu Belimbing",80, "-7.690449", "108.542076", getCurentTime(), sessionManager.getUsername(), "",ranger.getMemberOf());
        dbHelper.init("Penyusuar Batu Hiu utara", "Penyu Hijau",77, "-7.000973", "106.509930", getCurentTime(), sessionManager.getUsername(),"",ranger.getMemberOf());
        dbHelper.init("Penyusuar Pelabuhan Ratu", "Penyu Tempayan",93, "-6.981864", "106.540221", getCurentTime(), sessionManager.getUsername(),"",ranger.getMemberOf());
        dbHelper.init("Penyusuar Pangumbahan wetan", "Penyu Sisik",82, "-7.344612", "106.400885", getCurentTime(), sessionManager.getUsername(),"",ranger.getMemberOf());
        dbHelper.init("Penusuar Pangumbahan kidul", "Penyu Hijau",67, "-7.336865", "106.398546", getCurentTime(), sessionManager.getUsername(),"",ranger.getMemberOf());
    }


}
