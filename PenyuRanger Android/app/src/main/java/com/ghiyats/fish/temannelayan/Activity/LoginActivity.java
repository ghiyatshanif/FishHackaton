package com.ghiyats.fish.temannelayan.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ghiyats.fish.temannelayan.Helper.KonservasiDbHelper;
import com.ghiyats.fish.temannelayan.Helper.LocationDbHelper;
import com.ghiyats.fish.temannelayan.Helper.RangerDbHelper;
import com.ghiyats.fish.temannelayan.Model.KonservasiModel;
import com.ghiyats.fish.temannelayan.Model.RangerModel;
import com.ghiyats.fish.temannelayan.Model.TurtleModel;
import com.ghiyats.fish.temannelayan.R;
import com.ghiyats.fish.temannelayan.App.AppController;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmList;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String URL = "http://alitagps.16mb.com/api/login_api.php";

    @InjectView(R.id.user_id) EditText mUserID;
    @InjectView(R.id.user_pass) EditText mUserPass;
    @InjectView(R.id.btnLogin) Button mBtnLogin;

    public ProgressDialog pDialog;
    public SessionManager sessionManager;
    public String id;
    public String pass;
    public Configuration config;
    KonservasiDbHelper konservasiHelper;
    LocationDbHelper dbHelper;
    KonservasiModel konservasi;
    RangerDbHelper rangerHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if the user is already logged in
        sessionManager = new SessionManager(this);
        konservasiHelper = new KonservasiDbHelper(this);
        rangerHelper = new RangerDbHelper(this);
        dbHelper = new LocationDbHelper(this);
        //Log.d("TAG",konservasi.getNamaKonservasi());
        //initKonservasi();
        konservasi = konservasiHelper.get("KPBL Batu Hiu");
        //initData();
        //initLocations();

        if (sessionManager.isLoggedIn()) {
            Intent i = new Intent(this, NavigationDrawer.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);
        pDialog = new ProgressDialog(this);
        config = new Configuration.Builder().setDuration(3000).build();



    }

    public void login(View view) {
        Log.d(TAG, "Login Button Clicked");
        id = mUserID.getText().toString();
        pass = mUserPass.getText().toString();
        if (id.trim().length() > 0 && pass.trim().length() > 0) {
            checkLogin(id, pass);
        } else {
            Crouton.makeText(this, "Please enter ID and Password", Style.INFO).setConfiguration(config).show();
        }
    }

    /**
     * function to verify login details in mysql db
     * */
    public void checkLogin(final String id, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        Log.d(TAG, id + password);

        pDialog.setMessage("Logging in ...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                String modified = response.substring(15);
                Log.d(TAG, "Login Response modified: " + modified);

                pDialog.hide();

                try {
                    JSONObject object = new JSONObject(modified);
                    boolean success = object.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // user successfully logged in, get user info
                        String user_id = object.getString("id");
                        String name = object.getString("name");
                        String pass = object.getString("password");
                        // Create login session
                        sessionManager.createSession(user_id,name,pass);
                        Log.d(TAG,sessionManager.getUserId()+sessionManager.getPassword()+sessionManager.getUsername());
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                NavigationDrawer.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = object.getString("msg");
                        Crouton.makeText(LoginActivity.this, errorMsg, Style.ALERT).setConfiguration(config).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Crouton.makeText(LoginActivity.this, "An error occured, please check your internet connection and try again", Style.ALERT)
                .setConfiguration(config)
                .show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", id);
                params.put("password", password);
                    
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void initKonservasi(){
        konservasiHelper.init("KPBL Batu Hiu","Desa Ciliang Pantai Batu Hiu, Pangandaran",
                "Kelompok Penangkaran Biota Laut Batu Hiu didirikan pada tahun 2006 oleh Bapak Didin Saefudin","",
                "08787311293",new RealmList<TurtleModel>(),new RealmList<RangerModel>());

        konservasiHelper.init("Aliansi Konservasi Tompotika", "Tompotika, Sulawesi Tengah",
                "Aliansi Konservasi Tompotika (AlTo) bekerja sama dengan masyarakat setempat di dua lokasi untuk melakukan patroli pantai serta melindungi sarang penyu. ", "",
                "08787311293", new RealmList<TurtleModel>(), new RealmList<RangerModel>());

        konservasiHelper.init("Unit Pengelolaan Konservasi Penyu Sukamade", "Pantai Sukamade, Banyuwangi",
                "Pengelolaan penyu di Resort Sukamade dilakukan oleh UPKP. UPKP (Unit Pengelolaan Konservasi Penyu) yang dibentuk pada tahun 2010 merupakan unit yang khusus dibentuk untuk melakukan pengelolaan penyu di Resort Sukamade", "",
                "08787311293", new RealmList<TurtleModel>(), new RealmList<RangerModel>());

        konservasiHelper.init("Yayasan Penyu Laut Indonesia", "Jl. Kelapa Muda, Koja, Jakarta",
                "Nama Yayasan Penyu Laut Indonesia resmi disahkan pada tanggal 01 Oktober 2009, sebelumnya bernama Yayasan Alam Lestari yang berdiri sejak 27 April 1997. Yayasan Penyu Laut Indonesia atau disingkat YPLI adalah sebuah Lembaga Swadaya Masyarakat yang bersifat nirlaba dimana kegiatan utamanya adalah melakukan pelestarian dan menjaga habitat peneluran penyu di kepulauan Indonesia umumnya", "",
                "+6243931122", new RealmList<TurtleModel>(), new RealmList<RangerModel>());

        konservasiHelper.init("Konservasi penyu pacitan","Pantai Taman Ria, Pacitan",
                "Deskripsi tidak tersedia","",
                "08787311293",new RealmList<TurtleModel>(),new RealmList<RangerModel>());

        konservasiHelper.init("Turtle Education and Conservation Center (TCEC)", "Jl. Tukad Wisata No. 4, Kelurahan Serangan, Denpasar",
                "TCEC merupakan wahana konservasi yang diprakarsai oleh sejumlah tokoh pelestarian lingkungan di Bali, WWF, dan Pemerintah Provinsi Bali. Pilot project tempat ini dimulai pada tahun 1982, atas prakarsa antara lain Menteri Riset dan Teknologi ketika itu, B.J. Habibie.", "",
                "081236127202", new RealmList<TurtleModel>(), new RealmList<RangerModel>());
    }


    public void initData(){
        rangerHelper.init("3302","Maman",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpa1/v/t1.0-9/10401585_10201187950620761_5555408473048279898_n.jpg?_nc_eui=AWhi3g6283Ot6iePnmsL2nhEvZgpr73K-_qQeA&oh=f99bdcfe6fcbd7903d2664ea62727470&oe=55F9E69F");
        rangerHelper.init("3301","Satria Rahmadi",konservasi,"082193419641","satria",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xaf1/v/t1.0-9/10574526_1574968782719147_3881307069647061915_n.jpg?_nc_eui=AWik0vTVE3WDyRlD7ypSVdsJg2jFSzysO_BOTQ&oh=1334842542953bc02c0b41ad45de7dfd&oe=55F09868");
        rangerHelper.init("3303","Yudi Prasetyo",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/10625147_10202655478211469_8008595998519576575_n.jpg?_nc_eui=AWjnJJn1FZZLNKKn3Stqzlc6jj5YL5TtlTr-Ww&oh=39b0d61cfc35b7ad1d19e6c46aba049e&oe=560356D3");
        rangerHelper.init("3304","Muhammad Assegaf",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/1974975_10201737627348865_1989057729_n.jpg?_nc_eui=AWh-uXfxiYtMBV3MCzzDDEvVfUJN7IBIW4Kquw&oh=33aeee1ca89ea0322b49de25b239e44f&oe=55C0835F");
        rangerHelper.init("3305","Ludffi Rizkika",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/1511331_10204152537517735_2471003090369745169_n.jpg?_nc_eui=AWiIC3ch4tpC_wo-heEDz-jeGPU6Y_pyqx46BA&oh=5b195e69a1d99fa2e68492192b3032ca&oe=55F202EC");
        rangerHelper.init("3306","Andri Amirudin",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/10947207_927586910593428_2325548007938549976_n.jpg?_nc_eui=AWjUj1olcQ117YkAHhc5quDPSqWUDYAgHAuQIw&oh=260c03f7125ebaec45ee1eab0b492853&oe=55FEBBF6");
        rangerHelper.init("3307","Dimas Setiawan",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/12819_10202319653432515_8916180256083386116_n.jpg?_nc_eui=AWggzKp4ZAKNJIjS3LvkxO5t-Hh97HU-Rgq7Iw&oh=3378a747703abe0f12d18a6770baabfd&oe=55F8273B");
        rangerHelper.init("3308","Aldifianto",konservasi,"08787311293","maman1",
                "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/1535656_10201095863467444_399476200_n.jpg?_nc_eui=AWg2xXdXwksfqjmKaO-AxvheJSLg1PhsilDk-A&oh=323ce52b6bc63f3ea8fd0112806d2434&oe=55F165AB");
        RangerModel rangerModel = rangerHelper.get("3302");
        Log.d("added data",rangerModel.getRangerName()+rangerModel.getPhoneNumber()+rangerModel.getMemberOf());
    }

    public Date getCurentTime(){
        Calendar c = Calendar.getInstance();
        Date date= c.getTime();
        return date;
    }

    public void initLocations(){
        Log.d("masukin data", "dsfsafdasfasdfda");
        dbHelper.init("Teluk pangandaran", "Penyu Hijau", 92, "-7.691471", "108.538996", getCurentTime(), "maman", "", konservasi);
        dbHelper.init("Teluk pangandaran 2", "Penyu Sisik", 100, "-7.691273", "108.539095", getCurentTime(), "maman", "", konservasi);
        dbHelper.init("Teluk Pangandaran 3", "Penyu Belimbing",80, "-7.691021", "108.540475", getCurentTime(), "maman", "",konservasi);
        dbHelper.init("Penyusuar Cibenda", "Penyu Hijau",77, "-7.690959", "108.540697", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Cibenda 2", "Penyu Tempayan",93, "-7.690645", "108.542051", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Ciliang", "Penyu Sisik",82, "-7.694241", "108.529438", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Parigi", "Penyu Hijau",90, "-7.693301", "108.532133", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Pantai Batu Hiu", "Penyu Sisik",90, "-7.692496", "108.536116", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Pantai Batu Hiu 2", "Penyu Sisik",90, "-7.692384", "108.535993", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Parigi 2", "Penyu Hijau",87, "-7.693301", "108.532133", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Ciliang 2", "Penyu Hijau",97, "-7.695112", "108.526843", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Ciliang 3", "Penyu Sisik",77, "-7.693875", "108.530320", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Penyusuar Parigi 2", "Penyu Belimbing",84, "-7.694425", "108.528889", getCurentTime(), "maman","",konservasi);
        dbHelper.init("Teluk Pangandaran 4", "Penyu Belimbing",84, "-7.693875", "108.530320", getCurentTime(), "maman","",konservasi);

    }
}
