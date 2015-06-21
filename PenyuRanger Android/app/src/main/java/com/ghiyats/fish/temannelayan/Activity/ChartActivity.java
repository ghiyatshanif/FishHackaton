package com.ghiyats.fish.temannelayan.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.ResultDbHelper;
import com.ghiyats.fish.temannelayan.Model.ResultModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.Realm;
import io.realm.RealmResults;

public class ChartActivity extends ActionBarActivity {

    private final String API_KEY = "7e9f2b61d5ca76e67d8afb1e1047643ddb42d6e7";
    private final String VAR_ID = "5572795b76254210f1598f84";
    public ApiClient api;
    public TurtleModel turtle;
    public LocationDbHelper dbHelper;
    public ResultDbHelper resultHelper;
    ArrayList<String> dates;
    ArrayList<ResultModel> results;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        dbHelper = new LocationDbHelper(this);
        resultHelper = new ResultDbHelper(this);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = intent.getStringExtra("ID");
        turtle = dbHelper.get(id);
        try{
            new GetChartData().execute();
        }catch (Exception e){
            e.printStackTrace();
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

    public class GetChartData extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                api = new ApiClient(API_KEY);

                Variable pir = api.getVariable(VAR_ID);
                Value[] values = pir.getValues();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                for (Value val:values){
                    ResultModel result = new ResultModel(dateFormat.format(val.getTimestamp()),(int)val.getValue());
                    resultHelper.add(result);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("TAG","an error occured");
            }

            dates = getDateInBetween();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            results = sumByDate(dates);
            progressDialog.dismiss();
            setUpChart();


        }
    }

    public ArrayList<String> getDateInBetween(){
        ArrayList<String> dateList = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Calendar calendar = Calendar.getInstance();
        Date to = calendar.getTime();
        calendar.add(Calendar.DATE, -18);
        Date from = calendar.getTime(); //turtle.getSavedOn();

        calendar.setTime(from);
        while (calendar.getTime().before(to)){
            calendar.add(Calendar.DATE,1);
            dateList.add(dateFormat.format(calendar.getTime()));
            Log.d("Dates",dateFormat.format(calendar.getTime()));
        }

        return dateList;
    }

    public ArrayList<ResultModel> sumByDate(ArrayList<String> dates){
        ArrayList<ResultModel> results = new ArrayList<ResultModel>();
        Realm realm = Realm.getInstance(this);
        int sum;


        for (String date : dates){
            RealmResults<ResultModel> rm = realm.where(ResultModel.class).equalTo("date", date).findAll();
            if (rm != null) {
                sum = rm.sum("value").intValue();

                Log.d("summed value", date + " " + sum);
                results.add(new ResultModel(date, sum));
            }
            else {
                results.add(new ResultModel(date, 0));
                Log.d("summed value", date + " " + 0);
            }
        }
        resultHelper.clear();
        return results;
    }

    public void setUpChart(){
        ArrayList<Entry> entries = new ArrayList<>();
        int i =0;
        for (ResultModel result :results){
            entries.add(new Entry((float)result.getValue(),i));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries,"Ancaman");
        LineChart chart = new LineChart(this);
        setContentView(chart);

        LineData lineData = new LineData(dates,dataSet);
        chart.setData(lineData);
        chart.setDescription("Angka ancaman per hari");
        dataSet.setColor(getResources().getColor(R.color.actionbar_color));
        dataSet.setLineWidth(5);
        dataSet.setCircleSize(7);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPadding(10,20,10,10);
        chart.animateX(2000);
        chart.animateY(2000);

    }

}
