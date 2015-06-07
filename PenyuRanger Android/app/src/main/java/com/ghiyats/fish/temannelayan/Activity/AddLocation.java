package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ghiyats.fish.temannelayan.Helper.GPSManager;
import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class AddLocation extends ActionBarActivity {
    GPSManager gpsManager;
    SessionManager sessionManager;
    LocationDbHelper dbHelper;
    String spinnerContent [] = {"Penyu Hijau","Penyu Belimbing","Penyu Sisik","Penyu Pipih","Penyu Lekang","Penyu Tempayan", "Penyu Kemp's Ridley"};
    @InjectView(R.id.namaLokasi) EditText namaLokasi;
    @InjectView(R.id.penyuChooser) Spinner penyuChooser;
    @InjectView(R.id.jmlTelur) EditText jmlTelur;
    @InjectView(R.id.dropBoxLink)EditText dropboxLink;

    @InjectView(R.id.btnAdd) Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        ButterKnife.inject(this);
        gpsManager = new GPSManager(this);
        sessionManager = new SessionManager(this);
        dbHelper = new LocationDbHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> penyuAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerContent);
        penyuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        penyuChooser.setAdapter(penyuAdapter);
    }

    public void addLocation(View v ){
        if (!namaLokasi.getText().toString().equals("") && !jmlTelur.getText().toString().equals("")){
            String nama = namaLokasi.getText().toString();
            int jml= Integer.parseInt(jmlTelur.getText().toString());
            String jenis = penyuChooser.getSelectedItem().toString();
            String link = dropboxLink.getText().toString();
            String savedBy = sessionManager.getUsername();
            Date date = getCurentTime();
            String longitude = String.valueOf(gpsManager.getCurrentLocation().getLongitude());
            String latitude = String.valueOf(gpsManager.getCurrentLocation().getLatitude());

            dbHelper.add(nama,jenis,jml,latitude,longitude,date,savedBy,link);
            Toast.makeText(this,"Data telah tersimpan",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this,NavigationDrawer.class));
            finish();
        }
        else
            Crouton.makeText(this,"field harus terisi", Style.ALERT).show();
    }

    public Date getCurentTime(){
        Calendar c = Calendar.getInstance();
        Date date= c.getTime();
        Log.d("","");
        return date;
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
