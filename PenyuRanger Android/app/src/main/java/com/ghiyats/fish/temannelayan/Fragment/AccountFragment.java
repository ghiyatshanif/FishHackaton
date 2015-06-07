package com.ghiyats.fish.temannelayan.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ghiyats.fish.temannelayan.App.AppController;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;
import com.ghiyats.fish.temannelayan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final String CHANGE_PASS_URL = "http://alitagps.16mb.com/api/change_password_api.php";
    private static final String TAG = "AccountFragment";

    TextView accountID;
    TextView accountName;
    EditText oldPass;
    EditText newPass;
    EditText newPassConfirm;
    Button btnChangePass;

    SessionManager sessionManager;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        accountName = (TextView) view.findViewById(R.id.account_name);
        accountID = (TextView) view.findViewById(R.id.account_id);
        oldPass = (EditText) view.findViewById(R.id.oldPass);
        newPass = (EditText) view.findViewById(R.id.newPass);
        newPassConfirm = (EditText) view.findViewById(R.id.newPassConfirm);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePass);

        sessionManager = new SessionManager(getActivity());
        Log.d(TAG, sessionManager.getUserId() + sessionManager.getUsername());

        accountName.setText(sessionManager.getUsername());
        accountID.setText(sessionManager.getUserId());

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oldPass.getText().toString().trim().equals(sessionManager.getPassword())) {
                    Crouton.makeText(getActivity(), "Insert the correct old password ", Style.INFO).show();
                } else if (newPass.getText().toString().trim().equals("")) {
                    Crouton.makeText(getActivity(), "New password cant be empty", Style.INFO).show();
                }
                else if (!newPass.getText().toString().trim().equals(newPassConfirm.getText().toString().trim())) {
                    Crouton.makeText(getActivity(), "Confirm the new password", Style.INFO).show();
                }
                else {
                    Log.d(TAG,"finally its about to change");
                    changePassword();
            }
        }
    }

    );

    return view;
}

    private void changePassword() {
        StringRequest changePassRequest = new StringRequest(Request.Method.POST, CHANGE_PASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String modified = response.substring(15);
                        Log.d(TAG, modified);
                        try {
                            JSONObject object = new JSONObject(modified);
                            boolean success = object.getBoolean("success");
                            if (success) {
                                Crouton.makeText(getActivity(), "Password successfully changed", Style.CONFIRM).show();
                                sessionManager.setPassword(object.getString("new_password"));
                            } else {
                                Crouton.makeText(getActivity(), object.getString("msg"), Style.ALERT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Crouton.makeText(getActivity(), "An error occured while connecting to server", Style.ALERT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());
                Crouton.makeText(getActivity(), "An error occured please check your internet connection and try again", Style.ALERT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("sales_id", sessionManager.getUserId());
                params.put("new_password", newPass.getText().toString().trim());
                return params;
            }
        };

        AppController.getInstance().addtoRequestQueue(changePassRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Log.d(TAG,"options menu created");
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
