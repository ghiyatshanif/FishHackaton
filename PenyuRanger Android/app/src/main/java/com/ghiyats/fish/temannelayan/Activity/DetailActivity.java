package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

public class DetailActivity extends ActionBarActivity {

    Intent intent;
    LocationDbHelper dbHelper;
    TurtleModel turtle;
    @InjectView(R.id.namaTitle) TextView namaTitle;
    @InjectView(R.id.locationTitle) TextView locationTitle;
    @InjectView(R.id.btnOpenMap) TextView btnOpenMap;
    @InjectView(R.id.jmlTitle) TextView jmlTitle;
    @InjectView(R.id.jenisTitle) TextView jenisTitle;
    @InjectView(R.id.btnOpenDropbox) Button btnOpenDropbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        dbHelper = new LocationDbHelper(this);
        turtle = new TurtleModel();
        intent = getIntent();
        String ID = intent.getStringExtra("ID");
        turtle = dbHelper.get(ID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaTitle.setText(turtle.getName());
        locationTitle.setText(turtle.getLatitude() + ", " + turtle.getLongitude());
        jmlTitle.setText(String.valueOf(turtle.getJmlTelur()));
        jenisTitle.setText(turtle.getTurtleCategory());
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
                Intent intent = new Intent(this,EditLocation.class);
                intent.putExtra("ID", turtle.getID());
                startActivity(intent);
                break;
            case R.id.action_delete:
                dbHelper.delete(turtle.getID());
                startActivity(new Intent(this,NavigationDrawer.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDropbox(View view){
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        String uri = turtle.getDropboxLink();

        Intent dropboxIntent= new Intent(Intent.ACTION_VIEW);
        dropboxIntent.setData(Uri.parse(uri));
        startActivity(dropboxIntent);
    }

    public void openMap(View view){
        //Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        String uri="geo:0,0?q="+turtle.getLatitude()+","+turtle.getLongitude()+"("+turtle.getName()+")";

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(mapIntent);
    }

}
