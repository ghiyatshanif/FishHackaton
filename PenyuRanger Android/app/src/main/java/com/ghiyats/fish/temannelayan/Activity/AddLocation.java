package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ghiyats.fish.temannelayan.Helper.GPSManager;
import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
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
    RangerDbHelper rangerDbHelper;
    String spinnerContent [] = {"Penyu Hijau","Penyu Belimbing","Penyu Sisik","Penyu Pipih","Penyu Lekang","Penyu Tempayan", "Penyu Kemp's Ridley"};
    @InjectView(R.id.namaLokasi) EditText namaLokasi;
    @InjectView(R.id.penyuChooser) Spinner penyuChooser;
    @InjectView(R.id.jmlTelur) EditText jmlTelur;
    @InjectView(R.id.dropBoxLink)EditText dropboxLink;

    @InjectView(R.id.btnAdd) Button btnAdd;
    TurtleModel turtle;
    RangerModel ranger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        ButterKnife.inject(this);
        gpsManager = new GPSManager(this);
        gpsManager.buildGoogleApiClient();
        gpsManager.getGoogleApiClient().connect();

        sessionManager = new SessionManager(this);
        dbHelper = new LocationDbHelper(this);
        rangerDbHelper = new RangerDbHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> penyuAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerContent);
        penyuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        penyuChooser.setAdapter(penyuAdapter);
    }

    public void addLocation(View v ){
        turtle = new TurtleModel();
        ranger = rangerDbHelper.get(sessionManager.getUserId());
        if (!namaLokasi.getText().toString().equals("") && !jmlTelur.getText().toString().equals("")){
            turtle.setName(namaLokasi.getText().toString());
            turtle.setJmlTelur(Integer.parseInt(jmlTelur.getText().toString()));
            turtle.setTurtleCategory(penyuChooser.getSelectedItem().toString());
            turtle.setDropboxLink(dropboxLink.getText().toString());
            turtle.setSavedBy(sessionManager.getUsername());
            turtle.setSavedOn(getCurentTime());
            turtle.setLongitude(String.valueOf(gpsManager.getCurrentLocation().getLongitude()));
            turtle.setLatitude(String.valueOf(gpsManager.getCurrentLocation().getLatitude()));
            turtle.setKonservasiInCharge(ranger.getMemberOf());

            dbHelper.add(turtle);

            Crouton.makeText(this,"Data telah tersimpan",Style.CONFIRM).show();
            addEvent(60,namaLokasi.getText().toString());
            Crouton.makeText(this,"Reminder ditambahkan ke kalendar",Style.INFO).show();
            clearForm();


            //startActivity(new Intent(this, NavigationDrawer.class));
            //finish();
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


    public void addEvent(int daysToEvent,String locations){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysToEvent);
        Log.d("calendar",calendar.getTime().toString());
        Date eventDate = calendar.getTime();

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(Events.TITLE, "PenyuRanger Reminder");
        intent.putExtra(Events.DESCRIPTION, "Hari ke-" + daysToEvent + " semenjak pemasangan Penyusuar " + locations + ", harap segera menyiapkan untuk penetasan semi alami");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,eventDate.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,eventDate.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,true);

        startActivity(intent);
    }

    public void clearForm(){
        ViewGroup group = (ViewGroup)findViewById(R.id.addLocationGroup);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
        }
    }
}
