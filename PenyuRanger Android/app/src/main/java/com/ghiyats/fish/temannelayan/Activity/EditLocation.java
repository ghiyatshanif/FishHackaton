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
import android.widget.Toast;

import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class EditLocation extends ActionBarActivity {

    SessionManager sessionManager;
    LocationDbHelper dbHelper;
    String spinnerContent [] = {"Penyu Hijau","Penyu Belimbing","Penyu Sisik","Penyu Pipih","Penyu Lekang","Penyu Tempayan", "Penyu Kemp's Ridley"};

    @InjectView(R.id.editNamaLokasi) EditText namaLokasi;
    @InjectView(R.id.editPenyuChooser) Spinner penyuChooser;
    @InjectView(R.id.editJmlTelur) EditText jmlTelur;
    @InjectView(R.id.editDropBoxLink)EditText dropboxLink;
    @InjectView(R.id.btnEdit) Button btnEdit;

    Intent intent;
    TurtleModel turtle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        ButterKnife.inject(this);

        intent = getIntent();
        String id =intent.getStringExtra("ID");
//        Log.d("EditLocation",id);

        sessionManager = new SessionManager(this);
        dbHelper = new LocationDbHelper(this);
        turtle= new TurtleModel();

        turtle = dbHelper.get(id);

        ArrayAdapter<String> penyuAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerContent);
        penyuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        penyuChooser.setAdapter(penyuAdapter);

        namaLokasi.setText(turtle.getName());
        penyuChooser.setSelection(getSelection());
        jmlTelur.setText(String.valueOf(turtle.getJmlTelur()));
        dropboxLink.setText(turtle.getDropboxLink());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int getSelection(){
        int i;
        for (i=0;i<spinnerContent.length;i++){
            if (turtle.getTurtleCategory().equals(spinnerContent[i])){
                break;
            }
        }
        return i;
    }

    public void editLocation(View view){
        Realm realm = Realm.getInstance(this);

        realm.beginTransaction();

        turtle.setDropboxLink(dropboxLink.getText().toString());
        turtle.setJmlTelur(Integer.parseInt(jmlTelur.getText().toString()));
        turtle.setTurtleCategory(penyuChooser.getSelectedItem().toString());
        turtle.setName(namaLokasi.getText().toString());

        realm.commitTransaction();

        Log.d("edited",turtle.getKonservasiInCharge().getNamaKonservasi());
        //dbHelper.edit(turtle.getID(), edit);
        Toast.makeText(this, "Location Edited",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("ID",turtle.getID());
        startActivity(intent);
        finish();
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
