package com.ghiyats.fish.temannelayan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.ghiyats.fish.temannelayan.Fragment.AboutFragment;
import com.ghiyats.fish.temannelayan.Fragment.LegalFragment;
import com.ghiyats.fish.temannelayan.Fragment.LocationFragment;
import com.ghiyats.fish.temannelayan.Fragment.MainFragment;
import com.ghiyats.fish.temannelayan.R;
import com.ghiyats.fish.temannelayan.Helper.SessionManager;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

/**
 * Created by Ghiyats on 5/24/2015.
 */
public class NavigationDrawer extends MaterialNavigationDrawer {
    @Override
    public void init(Bundle savedInstanceState) {
        allowArrowAnimation();

        final SessionManager session = new SessionManager(this);
        setDrawerHeaderImage(R.drawable.header);

        MaterialSectionListener logoutListener = new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection materialSection) {
                session.destroySession();
                Log.d(this.getClass().getSimpleName(), "user logged out");
                Intent in = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(in);
                finish();
            }
        };

        MaterialAccount acc = new MaterialAccount(getResources(),session.getUsername().toUpperCase(),"Penyu Ranger",R.drawable.account_placeholder,R.drawable.penyu_header );
        addAccount(acc);


        MaterialSection location_section = this.newSection(getResources().getString(R.string.menu_daftar_lokasi),
                getResources().getDrawable(R.drawable.ic_location_on_white_48dp),
                new LocationFragment());
        addSection(location_section);

        MaterialSection compass_section = this.newSection(getResources().getString(R.string.menu_infografis),
                getResources().getDrawable(R.drawable.ic_assessment_white_48dp),
                new MainFragment());
        addSection(compass_section);

        MaterialSection acc_section = newSection(getResources().getString(R.string.menu_akun),
                getResources().getDrawable(R.drawable.ic_account_box_white_48dp),
                new MainFragment());
        addSection(acc_section);

        this.addDivisor();

        MaterialSection about_section = newSection("About",
                getResources().getDrawable(R.drawable.ic_info_white_48dp),
                new AboutFragment());
        addSection(about_section);

        MaterialSection legal_section = newSection("Legal",
                getResources().getDrawable(R.drawable.ic_subject_white_48dp),
                new LegalFragment());
        addSection(legal_section);

        MaterialSection logout_section = newSection("Logout",logoutListener);
        addBottomSection(logout_section);

    }
}

