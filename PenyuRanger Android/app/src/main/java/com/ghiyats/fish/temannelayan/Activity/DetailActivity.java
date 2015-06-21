package com.ghiyats.fish.temannelayan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class DetailActivity extends ActionBarActivity {

    Intent intent;
    LocationDbHelper dbHelper;
    TurtleModel turtle;
    String ID;
    @InjectView(R.id.namaTitle) TextView namaTitle;
    @InjectView(R.id.locationTitle) TextView locationTitle;
    @InjectView(R.id.btnOpenMap) TextView btnOpenMap;
    @InjectView(R.id.jmlTitle) TextView jmlTitle;
    @InjectView(R.id.jenisTitle) TextView jenisTitle;
    @InjectView(R.id.btnOpenDropbox) Button btnOpenDropbox;
    @InjectView(R.id.konservasi) TextView konservasiText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        dbHelper = new LocationDbHelper(this);
        turtle = new TurtleModel();
        intent = getIntent();
        Log.d("hasExtra", String.valueOf(intent.hasExtra("ID")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            if (intent.getStringExtra("ID") != null) {
                ID =intent.getStringExtra("ID");
                turtle = dbHelper.get(ID);
                namaTitle.setText(turtle.getName());
                locationTitle.setText(turtle.getLatitude() + ", " + turtle.getLongitude());
                jmlTitle.setText(String.valueOf(turtle.getJmlTelur()));
                jenisTitle.setText(turtle.getTurtleCategory());
                konservasiText.setText(turtle.getKonservasiInCharge().getNamaKonservasi());
            }
            ID = savedInstanceState.getString("TURTLE_ID");
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("restoreSavedInstance",savedInstanceState.getString("TURTLE_ID"));
        ID = savedInstanceState.getString("TURTLE_ID");

        if (ID != null){

            turtle = dbHelper.get(ID);
            namaTitle.setText(turtle.getName());
            locationTitle.setText(turtle.getLatitude() + ", " + turtle.getLongitude());
            jmlTitle.setText(String.valueOf(turtle.getJmlTelur()));
            jenisTitle.setText(turtle.getTurtleCategory());
            konservasiText.setText(turtle.getKonservasiInCharge().getNamaKonservasi());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_edit:
                Intent edit = new Intent(this,EditLocation.class);
                edit.putExtra("ID", ID);
                startActivity(edit);
                break;
            case R.id.action_delete:
                confirmDialog();
                break;
            case R.id.action_stat:
                Intent chart = new Intent(this,ChartActivity.class);
                chart.putExtra("ID", turtle.getID());
                startActivity(chart);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDropbox(View view){
        try {
            String uri = turtle.getDropboxLink();

            Intent dropboxIntent = new Intent(Intent.ACTION_VIEW);
            dropboxIntent.setData(Uri.parse(uri));
            startActivity(dropboxIntent);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("link dropbox",turtle.getDropboxLink());
            Crouton.makeText(this,"Link Dropbox belum di Set/ tidak valid", Style.ALERT).show();
        }
    }

    public void openMap(View view){
        //Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        String uri="geo:0,0?q="+turtle.getLatitude()+","+turtle.getLongitude()+"("+turtle.getName()+")";

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(mapIntent);
    }

    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setTitle("Hapus Data Penyusuar?")
                .setMessage("Pastikan data di hapus karena ada kesalahan data atau karena Telur telah menetas")
                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.delete(turtle.getID());
                        Intent intent = new  Intent(getBaseContext(), NavigationDrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("save instance",turtle.getID());
        outState.putString("TURTLE_ID", turtle.getID());
        super.onSaveInstanceState(outState);
    }
}
