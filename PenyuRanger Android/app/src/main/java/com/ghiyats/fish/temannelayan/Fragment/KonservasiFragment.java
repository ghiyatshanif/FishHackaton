package com.ghiyats.fish.temannelayan.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ghiyats.fish.temannelayan.Activity.DetailKonservasi;
import com.ghiyats.fish.temannelayan.Adapter.KonservasiAdapter;
import com.ghiyats.fish.temannelayan.Adapter.RangerAdapter;
import com.ghiyats.fish.temannelayan.Helper.KonservasiDbHelper;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class KonservasiFragment extends Fragment {
    SessionManager sessionManager;
    KonservasiDbHelper konservasiHelper;
    ListView lvKonservasi;
    ArrayList<KonservasiModel> konservasies;
    KonservasiAdapter adapter;


    public KonservasiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sessionManager = new SessionManager(getActivity());
        konservasiHelper = new KonservasiDbHelper(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_konservasi, container, false);

        lvKonservasi = (ListView) view.findViewById(R.id.konservasiList);
        konservasies = new ArrayList<KonservasiModel>();
        konservasies = konservasiHelper.load();
        adapter = new KonservasiAdapter(getActivity(),konservasies);
        new setAdapterAsync().execute();

        lvKonservasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailKonservasi.class);
                intent.putExtra("ID",konservasies.get(position).getID());
                startActivity(intent);
            }
        });

        return view;
    }

    public class setAdapterAsync extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(adapter != null) lvKonservasi.setAdapter(adapter);
        }
    }



}
