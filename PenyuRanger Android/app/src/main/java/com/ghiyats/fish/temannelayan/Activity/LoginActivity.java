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
import com.ghiyats.fish.temannelayan.R;
import com.ghiyats.fish.temannelayan.App.AppController;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if the user is already logged in
        sessionManager = new SessionManager(this);
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
        Log.d(TAG,id+password);

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
}
