package com.ghiyats.fish.temannelayan.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghiyats.fish.temannelayan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LegalFragment extends Fragment {
    TextView legalTextView;
    String legal = "<h1>Penyu Ranger, 2015</h1>\n" +
            "\n" +
            "<p>Libraries which&nbsp;used in this Project, Thanks to all the Open Source Community!</p>\n" +
            "\n" +
            "<br>" +
            "\t - Volley <br>\n" +
            "\t - ButterKnife<br>\n" +
            "\t - Realm IO <br>\n" +
            "\t - MaterialNavigationDrawer<br>\n" +
            "\t - Crouton<br>\n" +
            "\t - MaterialTabs<br>\n" +
            "\t - GooglePlayService<br>\n" +
            "</ul>\n" +
            "\n" +
            "<hr />\n" +
            "<p><br />\n" +
            "These following Library are Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);<br />\n" +
            "you may not use this file except in compliance with the License.<br />\n" +
            "You may obtain a copy of the License at</p>\n" +
            "\n" +
            "<a href:http://www.apache.org/licenses/LICENSE-2.0 />\n" +
            "\n" +
            "<p>Unless required by applicable law or agreed to in writing, software<br />\n" +
            "distributed under the License is distributed on an &quot;AS IS&quot; BASIS,<br />\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br />\n" +
            "See the License for the specific language governing permissions and<br />\n" +
            "limitations under the License.</p>\n";


    public LegalFragment() {
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
        setHasOptionsMenu(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_legal, container, false);
        legalTextView = (TextView) view.findViewById(R.id.legalTextView);
        legalTextView.setText(Html.fromHtml(legal));
        return view;
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
