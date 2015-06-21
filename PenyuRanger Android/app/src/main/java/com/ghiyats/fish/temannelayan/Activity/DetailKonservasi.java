package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ghiyats.fish.temannelayan.Fragment.RangerFragment;
import com.ghiyats.fish.temannelayan.Helper.KonservasiDbHelper;
import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailKonservasi extends ActionBarActivity {
    Intent intent;
    String ID;
    KonservasiModel konservasi;
    KonservasiDbHelper konservasiHelper;

    @InjectView(R.id.konservasiName) TextView mKonservasiName;
    @InjectView(R.id.alamatText) TextView mAlamatText;
    @InjectView(R.id.phoneText) TextView mPhoneText;
    @InjectView(R.id.jmlRanger) TextView mJmlranger;
    @InjectView(R.id.jmlTurtle) TextView mJmlTurtle;
    @InjectView(R.id.deskripsiText) TextView mDeskripsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konservasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);
        konservasiHelper = new KonservasiDbHelper(this);
        intent = getIntent();

        try {
            ID = intent.getStringExtra("ID");
            if (ID != null) {
                konservasi = konservasiHelper.getByID(ID);
                mKonservasiName.setText(konservasi.getNamaKonservasi());
                mAlamatText.setText(konservasi.getAlamat());
                mPhoneText.setText(konservasi.getTelepon());
                mJmlranger.setText(konservasi.getRangers().size()+" Ranger");
                mJmlTurtle.setText(konservasi.getLocations().size()+" lokasi");
                mDeskripsi.setText(konservasi.getDeskripsi());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (findViewById(R.id.rangerLvContainer) != null){

            if (savedInstanceState != null){
                return;
            }

            RangerFragment rangerFragment = new RangerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ID",konservasi.getID());

            rangerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.rangerLvContainer,rangerFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
