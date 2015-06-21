package com.ghiyats.fish.temannelayan.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ghiyats.fish.temannelayan.Adapter.RangerAdapter;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RangerFragment extends Fragment {
    SessionManager sessionManager;
    RangerDbHelper rangerHelper;
    ListView rangerList;
    ArrayList<RangerModel> rangers;
    RangerAdapter adapter;
    RangerModel ranger;


    public RangerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rangerHelper = new RangerDbHelper(getActivity());
//        ranger = rangerHelper.get(sessionManager.getUserId());
//        rangerHelper.clear();
//        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ranger, container, false);

        sessionManager = new SessionManager(getActivity());
        String konservasiID = getArguments().getString("ID");
        //rangerHelper =
        rangerList = (ListView) view.findViewById(R.id.rangerList);
        rangers = new ArrayList<RangerModel>();
        rangers = rangerHelper.loadByKonservasi(konservasiID);

        adapter = new RangerAdapter(rangers,getActivity());

        new setAdapterAsync().execute();

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
            if(adapter != null) rangerList.setAdapter(adapter);
        }
    }


//    public void initData(){
//        ArrayList<TurtleModel> turtles = new ArrayList<TurtleModel>();
//        rangerHelper.init("3302","Maman","WWF Indonesia","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpa1/v/t1.0-9/10401585_10201187950620761_5555408473048279898_n.jpg?_nc_eui=AWhi3g6283Ot6iePnmsL2nhEvZgpr73K-_qQeA&oh=f99bdcfe6fcbd7903d2664ea62727470&oe=55F9E69F");
//        rangerHelper.init("3301","Satria Rahmadi","Masyarakat Peduli Perikanan Indonesia","082193419641","satria",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xaf1/v/t1.0-9/10574526_1574968782719147_3881307069647061915_n.jpg?_nc_eui=AWik0vTVE3WDyRlD7ypSVdsJg2jFSzysO_BOTQ&oh=1334842542953bc02c0b41ad45de7dfd&oe=55F09868");
//        rangerHelper.init("3303","Yudi Prasetyo","WWF Indonesia","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/10625147_10202655478211469_8008595998519576575_n.jpg?_nc_eui=AWjnJJn1FZZLNKKn3Stqzlc6jj5YL5TtlTr-Ww&oh=39b0d61cfc35b7ad1d19e6c46aba049e&oe=560356D3");
//        rangerHelper.init("3304","Muhammad Assegaf","Kementrian Perairan dan Perikanan","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/1974975_10201737627348865_1989057729_n.jpg?_nc_eui=AWh-uXfxiYtMBV3MCzzDDEvVfUJN7IBIW4Kquw&oh=33aeee1ca89ea0322b49de25b239e44f&oe=55C0835F");
//        rangerHelper.init("3305","Ludffi Rizkika",,"08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/1511331_10204152537517735_2471003090369745169_n.jpg?_nc_eui=AWiIC3ch4tpC_wo-heEDz-jeGPU6Y_pyqx46BA&oh=5b195e69a1d99fa2e68492192b3032ca&oe=55F202EC");
//        rangerHelper.init("3306","Andri Amirudin","Konservasi Penyu Gili Trawangan","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/10947207_927586910593428_2325548007938549976_n.jpg?_nc_eui=AWjUj1olcQ117YkAHhc5quDPSqWUDYAgHAuQIw&oh=260c03f7125ebaec45ee1eab0b492853&oe=55FEBBF6");
//        rangerHelper.init("3307","Dimas Setiawan","Masyarakat Peduli Perikanan Indonesia","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/12819_10202319653432515_8916180256083386116_n.jpg?_nc_eui=AWggzKp4ZAKNJIjS3LvkxO5t-Hh97HU-Rgq7Iw&oh=3378a747703abe0f12d18a6770baabfd&oe=55F8273B");
//        rangerHelper.init("3308","Aldifianto","Konservasi Penyu Gili Trawangan","08787311293","maman1",turtles,
//                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/1535656_10201095863467444_399476200_n.jpg?_nc_eui=AWg2xXdXwksfqjmKaO-AxvheJSLg1PhsilDk-A&oh=323ce52b6bc63f3ea8fd0112806d2434&oe=55F165AB");
//    }


}
